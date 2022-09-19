package info.zhairong.shorturl

import com.ninjasquad.springmockk.MockkBean
import info.zhairong.shorturl.interceptor.AccessLimitInterceptor
import info.zhairong.shorturl.to.Response
import info.zhairong.shorturl.utilities.HashUtils
import io.mockk.every
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory


@SpringBootTest(classes = [ShorturlApplication::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShorturlApplicationIntegrationTest {

    @MockkBean(relaxed = true)
    lateinit var interceptor: AccessLimitInterceptor

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @BeforeEach
    fun setUp() {
        every {
            interceptor.preHandle(any(), any(), any())
        } returns true
    }

    @Test
    fun generate_shortUrl_with_validUrl() {
        val testUrl = "http://gmail.com"
        val result = restTemplate.postForEntity("/generate?url=$testUrl", null, Response::class.java)
        assertNotNull(result)
        assertEquals(HttpStatus.OK, result?.statusCode)
        assertEquals(200, result.body?.code)
        assertEquals(UrlController.SUCCESS, result.body?.msg)
        assertEquals(HashUtils.hashToBase62(testUrl), result.body?.data)
    }

    @Test
    fun generate_shortUrl_with_invalidUrl() {

        val testUrl = "http-s-://gmail.com"
        val result = restTemplate.postForEntity("/generate?url=$testUrl", null, Response::class.java)
        assertNotNull(result)
        assertEquals(HttpStatus.BAD_REQUEST, result?.statusCode)
        assertEquals(400, result.body?.code)
        assertEquals(UrlController.INVALID_URL, result.body?.msg)
        assertNull(result.body?.data)
    }

    @Test
    fun get_originalUrl_by_exists_shortUrl() {
        val testUrl = "http://gmail.com"
        // insert one shortUrl map.
        val resultShort = restTemplate.postForEntity("/generate?url=$testUrl", null, Response::class.java)
        assertNotNull(resultShort)
        val shortUrl = resultShort.body?.data

        val resultOriginal = restTemplate.getForEntity("/get/$shortUrl", Response::class.java)
        assertNotNull(resultOriginal)
        val originalUrl = resultOriginal.body?.data
        assertEquals(testUrl, originalUrl)
    }

    @Test
    fun get_originalUrl_by_notExists_shortUrl() {
        val notExistsShortUrl = "eisla"
        val result = restTemplate.getForEntity("/get/$notExistsShortUrl", Response::class.java)
        assertNotNull(result)
        assertEquals(HttpStatus.BAD_REQUEST, result?.statusCode)
        assertEquals(400, result.body?.code)
        assertEquals(UrlController.PATTERN_NO_SHORT_URL_FOUND+notExistsShortUrl, result.body?.msg)
        assertNull(result.body?.data)
    }

    @Test
    fun redirect_to_originalUrl_by_existsShortUrl() {
        val testUrl = "https://www.google.com/gmail/"
        // insert one shortUrl map.
        val resultShort = restTemplate.postForEntity("/generate?url=$testUrl", null, Response::class.java)
        assertNotNull(resultShort)
        val shortUrl = resultShort.body?.data
        val result = restTemplate.getForEntity("/rd/$shortUrl", String::class.java)
        assertNotNull(result)
        assertEquals(HttpStatus.FOUND, result.statusCode)
        assertEquals(testUrl, result.headers["Location"]?.get(0))
    }

    @Test
    fun redirect_to_originalUrl_by_NotexistsShortUrl() {

        // insert one shortUrl map.

        val shortUrl = "asf0e";
        val result = restTemplate.getForEntity("/rd/$shortUrl", String::class.java)
        assertNotNull(result)
        assertEquals(HttpStatus.FOUND, result.statusCode)
        assertEquals(UrlController.DEFAULT_REDIRECT_URL, result.headers["Location"]?.get(0))
    }
}