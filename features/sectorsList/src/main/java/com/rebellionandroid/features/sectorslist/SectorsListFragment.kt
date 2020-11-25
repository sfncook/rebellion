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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SectorsListFragment : Fragment() {

    lateinit var gameStateViewModel: GameStateViewModel

    lateinit var viewBinding: FragmentSectorsListBinding
    lateinit var viewAdapter: SectorListAdapter
    private val mainScope = MainScope()
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        initAppDependencyInjection()
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sectors_list, container, false)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.viewModel = gameStateViewModel
    }

    private fun initAppDependencyInjection() {
        val gameStateComponent = DaggerGameStateComponent
                .builder()
                .contextModule(ContextModule(requireContext()))
                .build()
        gameStateViewModel = gameStateComponent.gameStateViewModel()
        mainScope.launch(Dispatchers.IO) {
            if(gameStateViewModel.getGameState() != null) {
                viewAdapter = SectorListAdapter(gameStateViewModel.getCurrentGameStateWithSectors().sectors, requireContext())
                recyclerView = viewBinding.root.findViewById(R.id.sectors_list)
                mainScope.launch(Dispatchers.Main) {
                    recyclerView.adapter = viewAdapter
                    viewBinding.sectorsList.apply {
                        adapter = viewAdapter
                    }
                }
            }
        }
    }
}
