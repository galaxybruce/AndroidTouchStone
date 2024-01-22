package com.galaxybruce.component.net.exception

class AppNetException : Exception {
    var code: String

    constructor(code: String, message: String?) : super(message) {
        this.code = code
    }

    constructor(code: Int, message: String?) : super(message) {
        this.code = code.toString()
    }

    constructor(netError: AppNetError, e: Throwable? = null) : super("${netError.msg}${if(e?.message.isNullOrEmpty()) "" else "[${e?.message}]"}") {
        this.code = netError.code
    }
}