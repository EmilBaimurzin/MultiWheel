package com.multi.game.ui.bonuses.first

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.multi.game.R
import com.multi.game.core.library.GameFragment
import com.multi.game.databinding.FragmentFirstBonusBinding
import com.multi.game.ui.bonuses.CallbackViewModel
import com.multi.game.ui.other.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FragmentFirstBonus :
    GameFragment<FragmentFirstBonusBinding>(FragmentFirstBonusBinding::inflate) {
    private val viewModel: FirstBonusViewModel by viewModels()
    private val callbackViewModel: CallbackViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.close.setOnClickListener {
            (requireActivity() as MainActivity).navigateBack()
        }

        binding.balloon1.setOnClickListener {
            lifecycleScope.launch {
                viewModel.click(1)
                delay(2000)
                callbackViewModel.callback?.invoke(viewModel.win)
                (requireActivity() as MainActivity).navigateBack()
            }
        }

        binding.balloon2.setOnClickListener {
            lifecycleScope.launch {
                viewModel.click(2)
                delay(2000)
                callbackViewModel.callback?.invoke(viewModel.win)
                (requireActivity() as MainActivity).navigateBack()
            }
        }

        binding.balloon3.setOnClickListener {
            lifecycleScope.launch {
                viewModel.click(3)
                delay(2000)
                callbackViewModel.callback?.invoke(viewModel.win)
                (requireActivity() as MainActivity).navigateBack()
            }
        }

        binding.balloon4.setOnClickListener {
            lifecycleScope.launch {
                viewModel.click(4)
                delay(2000)
                callbackViewModel.callback?.invoke(viewModel.win)
                (requireActivity() as MainActivity).navigateBack()
            }
        }

        viewModel.state.observe(viewLifecycleOwner) { value ->
            binding.apply {
                val balloons = listOf(balloon1, balloon2, balloon3, balloon4)
                balloons.forEach {
                    it.isVisible = !value
                }
                result.isVisible = value

                result.text = when (viewModel.win) {
                    1 -> "Loss"
                    2 -> "x2"
                    else -> "x3"
                }

                if (value) {
                    binding.win.text = (arguments?.getInt("WIN")!! * viewModel.win).toString()
                }

                resultBalloon.isVisible = value
                resultBalloon.setImageResource(
                    when (viewModel.balloon) {
                        1 -> R.drawable.air_balloon01_big
                        2 -> R.drawable.air_balloon021_big
                        3 -> R.drawable.air_balloon03_big
                        else -> R.drawable.air_balloon04_big
                    }
                )
            }
        }
    }
}