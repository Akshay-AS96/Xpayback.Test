package com.xpayback.usersquicklist.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.xpayback.usersquicklist.R
import com.xpayback.usersquicklist.databinding.FragmentUserBinding
import com.xpayback.usersquicklist.network.CloudManager
import com.xpayback.usersquicklist.repository.UsersRepository
import com.xpayback.usersquicklist.viewmodel.UsersViewModel
import com.xpayback.usersquicklist.viewmodel_factory.UserListViewModelFactory

// Fragment to display details of a single user
class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding

    // Services and repositories for fetching user data
    private val userService = CloudManager.apiService
    private val userRepository = UsersRepository(userService)

    // ViewModel for managing user details
    private val userListViewModel: UsersViewModel by viewModels {
        UserListViewModelFactory(userRepository)
    }

    private var userId: Int = 0 // ID of the user to display


    // Inflates the layout for the fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Observes data from the ViewModel
    private fun observeViewModel() {
        // Observe the user data and update the UI
        userListViewModel.userData.observe(viewLifecycleOwner) { user ->
            Glide.with(binding.root) // Load user image using Glide
                .load(user.image)
                .circleCrop()
                .into(binding.ivProfile)

            // Set text values for various user details
            binding.tvStudentName.text = user.username.uppercase()
            binding.tvNameOfRole.text = user.role
            binding.tvMobileNumber.text = user.phone
            binding.tvCompany.text = user.company.name
            binding.userFullName.text = "${user.firstName} ${user.lastName}"
            binding.useEmail.text = user.email
            binding.userAge.text = user.age.toString()
            binding.userGender.text = user.gender
            binding.userBirthDate.text = user.birthDate
            binding.userHeightWeight.text = "${user.height} / ${user.weight}"
            binding.userEyeColor.text = user.eyeColor
            binding.userHairColor.text = user.hair.toString()
            binding.userBloodGroup.text = user.bloodGroup
            binding.userIpMac.text = user.macAddress
            binding.userUniversity.text = user.university
            binding.userEinSsn.text = user.ssn
            binding.userAgent.text = user.userAgent
            binding.userCrypto.text = user.crypto.toString()
            binding.userRole.text = user.role
        }

        // Observe error messages and display them as Toasts
        userListViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve user ID from arguments
        val arguments = arguments
        val receivedData = arguments?.getInt("user_id")
        userId = receivedData ?: 0

        // Fetch user details using the ViewModel
        userListViewModel.userDetails(userId)
        observeViewModel()
    }
}