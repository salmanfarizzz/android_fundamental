package com.example.subfundamental.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.subfundamental.ui.folow.FolowFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val username = ""

        val fragment = FolowFragment()
        fragment.arguments = Bundle().apply {
            putInt(FolowFragment.ARG_POSITION, position + 1)
            putString(FolowFragment.ARG_USERNAME, username)
        }
        return fragment
    }
}