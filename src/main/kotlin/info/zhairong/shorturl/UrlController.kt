package info.zhairong.shorturl

import info.zhairong.shorturl.services.ShortUrlGenerator
import info.zhairong.shorturl.services.UrlCache
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView

@RestController
@RequestMapping("/api")
class UrlController(private val urlCache: UrlCache, private val generator: ShortUrlGenerator) {
    private val timeout: Long = 10

   @GetMapping("/short/{shortUrl}")
    fun redirect(@PathVariable shortUrl: String):String {
        val longUrl = urlCache.get(shortUrl)
       println("longUrl: $longUrl");
        if(longUrl!=null) {
            return "http://$longUrl"
        }
       return "http://localhost:8080"
    }

    @GetMapping("/long/{longUrl}")
    fun addMap(@PathVariable longUrl: String):String {
        val key = generator.generate(longUrl)
        urlCache.put(key, longUrl)
        return key
    }
}