package com.fevly.kasuariprogroom.constants/*==========================================
Author : Fevly Pallar
contact : fevly.pallar@gmail.com
=========================================*/
import com.fevly.kasuariprogroom.CustomItem
import com.fevly.kasuariprogroom.R

class Keywords {
    private var keyWordMap = mutableMapOf<String, Int>()
    private var keyWordList = mutableListOf<CustomItem>()

    init {
        keyWordMap = mutableMapOf()
        keyWordList = mutableListOf()
    }

    fun addKeyword(newKeyword: String, category: Char) {
        if (category == 'c') this.keyWordMap.put(newKeyword, R.drawable.c)
        if (category == 'f') this.keyWordMap.put(newKeyword, R.drawable.f)

        /*=========================================================
        Kategory 'u' adlh user keyword, program bakalan
        caching apapun yg diketik user dan transform menjadi keywords
        ========================================================= */
        if (category == 'u') this.keyWordMap.put(newKeyword, R.drawable.u)
    }

    // adapter list butuh tipe List<CustomeItem>, gak bisa inflate lgngsung dari map
    // so butuh konversi dulu
    fun mapToList() {
        for (keyValuenya in this.keyWordMap) {
            val item = CustomItem(keyValuenya.value, keyValuenya.key)
            keyWordList.add(item)
        }
    }
    fun getKewordList (): MutableList<CustomItem>{
        return this.keyWordList
    }
}