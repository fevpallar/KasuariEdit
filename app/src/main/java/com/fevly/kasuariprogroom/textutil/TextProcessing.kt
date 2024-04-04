package com.fevly.kasuariprogroom.textutil

import android.widget.EditText
import android.widget.ListView
import com.fevly.kasuariprogroom.CustomItem

class TextProcessing {

    fun replaceAndGetUpdatedtext(
        start: Int,
        currText: String,
        editText: EditText,
        listView: ListView,
        filteredSuggestions: List<CustomItem>
    ) {

        listView.setOnItemClickListener { parent, view, position, id ->

            //  val selectedItem: CustomItem = filteredSuggestions[position]

            val tempCurrText = currText
            editText.setText("") // reset edittextnya

            var textToReplace = tempCurrText

            var counterSpace = 0;
            for (c in textToReplace) {
                if (c == ' ') counterSpace++
            }
            /*=============Note 29/03/24 :====================
                 sample textnya :
                 satu dua    (* char trakhir adlh space)

                Maka :
                count = 1
                counterSpace=2
                start= 8
                awal-index dari word yg mau diganti = (start - counterSpace) --> yaitu (8-2) = 6 (krn 0-base index, jdi 6-1)= 5
                akhir-index dari word yg mau diretain = (start - counterSpace) --> yaitu (8-2) = 6 (krn 0-base index, jdi 6-1)= 5
            =============================================
               note : issue 28/03/2024

                1). Kalau yg mau direplace cuma 1 word (seluruh text = 1 word)
                 posisi replacenya malah salah.

                 ** Persisnya tidak tahu kenapa...., aneh emang hidup.., ehhh, maksudnya eneh emang android UI ...

               2). Setelah replace, posisi cursor reset ke index-0 (secara visual bukan coding)
             ============================================*/

            Log.d("kasuariprogroom", "counterSpace = $counterSpace")

            var lastIndexPreviousWord = start - counterSpace;
            var startIndexWordYgMauDiganti =
                start - counterSpace // termasuk space sebelum kata itu

            var previousWord = tempCurrText.substring(0, lastIndexPreviousWord - 1)
            Log.d("kasuariprogroom", "previousWord = $previousWord")
            var toReplaceWord =
                tempCurrText.substring(
                    lastIndexPreviousWord - 1,
                    tempCurrText.length
                )
            Log.d("kasuariprogroom", "toReplaceWord = $toReplaceWord")
            var finalText = currText.replace(toReplaceWord, "baru")
            editText.setText(finalText)

        }
    }
}