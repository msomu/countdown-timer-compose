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

import android.os.CountDownTimer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.bgButton
import com.example.androiddevchallenge.ui.theme.bgCenterColor
import com.example.androiddevchallenge.ui.theme.bgEdgeColor
import com.example.androiddevchallenge.ui.theme.bgText
import com.example.androiddevchallenge.ui.theme.darkRed
import com.example.androiddevchallenge.ui.theme.lightOrange
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Preview
@Composable
fun CountDownPreview() {
    MyTheme {
        Countdown()
    }
}

@Composable
fun Countdown() {
    val initialCountDown = 30000
    var timerRunning by remember { mutableStateOf(false) }
    val countdownStart by remember { mutableStateOf(initialCountDown) }
    var timeLeft by remember { mutableStateOf(30) }

    val timer: CountDownTimer = object : CountDownTimer(countdownStart.toLong(), 10) {

        override fun onTick(millisUntilFinished: Long) {
            timeLeft = (millisUntilFinished / 1000).toInt()
        }

        override fun onFinish() {
            cancel()
            timerRunning = false
            timeLeft = (countdownStart / 1000)
        }
    }

    Surface {
        Column(
            Modifier
                .fillMaxSize()
                .background(Brush.radialGradient(listOf(bgCenterColor, bgEdgeColor))),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Timer",
                style = TextStyle(
                    color = bgCenterColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                )
            )
            Text(
                text = getReadableTime(countdownStart / 1000),
                style = TextStyle(
                    color = bgCenterColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                )
            )
            Box(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getReadableTime(timeLeft),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 48.sp,
                        textAlign = TextAlign.Center,
                    )
                )
                for (i in 0 until 40) {
                    Ticker(
                        angle = (i * 9) + 270,
                        on = shouldLightUp(timerRunning, countdownStart, timeLeft, i, 40),
                        colorLine = getTheColor(timerRunning, i, 40)
                    )
                }
            }
            Spacer(modifier = Modifier.padding(32.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = {
                    },
                    modifier = Modifier.background(color = bgButton, shape = CircleShape)
                ) {
                    Icon(
                        Icons.Filled.Refresh,
                        contentDescription = null,
                        tint = bgText,
                    )
                }
                Button(
                    onClick = {
                        when (timerRunning) {
                            true -> {
                                timer.cancel()
                                timerRunning = false
                                timeLeft = (countdownStart / 1000)
                            }
                            false -> {
                                timer.start()
                                timerRunning = true
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (timerRunning) Color.White else MaterialTheme.colors.primary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = if (timerRunning) "STOP" else "START",
                        Modifier.padding(16.dp, 8.dp),
                        style = TextStyle(
                            fontSize = 18.sp,
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Black,
                            color = if (timerRunning) MaterialTheme.colors.primary else Color.White
                        )
                    )
                }
                IconButton(
                    onClick = {
                    },
                    modifier = Modifier.background(color = bgButton, shape = CircleShape)
                ) {
                    Icon(
                        Icons.Filled.VolumeOff,
                        contentDescription = null,
                        tint = bgText,
                    )
                }
            }
        }
    }
}

fun getTheColor(
    isTimerRunning: Boolean,
    position: Int,
    maxPosition: Int
): Color {
    if (!isTimerRunning) {
        return Color.White.copy(0.1f)
    }
    val positionPercentage: Float = (position.toFloat() / maxPosition.toFloat())
    return Color(ColorUtils.blendARGB(lightOrange.toArgb(), darkRed.toArgb(), positionPercentage))
}

fun shouldLightUp(
    isTimerRunning: Boolean,
    countdownStart: Int,
    timeLeft: Int,
    position: Int,
    maxPosition: Int
): Boolean {
    if (!isTimerRunning) {
        return true
    }
    val timePercentage: Float = (timeLeft.toFloat() / (countdownStart.toFloat() / 1000f)) * 100f
    val positionPercentage: Float = (position.toFloat() / maxPosition.toFloat()) * 100f
    return timePercentage > positionPercentage
}

const val EndRadiusFraction = 0.75f
const val StartRadiusFraction = 0.55f
const val TickWidth = 16f

@Composable
fun Ticker(
    angle: Int,
    on: Boolean,
    colorLine: Color
) {
    Box(
        modifier =
        Modifier
            .fillMaxSize()
            .drawBehind {
                val theta = angle * PI.toFloat() / 180f
                val startRadius = size.width / 2 * StartRadiusFraction
                val endRadius = size.width / 2 * EndRadiusFraction
                val startPos = Offset(
                    cos(theta) * startRadius,
                    sin(theta) * startRadius
                )
                val endPos = Offset(
                    cos(theta) * endRadius,
                    sin(theta) * endRadius
                )
                drawLine(
                    if (on) colorLine else Color.White.copy(0.2f),
                    center + startPos,
                    center + endPos,
                    TickWidth,
                    StrokeCap.Round
                )
            }
    )
}
