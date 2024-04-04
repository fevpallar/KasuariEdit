package com.fevly.kasuariprogroom.textutil

import android.widget.EditText
import android.widget.ListView
import com.fevly.kasuariprogroom.CustomItem

class TextProcessing {

    fun replaceAndGetUpdatedtext(
        start: Int,
        count: Int,
        currText: String,
        editText: EditText,
        listView: ListView,
        filteredSuggestions: List<CustomItem>
    ) {

        listView.setOnItemClickListener { parent, view, position, id ->

            var tempCurrText = currText
            editText.setText("") // reset dulu
            var tempCount = count
            var tempStart = start
            var toReplaceWord = tempCurrText.substring(0, tempStart)  +filteredSuggestions.get(position).label
            editText.setText(toReplaceWord)

        }
    }
}