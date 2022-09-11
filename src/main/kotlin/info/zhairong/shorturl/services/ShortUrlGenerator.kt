package info.zhairong.shorturl.services

import org.springframework.stereotype.Service
import java.util.concurrent.ThreadLocalRandom
import kotlin.streams.asSequence

@Service
class ShortUrlGenerator {
    val STRING_LENGTH = 10;
    val ALPHANUMERIC_REGEX = "[a-zA-Z0-9]+";
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    fun generate(longUrl:String):String {
        return ThreadLocalRandom.current()
            .ints(STRING_LENGTH.toLong(), 0, charPool.size)
            .asSequence()
            .map(charPool::get)
            .joinToString("")
    }
}