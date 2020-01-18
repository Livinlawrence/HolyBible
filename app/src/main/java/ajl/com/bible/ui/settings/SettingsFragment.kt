package ajl.com.bible.ui.settings

import ajl.com.bible.ui.home.HomeActivity
import ajl.com.bible.utility.Constants.Companion.HOLY_BIBLE_PREFERENCE
import ajl.com.bible.utility.Constants.Companion.PREFERENCE_BACK_BUTTON
import ajl.com.bible.utility.Constants.Companion.PREFERENCE_FULL_SCREEN
import ajl.com.bible.utility.Constants.Companion.PREFERENCE_NAVIGATION
import ajl.com.bible.utility.Constants.Companion.PREFERENCE_SCREEN_ON
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_VISIBLE
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(ajl.com.bible.R.layout.fragment_settings, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref: SharedPreferences = context!!.getSharedPreferences(HOLY_BIBLE_PREFERENCE, Context.MODE_PRIVATE)


        cbScreenOn.isChecked = sharedPref.getBoolean(PREFERENCE_SCREEN_ON, false)
        cbFullScreen.isChecked = sharedPref.getBoolean(PREFERENCE_FULL_SCREEN, false)
        cbBackButton.isChecked = sharedPref.getBoolean(PREFERENCE_BACK_BUTTON, false)
        cbHideNavigation.isChecked = sharedPref.getBoolean(PREFERENCE_NAVIGATION, false)




        cbScreenOn.setOnCheckedChangeListener { buttonView, isChecked ->
            sharedPref.edit {
                putBoolean(
                    PREFERENCE_SCREEN_ON,
                    isChecked
                )
            }
        }

        cbFullScreen.setOnCheckedChangeListener { buttonView, isChecked ->
            sharedPref.edit {
                putBoolean(
                    PREFERENCE_FULL_SCREEN,
                    isChecked
                )
            }

            if (isChecked) {
                activity?.window?.decorView?.apply {
                    // Hide both the navigation bar and the status bar.
                    // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
                    // a general rule, you should design your app to hide the status bar whenever you
                    // hide the navigation bar.
                    systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN)
                }

                (this.activity as HomeActivity).toolbar.visibility = View.GONE
                (this.activity as HomeActivity).bottomNav.visibility = View.GONE


            } else {
                activity?.window?.decorView?.apply {
                    // Hide both the navigation bar and the status bar.
                    // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
                    // a general rule, you should design your app to hide the status bar whenever you
                    // hide the navigation bar.
                    systemUiVisibility = (SYSTEM_UI_FLAG_VISIBLE)
                }
                (this.activity as HomeActivity).toolbar.visibility = View.VISIBLE
                (this.activity as HomeActivity).bottomNav.visibility = View.VISIBLE
            }

        }

        cbBackButton.setOnCheckedChangeListener { buttonView, isChecked ->
            sharedPref.edit {
                putBoolean(
                    PREFERENCE_BACK_BUTTON,
                    isChecked
                )
            }
        }

        cbHideNavigation.setOnCheckedChangeListener { buttonView, isChecked ->
            sharedPref.edit {
                putBoolean(
                    PREFERENCE_NAVIGATION,
                    isChecked
                )
            }

            if (isChecked) {
                (this.activity as HomeActivity).bottomNav.visibility = View.GONE
            } else {
                (this.activity as HomeActivity).bottomNav.visibility = View.VISIBLE
            }
        }
    }
}
