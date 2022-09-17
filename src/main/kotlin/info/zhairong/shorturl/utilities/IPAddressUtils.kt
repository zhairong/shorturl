package info.zhairong.shorturl.utilities

import mu.KotlinLogging
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component
import java.net.InetAddress
import java.net.UnknownHostException
import javax.servlet.http.HttpServletRequest

object IPAddressUtils {
    private val logger = KotlinLogging.logger {}
    /**
     *
     * @param request
     * @return
     */

    fun getIpAddress(request: HttpServletRequest): String {
        var ip = request.getHeader("X-Real-IP")
        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("x-forwarded-for")
        }
        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("Proxy-Client-IP")
        }
        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("WL-Proxy-Client-IP")
        }
        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("HTTP_CLIENT_IP")
        }
        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR")
        }
        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.remoteAddr
            if ("127.0.0.1" == ip || "0:0:0:0:0:0:0:1" == ip) {
                var inet: InetAddress? = null
                try {
                    inet = InetAddress.getLocalHost()
                } catch (e: UnknownHostException) {
                    logger.error("getIpAddress exception:", e);
                }
                ip = inet!!.hostAddress
            }
        }
        return StringUtils.substringBefore(ip, ",")
    }
}