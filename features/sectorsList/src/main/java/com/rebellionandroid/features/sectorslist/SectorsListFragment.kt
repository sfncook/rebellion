package com.rebellionandroid.features.sectorslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.di.DaggerGameStateComponent
import com.rebllelionandroid.core.di.modules.ContextModule
import com.rebllelionandroid.features.sectorsList.R
import com.rebllelionandroid.features.sectorsList.databinding.FragmentSectorsListBinding
import javax.inject.Inject

class SectorsListFragment : Fragment() {

    lateinit var viewModel: GameStateViewModel

    lateinit var viewBinding: FragmentSectorsListBinding
    lateinit var viewAdapter: SectorListHandler

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        initAppDependencyInjection()
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sectors_list, container, false)

        val recyclerView: RecyclerView = viewBinding.root.findViewById(R.id.sectors_list)
        recyclerView.adapter = viewAdapter

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.viewModel = viewModel
        viewBinding.sectorsList.apply {
            adapter = viewAdapter
//            gridLayoutManager?.spanSizeLookup = viewAdapter.getSpanSizeLookup()
        }
    }

    private fun initAppDependencyInjection() {
        val gameStateComponent = DaggerGameStateComponent
                .builder()
                .contextModule(ContextModule(requireContext()))
                .build()
        viewModel = gameStateComponent.gameStateViewModel()
        viewAdapter = SectorListHandler(viewModel)
    }
}