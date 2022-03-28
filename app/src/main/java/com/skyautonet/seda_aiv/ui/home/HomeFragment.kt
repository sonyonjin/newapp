package com.skyautonet.seda_aiv.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.skyautonet.seda_aiv.R
import com.skyautonet.seda_aiv.databinding.FragmentHomeBinding
import com.skyautonet.seda_aiv.ui.BaseFragment

class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var viewModel: HomeViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        _binding = FragmentHomeBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }

        viewModel.connectDate.observe(viewLifecycleOwner) {
            binding.tvConnectionDate.text = it
        }
        viewModel.userName.observe(viewLifecycleOwner) {
            binding.tvUserName.text = it
        }
        viewModel.productRegNum.observe(viewLifecycleOwner) {
            binding.tvProductRegNum.text = it
        }
        viewModel.networkState.observe(viewLifecycleOwner) {
            binding.tvNetworkState.text = it
            binding.tvNetworkState.isSelected = it.equals("Connect")
        }

        initView()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() {

    }
}