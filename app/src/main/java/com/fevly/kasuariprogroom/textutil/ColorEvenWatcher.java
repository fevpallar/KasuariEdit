package com.fevly.kasuariprogroom.textutil;
/*==========================================
Author : Fevly Pallar
contact : fevly.pallar@gmail.com
=========================================*/
import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

public class ColorEvenWatcher {
    private EditText editText;

    public ColorEvenWatcher(EditText editText) {
        this.editText = editText;
    }

    public void setSpan(String targetText, Editable s) {
        String text = editText.getText().toString();
        int startIndex = text.indexOf(targetText);
        if (startIndex != -1) {
            int endIndex = startIndex + targetText.length();
            // Apply color to the target text
            s.setSpan(new ForegroundColorSpan(Color.BLUE), startIndex, endIndex, 0);
        }
    }

}
