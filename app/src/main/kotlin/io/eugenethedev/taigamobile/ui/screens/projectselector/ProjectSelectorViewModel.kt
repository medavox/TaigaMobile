package io.eugenethedev.taigamobile.ui.screens.projectselector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import io.eugenethedev.taigamobile.Session
import io.eugenethedev.taigamobile.TaigaApp
import io.eugenethedev.taigamobile.domain.entities.Project
import io.eugenethedev.taigamobile.domain.paging.CommonPagingSource
import io.eugenethedev.taigamobile.domain.repositories.ISearchRepository
import io.eugenethedev.taigamobile.ui.commons.ScreensState
import io.eugenethedev.taigamobile.ui.utils.asLazyPagingItems
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class ProjectSelectorViewModel : ViewModel() {

    @Inject lateinit var searchRepository: ISearchRepository
    @Inject lateinit var session: Session
    @Inject lateinit var screensState: ScreensState

    val currentProjectId get() = session.currentProjectId

    init {
        TaigaApp.appComponent.inject(this)
    }

    fun start() {
        projects.refresh()
    }

    private val projectsQuery = MutableStateFlow("")
    @OptIn(ExperimentalCoroutinesApi::class)
    val projects by lazy {
        projectsQuery.flatMapLatest { query ->
            Pager(PagingConfig(CommonPagingSource.PAGE_SIZE)) {
                CommonPagingSource { searchRepository.searchProjects(query, it) }
            }.flow
        }.asLazyPagingItems(viewModelScope)
    }

    fun searchProjects(query: String) {
        projectsQuery.value = query
    }

    fun selectProject(project: Project) {
        session.apply {
            currentProjectId = project.id
            currentProjectName = project.name
        }
        screensState.modify()
    }
}