/*==========================================
Author : Fevly Pallar
contact : fevly.pallar@gmail.com
=========================================*/
package com.fevly.kasuariprogroom

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fevly.kasuariprogroom.constants.Keywords
import com.fevly.kasuariprogroom.storage.Permission
import com.fevly.kasuariprogroom.storage.StorageManager
import com.fevly.kasuariprogroom.storage.StorageUtil


class MainActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var listView: ListView
    private lateinit var save: Button
    private lateinit var open: Button

    lateinit var permission: Permission
    lateinit var storageUtil: StorageUtil
    lateinit var storageManager: StorageManager

    lateinit var textProcessing: TextProcessing

    lateinit var keyword: Keywords


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // [sementara] init custome keywords
        keyword = Keywords()
        keyword.addKeyword("class", 'c')
        keyword.addKeyword("main", 'f')
        keyword.addKeyword("Integer", 'c')

        keyword.mapToList() // convert list ke map (tdk bisa inflate lgsung dari map)


        textProcessing = TextProcessing()
        permission = Permission()
        storageUtil = StorageUtil()
        storageManager = StorageManager(applicationContext)

        editText = findViewById(R.id.editText)
        listView = findViewById(R.id.listview)
        save = findViewById(R.id.save)
        open = findViewById(R.id.browse)

        permission.askRuntimePermission(this)

        val adapter = CustomAdapter(this, keyword.getKewordList())
        listView.adapter = adapter


        /*=============================================================================
               Note: disini *issue description 25/03/2024 untuk akses internal app'dir

             ==> Akses ke root internal app dir. sungguh, aduhaii..memusingkan

              why?

              - semenjak android >=10 . Priviledge system folder  sangat ketat
                buth implementasi custome dari SAF (storage access framework)
              - SAF. at least untuk implementasi disini. belum juga bisa grant priviledge ke internal dir. target
               (bisa akses) tapi untuk bound file picker via Intent belum bisa
              - exposing URI target generates "uri exposed exception.."


             Misc issue
                - exposing URI langsung ke intent seperti diblok secara internal dari android semenjak Android >=10.
                 Entah karena rule tersebut ataukah memang ada semacam "restriction" di Sony Xperia ku.
                -implementasi Uri internal api dari JAVA berbeda dgn Android
                - protocol uri ada semacam 2. yg satu "file://" yg satunya (via SAF) "content//"

             Attempts :

                -  berbagai macam cara sy coba dari parsing root uri, sana-sini. Tapi tidak mau bound ke Intent

================================================================================================*/
//      val urinya = FileProvider.getUriForFile(this, "com.fevly.kasuariprogroom.provider", File(this.filesDir, "myfiles"))
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        intent.setDataAndType(urinya, "text/plain")
        intent.setType("application/pdf")
//        startActivityForResult(intent, 12)

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val currText = s.toString()
                /*=========================================================================
                count selalu sama dng 1 jika space diketik diposisi manapun

                waktu count=1 ==>> "start" = panjang sluruh text sebelumnya + 1 (space),
                tapi karena 0-index based alhasil
                "start" == panjang seluruh text sebelumnya (include space2 sebelumnya)
                 ==========================================================================*/
                Log.d("kasuariprogroom", "$currText")
                Log.d("kasuariprogroom", "count = $count")
                Log.d("kasuariprogroom", "start = $start")
                /*Log.d("kasuariprogroom","")
                Log.d("kasuariprogroom","")*/
                if (count == 1 && currText[start] == ' ') { // Check if the last character typed is a space
                    val parts = currText.split("\\s+".toRegex()) // Split text by spaces
                    val lastPart: String = parts.lastOrNull() ?: ""
                    Log.d("kasuariprogroom", "lastPart = $lastPart")

                    val filterText: String = lastPart.trim().toLowerCase()
                    Log.d("kasuariprogroom", "filterText = $filterText")

                    val filteredSuggestions =
                        keyword.getKewordList().filter {
                            it.label.toLowerCase().startsWith(filterText)
                        }
                    textProcessing.replaceAndGetUpdatedtext(
                        start,
                        currText,
                        editText,
                        listView,
                        filteredSuggestions
                    )

                    updateSuggestions(filteredSuggestions)
                } else {
                    updateSuggestions(emptyList())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        save.setOnClickListener(
            View.OnClickListener {
                if (storageManager.isExternalStorageWritable()) {
                    storageManager.saveDatePrivate(editText.text.toString(), "samplename.java")
                }
            }
        )

        open.setOnClickListener(
            View.OnClickListener {
                if (storageManager.isExternalStorageWritable()) {
                   storageManager.loadData("samplename.java", editText)

                }
            }
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 12 && resultCode == Activity.RESULT_OK) {
            val fileUri: Uri? = data?.data // uri dari data yg ditarget oleh Intent ini

            var currSelectedFile =
                fileUri //content://com.android.providers.media.documents/document/image%3A44319
            Log.d("kasuariprogroom", "uri file yg diselect " + currSelectedFile)
            val fileNamed = fileUri?.let { storageUtil.getFileNameFromUri(this, it) }
            if (fileNamed != null) {
                // Do something with the filename
                Log.d("kasuariprogroom", "namanya " + fileNamed)
            }

            Toast.makeText(
                this,
                "File name = " + fileNamed.toString() + "\n" + "Uri = " + fileUri.toString(),
                Toast.LENGTH_LONG
            ).show()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateSuggestions(filteredSuggestions: List<CustomItem>) {
        val adapter = CustomAdapter(this, filteredSuggestions)
        listView.adapter = adapter
        if (filteredSuggestions.isEmpty())
            listView.visibility = ListView.GONE
        else
            listView.visibility = ListView.VISIBLE

    }
}

