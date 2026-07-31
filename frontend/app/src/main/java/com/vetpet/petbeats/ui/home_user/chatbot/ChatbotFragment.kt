package com.vetpet.petbeats.ui.home_user.chatbot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentChatbotBinding
import com.example.VetPet.databinding.FragmentConfirmAppointmentBinding
import com.vetpet.petbeats.data.remote.api.ApiUserHome
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.vetpet.petbeats.data.repository.HomeUserRepository
import com.vetpet.petbeats.ui.home_user.chatbot.adapter.ChatbotAdapter
import kotlinx.coroutines.launch
import kotlin.getValue


class ChatbotFragment : Fragment() {
    private var _binding: FragmentChatbotBinding ?= null
    private val binding get() = _binding!!
    private lateinit var chatbotAdapter: ChatbotAdapter
    private val viewModel: ChatbotViewModel by viewModels {
        ChatbotViewModelFactory(
            HomeUserRepository(
                RetrofitInstance.getAuthRetrofit(requireContext()).create(ApiUserHome::class.java)
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentChatbotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatbotAdapter = ChatbotAdapter()
        binding.recycle.adapter = chatbotAdapter

        setOnClick()
        stateData()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClick() {
        binding.editAskChat.addTextChangedListener {
            viewModel.onChatUserChange(it.toString())
        }

        binding.btnPushMessage.setOnClickListener {
            viewModel.onChatBot()
        }
    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    if (state.isChatUser) {
                        binding.editAskChat.setBackgroundResource(R.drawable.button_input)
                        binding.note.setImageResource(R.drawable.note_blue)
                        binding.btnPushMessage.setImageResource(R.drawable.up_blue)
                    }
                    else {
                        binding.editAskChat.setBackgroundResource(R.drawable.tittle_search)
                        binding.note.setImageResource(R.drawable.note)
                        binding.btnPushMessage.setImageResource(R.drawable.up)
                    }

                    if (state.isLogo) {
                        binding.logo.visibility = View.GONE
                        binding.textLogo.visibility = View.GONE
                    }




                    //Check input
                    if (binding.editAskChat.text.toString() != state.chatUser) {
                        binding.editAskChat.setText(state.chatUser)
                    }

                }
            }
        }
    }

    private fun eventData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    // Đẩy dữ liệu vào RecyclerView
                     chatbotAdapter.submitList(event.toList())

                    // Tự động cuộn xuống cuối cùng
                    if (event.isNotEmpty()) {
                        binding.recycle.post {
                            binding.recycle.smoothScrollToPosition(event.size - 1)
                        }
                    }
                }
            }
        }
    }

}