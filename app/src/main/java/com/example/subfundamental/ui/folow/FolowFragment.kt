package com.example.subfundamental.ui.folow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subfundamental.data.response.FollowResponseItem
import com.example.subfundamental.databinding.FragmentFolowBinding
import com.example.subfundamental.ui.adapter.FollowAdapter

class FolowFragment : Fragment() {

    private var _binding: FragmentFolowBinding? = null
    private val binding get() = _binding!!

    private val followViewModel by viewModels<FollowViewModel>()

    companion object {

        const val ARG_USERNAME = "username"
        const val ARG_POSITION = "position"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFolowBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.fragmentRv.layoutManager = layoutManager

        val index = arguments?.getInt(ARG_POSITION, 0)
        val username = activity?.intent?.getStringExtra(ARG_USERNAME)

        followViewModel.listFollowing.observe(viewLifecycleOwner) { listFollowing ->
            setFollowUser(listFollowing)
        }

        followViewModel.listFollowers.observe(viewLifecycleOwner) { listFollowers ->
            setFollowUser(listFollowers)
        }

        followViewModel.isLoading.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandled()?.let {
                showLoading(it)
            }
        }

        followViewModel.isFound.observe(viewLifecycleOwner) {
            showDataIsFound(it)
        }

        if (index == 1) {
            if (username != null) {
                followViewModel.findFollowingUser(username)
            }

        } else {
            if (username != null) {
                followViewModel.findFollowersUser(username)
            }
        }
    }

    private fun setFollowUser(user: List<FollowResponseItem>) {
        val adapter = FollowAdapter()
        adapter.submitList(user)
        binding.fragmentRv.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.fragmentProgressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showDataIsFound(isFound: Boolean) {
        if (isFound) {
            binding.fragmentNotfound.visibility = View.VISIBLE
        } else {
            binding.fragmentNotfound.visibility = View.INVISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}