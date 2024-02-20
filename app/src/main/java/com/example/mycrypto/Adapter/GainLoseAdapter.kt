package com.example.mycrypto

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mycrypto.Fragment.GainLoseFragment

class GainLoseAdapter(fragment:Fragment):FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment=GainLoseFragment()
        val bundle=Bundle()
        bundle.putInt("position",position)
        fragment.arguments=bundle
        return fragment
    }

}