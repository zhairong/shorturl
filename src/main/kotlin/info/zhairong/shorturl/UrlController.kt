package info.zhairong.shorturl

import info.zhairong.shorturl.annotation.AccessLimit
import info.zhairong.shorturl.services.UrlService
import info.zhairong.shorturl.to.Response
import info.zhairong.shorturl.utilities.HashUtils
import org.apache.commons.validator.routines.UrlValidator
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.view.RedirectView

@RestController
@RequestMapping
class UrlController(private val urlService: UrlService) {

    companion object{
        const val PATTERN_NO_SHORT_URL_FOUND = "no mapped url found for "
        const val SUCCESS = "success"
        const val INVALID_URL = "url is invalid!"
        const val DEFAULT_REDIRECT_URL = "http://google.com"
    }
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
    fun getOriginalUrl(@PathVariable shortUrl: String): ResponseEntity<Response> {
        val longUrl = urlService.getLongUrlByShortUrl(shortUrl)
       println("longUrl: $longUrl")
       if (longUrl != null) {
           return ResponseEntity.ok(Response.ok("success", longUrl))
       }
       return ResponseEntity.badRequest().body(Response.create(400, PATTERN_NO_SHORT_URL_FOUND + shortUrl))
    }

    @AccessLimit(maxCount = 1, msg = "one generator every 10 seconds for one connection")
    @PostMapping("/generate")
    @ResponseBody
    fun generateShort(@RequestParam url: String): ResponseEntity<Response> {
        if(UrlValidator.getInstance().isValid(url)) {
            val key = urlService.saveUrlMap(HashUtils.hashToBase62(url), url, url)
            return ResponseEntity.ok(Response.ok("success", key))
        }
        return ResponseEntity.badRequest().body(Response.create(400,INVALID_URL))
    }

    /**
     *  for redirect shorturl to original url.
     */
    @GetMapping("/rd/{shortUrl}")
    fun redirect(@PathVariable shortUrl: String): RedirectView? {
        val longUrl = urlService.getLongUrlByShortUrl(shortUrl)
        if (longUrl != null) {
            return RedirectView("$longUrl")
        }
        return RedirectView(DEFAULT_REDIRECT_URL)
    }
}