package info.zhairong.shorturl.utilities

import cn.hutool.core.lang.hash.MurmurHash

object HashUtils {
    private val CHARS = charArrayOf(
        '0',
        '1',
        '2',
        '3',
        '4',
        '5',
        '6',
        '7',
        '8',
        '9',
        'A',
        'B',
        'C',
        'D',
        'E',
        'F',
        'G',
        'H',
        'I',
        'J',
        'K',
        'L',
        'M',
        'N',
        'O',
        'P',
        'Q',
        'R',
        'S',
        'T',
        'U',
        'V',
        'W',
        'X',
        'Y',
        'Z',
        'a',
        'b',
        'c',
        'd',
        'e',
        'f',
        'g',
        'h',
        'i',
        'j',
        'k',
        'l',
        'm',
        'n',
        'o',
        'p',
        'q',
        'r',
        's',
        't',
        'u',
        'v',
        'w',
        'x',
        'y',
        'z'
    )
    private val SIZE = CHARS.size
    private fun convertDecToBase62(num: Long): String {
        var num = num
        val sb = StringBuilder()
        while (num > 0) {
            val i = (num % SIZE).toInt()
            sb.append(CHARS[i])
            num /= SIZE.toLong()
        }
        return sb.reverse().toString()
    }

    fun hashToBase62(str: String?): String {
        val i: Int = MurmurHash.hash32(str);
        val num = if (i < 0) Int.MAX_VALUE - i.toLong() else i.toLong()
        return convertDecToBase62(num)
    }
}