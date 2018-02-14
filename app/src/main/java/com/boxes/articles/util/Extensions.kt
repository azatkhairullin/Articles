package com.boxes.articles.util

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.ViewGroup


fun FragmentActivity.openFragment(fragment: Fragment, container: Int) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.add(container, fragment)
    transaction.commit()
}

inline fun ViewGroup.forEach(action: (View) -> Unit) {
    for (index in 0 until childCount) {
        action(getChildAt(index))
    }
}