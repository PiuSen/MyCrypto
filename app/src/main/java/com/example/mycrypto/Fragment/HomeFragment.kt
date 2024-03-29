package com.example.mycrypto.Fragment

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.viewpager2.widget.ViewPager2

import com.example.mycrypto.Adapter.TopMarketAdapter
import com.example.mycrypto.GainLoseAdapter
import com.example.mycrypto.Model.CryptoCurrency

import com.example.mycrypto.Retrofit.ApiInstance
import com.example.mycrypto.Retrofit.ApiInterface
import com.example.mycrypto.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mlist:List<CryptoCurrency>

    private lateinit var adapter: TopMarketAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(layoutInflater)
        mlist= listOf()
        adapter= TopMarketAdapter(requireContext(),mlist)
        binding.topCurrencyRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.topCurrencyRecyclerView.adapter=adapter

        getTopCurrency()
        setTabLayout()
        return binding.root
    }

    private fun setTabLayout() {
      val adapter=GainLoseAdapter(this)
        binding.contentViewPager.adapter=adapter
        binding.contentViewPager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position==0){
                    binding.topGainIndicator.visibility=VISIBLE
                    binding.topLoseIndicator.visibility=GONE

            }
                else{
                    binding.topGainIndicator.visibility=GONE
                    binding.topLoseIndicator.visibility= VISIBLE

                }
            }

        })
        TabLayoutMediator(binding.tabLayout,binding.contentViewPager){
            tab,position->
            val title=if(position==0){
                "Top Gainers"
            }
            else{
                "Top Looser"
            }
            tab.text=title
        }.attach()
    }

    private fun getTopCurrency() {
       lifecycleScope.launch(Dispatchers.IO) {
           val result=ApiInstance.getInstance().create(ApiInterface::class.java).getMarketData()
           withContext(Dispatchers.Main){
               binding.topCurrencyRecyclerView.adapter=TopMarketAdapter(requireContext(),result.body()!!.data.cryptoCurrencyList)
           }
           Log.d("Piu","getTopCurrencyList: ${result.body()!!.data.cryptoCurrencyList}")


       }
    }


}