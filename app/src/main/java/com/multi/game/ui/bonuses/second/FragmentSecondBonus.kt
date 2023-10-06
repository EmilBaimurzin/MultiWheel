package com.multi.game.ui.bonuses.second

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.multi.game.R
import com.multi.game.core.library.GameFragment
import com.multi.game.databinding.FragmentSecondBonusBinding
import com.multi.game.ui.bonuses.CallbackViewModel
import com.multi.game.ui.other.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FragmentSecondBonus :
    GameFragment<FragmentSecondBonusBinding>(FragmentSecondBonusBinding::inflate) {
    private val viewModel: SecondBonusViewModel by viewModels()
    private val callbackViewModel: CallbackViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.close.setOnClickListener {
            (requireActivity() as MainActivity).navigateBack()
        }

        binding.odd.setOnClickListener {
            viewModel.click(false)
            lifecycleScope.launch {
                delay(2000)
                val isWin =
                    ((viewModel.number % 2 == 0) && viewModel.choice) || ((viewModel.number % 2 != 0) && !viewModel.choice)
                if (isWin) {
                    callbackViewModel.callback?.invoke(2)
                } else {
                    callbackViewModel.callback?.invoke(1)
                }
                (requireActivity() as MainActivity).navigateBack()
            }
        }

        binding.even.setOnClickListener {
            viewModel.click(true)
            lifecycleScope.launch {
                delay(2000)
                val isWin =
                    ((viewModel.number % 2 == 0) && viewModel.choice) || ((viewModel.number % 2 != 0) && !viewModel.choice)
                if (isWin) {
                    callbackViewModel.callback?.invoke(2)
                } else {
                    callbackViewModel.callback?.invoke(1)
                }
                (requireActivity() as MainActivity).navigateBack()
            }
        }

        viewModel.state.observe(viewLifecycleOwner) { value ->
            binding.previewImg.isVisible = !value
            binding.odd.isVisible = !value
            binding.even.isVisible = !value
            binding.resultImg.isVisible = value
            binding.resultImg.setImageResource(
                when (viewModel.number) {
                    1 -> R.drawable.dice01
                    2 -> R.drawable.dice02
                    3 -> R.drawable.dice03
                    4 -> R.drawable.dice04
                    5 -> R.drawable.dice05
                    else -> R.drawable.dice06
                }
            )
            if (value) {
                val isWin =
                    ((viewModel.number % 2 == 0) && viewModel.choice) || ((viewModel.number % 2 != 0) && !viewModel.choice)
                binding.win.text = if (isWin) (arguments?.getInt("WIN")!! * 2).toString() else ""
                if (!isWin) binding.winImg.setImageResource(R.drawable.loss)
                if (!isWin) binding.winResult.setImageResource(R.drawable.loss) else binding.winResult.setImageResource(R.drawable.win)
            }
        }
    }
}