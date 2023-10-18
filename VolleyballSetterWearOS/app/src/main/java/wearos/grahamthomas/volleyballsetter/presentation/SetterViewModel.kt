package wearos.grahamthomas.volleyballsetter.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.StringRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class SetterViewModel(volleyClient: RequestQueue) : ViewModel() {
    private val _setterState: MutableState<SetterState> = mutableStateOf(SetterState.READY)
    private val _totalSetTime: MutableState<Float> = mutableStateOf(0f)
    private val _remainingSetTime: MutableState<Float> = mutableStateOf(0f)
    private val _remainingCooldownTime: MutableState<Float> = mutableStateOf(0f)
    private val _backendHealthy: MutableState<Boolean> = mutableStateOf(false)

    val setterState: SetterState
        get() = _setterState.value
    val totalSetTime: Float
        get() = _totalSetTime.value
    val remainingSetTime: Float
        get() = _remainingSetTime.value
    val remainingCooldownTime: Float
        get() = _remainingCooldownTime.value
    val backendHealthy: Boolean
        get() = _backendHealthy.value

    private val TOTAL_COOLDOWN_TIME = 1.5f
    private val ENDPOINT = "10.0.2.2:9916"

    val timerPercentageCompletion: Float
        get() = 1f - (_remainingSetTime.value / _totalSetTime.value)

    val cooldownPercentageCompletion: Float
        get() = 1f - (_remainingCooldownTime.value / TOTAL_COOLDOWN_TIME)

    val secondsRemaining: String
        get() = _remainingSetTime.value.roundToInt().toString()

    private var _volleyClient: RequestQueue
    init {
        _volleyClient = volleyClient
        _totalSetTime.value = 2f
        viewModelScope.launch { pollBackendUntilHealthy() }
    }


    fun requestSet(){
        _setterState.value = SetterState.REQUESTED
        viewModelScope.launch { handleSetCountdown() }
    }

    private suspend fun handleSetCountdown(){
        _setterState.value = SetterState.SETTING
        _remainingSetTime.value = _totalSetTime.value

        while (_remainingSetTime.value > 0f){
            val timeSkip = 0.01f
            _remainingSetTime.value -= timeSkip
            delay ((1000 * timeSkip).toLong())
        }
        makeSetRequestToApi()

        handleCooldown()
    }

    private suspend fun handleCooldown(){
        _setterState.value = SetterState.COOLDOWN
        _remainingCooldownTime.value = TOTAL_COOLDOWN_TIME

        while (_remainingCooldownTime.value > 0f){
            val timeSkip = 0.01f
            _remainingCooldownTime.value -= timeSkip
            delay ((1000 * timeSkip).toLong())
        }

        resetState()
    }

    private fun resetState(){
        _remainingSetTime.value = 0f
        _remainingCooldownTime.value = 0f
        _setterState.value = SetterState.READY
    }

    private suspend fun pollBackendUntilHealthy(){
        while (!_backendHealthy.value){
            checkBackendHealth()
            delay(2000)
        }
    }

    private fun checkBackendHealth(){
        val url = "http://$ENDPOINT/health"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { _ ->
                _backendHealthy.value = true
            },
            { _ -> _backendHealthy.value = false })

        stringRequest.retryPolicy = DefaultRetryPolicy(1500,0,0f)
        _volleyClient.add(stringRequest)
    }

    private fun makeSetRequestToApi(){
        val url = "http://$ENDPOINT/actuate"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                Log.d("requestSet", "Response is: $response")
            },
            { error ->
                Log.d("requestSet","Request Fail $error!")
                resetState()
                _backendHealthy.value = false
                viewModelScope.launch { pollBackendUntilHealthy() }
            })

        stringRequest.retryPolicy = DefaultRetryPolicy(1500,0,0f)
        _volleyClient.add(stringRequest)
    }
}