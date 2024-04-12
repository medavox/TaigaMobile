package io.eugenethedev.taigamobile

import android.app.Application
import android.util.Log
import com.google.android.material.color.DynamicColors
import io.eugenethedev.taigamobile.BuildConfig
import io.eugenethedev.taigamobile.dagger.AppComponent
import io.eugenethedev.taigamobile.utils.FileLoggingTree
import timber.log.Timber

class TaigaApp : Application() {

    // logging
    private var fileLoggingTree: FileLoggingTree? = null
    val currentLogFile get() = fileLoggingTree?.currentFile


    override fun onCreate() {
        super.onCreate()

        // logging configs
        val minLoggingPriority = if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Log.DEBUG
        } else {
            Log.WARN
        }

        try {
            fileLoggingTree = FileLoggingTree(applicationContext.getExternalFilesDir("logs")!!.absolutePath, minLoggingPriority)
            Timber.plant(fileLoggingTree!!)
        } catch (e: NullPointerException) {
            Timber.w("Cannot setup FileLoggingTree, skipping")
        }

        // Apply dynamic color
        DynamicColors.applyToActivitiesIfAvailable(this)
    }

}