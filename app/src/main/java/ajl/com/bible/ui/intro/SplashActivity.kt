package ajl.com.bible.ui.intro

import ajl.com.bible.R
import ajl.com.bible.extensions.launchActivity
import ajl.com.bible.ui.home.HomeActivity
import ajl.com.data.db.AppDatabase
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {


    private lateinit var tvTitle: View
    private var shortAnimationDuration: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        tvTitle = findViewById(R.id.tvTitle)

        // Initially hide the content view.
        tvTitle.visibility = View.GONE

        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
        crossfade()

        AppDatabase.invoke(this)

        Handler().postDelayed({
            launchActivity<HomeActivity> { }
            finish()
        }, 500)

    }

    private fun crossfade() {
        tvTitle.apply {
            // Set the content view to 0% opacity but visible, so that it is visible
            // (but fully transparent) during the animation.
            alpha = 0f
            visibility = View.VISIBLE

            // Animate the content view to 100% opacity, and clear any animation
            // listener set on the view.
            animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration.toLong())
                .setListener(null)
        }

    }

}
