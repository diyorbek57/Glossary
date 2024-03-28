package uz.maxtac.glossary.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.maxtac.glossary.R
import uz.maxtac.glossary.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
    }



    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_home -> showBottomNav()
                R.id.nav_saves -> showBottomNav()
                else -> hideBottomNav()
            }
        }
    }

    private fun showBottomNav() {
        binding.bottomNavView.visibility = View.VISIBLE
        binding.bottomNavView.clearAnimation();
        binding.bottomNavView.animate().translationY(0F).duration = 300;

    }

    private fun hideBottomNav() {
        binding.bottomNavView.visibility = View.GONE
//        binding.bottomNavView.clearAnimation();
//        binding.bottomNavView.animate().translationY(binding.bottomNavView.height.toFloat()).duration =
//            300;

    }
}