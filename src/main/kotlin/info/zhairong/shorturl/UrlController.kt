package info.zhairong.shorturl

import info.zhairong.shorturl.annotation.AccessLimit
import info.zhairong.shorturl.services.UrlCache
import info.zhairong.shorturl.services.UrlService
import info.zhairong.shorturl.to.Response
import info.zhairong.shorturl.utilities.HashUtils
import org.apache.commons.validator.routines.UrlValidator
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.view.RedirectView

private const val DEFAULT_URL = "http://localhost:8080"

@RestController
@RequestMapping
class UrlController(private val urlService: UrlService, private val urlCache: UrlCache) {
    private var host: String? = null
    private var port: String? = null

    @Value("\${server.host}")
    fun setHost(host: String?) {
        this.host = host
    }
    @Value("\${server.port}")
    fun setPort(port: String?) {
        this.port = port
    }
   @GetMapping("/get/{shortUrl}")
    fun getOriginalUrl(@PathVariable shortUrl: String):String {
        val longUrl = urlService.getLongUrlByShortUrl(shortUrl)
       println("longUrl: $longUrl");
       if (longUrl != null) {
           return longUrl;
       }
       return DEFAULT_URL
    }

    @AccessLimit(maxCount = 1, msg = "one generator every 10 seconds for one connection")
    @PostMapping("/generate")
    @ResponseBody
    fun generateShort(@RequestParam url: String): Response {
        if(UrlValidator.getInstance().isValid(url)) {
            var longUrl = url
            val key = urlService.saveUrlMap(HashUtils.hashToBase62(longUrl), longUrl, url);
            return Response.ok("success", "$host:$port/rd/$key");
        }
        return Response.create(400,"url is invalid!")
    }

    @GetMapping("/rd/{shortUrl}")
    fun redirect(@PathVariable shortUrl: String): RedirectView? {
        val longUrl = urlCache[shortUrl]
        if (longUrl != null) {
            return RedirectView("$longUrl")
        }
        return RedirectView("http://$host:$port")
    }
}