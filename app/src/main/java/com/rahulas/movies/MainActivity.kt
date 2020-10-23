package com.rahulas.movies

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.iammert.library.readablebottombar.ReadableBottomBar
import com.rahulas.movies.ui.FavMoviesFragment
import com.rahulas.movies.ui.MovieFragment

class MainActivity : AppCompatActivity() {
    companion object {
        private const val ID_HOME = 0
        private const val ID_FAV = 1

        private val TAG: String? = MainActivity::class.java.simpleName
    }

    private var movieFragment: MovieFragment? = null
    private var favMoviesFragment: FavMoviesFragment? = null
    private var bottomNavigation: ReadableBottomBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        movieFragment = MovieFragment()
        favMoviesFragment = FavMoviesFragment()

        bottomNavigation = findViewById(R.id.bottomNavigation)
        loadNowPlaying()
        bottomNavigation?.setOnItemSelectListener(object : ReadableBottomBar.ItemSelectListener {
            override fun onItemSelected(index: Int) {
                when (index) {
                    ID_HOME -> {
                        loadNowPlaying()
                    }
                    ID_FAV -> {
                        if (favMoviesFragment == null)
                            favMoviesFragment = FavMoviesFragment()
                        FavMoviesFragment.TAG?.let { it1 -> loadFragment(favMoviesFragment!!, it1) }
                    }
                }
            }
        })
        bottomNavigation?.selectItem(ID_HOME)

    }

    private fun loadNowPlaying() {
        if (movieFragment == null)
            movieFragment = MovieFragment()
        MovieFragment.TAG?.let { it1 -> loadFragment(movieFragment!!, it1) }
    }

    private fun loadFragment(fragment: Fragment, fragmentTAG: String) {
        invalidateOptionsMenu()
        supportFragmentManager.beginTransaction()
            .replace(R.id.homeScreenContainer, fragment, fragmentTAG)
            .addToBackStack(fragmentTAG)
            .commitAllowingStateLoss()
    }
}