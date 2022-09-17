package info.zhairong.shorturl.services

import org.springframework.scheduling.annotation.Async


interface UrlService {
    fun getLongUrlByShortUrl(shortUrl: String): String?
    fun saveUrlMap(shortUrl: String, longUrl: String, originalUrl: String): String
}