package me.showang.mygithubusers.ui.userlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import me.showang.mygithubusers.R
import me.showang.mygithubusers.model.UserInfo
import me.showang.recyct.RecyctViewHolder
import me.showang.recyct.items.RecyctItemBase

class UserInfoItem : RecyctItemBase() {
    override fun create(inflater: LayoutInflater, parent: ViewGroup): RecyctViewHolder {
        return object : RecyctViewHolder(inflater, parent, R.layout.item_user_info) {
            private val coverImage: ImageView by id(R.id.coverImage)
            private val titleText: TextView by id(R.id.titleText)
            private val staffBadge: View by id(R.id.staffBadge)

            override fun bind(data: Any, atIndex: Int) {
                when (data) {
                    is UserInfo -> data.bind()
                }
            }

            private fun UserInfo.bind() {
                Glide.with(coverImage)
                    .load(avatarUrl)
                    .circleCrop()
                    .into(coverImage)
                titleText.text = account
                staffBadge.isVisible = isStaff
            }
        }
    }

}