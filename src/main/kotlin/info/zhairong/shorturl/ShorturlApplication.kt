package info.zhairong.shorturl

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@SpringBootApplication
@EnableWebMvc
class ShorturlApplication

fun main(args: Array<String>) {
	runApplication<ShorturlApplication>(*args)
}
