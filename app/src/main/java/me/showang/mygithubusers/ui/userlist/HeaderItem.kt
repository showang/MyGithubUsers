package me.showang.mygithubusers.ui.userlist

import android.view.LayoutInflater
import android.view.ViewGroup
import me.showang.mygithubusers.R
import me.showang.recyct.RecyctViewHolder
import me.showang.recyct.items.RecyctItemBase

class HeaderItem : RecyctItemBase() {
    override fun create(inflater: LayoutInflater, parent: ViewGroup) =
        object : RecyctViewHolder(inflater, parent, R.layout.item_section_title) {
            override fun bind(data: Any, atIndex: Int) {}
        }
}