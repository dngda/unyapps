package id.infiniteuny.apps.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import id.infiniteuny.apps.R
import id.infiniteuny.apps.adapter.SliderAdapterExample

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setSlider()
    }

    private fun setSlider() {
        val sliderView: SliderView = findViewById(R.id.imageSlider)

        val adapter = SliderAdapterExample(this)

        sliderView.sliderAdapter = adapter

        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!

        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        sliderView.indicatorSelectedColor = Color.WHITE
        sliderView.indicatorUnselectedColor = R.color.colorPrimary
        sliderView.scrollTimeInSec = 4 //set scroll delay in seconds :

        sliderView.startAutoCycle()
    }
}
