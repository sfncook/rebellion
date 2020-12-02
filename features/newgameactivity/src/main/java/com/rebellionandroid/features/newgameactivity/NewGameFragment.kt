package com.rebellionandroid.features.newgameactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton

class NewGameFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_new_game, container, false)

        root.findViewById<MaterialButton>(R.id.btn_go_back).setOnClickListener {
            root.findNavController().navigateUp()
        }

        return root
    }

}