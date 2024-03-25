/*==========================================
Author : Fevly Pallar
contact : fevly.pallar@gmail.com
=========================================*/
package com.fevly.kasuariedit

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fevly.kasuariedit.storage.Permission
import com.fevly.kasuariedit.storage.StorageUtil


class MainActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var listView: ListView

    lateinit var permission: Permission
    lateinit var storageUtil: StorageUtil

    private lateinit var adapter: ArrayAdapter<String>

    private val suggestions = listOf("Apple", "Banana", "Orange", "Mango", "Grapes")


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 12 && resultCode == Activity.RESULT_OK) {
            val fileUri: Uri? = data?.data // uri dari data yg ditarget oleh Intent ini

            var currSelectedFile =
                fileUri //content://com.android.providers.media.documents/document/image%3A44319
            Log.d("kasuariedit", "uri file yg diselect " + currSelectedFile)
            val fileNamed = fileUri?.let { storageUtil.getFileNameFromUri(this, it) }
            if (fileNamed != null) {
                // Do something with the filename
                Log.d("kasuariedit", "namanya " + fileNamed)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permission = Permission()
        storageUtil = StorageUtil()

        editText = findViewById(R.id.editText)
        listView = findViewById(R.id.listView)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, suggestions)
        listView.adapter = adapter


        permission.askRuntimePermission(this)

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
//      val urinya = FileProvider.getUriForFile(this, "com.fevly.kasuariedit.provider", File(this.filesDir, "myfiles"))
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        intent.setDataAndType(urinya, "text/plain")
        intent.setType("application/pdf")
        startActivityForResult(intent, 12)

        // Show suggestions when user types
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val text = s.toString()
                /*
                count selalu sama dng 1 jika space diketik diposisi manapun
                waktu count=1, start adalah current index(0-based) dari count(space) itu yaitu
                panjang text sebelumnya
                 */
                Log.d("kasuariedit", "$text")
                Log.d("kasuariedit", "count = $count")
                Log.d("kasuariedit", "start = $start")
                /*Log.d("kasuariedit","")
                Log.d("kasuariedit","")*/
                if (count == 1 && text[start] == ' ') { // Check if the last character typed is a space
                    val parts = text.split("\\s+".toRegex()) // Split text by spaces
                    val lastPart = parts.lastOrNull() ?: ""
                    val filterText = lastPart.trim().toLowerCase()
                    val filteredSuggestions =
                        suggestions.filter { it.toLowerCase().startsWith(filterText) }
                    updateSuggestions(filteredSuggestions)
                } else {
                    // If the last character typed is not a space, clear the suggestions
                    updateSuggestions(emptyList())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateSuggestions(filteredSuggestions: List<String>) {
        val newAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, filteredSuggestions)
        listView.adapter = newAdapter

        // Show/hide the ListView based on if there are suggestions
        if (filteredSuggestions.isEmpty()) {
            listView.visibility = ListView.GONE
        } else {
            listView.visibility = ListView.VISIBLE
        }
    }
}

