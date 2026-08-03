package com.vetpet.petbeats.ui.home_user.list_friend_locket

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.VetPet.R
import com.example.VetPet.databinding.FragmentListFriendLocketBinding
import com.example.VetPet.databinding.LayoutPopupDialogBinding
import com.vetpet.petbeats.data.remote.api.ApiUserHome
import com.vetpet.petbeats.data.remote.retrofitInstance.RetrofitInstance
import com.vetpet.petbeats.data.repository.HomeUserRepository
import com.vetpet.petbeats.ui.home_user.list_friend_locket.adapter.AddFriendLocketAdapter
import com.vetpet.petbeats.ui.home_user.list_friend_locket.adapter.MyFriendLocketAdapter
import kotlinx.coroutines.launch
import kotlin.getValue


class ListFriendLocketFragment : Fragment() {
    private var _binding: FragmentListFriendLocketBinding ?= null
    private val binding get() = _binding!!
    private lateinit var addFriendLocketAdapter: AddFriendLocketAdapter
    private lateinit var myFriendLocketAdapter: MyFriendLocketAdapter
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


        clickListAddFriend()
        clickListMyFriend()

        binding.recycleAddFriend.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleAddFriend.adapter = addFriendLocketAdapter
        binding.recycleMyFriend.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleMyFriend.adapter = myFriendLocketAdapter


        viewModel.onAddFriendList()
        viewModel.onMyFriendList()


        setOnClick()
        stateData()
        eventData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClick() {
        binding.btnBack.setOnClickListener {
            viewModel.locketClick()
        }
    }



    private fun clickListAddFriend() {
        addFriendLocketAdapter = AddFriendLocketAdapter(
            onAddClick = { id ->
                viewModel.itemClickAddFriend(id)
            },
            onCancelClick = { id ->
                viewModel.itemClickCancelFriend(id)
            }
        )
    }
    private fun clickListMyFriend() {
        myFriendLocketAdapter = MyFriendLocketAdapter { id ->
            showPopupDialog(
                message = "Bạn có chắc muốn xoá người bạn này không?",
                leftButton = "Hủy",
                rightButton = "Xóa",
                onRightButtonClick = {
                    viewModel.itemClickMyFriend(id)
                }
            )
        }
    }



    private fun showPopupDialog(
        message: String,
        leftButton: String,
        rightButton: String,

        onLeftButtonClick: (() -> Unit)? = null,
        onRightButtonClick: (() -> Unit)? = null
    ) {
        //Khởi tạo binding
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogBinding = LayoutPopupDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

        //Xử lý giao diện
        dialogBinding.tvDialogTitle.text = message
        dialogBinding.btnLeft.text = leftButton
        dialogBinding.btnRight.text = rightButton

        dialogBinding.btnLeft.setOnClickListener {
            dialog.dismiss()
            onLeftButtonClick?.invoke()
        }

        dialogBinding.btnRight.setOnClickListener {
            dialog.dismiss()
            onRightButtonClick?.invoke()
        }

        dialog.show()
    }



    private fun stateData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    addFriendLocketAdapter.submitList(state.pendingFriend)
                    myFriendLocketAdapter.submitList(state.myFriend)



                    //check pendingFriend
                    if (state.pendingFriend != emptyList<AddFriendLocketAdapter>()) {
                        binding.tvAddFriend.visibility = View.VISIBLE
                        binding.recycleAddFriend.visibility = View.VISIBLE
                    }
                    else {
                        binding.tvAddFriend.visibility = View.GONE
                        binding.recycleAddFriend.visibility = View.GONE
                    }

                }
            }
        }
    }

    private fun eventData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is ListFriendLocketEvent.NavigationLocket -> {
                            findNavController().navigate(R.id.listFriendLocket_locket)
                        }
                    }
                }
            }
        }
    }


}