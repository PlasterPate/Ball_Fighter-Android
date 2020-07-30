package com.example.timefighter

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

abstract class CountDownTimer(private var totalMillis : Long,private var interval : Long, lifecycle: Lifecycle) : LifecycleObserver {

    private var remainingMillis : Long = totalMillis
    private lateinit var countDownTimer: CountDownTimer
    private var isPaused = true
    private var isStarted = false

    init {
        lifecycle.addObserver(this)
    }

    private fun createTimer(){
        countDownTimer = object : CountDownTimer(remainingMillis, interval){
            override fun onTick(millisUntilFinished: Long) {
                remainingMillis = millisUntilFinished
                this@CountDownTimer.onTick(millisUntilFinished)
            }

            override fun onFinish() {
                this@CountDownTimer.onFinish()
                this@CountDownTimer.cancel()
            }
        }
    }

    abstract fun onTick(millisUntilFinished: Long)

    abstract fun onFinish()

    fun start() : com.example.timefighter.CountDownTimer {
        if(isPaused){
            createTimer()
            countDownTimer.start()
            isPaused = false
            isStarted = true
        }
        return this
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cancel(){
        if (isStarted) {
            Log.i("PlayFragment", "observer destroy")
            countDownTimer.cancel()
            isPaused = true
            isStarted = false
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pause(){
        if (isStarted && !isPaused){
            Log.i("PlayFragment", "observer pause")
            countDownTimer.cancel()
            isPaused = true
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resume(){
        if(isStarted && isPaused){
            Log.i("PlayFragment", "observer resume")
            start()
            isPaused = false
        }
    }

}