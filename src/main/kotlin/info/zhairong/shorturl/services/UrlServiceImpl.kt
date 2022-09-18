package info.zhairong.shorturl.services

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

    override fun saveUrlMap(shortUrl: String, longUrl: String, originalUrl: String): String {
        //保留长度为1的短链接
        var shortURL = shortUrl
        var longURL = longUrl
        if (shortURL.length == 1) {
            longURL += DUPLICATE
            shortURL = saveUrlMap(hashToBase62(longURL), longURL, originalUrl)
        } else if (urlCache.contains(shortURL) && urlCache[shortURL]!=null) {
            val cachedLongURL = urlCache[shortURL]
            // longUrl is already inserted and just return the shortUrl.
            if (originalUrl == cachedLongURL) {
                return shortURL
            }
            longURL += DUPLICATE
            shortURL = saveUrlMap(hashToBase62(longURL), longURL, originalUrl)
        } else {
            // not exists, put it to database.
            urlMapper.saveUrlMap(UrlMap(shortURL, originalUrl))
            urlCache.put(shortURL, originalUrl)
        }
        return shortURL
    }

    companion object {
        private const val DUPLICATE = "*"
    }
}