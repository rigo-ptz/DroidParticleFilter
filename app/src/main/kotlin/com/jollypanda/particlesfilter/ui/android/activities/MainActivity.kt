package com.jollypanda.particlesfilter.ui.android.activities

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.jollypanda.particlesfilter.R
import com.jollypanda.particlesfilter.data.Robot
import com.jollypanda.particlesfilter.pf.ParticleFilter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

/**
 * @author Yamushev Igor
 * @since  13.05.17
 */
class MainActivity : AppCompatActivity() {

    val screenHeight by lazy {
        resources.displayMetrics.heightPixels
    }

    val screenWidth by lazy {
        resources.displayMetrics.widthPixels
    }

    lateinit var pf: ParticleFilter

    val hc: Handler.Callback = Handler.Callback {
        vParticle.updateDroid(pf.robot, pf.particlesList)
        return@Callback true
    }

    val handler = Handler(hc)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initPf()
    }

    private fun initPf() {
        vParticle.measure(0, 0)
        val widthConstraint = screenWidth - vParticle.icBitmap.width
        val heightConstraint = screenHeight - vParticle.icBitmap.height
        pf = ParticleFilter(Robot(widthConstraint, heightConstraint), widthConstraint, heightConstraint)
        vParticle.updateDroid(pf.robot, pf.particlesList)
    }

    override fun onResume() {
        super.onResume()
        val timer = Timer()
        timer.scheduleAtFixedRate(1000, 2000) {
            pf.move()
            handler.sendEmptyMessage(0)
            pf.filtrate()
        }
    }
}