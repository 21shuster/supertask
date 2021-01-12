package es.eoi.supertaskag.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import es.eoi.supertaskag.R
import es.eoi.supertaskag.home.views.TasksFragment
import es.eoi.supertaskag.home.views.TomatoFragment
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Paper.init(this)
        initBottomNavBar()
    }

    fun initBottomNavBar() {
        bottom_navigation_view.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_taks -> {
                    goToFragment(TasksFragment())
                    true
                }
                R.id.nav_tomato -> {
                    goToFragment(TomatoFragment())
                    true
                }
                else -> false
            }
        }
        bottom_navigation_view.selectedItemId = R.id.nav_taks
    }

    fun goToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit()
    }
}