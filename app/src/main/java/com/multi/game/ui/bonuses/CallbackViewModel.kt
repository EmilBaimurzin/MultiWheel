package com.multi.game.ui.bonuses

import androidx.lifecycle.ViewModel

class CallbackViewModel : ViewModel() {
    var callback: ((Int) -> Unit)? = null
}