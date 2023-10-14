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

    private val _remainingCooldownTime: MutableState<Float> = mutableStateOf(0f)
    val remainingCooldownTime: Float
        get() = _remainingCooldownTime.value

    private val TOTAL_COOLDOWN_TIME = 1.5f

    val timerPercentageCompletion: Float
        get() = 1f - (_remainingSetTime.value / _totalSetTime.value)

    val cooldownPercentageCompletion: Float
        get() = 1f - (_remainingCooldownTime.value / TOTAL_COOLDOWN_TIME)

    val secondsRemaining: String
        get() = _remainingSetTime.value.roundToInt().toString()

    public fun requestSet(){
        _setterState.value = SetterState.REQUESTED
        viewModelScope.launch { beginCountdown() }
    }

    private suspend fun beginCountdown(){
        delay(1000)
        _setterState.value = SetterState.SETTING
        _totalSetTime.value = 2f
        _remainingSetTime.value = _totalSetTime.value
        handleCountdown()
    }

    private suspend fun handleCountdown(){
        while (_remainingSetTime.value > 0f){
            val timeSkip = 0.01f
            _remainingSetTime.value -= timeSkip
            delay ((1000 * timeSkip).toLong())
        }

        _setterState.value = SetterState.COOLDOWN
        _remainingSetTime.value = 0f
        _totalSetTime.value = 0f
        handleCooldown()
    }

    private suspend fun handleCooldown(){
        _remainingCooldownTime.value = TOTAL_COOLDOWN_TIME

        while (_remainingCooldownTime.value > 0f){
            val timeSkip = 0.01f
            _remainingCooldownTime.value -= timeSkip
            delay ((1000 * timeSkip).toLong())
        }

        _remainingCooldownTime.value = 0f
        _setterState.value = SetterState.READY
    }
}