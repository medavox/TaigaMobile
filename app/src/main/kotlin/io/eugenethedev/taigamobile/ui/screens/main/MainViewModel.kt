package io.eugenethedev.taigamobile.ui.screens.main

import androidx.lifecycle.*
import dagger.hilt.android.AndroidEntryPoint
import io.eugenethedev.taigamobile.state.Session
import io.eugenethedev.taigamobile.state.Settings
import io.eugenethedev.taigamobile.TaigaApp
import io.eugenethedev.taigamobile.dagger.AppComponent
import javax.inject.Inject

@AndroidEntryPoint
class MainViewModel : ViewModel() {
    @Inject lateinit var session: Session
    @Inject lateinit var settings: Settings

    val isLogged by lazy { session.isLogged }
    val isProjectSelected by lazy { session.isProjectSelected }

    val theme by lazy { settings.themeSetting }

}
