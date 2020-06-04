package com.example.petspal.Home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petspal.R
import java.util.*
import kotlin.collections.ArrayList

class Adapter(data:ArrayList<PostItem>, var context: Context) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    var data:List<PostItem>

    init {
        this.data = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout = LayoutInflater.from(context).inflate(R.layout.home_item, parent, false )

        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = data[position].title
        holder.image.setImageResource(data[position].image)
        holder.desc.text = data[position].desc
        holder.date.text = data[position].date

    }

    override fun getItemCount(): Int {
        return data.size
    }


    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView
        var image: ImageView
        var date: TextView
        var desc: TextView

        init {
            title = itemView.findViewById(R.id.item_title)
            image = itemView.findViewById(R.id.item_image)
            desc = itemView.findViewById(R.id.item_desc)
            date = itemView.findViewById(R.id.item_date)
        }

    }
}