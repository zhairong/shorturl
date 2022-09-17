package info.zhairong.shorturl.utilities

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException
import java.io.InputStream


/**
 * @Description: Jackson Object Mapper
 * @Author: Rong Zhai
 * @Date: 2022-09-16
 */
object JacksonUtils {
    private val objectMapper = ObjectMapper()
    fun writeValueAsString(value: Any?): String {
        return try {
            objectMapper.writeValueAsString(value)
        } catch (e: JsonProcessingException) {
            e.printStackTrace()
            ""
        }
    }

    fun <T> readValue(content: String?, valueType: Class<T>?): T? {
        return try {
            objectMapper.readValue(content, valueType)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun <T> readValue(content: String?, valueTypeRef: TypeReference<T>?): T? {
        return try {
            objectMapper.readValue(content, valueTypeRef)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun <T> readValue(src: InputStream?, valueType: Class<T>?): T? {
        return try {
            objectMapper.readValue(src, valueType)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun <T> convertValue(fromValue: Any?, toValueType: Class<T>?): T {
        return objectMapper.convertValue(fromValue, toValueType)
    }
}
