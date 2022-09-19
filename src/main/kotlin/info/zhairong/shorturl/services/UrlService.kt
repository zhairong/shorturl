package info.zhairong.shorturl.services

interface UrlService {
    fun getLongUrlByShortUrl(shortUrl: String): String?
    fun saveUrlMap(shortUrl: String, longUrl: String, originalUrl: String): String
}