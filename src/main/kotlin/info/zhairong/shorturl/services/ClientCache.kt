package info.zhairong.shorturl.services

import org.apache.commons.collections4.map.PassiveExpiringMap
import org.springframework.stereotype.Service

@Service
class ClientCache {
    var cache: PassiveExpiringMap<String, Int> = PassiveExpiringMap<String, Int>(5000)

    fun get(key:String): Int? {
        return cache[key]
    }

    fun put(key:String, value:Int) {
        cache[key] = value
    }
}