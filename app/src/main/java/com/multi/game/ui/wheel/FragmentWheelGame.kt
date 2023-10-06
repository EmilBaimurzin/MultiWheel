package com.multi.game.ui.wheel

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.multi.game.R
import com.multi.game.core.library.GameFragment
import com.multi.game.core.library.random
import com.multi.game.databinding.FragmentWheelGameBinding
import com.multi.game.ui.bonuses.CallbackViewModel
import com.multi.game.ui.bonuses.first.FragmentFirstBonus
import com.multi.game.ui.bonuses.second.FragmentSecondBonus
import com.multi.game.ui.bonuses.third.FragmentThirdBonus
import com.multi.game.ui.other.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FragmentWheelGame :
    GameFragment<FragmentWheelGameBinding>(FragmentWheelGameBinding::inflate) {
    private val viewModel: WheelGameViewModel by viewModels()
    private val callbackViewModel: CallbackViewModel by activityViewModels()
    private val spinCallback by lazy {
        {
            lifecycleScope.launch {
                delay(10)
                val fragment = when (1 random 3) {
                    1 -> FragmentFirstBonus().apply {
                        arguments = Bundle().apply { putInt("WIN", viewModel.win.value!!) }
                    }

                    2 -> FragmentSecondBonus().apply {
                        arguments = Bundle().apply { putInt("WIN", viewModel.win.value!!) }
                    }
                    else -> FragmentThirdBonus().apply {
                        arguments = Bundle().apply { putInt("WIN", viewModel.win.value!!) }
                    }
                }
                (requireActivity() as MainActivity).navigate(fragment)
            }
            Unit
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callbackViewModel.callback = {
            when (it) {
                1 -> {}
                2 -> {
                    viewModel.buy(-(viewModel.win.value!!))
                }

                3 -> {
                    viewModel.buy(-(viewModel.win.value!! * 2))
                }
            }
        }

        binding.back.setOnClickListener {
            (requireActivity() as MainActivity).navigateBack()
        }

        binding.spin.setOnClickListener {
            viewModel.spin()
        }

        binding.yes.setOnClickListener {
            viewModel.setBonus(true)
        }

        binding.no.setOnClickListener {
            viewModel.setBonus(false)
        }

        binding.increaseLuck.setOnClickListener {
            if (viewModel.balance.value!! - 100 > 0 && viewModel.activatedBonus.value!! == 0) {
                viewModel.buy(100)
                viewModel.activateBonus(1)
            }
        }

        binding.doubleWin.setOnClickListener {
            if (viewModel.balance.value!! - 50 > 0 && viewModel.activatedBonus.value!! == 0) {
                viewModel.buy(50)
                viewModel.activateBonus(2)
            }
        }

        viewModel.spinCallback = spinCallback

        viewModel.isSpinning.observe(viewLifecycleOwner) { isSpin ->
            binding.apply {
                listOf(spin, increaseLuck, doubleWin).forEach {
                    it.isEnabled = !isSpin
                }
                bonusLayout.isVisible = !isSpin
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.rotation.collect {
                    binding.wheel.rotation = -it + 10
                }
            }
        }

        viewModel.balance.observe(viewLifecycleOwner) {
            binding.balance.text = it.toString()
        }

        viewModel.win.observe(viewLifecycleOwner) {
            binding.win.text = it.toString()
        }

        viewModel.activatedBonus.observe(viewLifecycleOwner) {
            binding.increaseLuck.alpha = if (it == 0 || it == 1) 1f else 0.6f
            binding.doubleWin.alpha = if (it == 2 || it == 0) 1f else 0.6f

            binding.wheel.setImageResource(
                when (it) {
                    0 -> R.drawable.wheel
                    1 -> R.drawable.increase_luck02
                    else -> R.drawable.double_win02
                }
            )
        }

        viewModel.isBonus.observe(viewLifecycleOwner) {
            binding.yes.setImageResource(if (it) R.drawable.yes else R.drawable.yes02)
            binding.no.setImageResource(if (!it) R.drawable.no else R.drawable.no02)
        }
    }
}