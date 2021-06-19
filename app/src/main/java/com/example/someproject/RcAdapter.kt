package com.example.someproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.someproject.databinding.RcItemBinding
import kotlin.collections.ArrayList

class RcAdapter(var list: ArrayList<String>, private val rcItemClickListener: RcItemClickListener) : RecyclerView.Adapter<RcAdapter.Holder>() {

    val staticList : ArrayList<String> = list.clone() as ArrayList<String>

    class Holder(var binding: RcItemBinding, private val rcItemClickListener: RcItemClickListener) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: String) {

            binding.tvArticle.text = article

            itemView.setOnClickListener {
                rcItemClickListener.onRcItemClickListener(article)
            }
        }
    }

    // region Default
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RcItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding, rcItemClickListener)
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {

        if (position >= list.count()) {
            holder.bind("")
        }
        holder.bind(list[position])

    }
    override fun getItemCount(): Int {
        return list.size
    }

    // endregion

    fun updateAdapter(listItems:List<String>) {
        list.clear()
        list.addAll(listItems)
        notifyDataSetChanged()
    }
    fun updateItemsView() {
        notifyDataSetChanged()
    }

    fun addItem(article: String) {
        list.add(article)
        notifyItemInserted(list.size - 1)
    }
    fun count(predicate: (String) -> (Boolean)) : Int {
        var count = 0
        for (item in list) {
            if (predicate(item)) {
                count++
            }
        }
        return count
    }

    fun getItem(pos: Int) : String = list[pos]

    fun sortAsc() {
        list.sortBy {
            it
        }
        notifyDataSetChanged()
    }

    fun sortDesc() {
        list.sortByDescending {
            it
        }
        notifyDataSetChanged()
    }


    fun filter(text: String) {
        if (text.isEmpty()) {
            list = staticList.clone() as ArrayList<String>
            notifyDataSetChanged()
            return
        }

        list.clear()

        staticList.forEach {
            if (it.contains(text)) {
                list.add(it)
            }
        }
        notifyDataSetChanged()
    }


}