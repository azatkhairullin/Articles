package com.boxes.articles.presentation.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v13.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.boxes.articles.R
import com.boxes.articles.util.openFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(hasLocationPermission()) {
            openArticlesFragment()
        } else {
            requestLocationPermissions()
        }
    }

    private fun openArticlesFragment() {
        var articlesFragment: ArticlesFragment? = supportFragmentManager.findFragmentById(R.id.fragmentsContainer) as ArticlesFragment?
        if (articlesFragment == null) {
            articlesFragment = ArticlesFragment()
            openFragment(articlesFragment, R.id.fragmentsContainer)
        }
    }

    private fun hasLocationPermission() : Boolean{
        val haveCoarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val haveFine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        return haveCoarse || haveFine

    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                1)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(hasLocationPermission()) {
            openArticlesFragment()
        } else {
            finish()
        }
    }
}
