package info.zhairong.shorturl.services

import info.zhairong.shorturl.data.UrlMap
import org.springframework.stereotype.Service

/**
 * @Description: persistence interface
 * @Author: Rong Zhai
 * @Date: 2022-09-16
 */
@Service
class UrlMapper {

    val db: HashMap<String, String> = hashMapOf<String,String>();
    fun getLongUrlByShortUrl(surl: String): String? {
        return db[surl];
    }
    fun saveUrlMap(urlMap: UrlMap) {
        db[urlMap.surl] = urlMap.lurl;
    }
}
