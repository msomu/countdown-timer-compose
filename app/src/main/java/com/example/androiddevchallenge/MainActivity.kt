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
import android.os.CountDownTimer
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.MyTheme

@ExperimentalAnimationApi
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme(darkTheme = true) {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@ExperimentalAnimationApi
@Composable
fun MyApp() {
    val initialCountDown = 10000
    val progress = remember { mutableStateOf(1F) }
    val timeLeft = remember { mutableStateOf(10) }
    val countdownStart = remember { mutableStateOf(initialCountDown) }
    val timerRunning = remember { mutableStateOf(false) }

    val timer: CountDownTimer = object : CountDownTimer(countdownStart.value.toLong(), 10) {

        override fun onTick(millisUntilFinished: Long) {
            val value: Float = millisUntilFinished.toFloat() / (countdownStart.value.toFloat())
            progress.value = value
            timeLeft.value = (millisUntilFinished / 1000).toInt()
        }

        override fun onFinish() {
            cancel()
            timerRunning.value = false
            progress.value = 1F
            timeLeft.value = (countdownStart.value / 1000)
        }
    }

    Surface(color = MaterialTheme.colors.background) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.weight(3f)
            ) {
                Box(
                    modifier = Modifier
                        .height(280.dp)
                        .width(280.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = 1F, strokeWidth = 8.dp, color = Color.White,
                        modifier = Modifier
                            .height(280.dp)
                            .width(280.dp)
                    )
                    CircularProgressIndicator(
                        progress = progress.value,
                        strokeWidth = 8.dp,
                        color = if (progress.value > 0.3) MaterialTheme.colors.primary else Color.Red,
                        modifier = Modifier
                            .height(280.dp)
                            .width(280.dp)
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            getReadableTime(timeLeft.value),
                            style = TextStyle(
                                fontSize = if (timeLeft.value >= 3600) 48.sp else 72.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colors.primary
                            )
                        )
                    }
                }
                Button(
                    onClick = {
                        when (timerRunning.value) {
                            true -> {
                                timer.cancel()
                                timerRunning.value = false
                                progress.value = 1F
                                timeLeft.value = (countdownStart.value / 1000)
                            }
                            false -> {
                                timer.start()
                                timerRunning.value = true
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (timerRunning.value) Color.Red else MaterialTheme.colors.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = if (timerRunning.value) "Stop" else "Start",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )
                    )
                }
            }
            Row(modifier = Modifier.weight(1f)) {
                AnimatedVisibility(
                    visible = !timerRunning.value,
                    enter = slideInVertically(
                        initialOffsetY = { -40 }
                    ) + expandVertically(
                        expandFrom = Alignment.Top
                    ) + fadeIn(initialAlpha = 0.3f),
                    exit = slideOutVertically() + shrinkVertically() + fadeOut()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Row(modifier = Modifier.clip(RoundedCornerShape(12))) {
                            IconButton(
                                onClick = {
                                    timer.cancel()
                                    timerRunning.value = false
                                    progress.value = 1F
                                    timeLeft.value = (initialCountDown / 1000)
                                }
                            ) {
                                Text(
                                    "Reset",
                                    style = TextStyle(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                )
                            }
                        }
                        Row(modifier = Modifier.clip(RoundedCornerShape(12))) {
                            IconButton(
                                onClick = {

                                    val value = 1000

                                    if (countdownStart.value > value) {
                                        countdownStart.value = countdownStart.value - value
                                        timeLeft.value = (countdownStart.value / 1000)
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.Filled.Remove,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                            IconButton(
                                onClick = {
                                    val value = 1000

                                    countdownStart.value = countdownStart.value + value
                                    timeLeft.value = (countdownStart.value / 1000)
                                }
                            ) {
                                Icon(
                                    Icons.Filled.Add,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// @Preview("Light Theme", widthDp = 360, heightDp = 640)
// @Composable
// fun LightPreview() {
//    MyTheme {
//        MyApp()
//    }
// }

@ExperimentalAnimationApi
@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
