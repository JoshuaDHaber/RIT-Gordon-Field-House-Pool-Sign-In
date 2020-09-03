package com.example.poolsignin

/* Copyright (C) JDHaber Development, Inc - All Rights Reserved
 * Unauthorized usage and/or copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Joshua D. Haber <me@jdhaber.com>, October 2019
 */


import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.multidex.MultiDexApplication
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception


class MainActivity : AppCompatActivity(){

    //for the web test
    private var mTracker: Tracker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //testing google analytics
        // Obtain the shared Tracker instance.
        val application = application as AnalyticsApplication
        mTracker = application.defaultTracker

        mTracker?.setScreenName("SLC")
        mTracker?.send(HitBuilders.ScreenViewBuilder().build())
    }

    fun logItem(view: View) {
        var text = ""
        //determine the user type
        when(view.id) {
            R.id.faculty_staff_button -> text = "faculty_staff"
            R.id.sponsered_button -> text = "sponsored_member"
            R.id.stu_button -> text = "student"
            R.id.alumni_button -> text = "alumni"
            R.id.guest_button -> text = "guest"
        }
        //testing web
        //add a try to make sure it works
        try {
            mTracker?.send(
                HitBuilders.EventBuilder()
                    .setCategory("Log").setAction(text).setValue(1).build())
            makePopup()
        }catch (e: Exception) {
            failToast(e.message)
        }
    }
    //make a toast if something went wrong
    private fun failToast( msg: String?) {
        val to = Toast.makeText(this, msg, Toast.LENGTH_LONG)
        to.show()
    }

    private fun makePopup() {
        //some fun pop up magic
        //creds: https://android--code.blogspot.com/2018/02/android-kotlin-popup-window-example.html
        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // Inflate a custom view using layout inflater
        val view = inflater.inflate(R.layout.popup,null)

        // Initialize a new instance of popup window
        val popupWindow = PopupWindow(
            view, // Custom view to show in popup window
            LinearLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
            LinearLayout.LayoutParams.WRAP_CONTENT // Window height
        )

        // Set an elevation for the popup window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.elevation = 10.0F
        }


        // If API level 23 or higher then execute the code
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Create a new slide animation for popup window enter transition
            val slideIn = Slide()
            slideIn.slideEdge = Gravity.TOP
            popupWindow.enterTransition = slideIn

            // Slide animation for popup window exit transition
            val slideOut = Slide()
            slideOut.slideEdge = Gravity.RIGHT
            popupWindow.exitTransition = slideOut

        }

        Handler().postDelayed({popupWindow.dismiss() },5000)

        // Finally, show the popup window on app
        TransitionManager.beginDelayedTransition(root_layout)
        popupWindow.showAtLocation(
            root_layout, // Location to display popup window
            Gravity.CENTER, // Exact position of layout to display popup
            0, // X offset
            0 // Y offset
        )
    }
}
