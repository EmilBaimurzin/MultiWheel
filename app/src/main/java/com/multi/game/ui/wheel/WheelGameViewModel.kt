package com.multi.game.ui.wheel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.multi.game.core.library.GameViewModel
import com.multi.game.core.library.random
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class WheelGameViewModel : GameViewModel() {
    private val _isSpinning = MutableLiveData(false)
    val isSpinning: LiveData<Boolean> = _isSpinning

    private val _isBonus = MutableLiveData(false)
    val isBonus: LiveData<Boolean> = _isBonus

    private val _rotation = MutableStateFlow(0f)
    val rotation = _rotation.asStateFlow()

    var spinCallback: (() -> Unit)? = null

    private val _balance = MutableLiveData(600)
    val balance: LiveData<Int> = _balance

    private val _win = MutableLiveData(0)
    val win: LiveData<Int> = _win

    private val _activatedBonus = MutableLiveData(0)
    val activatedBonus: LiveData<Int> = _activatedBonus

    fun setBonus(value: Boolean) {
        _isBonus.postValue(value)
    }

    fun buy(value: Int) {
        _balance.postValue(_balance.value!! - value)
    }

    fun activateBonus(value: Int) {
        _activatedBonus.postValue(value)
    }

    fun spin() {
        viewModelScope.launch {
            _isSpinning.postValue(true)
            var value = (8..25).random().toFloat()
            val randomDelay = 10 random 13
            val randomDecrease = Random.nextFloat() * (0.044f - 0.067f) + 0.084f
            while (value > 0.10) {
                delay(randomDelay.toLong())
                if (value > 0.10f) {
                    value -= randomDecrease
                }
                if (value - randomDecrease < 0.10f) {
                    val valuesBasic = listOf(
                        5, -10, 100, -5, 10, -100, 5, -10, 50, -5, 10, -50
                    )
                    val valuesIncreaseLuck = listOf(
                        5, 10, 100, 5, 10, 100, 5, 10, 50, 5, 10, 50
                    )
                    val valuesDoubleWin = listOf(
                        10, -20, 200, -10, 20, -200, 10 - 20, 100, -10, 20, -100
                    )
                    val totalSections = valuesBasic.size
                    val degreesPerSection = 360f / totalSections
                    val normalizedDegrees = (_rotation.value!!) % 360f

                    val sectionIndex = normalizedDegrees / degreesPerSection
                    if (_isSpinning.value!!) {
                        _isSpinning.postValue(false)
                        var winning = valuesBasic[sectionIndex.toInt()]

                        if (_activatedBonus.value!! == 1) {
                            winning = valuesIncreaseLuck[sectionIndex.toInt()]
                        }

                        if (_activatedBonus.value!! == 2) {
                            winning = valuesDoubleWin[sectionIndex.toInt()]
                        }

                        _win.postValue(winning)
                        _balance.postValue(_balance.value!! + winning)

                        if (_isBonus.value!!) {
                            spinCallback?.invoke()
                        }

                        _isBonus.postValue(false)
                        _activatedBonus.postValue(0)

                        if (_balance.value!! < 0) {
                            _balance.postValue(1000)
                        }
                    }
                }
                _rotation.value = (_rotation.value + value)
            }
        }
    }
}