package com.xpayback.usersquicklist.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xpayback.usersquicklist.databinding.UserListItemBinding
import com.xpayback.usersquicklist.model.User

// Adapter for displaying a list of users in a RecyclerView
class UserListAdapter(private val onUserClick: (User) -> Unit) :
    RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    private var users: List<User> = listOf() // List to store user data

    // ViewHolder for individual user items
    class UserViewHolder(
        private val binding: UserListItemBinding,
        private val onUserClick: (User) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            // Set click listener on the item view
            binding.root.setOnClickListener {
                binding.user?.let { user ->
                    onUserClick(user)
                }
            }
        }

        // Binds user data to the view
        fun bind(user: User) {
            binding.user = user // Set user data for data binding
            binding.executePendingBindings()

            // Load user image using Glide
            Glide.with(binding.root)
                .load(user.image)
                .into(binding.userImage)
        }
    }

    // Creates a new ViewHolder instance
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding, onUserClick)
    }

    // Binds user data to the ViewHolder at the specified position
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    // Returns the total number of items in the list
    override fun getItemCount(): Int = users.size

    // Updates the user list and notifies the adapter of changes
    fun submitList(userList: List<User>) {
        users = userList
        notifyDataSetChanged()
    }
}