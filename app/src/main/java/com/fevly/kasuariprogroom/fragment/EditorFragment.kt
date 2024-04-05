
package com.fevly.kasuariprogroom.fragment

/*==========================================
Author : Fevly Pallar
contact : fevly.pallar@gmail.com
=========================================*/
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.fevly.kasuariprogroom.CustomAdapter
import com.fevly.kasuariprogroom.CustomItem
import com.fevly.kasuariprogroom.R
import com.fevly.kasuariprogroom.constants.Keywords
import com.fevly.kasuariprogroom.storage.StorageManager
import com.fevly.kasuariprogroom.storage.StorageUtil
import com.fevly.kasuariprogroom.textutil.ColorEvenWatcher
import com.fevly.kasuariprogroom.textutil.TextProcessing
import com.fevly.kasuariprogroom.transmission.KasuariNetworkChannelManager


class EditorFragment : Fragment() {

    private lateinit var editText: EditText
    private lateinit var listView: ListView
    private lateinit var save: Button
    private lateinit var open: Button

    lateinit var storageUtil: StorageUtil
    lateinit var storageManager: StorageManager

    lateinit var textProcessing: TextProcessing
    lateinit var kasuariNetworkChannelManager : KasuariNetworkChannelManager

    lateinit var keyword: Keywords

    private fun updateSuggestions(filteredSuggestions: List<CustomItem>) {
        val adapter = CustomAdapter(requireContext(), filteredSuggestions)
        listView.adapter = adapter
        if (filteredSuggestions.isEmpty())
            listView.visibility = ListView.GONE
        else
            listView.visibility = ListView.VISIBLE

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.editor_fragment, container, false)

//        connect = view.findViewById<Button>(R.id.connect)

        keyword = Keywords()
        keyword.addKeyword("class", 'c')
        keyword.addKeyword("String", 'c')
        keyword.addKeyword("Float", 'c')
        keyword.addKeyword("FileOutputStream", 'c')
        keyword.addKeyword("main", 'f')
        keyword.addKeyword("Integer", 'c')

        keyword.mapToList() // convert list ke map (tdk bisa inflate lgsung dari map)


        textProcessing = TextProcessing()
        storageUtil = StorageUtil()
        storageManager = StorageManager(requireContext())
        kasuariNetworkChannelManager = KasuariNetworkChannelManager(requireContext())
        kasuariNetworkChannelManager.exposeServicePleaseDoItNowKasuari("sampleservicename",9999)


        editText = view.findViewById(R.id.editText)
        listView = view.findViewById(R.id.listview)
        save = view.findViewById(R.id.save)
        open = view.findViewById(R.id.browse)



        val adapter = CustomAdapter(requireContext(), keyword.getKewordList())
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

                Log.d("kasuariprogroom", "curr text = $currText")
                Log.d("kasuariprogroom", "count = $count")
                Log.d("kasuariprogroom", "start = $start")

                // note 040424, start = total chars + total current space
                // start tetap sama diposisi manapun, terupdate saat space ditrigger
                if (count == 1 && currText[start] != ' ') { // trigger diposisi mana sj yg match keyword
                    val parts = currText.split("\\s+".toRegex())
                    val lastPart: String = parts.lastOrNull() ?: ""
                    //Log.d("kasuariprogroom", "lastPart = $lastPart")
                    val filterText: String = lastPart.trim().toLowerCase()
                    //Log.d("kasuariprogroom", "filterText = $filterText")

                    val filteredSuggestions =
                        keyword.getKewordList().filter {
                            it.label.toLowerCase().startsWith(filterText)
                        }
                    textProcessing.replaceAndGetUpdatedtext(
                        start,
                        count,
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

            override fun afterTextChanged(s: Editable?) {
                var cIW = ColorEvenWatcher(editText)
                cIW.setSpan("Float",s)
                cIW.setSpan("Integer",s)
            }
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



        return view
    }


}