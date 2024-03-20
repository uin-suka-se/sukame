package com.sukase.sukame.ui.conversation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.sukase.core.domain.model.ConversationModel
import com.sukase.sukame.R
import com.sukase.sukame.databinding.ItemConversationBinding

class ConversationAdapter : RecyclerView.Adapter<ConversationAdapter.ListViewHolder>() {
    private lateinit var binding: ItemConversationBinding
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: ConversationModel)
        fun onProfileClicked(profile: String)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        binding =
            ItemConversationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder()
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    inner class ListViewHolder : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ConversationModel) {
            binding.apply {
                profileImage.load(R.drawable.ic_person) {
                    crossfade(true)
                    placeholder(R.drawable.ic_person)
                    transformations(CircleCropTransformation())
                }
                username.text = data.name
                lastMessage.text = data.lastMessage
                tvLastMessageTime.text = data.dateTime
                profileImage.setOnClickListener {
                    Log.d("conversation", "masuk click photo")
                    onItemClickCallback.onProfileClicked(data.photo)
                }
            }
            itemView.setOnClickListener {
                Log.d("conversation", "masuk click semua")
                onItemClickCallback.onItemClicked(data)
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<ConversationModel>() {
        override fun areItemsTheSame(
            oldItem: ConversationModel,
            newItem: ConversationModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ConversationModel,
            newItem: ConversationModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}