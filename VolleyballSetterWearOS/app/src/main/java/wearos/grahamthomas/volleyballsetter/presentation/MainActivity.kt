package wearos.grahamthomas.volleyballsetter.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.SportsVolleyball
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import wearos.grahamthomas.volleyballsetter.R
import wearos.grahamthomas.volleyballsetter.presentation.theme.VolleyballSetterWearOSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold {
                VolleyballSetter()
            }
        }
    }
}

@Composable
private fun VolleyballSetter(
    setterViewModel: SetterViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        if (setterViewModel.setterState == SetterState.SETTING) {
            Box() {
                Text(text = setterViewModel.secondsRemaining)
                CircularProgressIndicator(
                    progress = setterViewModel.timerPercentageCompletion,
                    modifier = Modifier.fillMaxSize(),
                    startAngle = 10f,
                    endAngle = 290f,
                    strokeWidth = 6.dp
                )
            }
        } else if (setterViewModel.setterState == SetterState.REQUESTED) {
            CircularProgressIndicator(
                indicatorColor = MaterialTheme.colors.secondary,
                trackColor = MaterialTheme.colors.onBackground.copy(alpha = 0.1f),
                strokeWidth = 4.dp
            )
        } else {
            Button(onClick = { setterViewModel.requestSet() },
                enabled = setterViewModel.setterState == SetterState.READY) {
                Icon(
                    imageVector = Icons.Rounded.SportsVolleyball,
                    contentDescription = "Launch Button"
                )
            }
        }
    }
}


