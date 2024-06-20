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
import com.xpayback.usersquicklist.databinding.FragmentUserListBinding
import com.xpayback.usersquicklist.network.CloudManager
import com.xpayback.usersquicklist.repository.UsersRepository
import com.xpayback.usersquicklist.viewmodel.UsersViewModel
import com.xpayback.usersquicklist.viewmodel_factory.UserListViewModelFactory

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private val userService = CloudManager.apiService
    private val userRepository = UsersRepository(userService)
    private val userListViewModel: UsersViewModel by viewModels {
        UserListViewModelFactory(
            userRepository
        )
    }
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun observeViewModel() {
        userListViewModel.userData.observe(viewLifecycleOwner) {
            Glide.with(binding.root)
                .load(it.image)
                .circleCrop()
                .into(binding.ivProfile)
            binding.tvStudentName.text=it.username.uppercase()
            binding.tvNameOfRole.text=it.role
            binding.tvMobileNumber.text=it.phone
            binding.tvCompany.text=it.company.name
            binding.userFullName.text="${it.firstName} ${it.lastName}"
            binding.useEmail.text=it.email
            binding.userAge.text= it.age.toString()
            binding.userGender.text=it.gender
            binding.userBirthDate.text=it.birthDate
            binding.userHeightWeight.text=it.height.toString()+" / "+it.weight
            binding.userEyeColor.text=it.eyeColor
            binding.userHairColor.text= it.hair.toString()
            binding.userBloodGroup.text= it.bloodGroup
            binding.userIpMac.text= it.macAddress
            binding.userUniversity.text= it.university
            binding.userEinSsn.text= it.ssn
            binding.userAgent.text= it.userAgent
            binding.userCrypto.text= it.crypto.toString()
            binding.userRole.text= it.role

        }

        userListViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments = arguments
        val receivedData = arguments?.getInt("user_id")
        userId=receivedData?:0
        userListViewModel.userDetails(userId)
        observeViewModel()
    }
}