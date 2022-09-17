package info.zhairong.shorturl.services

import cn.hutool.bloomfilter.BloomFilterUtil
import info.zhairong.shorturl.data.UrlMap
import info.zhairong.shorturl.utilities.HashUtils.hashToBase62
import org.springframework.stereotype.Service


/**
 * @Description: implementation of UrlService
 * @Author: Rong Zhai
 * @Date: 2022-09-16
 */
@Service
class UrlServiceImpl(private val urlMapper:UrlMapper, private val urlCache: UrlCache) : UrlService {
    override fun getLongUrlByShortUrl(shortUrl: String): String? {
        // find longUrl from cache.
        var longUrl: String? = urlCache[shortUrl]
        if (longUrl != null) {
            return longUrl
        }
        // not found in Cache, now searching in DB.
        longUrl = urlMapper.getLongUrlByShortUrl(shortUrl)
        if (longUrl != null) {
            // put longUrl to cache.
            urlCache.put(shortUrl, longUrl)
        }
        return longUrl
    }

    override fun saveUrlMap(shortURL: String, longURL: String, originalURL: String): String {
        //保留长度为1的短链接
        var shortURL = shortURL
        var longURL = longURL
        if (shortURL.length == 1) {
            longURL += DUPLICATE
            shortURL = saveUrlMap(hashToBase62(longURL), longURL, originalURL)
        } else if (urlCache.contains(shortURL) && urlCache[shortURL]!=null) {
            val cachedLongURL = urlCache[shortURL]
            // longUrl is already inserted and just return the shortUrl.
            if (originalURL == cachedLongURL) {
                return shortURL
            }
            longURL += DUPLICATE
            shortURL = saveUrlMap(hashToBase62(longURL), longURL, originalURL)
        } else {
            // not exists, put it to database.
            urlMapper.saveUrlMap(UrlMap(shortURL, originalURL))
            urlCache.put(shortURL, originalURL);
        }
        return shortURL
    }

    companion object {
        //自定义长链接防重复字符串
        private const val DUPLICATE = "*"

        //最近使用的短链接缓存过期时间(分钟)
        private const val TIMEOUT: Long = 10

        //创建布隆过滤器
        private val FILTER = BloomFilterUtil.createBitMap(10)
    }
}