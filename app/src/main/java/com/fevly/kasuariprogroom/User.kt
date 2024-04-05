package com.fevly.kasuariprogroom
/*==========================================
Author : Fevly Pallar
contact : fevly.pallar@gmail.com
=========================================*/
import com.fevly.kasuariprogroom.datum.Datum
import com.fevly.kasuariprogroom.transmission.GameEngine
import com.fevly.kasuariprogroom.transmission.KasuariClientEngine
import java.io.*
import java.net.Socket


fun main() {


    var userID = System.currentTimeMillis().toString()
    var userData = "public class, didalam kelas, disekolah... atau apapun itu..terserah..."
    var currUserDatum = Datum(userID, userData)
    var user = User(currUserDatum)


    var userThread = Thread(
        user.connect(9999)
    )
    userThread.start()

    /*==================================================
    Sample proper response :
        Server started. Waiting for client...
        Client connected: 127.0.0.1
        src_||1712231434746||public class, didalam kelas, disekolah... atau apapun itu..terserah...
        client dapat src_||1712231434746||public class, didalam kelas, disekolah... atau apapun itu..terserah...
        ini di client 1712231434746
      =================================================

       03042024
       implementasi sementara

       cast --src---> server--src--> other users
            <--src---
       ===============================================*/

    val kasuariClientEngine = KasuariClientEngine(9999)
    var connClient = kasuariClientEngine.getConn()

    /*=====================================================
    note 30032023
     thread-flag cukup sulit (kalau nanti usernya sdh banyak).
     jadi, instead of boolean flagging,
     dari sini hint saja ke internal serving pakai mark tertentu
     untuk proses generate responnya
    ======================================================== */

    // board tdk usah pakai indicator
    user.send(connClient, user.dummyDataUntukTransmisi(currUserDatum, "src_"))

    while (true) {
        user.getMessage(connClient)

    }


}

class User(_datum: Datum) {

    lateinit var datum: Datum

    init {
        this.datum = _datum

    }

    fun dummyDataUntukTransmisi(datum: Datum, pre: String): Array<String> {

        var userId = datum.userID
        var userData = datum.text
        var tempListDatum = Array(1) { "" }
        tempListDatum[0] = pre + "||" + userId + "||" + userData
        return tempListDatum
    }


    fun getMessage(sock: Socket) {
        val inputStream = ObjectInputStream(sock.getInputStream())

        val receivedData = inputStream.readObject()


        if (receivedData.toString().startsWith("src_")) {

            var dataArray = receivedData.toString().split("||")
            var tempUserID = dataArray[1]
            var tempSrc = dataArray[2]
            if (tempUserID == this.datum.userID) {
                // user yg sama dgn current user..
                println("client dapat: " + receivedData)
            } else {
                // nanti kedepan implementasi..
            }
        }

    }

    fun send(sock: Socket, message: Array<String>) {
        val outputStream = sock.getOutputStream()
        val objectOutputStream = ObjectOutputStream(outputStream)
        objectOutputStream.writeObject(message)

    }


    fun connect(port: Int): Runnable {
        return Runnable {
            var gameEngine = GameEngine()
            gameEngine.start(port)
        }

    }

}

