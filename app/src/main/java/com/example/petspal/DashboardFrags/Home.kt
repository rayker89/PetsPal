package com.example.petspal.DashboardFrags

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petspal.Home.Adapter
import com.example.petspal.Home.PostItem

import com.example.petspal.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class Home : Fragment() {



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val current = LocalDateTime.now()
        val layout = inflater.inflate(R.layout.fragment_home, container, false)
        val formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val formatted = current.format(formatter1)
        val items = ArrayList<PostItem>()
        items.add(PostItem("Android", R.drawable.logo, formatted, "Njim se državama \"30-plus\" dozvoljava da obavljaju nenaoružane posmatračke letove iznad svojih teritorija koji je potpisan pre 18 godina u radi promovisanja poverenja i sprečavanja sukoba." ))
        items.add(PostItem("Android", R.drawable.logo, formatted, "Njim se državama \"30-plus\" dozvoljava da obavljaju nenaoružane posmatračke letove iznad svojih teritorija koji je potpisan pre 18 godina u radi promovisanja poverenja i sprečavanja sukoba." ))
        items.add(PostItem("Android", R.drawable.logo, formatted, "Njim se državama \"30-plus\" dozvoljava da obavljaju nenaoružane posmatračke letove iznad svojih teritorija koji je potpisan pre 18 godina u radi promovisanja poverenja i sprečavanja sukoba." ))
        items.add(PostItem("Android", R.drawable.logo, formatted, "Njim se državama \"30-plus\" dozvoljava da obavljaju nenaoružane posmatračke letove iznad svojih teritorija koji je potpisan pre 18 godina u radi promovisanja poverenja i sprečavanja sukoba." ))



        val recycler = layout.findViewById<RecyclerView>(R.id.post_list)

        val adapter = Adapter(items, activity!!.applicationContext)


        recycler.layoutManager = GridLayoutManager(activity!!.applicationContext, 1)

        recycler.adapter = adapter

        // Inflate the layout for this fragment
        return layout
    }


}
