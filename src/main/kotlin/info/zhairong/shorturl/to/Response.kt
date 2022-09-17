package info.zhairong.shorturl.to

class Response {
    var code: Int
    var msg: String
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

    companion object {
        fun ok(msg: String, data: Any?): Response {
            return Response(200, msg, data)
        }

        fun create(code: Int, msg: String, data: Any?): Response {
            return Response(code, msg, data)
        }

        fun create(code: Int, msg: String): Response {
            return Response(code, msg)
        }
    }
}