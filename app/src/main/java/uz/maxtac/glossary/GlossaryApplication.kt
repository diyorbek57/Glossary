package uz.maxtac.glossary

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GlossaryApplication:Application() {

    override fun onCreate() {
        super.onCreate()
    }
}