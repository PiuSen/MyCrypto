package com.example.mycrypto.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController


import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycrypto.Fragment.HomeFragmentDirections
import com.example.mycrypto.Fragment.MarketFragmentDirections
import com.example.mycrypto.Fragment.WatchListFragmentDirections
import com.example.mycrypto.Model.CryptoCurrency
import com.example.mycrypto.R
import com.example.mycrypto.databinding.CurencyItemBinding

class MarketAdapter(var context: Context, var mlist: List<CryptoCurrency>, var type: String):RecyclerView.Adapter<MarketAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        var binding=CurencyItemBinding.bind(itemView)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return  MyViewHolder(LayoutInflater.from(context).inflate(R.layout.curency_item,parent,false))

    }
    fun updateData(dataItem:List<CryptoCurrency>){
        mlist=dataItem
        notifyDataSetChanged()
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item=mlist[position]
        holder.binding.currencyNameTextView.text=item.name
        holder.binding.currencySymbolTextView.text=item.symbol
        Glide.with(context).load("https://s2.coinmarketcap.com/static/img/coins/64x64/"+item.id+".png")
            .thumbnail(Glide.with(context).load(R.drawable.loading))
            .into(holder.binding.currencyImageView)

        Glide.with(context).load("https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/"+item.id+".png")
            .thumbnail(Glide.with(context).load(R.drawable.loading))
            .into(holder.binding.currencyChartImageView)
        holder.binding.currencyPriceTextView.text="${String.format("$%.02f",item.quotes[0].price)}"

        if(item.quotes!![0].percentChange24h>0){
            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.green))
            holder.binding.currencyChangeTextView.text="+${String.format(".02f",item.quotes[0].percentChange24h)}%"

        }
        else{
            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.currencyChangeTextView.text="${String.format(".02f",item.quotes[0].percentChange24h)}%"


        }

        holder.itemView.setOnClickListener {
            if(type=="home"){
                    findNavController(it)
                        .navigate(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(item))


                }
            else if(type=="market"){
                findNavController(it)
                    .navigate(MarketFragmentDirections.actionMarketFragmentToDetailsFragment(item))


            }
            else{
                findNavController(it)
                    .navigate(WatchListFragmentDirections.actionWatchListFragmentToDetailsFragment(item))

            }

        }



    }
    override fun getItemCount(): Int {
        return  mlist.size

    }
}