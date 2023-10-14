package wearos.grahamthomas.volleyballsetter.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class SetterViewModel: ViewModel() {
    private val _setterState: MutableState<SetterState> = mutableStateOf(SetterState.READY)
    val setterState: SetterState
        get() = _setterState.value

    private val _totalSetTime: MutableState<Float> = mutableStateOf(0f)
    val totalSetTime: Float
        get() = _totalSetTime.value

    private val _remainingSetTime: MutableState<Float> = mutableStateOf(0f)
    val remainingSetTime: Float
        get() = _remainingSetTime.value

    val timerPercentageCompletion: Float
        get() = 1f - (_remainingSetTime.value / _totalSetTime.value)

    val secondsRemaining: String
        get() = _remainingSetTime.value.roundToInt().toString()

    public fun requestSet(){
        _setterState.value = SetterState.REQUESTED
        viewModelScope.launch { beginCountdown() }
    }

    private suspend fun beginCountdown(){
        delay(2000)
        _setterState.value = SetterState.SETTING
        _totalSetTime.value = 5f
        _remainingSetTime.value = _totalSetTime.value
    }
}