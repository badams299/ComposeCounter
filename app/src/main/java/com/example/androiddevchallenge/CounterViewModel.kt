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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CounterViewModel : ViewModel() {

    private val _state = MutableLiveData<Timer>(Timer.New)
    val state: LiveData<Timer> = _state

    var time by mutableStateOf(10L)

    private var timer: TimeyMcTimer? = null
    private var currentTime: Long = 0L

    fun createTimer(mills: Long, interval: Long) {
        timer = TimeyMcTimer(
            mills,
            interval,
            this::onTimeChanged,
            this::onFinished
        )
        time = mills / 1000
        timer?.start()
        _state.value = Timer.Running
    }

    fun startTimer() {
        if (timer == null) {
            createTimer(currentTime, 1)
        }
        timer?.start()
        _state.value = Timer.Running
    }

    fun pauseTimer() {
        timer?.cancel()
        timer = null
        _state.value = Timer.Paused
    }

    fun resetTimer() {
        timer = null
        _state.value = Timer.New
    }

    private fun onTimeChanged(newTime: Long) {
        currentTime = newTime
        time = newTime / 1000
    }

    private fun onFinished() {
        timer = null
        _state.value = Timer.Launch
    }
}
