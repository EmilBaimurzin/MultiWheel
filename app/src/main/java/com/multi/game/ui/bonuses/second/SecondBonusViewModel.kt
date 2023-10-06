package com.multi.game.ui.bonuses.second

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.multi.game.core.library.random

class SecondBonusViewModel: ViewModel() {
    private val _state = MutableLiveData(false)
    val state: LiveData<Boolean> = _state

    var number = 1 random 6
    var choice = true

    fun click(_choice: Boolean) {
        choice = _choice
        _state.postValue(true)
    }
}