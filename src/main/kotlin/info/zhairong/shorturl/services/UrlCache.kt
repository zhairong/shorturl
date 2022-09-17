package info.zhairong.shorturl.services

import org.apache.commons.collections4.map.LRUMap
import org.springframework.stereotype.Service

private const val MAX_SIZE = 100

/**
 * Using for Delegate LRUMap.
 */
@Service
class UrlCache {

    var cache: LRUMap<String, String> = LRUMap(MAX_SIZE)

    operator fun get(key:String): String? {
        return cache[key]
    }

    fun put(key:String, value:String) {
        cache[key] = value
    }

    fun contains(key:String):Boolean {
        return cache.contains(key)
    }
}