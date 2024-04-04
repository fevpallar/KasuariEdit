package com.fevly.kasuariprogroom.transmission

import java.net.Socket

class KasuariClientEngine (_port: Int) {
    var port =0
    init {
        this.port=_port
    }

    fun getConn (): Socket {
       return Socket("127.0.0.1", this.port)
    }
}