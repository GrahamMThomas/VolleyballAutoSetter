package wearos.grahamthomas.volleyballsetter.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SignalWifiBad
import androidx.compose.material.icons.rounded.SportsVolleyball
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.tiles.material.ButtonColors
import com.android.volley.toolbox.Volley
import wearos.grahamthomas.volleyballsetter.presentation.theme.VolleyballSetterWearOSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var volleyClient = Volley.newRequestQueue(applicationContext)
        var viewModel = SetterViewModel(volleyClient)
        setContent {
            VolleyballSetterWearOSTheme {
                Scaffold {
                    VolleyballSetter(viewModel)
                }
            }
        }
    }
}

@Composable
private fun VolleyballSetter(
    setterViewModel: SetterViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        if (setterViewModel.setterState == SetterState.SETTING) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = setterViewModel.timerPercentageCompletion,
                    modifier = Modifier.fillMaxSize(),
                    startAngle = 290f,
                    endAngle = 250f,
                    strokeWidth = 10.dp
                )
                Text(text = String.format("%.0f", setterViewModel.remainingSetTime+1),
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp)

            }
        }
        Row ( modifier = Modifier.height(40.dp)){

        }
        Row ( verticalAlignment = Alignment.CenterVertically){
            if (setterViewModel.setterState == SetterState.REQUESTED) {
                CircularProgressIndicator(
                    indicatorColor = MaterialTheme.colors.secondary,
                    trackColor = MaterialTheme.colors.onBackground.copy(alpha = 0.1f),
                    strokeWidth = 6.dp,
                    modifier = Modifier
                        .width(52.dp)
                        .height(52.dp)
                )
            } else {
                Button(onClick = { setterViewModel.requestSet() },
                    enabled = (setterViewModel.setterState == SetterState.READY && setterViewModel.backendHealthy),
                    modifier = Modifier
                ) {

                    Icon(
                        imageVector = Icons.Rounded.SportsVolleyball,
                        contentDescription = "Launch Button"
                    )
                    if (setterViewModel.setterState == SetterState.COOLDOWN){
                        CircularProgressIndicator(
                            progress = setterViewModel.cooldownPercentageCompletion,
                            indicatorColor = MaterialTheme.colors.error,
                            modifier = Modifier
                                .width(52.dp)
                                .height(52.dp),
                            trackColor = MaterialTheme.colors.onBackground.copy(alpha = 0.1f),
                            strokeWidth = 6.dp
                        )
                    }
                }

            }
        }
        Row (modifier = Modifier.height(40.dp)) {
            if (!setterViewModel.backendHealthy){
                Icon(
                    imageVector = Icons.Rounded.SignalWifiBad,
                    contentDescription = "No Backend Connected"
                )
            }
        }
    }
}


