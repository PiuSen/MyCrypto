package com.example.mycrypto.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycrypto.Fragment.HomeFragmentDirections
import com.example.mycrypto.Model.CryptoCurrency
import com.example.mycrypto.R
import com.example.mycrypto.databinding.TopCurrencyBinding

class TopMarketAdapter(var context: Context,val mlist:List<CryptoCurrency>):RecyclerView.Adapter<TopMarketAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        var binding=TopCurrencyBinding.bind(itemView)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.top_currency,parent,false))
    }

    override fun getItemCount(): Int {
       return  mlist.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item=mlist[position]
        holder.binding.topCurrencyNameTextView.text=item.name
        Glide.with(context).load("https://s2.coinmarketcap.com/static/img/coins/64x64/"+item.id+".png")
            .thumbnail(Glide.with(context).load(R.drawable.loading))
            .into(holder.binding.topCurrencyImageView)
        if(item.quotes[0].percentChange24h>0){
            holder.binding.topCurrencyChangeTextView.setTextColor(context.resources.getColor(R.color.green))
            holder.binding.topCurrencyChangeTextView.text="+${item.quotes[0].percentChange24h}%"

        }
        else{
            holder.binding.topCurrencyChangeTextView.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.topCurrencyChangeTextView.text="${item.quotes[0].percentChange24h}%"



        }
        holder.itemView.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(item))
        }
    }
}