package info.zhairong.shorturl.to

class Response {
    var code: Int? = null
    var msg: String? = null
    var data: Any? = null
    constructor(code: Int, msg: String) {
        this.code = code
        this.msg = msg
    }

    constructor(code: Int, msg: String, data: Any?) {
        this.code = code
        this.msg = msg
        this.data = data
    }

    /**
     * will be used by deserializing with jackson.
     */
    constructor()

    companion object {
        fun ok(msg: String, data: Any?): Response {
            return Response(200, msg, data)
        }

        fun create(code: Int, msg: String): Response {
            return Response(code, msg)
        }
    }
}