package com.boryans.covidstats.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.Navigation
import com.boryans.covidstats.R
import java.util.*
import kotlin.concurrent.timerTask


class SplashScreen : Fragment(R.layout.fragment_splash_screen) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timer().schedule(timerTask{
                                  Navigation.findNavController(requireView()).navigate(SplashScreenDirections.actionSplashScreenToHomeFragment2())

        }, 1200)

    }
}