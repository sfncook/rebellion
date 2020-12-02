package com.rebellionandroid.features.sectorslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.rebllelionandroid.core.database.gamestate.GameStateWithSectors
import com.rebllelionandroid.features.sectorsList.R
import com.rebllelionandroid.features.sectorsList.databinding.ActivitySectorsListBinding
import kotlinx.coroutines.MainScope

class SectorsListFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_sectors_list, container, false)

        root.findViewById<MaterialButton>(R.id.btn_goto_detail).setOnClickListener {
            root.findNavController().navigate(R.id.third_graph)
        }

        return root
    }

}