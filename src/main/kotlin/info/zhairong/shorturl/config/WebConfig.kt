package info.zhairong.shorturl.config

import info.zhairong.shorturl.interceptor.AccessLimitInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    @Autowired
    var accessLimitInterceptor: AccessLimitInterceptor? = null
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(accessLimitInterceptor!!)
    }
}