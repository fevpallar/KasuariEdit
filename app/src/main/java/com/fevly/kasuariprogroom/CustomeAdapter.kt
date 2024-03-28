package com.fevly.kasuariprogroom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class CustomAdapter(
    context: Context,
    private val items: List<CustomItem>
) : ArrayAdapter<CustomItem>(context, R.layout.list_item_custom, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_custom, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val item = items[position]
        viewHolder.iconImageView.setImageResource(item.iconResourceId)
        viewHolder.labelTextView.text = item.label

        return view!!
    }

    private class ViewHolder(view: View) {
        val iconImageView: ImageView = view.findViewById(R.id.icon)
        val labelTextView: TextView = view.findViewById(R.id.label)
    }
}
