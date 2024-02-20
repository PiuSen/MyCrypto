package com.example.mycrypto.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import com.example.mycrypto.Adapter.MarketAdapter
import com.example.mycrypto.Model.CryptoCurrency
import com.example.mycrypto.Retrofit.ApiInstance
import com.example.mycrypto.Retrofit.ApiInterface
import com.example.mycrypto.databinding.FragmentMarketBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale


class MarketFragment : Fragment() {
    private lateinit var binding:FragmentMarketBinding
    private lateinit var mlist:List<CryptoCurrency>
    private lateinit var adapter:MarketAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentMarketBinding.inflate(layoutInflater)
        mlist= listOf()
        adapter= MarketAdapter(requireContext(),mlist,"market")
        binding.currencyRecyclerView.adapter=adapter

        lifecycleScope.launch (Dispatchers.IO){
            val result=ApiInstance.getInstance().create(ApiInterface::class.java).getMarketData()
            if(result.body()!=null){
                withContext(Dispatchers.Main){
                    mlist=result.body()!!.data.cryptoCurrencyList
                    adapter.updateData(mlist)
                    binding.spinKitView.visibility=GONE
                }
            }


        }
        searchCoin()


        return binding.root

    }
    lateinit var searchText:String

    private fun searchCoin() {


        binding.searchView.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                searchText=p0.toString().lowercase(Locale.getDefault())
                updateRecyclerView()

            }

        })
    }

    private fun updateRecyclerView() {
       val data=ArrayList<CryptoCurrency>()
        for(item in mlist){
            val coinName=item.name.lowercase(Locale.getDefault())
            val coinSymbol=item.symbol.lowercase(Locale.getDefault())
            if(coinName.contains(searchText) || coinSymbol.contains(searchText)){
                data.add(item)
            }
        }
        adapter.updateData(data)

    }


}