package com.snakes.kinoshka.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.snakes.kinoshka.R
import com.snakes.kinoshka.adapters.PagerAdapter
import com.snakes.kinoshka.ui.fragments.FirstFragment
import com.snakes.kinoshka.ui.fragments.SecondFragment
import com.snakes.kinoshka.ui.fragments.ThirdFragment
import com.snakes.kinoshka.util.Constants.BUNDLE_RESULT_KEY
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.movie_shimmer_placeholder.*

class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        with(fragments){
            add(FirstFragment())
            add(SecondFragment())
            add(ThirdFragment())
        }

        val titles = ArrayList<String>()
        with(titles){
            add("First")
            add("Second")
            add("Third")
        }

        val resultBundle = Bundle()
        resultBundle.putParcelable(BUNDLE_RESULT_KEY,args.result)

        val adapter = PagerAdapter(
            resultBundle,
            fragments,
            titles,
            supportFragmentManager
        )

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}