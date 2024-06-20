package com.xpayback.usersquicklist.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xpayback.usersquicklist.databinding.UserListItemBinding
import com.xpayback.usersquicklist.model.User

class UserListAdapter(private val onUserClick: (User) -> Unit) :
    RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    private var users: List<User> = listOf()

    class UserViewHolder(
        private val binding: UserListItemBinding,
        private val onUserClick: (User) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {


        init {
            binding.root.setOnClickListener {
                binding.user.let { user ->
                    onUserClick(user!!)
                }
            }
        }

        fun bind(user: User) {
            binding.user = user
            binding.executePendingBindings()
            Glide.with(binding.root)
                .load(user.image)
                .into(binding.userImage)
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding, onUserClick)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    fun submitList(userList: List<User>) {
        users = userList
        notifyDataSetChanged()
    }
}



