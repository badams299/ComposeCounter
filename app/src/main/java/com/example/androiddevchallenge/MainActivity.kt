/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {

    val counterViewModel by viewModels<CounterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp(counterViewModel)
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp(vm: CounterViewModel) {
    Surface(color = MaterialTheme.colors.background) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.weight(3f),
                verticalArrangement = Arrangement.Center
            ) {
                Counter(
                    vm
                )
            }
            Row(
                modifier = Modifier.weight(1f)
            ) {
                TimerControls(vm)
            }
        }
    }
}

@Composable
fun TimerControls(vm: CounterViewModel) {
    val state: Timer by vm.state.observeAsState(Timer.New)

    when (state) {
        Timer.Running -> IconButton(
            onClick = {
                vm.pauseTimer()
            },
            modifier = Modifier.background(
                MaterialTheme.colors.primaryVariant,
                shape = MaterialTheme.shapes.medium
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_pause_24px),
                contentDescription = "pause"
            )
        }
        Timer.Paused -> IconButton(
            onClick = {
                vm.startTimer()
            },
            modifier = Modifier.background(
                MaterialTheme.colors.primaryVariant,
                shape = MaterialTheme.shapes.medium
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_play_arrow_24px),
                contentDescription = "play"
            )
        }
        Timer.New, Timer.Launch -> AddTimerButton(onClick = { vm.createTimer(10000, 1000) })
    }
}

@Composable
fun AddTimerButton(onClick: () -> Unit) {

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant),
    ) {
        Text(
            "START LAUNCH",
            color = Color.White
        )
    }
}

@Composable
fun Counter(vm: CounterViewModel) {
    val state: Timer by vm.state.observeAsState(Timer.New)
    val scale = animateFloatAsState(11f - vm.time.toFloat())
    if (state !is Timer.Launch) {
        Text(
            text = "${vm.time}",
            fontSize = 24.sp,
            modifier = Modifier.scale(scale.value)
        )
    } else {
        val infiniteTransition = rememberInfiniteTransition()
        val color by infiniteTransition.animateFloat(
            initialValue = 0F,
            targetValue = 3F,
            animationSpec = infiniteRepeatable(
                animation = tween(10, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        Text(
            text = "Liftoff!!",
            fontSize = 48.sp,
            letterSpacing = color.sp
        )
    }
}

@Preview
@Composable
fun PreviewCounter() {
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp(CounterViewModel())
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp(CounterViewModel())
    }
}
