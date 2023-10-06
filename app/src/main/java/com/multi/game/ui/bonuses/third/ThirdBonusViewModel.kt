package com.multi.game.ui.bonuses.third

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.multi.game.core.library.random

class ThirdBonusViewModel: ViewModel() {
    private val _state = MutableLiveData(false)
    val state: LiveData<Boolean> = _state

    var choice = 0
    var result = 1 random 2

    fun click(_choice: Int) {
        choice = _choice
        _state.postValue(true)
    }
}