package ajl.com.bible.ui.searchhistory

import ajl.com.bible.R
import ajl.com.bible.controllers.OnFragmentInteractionListener
import ajl.com.domain.entities.SearchHistoryDisplayEntity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.search_list_activity.*

class SearchListActivity : AppCompatActivity(), OnFragmentInteractionListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_list_activity)


        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        //set back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        title = getString(R.string.label_search_details)
        val searchHistoryDisplayEntity =
            intent.getParcelableExtra<SearchHistoryDisplayEntity>("SearchHistoryDisplayEntity")
        if (savedInstanceState == null) {
            /* supportFragmentManager.beginTransaction()
                 .replace(R.id.container, SearchListFragment.newInstance(searchHistoryDisplayEntity))
                 .commitNow()*/

            val navController = Navigation.findNavController(this, R.id.nav_controller_fragment)
            val bundle = Bundle()
            bundle.putParcelable("searchHistory", searchHistoryDisplayEntity)
            navController.setGraph(navController.graph, bundle)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_history_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onFragmentInteraction(mode: Int, dat: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setTitle(title: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
