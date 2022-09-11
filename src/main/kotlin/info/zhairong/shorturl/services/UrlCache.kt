package info.zhairong.shorturl.services

import org.springframework.stereotype.Service

@Service
class UrlCache {
    private val cache = hashMapOf<String, String>()

    fun get(key:String): String? {
        return cache[key]
    }

    fun put(key:String, value:String) {
        cache[key] = value
    }
}