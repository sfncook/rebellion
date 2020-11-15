package com.rebllelionandroid.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rebllelionandroid.MainApplication
import com.rebllelionandroid.R
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.GameTimerViewModel
import javax.inject.Inject

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    @Inject lateinit var gameTimerViewModel: GameTimerViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        gameTimerViewModel.gameState.observe(viewLifecycleOwner, {
            println("it:$it")
//            textView.text = it.gameTime.toString()
        })

        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as MainApplication).appComponent.injectHome(this)
    }
}