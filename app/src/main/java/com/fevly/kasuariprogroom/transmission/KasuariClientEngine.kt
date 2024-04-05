package com.fevly.kasuariprogroom.transmission

import java.net.Socket

class KasuariClientEngine (_port: Int) {
    var port =0
    init {
        this.port=_port
    }

    fun getConn (): Socket {
        // ini cegah overlap dng thread server di User.kt
        // pastikan client engine start setelah server ready..
        Thread.sleep(1000)

        var sock = Socket()
        try {
          sock =  Socket("127.0.0.1", this.port)
        }
        catch (e : Exception){
            print("cause : "+e.cause+"\n"+e.message)
        }
        return sock
    }
}