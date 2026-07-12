package com.example.petbeats.ui.home.historyBook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.petbeats.R
import com.example.petbeats.data.remote.api.ApiHome
import com.example.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.example.petbeats.data.repository.HomeRepository
import com.example.petbeats.databinding.FragmentHistoryBookBinding
import com.example.petbeats.ui.home.book.BookViewModel
import com.example.petbeats.ui.home.book.BookViewModelFactory
import com.example.petbeats.ui.home.book.adapter.BookAdapter
import kotlin.getValue


class HistoryBookFragment : Fragment() {
    private var _binding: FragmentHistoryBookBinding ?= null
    private val binding get() = _binding!!
    private lateinit var adapter: BookAdapter
    private val viewModel: BookViewModel by viewModels {
        BookViewModelFactory(
            HomeRepository(
                RetrofitInstance.getAuthRetrofit(requireContext()).create(ApiHome::class.java)
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_book, container, false)
    }


}