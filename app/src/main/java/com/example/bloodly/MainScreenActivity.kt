package com.example.bloodly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.bloodly.AddRequestActivity
import com.example.bloodly.AllRequestsActivity
import com.example.bloodly.HomeScreenActivity
import com.example.bloodly.ProfileViewActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainScreenActivity : AppCompatActivity() {

    lateinit var toolbar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        //init
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation)
        val myToolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(myToolbar)
        toolbar = supportActionBar!!
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        toolbar.title = "Home Page"
        val homeFragment = HomeScreenActivity.newInstance()
        openFragment(homeFragment)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {

                toolbar.title = "Home Page"
                val homeFragment = HomeScreenActivity.newInstance()
                openFragment(homeFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_request -> {
                toolbar.title = "All Request"
                val rquestFragment = AllRequestsActivity.newInstance()
                openFragment(rquestFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_add_request -> {
                toolbar.title = "Add Request"
                val addrquestFragment = AddRequestActivity.newInstance()
                openFragment(addrquestFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_profile -> {
                toolbar.title = "Profile"
                val profileFragment = ProfileViewActivity.newInstance()
                openFragment(profileFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content1, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}