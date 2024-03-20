package com.sukase.sukame.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sukase.core.domain.model.ChatModel
import com.sukase.sukame.databinding.ItemChatBinding

class ChatAdapter(private val uid: String) : RecyclerView.Adapter<ChatAdapter.ListViewHolder>() {
    private lateinit var binding: ItemChatBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder()
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    inner class ListViewHolder : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ChatModel) {
            with(binding) {
                if (data.sender.id != uid) {
                    senderLayout.senderItem.visibility = View.GONE
                    receiverLayout.apply {
                        receiverItem.visibility = View.VISIBLE
                        receiverMessageText.text = data.message
                        receiverChatTimestamp.text = data.datetime
                    }
                } else {
                    receiverLayout.receiverItem.visibility = View.GONE
                    senderLayout.apply {
                        senderItem.visibility = View.VISIBLE
                        senderMessageText.text = data.message
                        senderChatTimestamp.text = data.datetime
                    }
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<ChatModel>() {
        override fun areItemsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}