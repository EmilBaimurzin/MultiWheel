package com.multi.game.ui.bonuses.third

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.multi.game.R
import com.multi.game.core.library.GameFragment
import com.multi.game.databinding.FragmentThirdBonusBinding
import com.multi.game.ui.bonuses.CallbackViewModel
import com.multi.game.ui.other.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FragmentThirdBonus: GameFragment<FragmentThirdBonusBinding>(FragmentThirdBonusBinding::inflate) {
    private val viewModel: ThirdBonusViewModel by viewModels()
    private val callbackViewModel: CallbackViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.close.setOnClickListener {
            (requireActivity() as MainActivity).navigateBack()
        }

        binding.woman.setOnClickListener {
            viewModel.click(2)
            lifecycleScope.launch {
                delay(2000)
                val isWin = viewModel.choice == viewModel.result
                callbackViewModel.callback?.invoke(if (isWin) 2 else 1)
                (requireActivity() as MainActivity).navigateBack()
            }
        }

        binding.man.setOnClickListener {
            viewModel.click(1)
            lifecycleScope.launch {
                delay(2000)
                val isWin = viewModel.choice == viewModel.result
                callbackViewModel.callback?.invoke(if (isWin) 2 else 1)
                (requireActivity() as MainActivity).navigateBack()
            }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            binding.manResult.isVisible = it
            binding.womanResult.isVisible = it

            binding.woman.setImageResource(if (viewModel.choice == 2) R.drawable.woman01 else R.drawable.woman02)
            binding.man.setImageResource(if (viewModel.choice == 1) R.drawable.man01 else R.drawable.man02)

            binding.womanResult.text = if (viewModel.result == 1) "LOSS" else "X2"
            binding.manResult.text = if (viewModel.result == 2) "LOSS" else "X2"

            if (it) {
                val isWin = viewModel.choice == viewModel.result
                binding.win.text = if (isWin) (arguments?.getInt("WIN")!! * 2).toString() else ""
                if (!isWin) binding.winImg.setImageResource(R.drawable.loss)
            }
        }
    }
}