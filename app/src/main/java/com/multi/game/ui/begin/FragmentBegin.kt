package com.multi.game.ui.begin

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.multi.game.core.library.GameFragment
import com.multi.game.databinding.FragmentBeginBinding
import com.multi.game.ui.other.MainActivity
import com.multi.game.ui.wheel.FragmentWheelGame

class FragmentBegin : GameFragment<FragmentBeginBinding>(FragmentBeginBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.start.setOnClickListener {
            (requireActivity() as MainActivity).navigate(FragmentWheelGame())
        }

        binding.exit.setOnClickListener {
            requireActivity().finish()
        }

        binding.privacyText.setOnClickListener {
            requireActivity().startActivity(
                Intent(
                    ACTION_VIEW,
                    Uri.parse("https://www.google.com")
                )
            )
        }
    }
}