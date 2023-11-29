package com.example.mycrypto.Fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.mycrypto.Model.CryptoCurrency
import com.example.mycrypto.R
import com.example.mycrypto.databinding.FragmentDetailsBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class DetailsFragment : Fragment() {
    private lateinit var binding:FragmentDetailsBinding
    private  val item:DetailsFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentDetailsBinding.inflate(layoutInflater)
        val data:CryptoCurrency=item.data!!
         setUpDetails(data)
        loadChart(data)
        addToWatchList(data)
        setButtonOnClickListener(data)
        return  binding.root
    }

    var watchList:ArrayList<String>?=null
    var watchListChecked=false

     private fun addToWatchList(data: CryptoCurrency) {
         readData()
         watchListChecked=if(watchList!!.contains(data.symbol)){
             binding.addWatchlistButton.setImageResource(R.drawable.baseline_star_24)
             true
         }
         else{
             binding.addWatchlistButton.setImageResource(R.drawable.baseline_star_border_24)
             false

         }
         binding.addWatchlistButton.setOnClickListener{
             watchListChecked=if(!watchListChecked){
                 if(!watchList!!.contains(data.symbol)){
                     watchList!!.add(data.symbol)
                 }
                 storedData()
                 binding.addWatchlistButton.setImageResource(R.drawable.baseline_star_24)
                 true
             }
             else{
                 binding.addWatchlistButton.setImageResource(R.drawable.baseline_star_border_24)
                 watchList!!.remove(data.symbol)
                 storedData()
                 false
             }
         }

    }
    private fun storedData(){
        val sharedPreferences=requireContext().getSharedPreferences("watchList",Context.MODE_PRIVATE)
        val editor=sharedPreferences.edit()
        val  gson=Gson()

        val json=gson.toJson(watchList)
        editor.putString("watchList",json)
        editor.apply()


    }

    private fun readData() {
        val sharedPreferences=requireContext().getSharedPreferences("watchList",Context.MODE_PRIVATE)
        val  gson=Gson()

        val json=sharedPreferences.getString("watchList",ArrayList<String>().toString())
        val type=object :TypeToken<ArrayList<String>>(){}.type
        watchList=gson.fromJson(json,type)
    }

    private fun setButtonOnClickListener(data: CryptoCurrency) {
        val oneMonth=binding.button
        val oneWeek=binding.button1
        val oneDay=binding.button2
        val fourHour=binding.button3
        val oneHour=binding.button4
        val fifteenMin=binding.button5
        val clickListener=View.OnClickListener {
            when(it.id){
                fifteenMin.id->loadChartData(it,"15",item,oneMonth,oneDay,oneHour,oneWeek,fourHour)
                oneDay.id->loadChartData(it,"D",item,oneMonth,fifteenMin,oneHour,oneWeek,fourHour)
                oneHour.id->loadChartData(it,"1H",item,oneMonth,oneDay,fifteenMin,oneWeek,fourHour)
                oneMonth.id->loadChartData(it,"1M",item,fifteenMin,oneDay,oneHour,oneWeek,fourHour)
                oneWeek.id->loadChartData(it,"1W",item,oneMonth,oneDay,oneHour,fifteenMin,fourHour)
                fourHour.id->loadChartData(it,"4H",item,oneMonth,oneDay,oneHour,oneWeek,fifteenMin)
            }
        }
        fifteenMin.setOnClickListener(clickListener)
        oneDay.setOnClickListener(clickListener)
        oneHour.setOnClickListener(clickListener)
        oneMonth.setOnClickListener(clickListener)
        oneWeek.setOnClickListener(clickListener)
        fourHour.setOnClickListener(clickListener)


    }

    @SuppressLint("ResourceAsColor")
    private fun loadChartData(
        it: View?,
        s: String,
        item: DetailsFragmentArgs,
        oneMonth: AppCompatButton,
        oneDay: AppCompatButton,
        oneHour: AppCompatButton,
        oneWeek: AppCompatButton,
        fourHour: AppCompatButton
    ) {
        disableButton(oneDay,oneHour,oneWeek,oneMonth,fourHour)
        it!!.setBackgroundColor(R.color.red)
        binding.detaillChartWebView.settings.javaScriptEnabled=true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)
        binding.detaillChartWebView.loadUrl("https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol"
                +item.toString()
                + "USD&interval=" +s+
                "&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        )

    }

    private fun disableButton(oneDay: AppCompatButton, oneHour: AppCompatButton, oneWeek: AppCompatButton, oneMonth: AppCompatButton, fourHour: AppCompatButton) {

        oneDay.background=null
        oneWeek.background=null
        oneHour.background=null
        oneMonth.background=null
        fourHour.background=null
    }

    private fun loadChart(data: CryptoCurrency) {
        binding.detaillChartWebView.settings.javaScriptEnabled=true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)
        binding.detaillChartWebView.loadUrl("https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol"
            +item.toString()
                + "USD&interval=D&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
           )

    }

    private fun setUpDetails(data: CryptoCurrency) {
        binding.detailSymbolTextView.text=data.symbol
        Glide.with(requireContext()).load("https://s3.coinmarketcap.com/static/img/coins/64x64/"+data.id+".png")
            .thumbnail(Glide.with(requireContext()).load(R.drawable.loading))
            .into(binding.detailImageView)
        binding.detailPriceTextView.text="${String.format("$%.4f",data.quotes[0].price)}"
        if(data.quotes!![0].percentChange24h>0){
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.green))
            binding.detailChangeImageView.setImageResource(R.drawable.caret_up)
            binding.detailChangeTextView.text="+${String.format(".02f",data.quotes[0].percentChange24h)}%"

        }
        else{
            binding.detailChangeImageView.setImageResource(R.drawable.caret_down)
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.red))
            binding.detailChangeTextView.text="${String.format(".02f",data.quotes[0].percentChange24h)}%"


        }

    }


}