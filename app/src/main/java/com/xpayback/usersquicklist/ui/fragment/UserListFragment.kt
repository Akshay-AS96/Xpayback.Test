package com.xpayback.usersquicklist.ui.fragment

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xpayback.usersquicklist.R
import com.xpayback.usersquicklist.databinding.FragmentUserListBinding
import com.xpayback.usersquicklist.model.User
import com.xpayback.usersquicklist.network.CloudManager
import com.xpayback.usersquicklist.repository.UsersRepository
import com.xpayback.usersquicklist.ui.adapter.UserListAdapter
import com.xpayback.usersquicklist.viewmodel.UsersViewModel
import com.xpayback.usersquicklist.viewmodel_factory.UserListViewModelFactory


class UserListFragment : Fragment() {

    private lateinit var binding: FragmentUserListBinding
    private val userService = CloudManager.apiService
    private val userRepository = UsersRepository(userService)
    private lateinit var layoutManagerUser: LinearLayoutManager
    private var userList: MutableList<User> = mutableListOf()
    private val userListViewModel: UsersViewModel by viewModels {
        UserListViewModelFactory(
            userRepository
        )
    }
    private lateinit var usersAdapter: UserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userListViewModel.loadUsers()
        onClickRecyclerview()
        initRecyclerview()
        observeViewModel()
    }

    private fun onClickRecyclerview() {
        usersAdapter = UserListAdapter {
            val args = Bundle()
            args.putInt("user_id", it.id)
            findNavController().navigate(R.id.action_UserListFragment_to_UserFragment, args)
        }
    }

    private fun initRecyclerview() {
        userListViewModel.loadingState.observe(viewLifecycleOwner) {
            binding.progress.visibility = if (it) View.VISIBLE else View.GONE
        }
        layoutManagerUser = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.list.apply {
            addOnScrollListener(recyclerViewOnScrollListenerUser)
            layoutManager = layoutManagerUser
            adapter = usersAdapter
        }
    }

    private fun observeViewModel() {
        userListViewModel.userList.observe(viewLifecycleOwner) { products ->
            products.forEach {
                userList.add(it)
            }
            usersAdapter.submitList(userList)
        }

        userListViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }


    private val recyclerViewOnScrollListenerUser = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val firstVisibleItemPosition = layoutManagerUser.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManagerUser.childCount
            val totalItemCount = layoutManagerUser.itemCount
            if (dy > 0) { //scrolled down
                println("scrolled down")
                userPagination(visibleItemCount, firstVisibleItemPosition, totalItemCount)
            }
        }
    }

    private fun userPagination(
        visibleItemCount: Int,
        firstVisibleItemPosition: Int,
        totalItemCount: Int
    ) { //pagination
        if (!userListViewModel.isLoading() && !userListViewModel.isLastPage()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                Log.d("pagination", "called")
                userListViewModel.loadMoreUsers()
            }
        }
    }


}