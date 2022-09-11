package info.zhairong.shorturl

import info.zhairong.shorturl.services.UrlCache
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView

@RestController
@RequestMapping("/rd")
class RedirectController(private val urlCache: UrlCache) {
    @GetMapping("/{shortUrl}")
    fun redirect(@PathVariable shortUrl: String): RedirectView? {
        val longUrl = urlCache.get(shortUrl)
        if (longUrl != null) {
            return RedirectView("http://$longUrl")
        }
        return RedirectView("http://localhost:8080")
    }
}