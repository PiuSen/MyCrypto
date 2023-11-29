package com.example.mycrypto.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.webkit.RenderProcessGoneDetail
import androidx.lifecycle.lifecycleScope
import com.example.mycrypto.Adapter.MarketAdapter
import com.example.mycrypto.Adapter.TopMarketAdapter
import com.example.mycrypto.Model.CryptoCurrency
import com.example.mycrypto.R
import com.example.mycrypto.Retrofit.ApiInstance
import com.example.mycrypto.Retrofit.ApiInterface
import com.example.mycrypto.databinding.FragmentWatchListBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class WatchListFragment : Fragment() {
    private lateinit var binding:FragmentWatchListBinding
    private lateinit var watchList:ArrayList<String>
    private lateinit var watchListItem:ArrayList<CryptoCurrency>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding=FragmentWatchListBinding.inflate(layoutInflater)
        readData()
        lifecycleScope.launch(Dispatchers.IO) {
            val result= ApiInstance.getInstance().create(ApiInterface::class.java).getMarketData()
            if(result.body()!=null){
                withContext(Dispatchers.Main){
                    watchListItem=ArrayList()
                    watchListItem.clear()
                   for(watchData in watchList){
                       for(item in result.body()!!.data.cryptoCurrencyList){
                           if(watchData ==item.symbol){
                               watchListItem.add(item)
                           }
                       }
                   }
                    binding.spinKitView.visibility=GONE
                    binding.watchlistRecyclerView.adapter=MarketAdapter(requireContext(),watchListItem,"watchList")
                }

            }



        }
        return binding.root
    }
    private fun readData() {
        val sharedPreferences=requireContext().getSharedPreferences("watchList", Context.MODE_PRIVATE)
        val  gson= Gson()

        val json=sharedPreferences.getString("watchList",ArrayList<String>().toString())
        val type=object : TypeToken<ArrayList<String>>(){}.type
        watchList=gson.fromJson(json,type)
    }


}