package com.rebellionandroid.features.sectorslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rebllelionandroid.features.sectorsList.R

class SectorsListFragment : Fragment() {

    private lateinit var viewModel: SectorsListViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel =
                ViewModelProvider(this).get(SectorsListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_sectors_list, container, false)
        val textView: TextView = root.findViewById(R.id.text_foobar)
        viewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}