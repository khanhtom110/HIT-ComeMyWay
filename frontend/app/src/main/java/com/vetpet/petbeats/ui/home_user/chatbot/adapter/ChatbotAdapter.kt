package com.vetpet.petbeats.ui.home_user.chatbot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.VetPet.databinding.ItemChatBotBinding
import com.example.VetPet.databinding.ItemChatBotTypingBinding
import com.example.VetPet.databinding.ItemChatUserBinding
import com.vetpet.petbeats.ui.home_user.chatbot.ChatbotEvent

class ChatbotAdapter(
    private val onItemClick: (Int) -> Unit
): ListAdapter<ChatbotEvent, RecyclerView.ViewHolder>(ChatbotDiffcallback()) {

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_BOT = 2
        private const val VIEW_TYPE_BOT_TYPING = 3
        private const val VIEW_TYPE_BOT_SUGGEST = 4
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ChatbotEvent.UserMessage -> VIEW_TYPE_USER
            is ChatbotEvent.BotMessage -> VIEW_TYPE_BOT
            is ChatbotEvent.BotTyping -> VIEW_TYPE_BOT_TYPING
            is ChatbotEvent.BotSuggest -> VIEW_TYPE_BOT_SUGGEST
        }
    }


    override fun onCreateViewHolder(holder: ViewGroup, position: Int): RecyclerView.ViewHolder {
        return when (position) {
            VIEW_TYPE_USER -> {
                val binding = ItemChatUserBinding.inflate(LayoutInflater.from(holder.context), holder, false)
                UserViewHolder(binding)
            }
            VIEW_TYPE_BOT -> {
                val binding = ItemChatBotBinding.inflate(LayoutInflater.from(holder.context), holder, false)
                BotViewHolder(binding)
            }
            VIEW_TYPE_BOT_TYPING -> {
                val binding = ItemChatBotTypingBinding.inflate(LayoutInflater.from(holder.context), holder, false)
                BotTypingViewHolder(binding)
            }
            VIEW_TYPE_BOT_SUGGEST -> {
                val binding = ItemChatBotBinding.inflate(LayoutInflater.from(holder.context), holder, false)
                BotSuggestViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Không có kiểu view này")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)

        when (holder) {
            is UserViewHolder -> {
                holder.bind(currentItem as ChatbotEvent.UserMessage)
            }
            is BotViewHolder -> {
                if (currentItem is ChatbotEvent.BotMessage) {
                    holder.bind(currentItem)
                }
                if (currentItem is ChatbotEvent.BotSuggest) {
                    holder.bind(currentItem.message)
                }
            }
            is BotTypingViewHolder -> {
                holder.bind()
            }
            is BotSuggestViewHolder -> {
                holder.bind(currentItem as ChatbotEvent.BotSuggest)
            }
        }
    }

    class UserViewHolder(private val binding: ItemChatUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ChatbotEvent.UserMessage) {
            binding.tvMessage.text = event.message
        }
    }

    class BotViewHolder(private val binding: ItemChatBotBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ChatbotEvent.BotMessage) {
            binding.tvMessage.text = event.message
        }
        fun bind(message: String) {
            binding.tvMessage.text = message
        }
    }

    inner class BotSuggestViewHolder(private val binding: ItemChatBotBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ChatbotEvent.BotSuggest) {
            binding.tvMessage.text = event.message

            binding.recycle.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(itemView.context, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)

            val clinicAdapter = ListClinicAdapter { id ->
                onItemClick(id)
            }
            binding.recycle.adapter = clinicAdapter
            clinicAdapter.submitList(event.clinic)
        }
    }

    class BotTypingViewHolder(private val binding: ItemChatBotTypingBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind() {

        }
    }


}