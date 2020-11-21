package com.rebellionandroid.features.sectorslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.di.DaggerGameStateComponent
import com.rebllelionandroid.core.di.modules.ContextModule
import com.rebllelionandroid.features.sectorsList.R
import com.rebllelionandroid.features.sectorsList.databinding.FragmentSectorsListBinding
import javax.inject.Inject

class SectorsListFragment : Fragment() {

    @Inject
    lateinit var gameStateViewModel: GameStateViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        initAppDependencyInjection()
        val viewBinding: FragmentSectorsListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sectors_list, container, false)
        return viewBinding.root
//        binding.lifecycleOwner = this
//        binding.viewModel = gameStateViewModel
//        viewModel =
//                ViewModelProvider(this).get(SectorsListViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_sectors_list, container, false)
//        val textView: TextView = root.findViewById(R.id.text_foobar)
//        viewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
//        return root
    }

    private fun initAppDependencyInjection() {
        val gameStateComponent = DaggerGameStateComponent
                .builder()
                .contextModule(ContextModule(requireContext()))
                .build()
        gameStateViewModel = gameStateComponent.gameStateViewModel()
    }
}