package info.zhairong.shorturl

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content

@WebMvcTest(controllers = [HtmlController::class])
class HtmlControllerTest(@Autowired val mockMvc: MockMvc) {
    @Test
    fun getDefaultPage() {
        mockMvc.perform(get("/").accept(MediaType.TEXT_PLAIN))
            .andExpect(status().isOk)
            .andExpect(content().string("default page"))
    }
}