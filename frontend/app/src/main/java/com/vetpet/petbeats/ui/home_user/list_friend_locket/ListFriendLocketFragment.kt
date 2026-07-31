package com.vetpet.petbeats.ui.home_user.list_friend_locket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentInformationRoomBinding
import com.example.VetPet.databinding.FragmentListFriendLocketBinding
import com.example.VetPet.databinding.FragmentLocketBinding
import com.vetpet.petbeats.data.remote.api.ApiUserHome
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.vetpet.petbeats.data.repository.HomeUserRepository
import com.vetpet.petbeats.ui.home_user.informationroom.InformationRoomViewModel
import com.vetpet.petbeats.ui.home_user.informationroom.InformationRoomViewModelFactory
import kotlinx.coroutines.launch
import kotlin.getValue


class ListFriendLocketFragment : Fragment() {
    private var _binding: FragmentListFriendLocketBinding ?= null
    private val binding get() = _binding!!
    private val viewModel: ListFriendLocketViewModel by viewModels {
        ListFriendLocketViewModelFactory(
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
        _binding = FragmentListFriendLocketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClick() {

    }

    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->

                }
            }
        }
    }

    private fun eventData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->

                }
            }
        }
    }


}