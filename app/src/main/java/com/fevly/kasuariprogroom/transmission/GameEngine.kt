package com.fevly.kasuariprogroom.transmission

import com.fevly.kasuariprogroom.depend.KasuariServerEngine


/*==========================================
Author : Fevly Pallar
contact : fevly.pallar@gmail.com
=========================================*/


// note 30032023 : sluruh bagian disini coupled sama thread server (jadi tdk bisa client disini)
class GameEngine {

    var kasuariServerEngine: KasuariServerEngine


    init {
        kasuariServerEngine = KasuariServerEngine()

    }

    fun start(port : Int) {
        kasuariServerEngine.startServer(port)
    }
}

