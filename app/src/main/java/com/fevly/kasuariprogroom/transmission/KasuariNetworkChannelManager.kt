package com.fevly.kasuariprogroom.transmission

import android.content.Context
import com.fevly.kasuarinet.KasuariServiceExposer

/*==========================================
Author : Fevly Pallar
contact : fevly.pallar@gmail.com
=========================================*/
class KasuariNetworkChannelManager(_context: Context) {
    var kasuariServiceExposer: KasuariServiceExposer
    lateinit var context: Context
    lateinit var threadChannel: Thread

    init {
        kasuariServiceExposer = KasuariServiceExposer()
        this.context = _context
    }

    /*=====================================
     note: 29032024

     channelnya sudah buka, handshake OK.

     Tapi :

     Antivirus masih intercept custome-internal-server yg saya buat,
     mesti exclude manual kalau AV nya minta....
     ===========================================================*/
    fun workerThread(serviceName: String, port: Int): Runnable {

        return Runnable {
            kasuariServiceExposer.exposeServicePlease(this.context, serviceName, port)
        }
    }

    fun exposeServicePleaseDoItNowKasuari(serviceName: String, port: Int) {
        threadChannel = Thread(workerThread(serviceName, port))
        threadChannel.start()

    }

}