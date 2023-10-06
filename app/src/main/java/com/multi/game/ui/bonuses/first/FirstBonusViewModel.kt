package com.multi.game.ui.bonuses.first

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.multi.game.core.library.random

class FirstBonusViewModel : ViewModel() {
    private val _state = MutableLiveData(false)
    val state: LiveData<Boolean> = _state

    var win = 1 random 3
    var balloon = 0

    fun click(_balloon: Int) {
        balloon = _balloon
        _state.postValue(true)
    }
}