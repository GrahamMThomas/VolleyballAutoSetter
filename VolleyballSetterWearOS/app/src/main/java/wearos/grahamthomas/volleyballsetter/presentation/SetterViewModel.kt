package wearos.grahamthomas.volleyballsetter.presentation

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

class SetterViewModel(volleyClient: RequestQueue) : ViewModel() {
    private val _setterState: MutableState<SetterState> = mutableStateOf(SetterState.READY)
    private val _totalSetTime: MutableState<Float> = mutableStateOf(0f)
    private val _remainingSetTime: MutableState<Float> = mutableStateOf(0f)
    private val _remainingCooldownTime: MutableState<Float> = mutableStateOf(0f)
    private val _networkConnected: MutableState<Boolean> = mutableStateOf(false)

    val setterState: SetterState
        get() = _setterState.value
    val totalSetTime: Float
        get() = _totalSetTime.value
    val remainingSetTime: Float
        get() = _remainingSetTime.value
    val remainingCooldownTime: Float
        get() = _remainingCooldownTime.value
    val networkConnected: Boolean
        get() = _networkConnected.value

    private val TOTAL_COOLDOWN_TIME = 1.5f

    val timerPercentageCompletion: Float
        get() = 1f - (_remainingSetTime.value / _totalSetTime.value)

    val cooldownPercentageCompletion: Float
        get() = 1f - (_remainingCooldownTime.value / TOTAL_COOLDOWN_TIME)

    val secondsRemaining: String
        get() = _remainingSetTime.value.roundToInt().toString()

    private var _volleyClient: RequestQueue
    init {
        _volleyClient = volleyClient
    }

    fun requestSet(){
//        _setterState.value = SetterState.REQUESTED
        val url = "http://10.0.2.2:9916/actuate"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                // Display the first 500 characters of the response string.
                Log.d("thing", "Response is: $response")
            },
            { error -> Log.d("thing","Request Fail $error!") })

        _volleyClient.add(stringRequest)
//        viewModelScope.launch { beginCountdown() }
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