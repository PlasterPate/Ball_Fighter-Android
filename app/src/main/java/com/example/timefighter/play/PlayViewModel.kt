package com.example.timefighter.play

import android.graphics.Point
import android.text.format.DateUtils
import androidx.lifecycle.*
import com.example.timefighter.CountDownTimer
import kotlin.random.Random

class PlayViewModel : ViewModel(){

    companion object{
        const val COUNTDOWN_TIME: Long = 10000
        const val ONE_SECOND: Long = 1000
    }


    lateinit var timer: CountDownTimer

    // score
    private val _score = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = _score

    //time left
    private val _timeLeft = MutableLiveData<Long>()
    val timeLeft : LiveData<Long>
        get() = _timeLeft
    val timeLeftString = Transformations.map(timeLeft){time ->
        DateUtils.formatElapsedTime(time)
    }

    //end game event
    private val _eventGameFinished = MutableLiveData<Boolean>()
    val eventGameFinished : LiveData<Boolean>
        get() = _eventGameFinished

    val tapMeButtonStartingCoords = Point(0, 0)
    var gameStarted = false

    init {
        _score.value = 0
        _timeLeft.value = COUNTDOWN_TIME
        _eventGameFinished.value = false
    }

    fun createCountDownTimer(lifecycle: Lifecycle){
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND, lifecycle) {
            override fun onTick(millisUntilFinished: Long) {
                changeTimeLeft(millisUntilFinished / 1000)
            }

            override fun onFinish() {
                _eventGameFinished.postValue(true)
            }
        }
    }

    fun randomNumber(firstNum : Int, secondNum : Int) : Float{
        return Random.nextInt(firstNum, secondNum).toFloat()
    }

    fun incrementScore() {
        _score.value = score.value?.plus(1)
    }

    fun resetGame(){
        _score.value = 0
        changeTimeLeft(COUNTDOWN_TIME / 1000)
    }

    fun changeTimeLeft(time : Long){
        _timeLeft.value = time
    }

    fun onGameFinishCompleted() {
        _eventGameFinished.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
    }
}
