package com.boryans.covidstats.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.boryans.covidstats.R
import com.boryans.covidstats.model.Country
import com.boryans.covidstats.ui.CountryList
import kotlinx.android.synthetic.main.item_country.view.*


class CountryListRecyclerAdapter: RecyclerView.Adapter<CountryListRecyclerAdapter.HomeViewHolder>() {

     var countries =  arrayListOf<Country>()

    fun updateCountriesList(countryList: ArrayList<Country>) {
        val oldList = countries
        val difResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            CountryItemDiffCallback(
                oldList,
                countryList
            )
        )
        countries = countryList
        difResult.dispatchUpdatesTo(this)
    }

   inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val items = countries[position]
        holder.itemView.countryNameTxt.text = items.all.country
    }

    override fun getItemCount() = countries.size
}

class CountryItemDiffCallback(
    var oldCountryList: List<Country>,
    var newCountryList: List<Country>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldCountryList.size

    override fun getNewListSize() = newCountryList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return (oldCountryList[oldItemPosition].all.country == newCountryList[newItemPosition].all.country)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldCountryList[oldItemPosition].all.country == newCountryList[newItemPosition].all.country)
    }

}