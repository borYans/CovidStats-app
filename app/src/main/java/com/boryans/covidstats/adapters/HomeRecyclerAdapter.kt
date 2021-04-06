package com.boryans.covidstats.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boryans.covidstats.R
import com.boryans.covidstats.api.Countries
import kotlinx.android.synthetic.main.item_country.view.*


class HomeRecyclerAdapter: RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {

     var countries =  arrayListOf<String>()

   inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val items = countries[position]
        holder.itemView.countryNameTxt.text = items
    }

    override fun getItemCount() = countries.size
}