package com.example.timefighter.play


import android.animation.ObjectAnimator
import android.app.Dialog
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.timefighter.R
import com.example.timefighter.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.fragment_final_score.*

class PlayFragment : androidx.fragment.app.Fragment() {

    lateinit var binding : com.example.timefighter.databinding.FragmentPlayBinding

    lateinit var viewModel: PlayViewModel

    lateinit var finalScoreDialog: Dialog
    lateinit var playgroundLayout: ConstraintLayout
    lateinit var readyTextView: TextView
    lateinit var tapMeButton: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_play, container, false)
        viewModel = ViewModelProviders.of(this).get(PlayViewModel::class.java)
        binding.playViewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
        //return inflater.inflate(R.layout.fragment_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("onViewCreated")
        initializeLayouts(view)

        viewModel.eventGameFinished.observe(this, Observer { isFinished ->
            if (isFinished){
                gameFinished()
                viewModel.onGameFinishCompleted()
            }
        })

        var layoutWidth = 0
        var layoutHeight = 0
        var buttonWidth = 0
        var buttonHeight = 0
        playgroundLayout.post {
            layoutWidth = playgroundLayout.width
            layoutHeight = playgroundLayout.height

            buttonWidth = tapMeButton.width
            buttonHeight = tapMeButton.height
        }
        tapMeButton.post {
            viewModel.tapMeButtonStartingCoords.apply {
                x = tapMeButton.x.toInt()
                y = tapMeButton.y.toInt()
            }
            initializeGame()
        }

        tapMeButton.setOnClickListener { button ->
            if (readyTextView.visibility != View.GONE) {
                startGame()
            }
            val randomX = viewModel.randomNumber(0, layoutWidth - buttonWidth)
            val randomY = viewModel.randomNumber(0, layoutHeight - buttonHeight)

            ObjectAnimator.ofFloat(button, "x", randomX).apply {
                duration = 500
                start()
            }
            ObjectAnimator.ofFloat(button, "y", randomY).apply {
                duration = 500
                start()
            }
            viewModel.incrementScore()

//            val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
//            bounceAnimation.interpolator = BounceInterpolator()
//            //button.animation = bounceAnimation
//            button.startAnimation(bounceAnimation)
        }
    }

    private fun initializeLayouts(view: View) {
        initializeFinalScoreDialog()
        playgroundLayout = binding.playgroundLayout
        readyTextView = binding.readyTextView
        tapMeButton = binding.tapMeButton
    }

    private fun initializeFinalScoreDialog() {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        finalScoreDialog = Dialog(this.context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
        with(finalScoreDialog) {
            setContentView(R.layout.fragment_final_score)
            replay_button.setOnClickListener {
                initializeGame()
                cancel()
            }
            home_button.setOnClickListener {
                cancel()
                findNavController().popBackStack(R.id.homeFragment, false)
            }
        }
    }

    private fun initializeGame() {
        viewModel.resetGame()
        tapMeButton.animate().apply {
            x(viewModel.tapMeButtonStartingCoords.x.toFloat())
            y(viewModel.tapMeButtonStartingCoords.y.toFloat())
        }
        startTextAnimation()
        viewModel.createCountDownTimer(this.lifecycle)
    }

    private fun startTextAnimation() {
        val enlargeAnim = AnimationUtils.loadAnimation(this.context, R.anim.enlarge)
        readyTextView.visibility = View.VISIBLE
        readyTextView.startAnimation(enlargeAnim)
    }

    private fun gameFinished() {
        //viewModel.gameStarted = false
        finalScoreDialog.final_score.text = getString(R.string.final_score, viewModel.score.value.toString())
        finalScoreDialog.show()
    }

    private fun startGame() {
        viewModel.timer.start()
        readyTextView.clearAnimation()
        readyTextView.visibility = View.GONE
        //viewModel.gameStarted = true
    }

    override fun onStart() {
        super.onStart()
        println("onStart")
    }

    override fun onStop() {
        super.onStop()
        println("onStop")
    }

    override fun onResume() {
        super.onResume()
        println("onResume")
    }

    override fun onPause() {
        super.onPause()
        println("onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("onDestroy")
    }

}
