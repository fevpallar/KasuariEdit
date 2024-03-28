package com.fevly.kasuariprogroom.storage/*==========================================
Author : Fevly Pallar
contact : fevly.pallar@gmail.com
=========================================*/
import android.content.Context
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class StorageManager(_context: Context) {


    private lateinit var context: Context
    private lateinit var thread: Thread
    private var uiHandler: Handler
    private lateinit var loadedBuffer: StringBuilder

    init {
        this.context = _context
        uiHandler =
            Handler(Looper.getMainLooper()) // looper ui global, jdi pakai bebas dimanapun disini, safe!.
       loadedBuffer= StringBuilder()
    }


     fun loadData(fileName: String, editText: EditText) {
        /*=======================================================
        28/03/2024 issue

        note to future me:

        maunya ini buat bagaimana cara supaya return buffer lalu lempar ke UI.

        Masalahnya adalah :  karena disini worker thread. saat load text,
        thread yg disini overlap dgn UI thread (jadi ui thread langsung ngeset tanpa nunggu thread disini selesai),
        dgn kata lain buffer belum diflush, ehhh.. ui thread langsung eksekusi .. (aka napsu)
        alhasil 2 threads ini deadlock dan kesluruhan aplikasi macet

        sementara couple dulu loadData dng main thread,
        besok2 decoupled.
        ======================================================= */
        Thread(Runnable {
            try {
                val input: FileInputStream = this.context.openFileInput(fileName)
                input.use {
                    var buffer = StringBuilder()
                    var bytes_read = input.read()
                    while (bytes_read != -1) {
                        buffer.append(bytes_read.toChar())
                        bytes_read = input.read()
                    }

                    uiHandler.post({
                        editText.setText(buffer)
                    })


                    input.close()
                    this.loadedBuffer = buffer
                    Log.d("kasuariprogroom", "$loadedBuffer")
                }
            } catch (fnfe: FileNotFoundException) {
                uiHandler.post({
                    Toast.makeText(
                        this.context, "Data tdk ditemukan : $fnfe", Toast.LENGTH_SHORT
                    ).show()
                })
            } catch (ioe: IOException) {
                uiHandler.post({
                    Toast.makeText(
                        this.context, "Data tidak ditemukan: $ioe", Toast.LENGTH_SHORT
                    ).show()
                })
            }
        }).start()

    }

    fun isExternalStorageWritable(): Boolean {
        return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
    }

    fun saveDatePrivate(
        content: String, fileName: String
    ) {

        this.thread =
            Thread(Runnable { // Note 28/03/2023: worker-thread,  selalu update pakai dgn looper milik main-thread
                try {
                    val out: FileOutputStream =
                        this.context.openFileOutput(fileName, Context.MODE_PRIVATE)
                    out.use {
                        out.write(content.toByteArray())
                        out.close()

                        // 28032024 transform lambda ini ke external methods
                        uiHandler.post({
                            Toast.makeText(
                                this.context, "Data tersimpn", Toast.LENGTH_SHORT
                            ).show()
                        })
                    }

                } catch (ioe: IOException) {
                    uiHandler.post({
                        Toast.makeText(
                            this.context, "Error : $ioe", Toast.LENGTH_SHORT
                        ).show()
                    })
                }
            })
        this.thread.start()
    }
}