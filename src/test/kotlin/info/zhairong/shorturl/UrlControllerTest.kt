package info.zhairong.shorturl

import com.ninjasquad.springmockk.MockkBean
import info.zhairong.shorturl.services.UrlService
import info.zhairong.shorturl.utilities.HashUtils
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import info.zhairong.shorturl.interceptor.AccessLimitInterceptor
import org.junit.jupiter.api.BeforeEach

@WebMvcTest
class UrlControllerTest(@Autowired val mockMvc: MockMvc) {
    @MockkBean
    lateinit var urlService: UrlService
    @MockkBean(relaxed = true)
    lateinit var interceptor: AccessLimitInterceptor

    @BeforeEach
    fun setUp() {
        every {
            interceptor.preHandle(any(), any(), any())
        } returns true
    }

    @Test
    fun getOriginalUrl_given_notExistsShortUrl_then_400_withErrorMessage() {
        val shortUrl = "xhsieu"
        every {
            urlService.getLongUrlByShortUrl(shortUrl)} returns null
        mockMvc.perform(MockMvcRequestBuilders.get("/get/$shortUrl"))
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("code").value(400))
            .andExpect(jsonPath("msg").value(UrlController.PATTERN_NO_SHORT_URL_FOUND+ shortUrl))
            .andExpect(jsonPath("data").value(null))
    }

    @Test
    fun getOriginalUrl_given_existsShortUrl_then_200_withSuccess_and_ReturnLongUrl() {
        val shortUrl = "xhsieu"
        val longUrl = "http://www.tu.berlin"
        every {
            urlService.getLongUrlByShortUrl(shortUrl)} returns longUrl
        mockMvc.perform(MockMvcRequestBuilders.get("/get/$shortUrl"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("code").value(200))
            .andExpect(jsonPath("msg").value(UrlController.SUCCESS))
            .andExpect(jsonPath("data").value(longUrl))
    }

    @Test
    fun generateShort_validUrl_then_200_withSuccess_and_returnShortUrl() {
        val shortUrl = "xhsieu"
        val longUrl = "http://www.tu.berlin"
        val coded = HashUtils.hashToBase62(longUrl)

        every {
            urlService.saveUrlMap(coded, longUrl, longUrl)} returns shortUrl
        mockMvc.perform(MockMvcRequestBuilders.post("/generate?url=$longUrl").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("code").value(200))
            .andExpect(jsonPath("msg").value(UrlController.SUCCESS))
            .andExpect(jsonPath("data").value(shortUrl))
    }

    @Test
    fun generateShort_invalidUrl_then_400_withMessage_INVALID_URL() {
        // bad request
        val longUrl = "http-d-://www.tu.berlin"
        mockMvc.perform(MockMvcRequestBuilders.post("/generate?url=$longUrl").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("code").value(400))
            .andExpect(jsonPath("msg").value(UrlController.INVALID_URL))
            .andExpect(jsonPath("data").value(null))
    }

    @Test
    fun redirect_without_existsShortUrl_returned_DefaultURL() {
        val shortUrl = "xhsieu"
        every {
            urlService.getLongUrlByShortUrl(shortUrl)} returns null
        mockMvc.perform(MockMvcRequestBuilders.get("/rd/$shortUrl"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl(UrlController.DEFAULT_REDIRECT_URL))
    }

    @Test
    fun redirect_with_existsShortUrl_Returned_mappedOriginalUrl() {
        val shortUrl = "xhsieu"
        val longUrl = "http://www.tu.berlin"
        every {
            urlService.getLongUrlByShortUrl(shortUrl)} returns longUrl
        mockMvc.perform(MockMvcRequestBuilders.get("/rd/$shortUrl"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl(longUrl))
    }
}