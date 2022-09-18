package info.zhairong.shorturl.interceptor

import info.zhairong.shorturl.annotation.AccessLimit
import info.zhairong.shorturl.services.ClientCache
import info.zhairong.shorturl.to.Response.Companion.create
import info.zhairong.shorturl.utilities.IPAddressUtils.getIpAddress
import info.zhairong.shorturl.utilities.JacksonUtils.writeValueAsString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * @Description: Access Interceptor
 * @Author: Rong Zhai
 */
@Component
class AccessLimitInterceptor : HandlerInterceptor {
    @Autowired
    var cache: ClientCache? = null

    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod) {
            val accessLimit = handler.getMethodAnnotation(AccessLimit::class.java) ?: return  true
            val maxCount: Int = accessLimit.maxCount
            val ip = getIpAddress(request)
            val method = request.method
            val requestURI = request.requestURI
            val ipAddr = "$ip:$method:$requestURI"
            val countIpAccess = cache!!.get(ipAddr)
            if (countIpAccess == null) {
                cache!!.put(ipAddr, 1)
            } else {
                if (countIpAccess >= maxCount) {
                    //more than limit
                    response.contentType = "application/json;charset=utf-8"
                    response.status = HttpStatus.FORBIDDEN.value()
                    val out = response.writer
                    val result = create(403, accessLimit.msg)
                    out.write(writeValueAsString(result))
                    out.flush()
                    out.close()
                    return false
                } else {
                    cache!!.put(ipAddr, countIpAccess.inc())
                }
            }
        }
        return true
    }
}