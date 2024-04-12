package io.eugenethedev.taigamobile.ui.screens.wiki.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.eugenethedev.taigamobile.R
import io.eugenethedev.taigamobile.TaigaApp
import io.eugenethedev.taigamobile.dagger.AppComponent
import io.eugenethedev.taigamobile.domain.entities.Attachment
import io.eugenethedev.taigamobile.domain.entities.User
import io.eugenethedev.taigamobile.domain.entities.WikiLink
import io.eugenethedev.taigamobile.domain.entities.WikiPage
import io.eugenethedev.taigamobile.domain.repositories.IUsersRepository
import io.eugenethedev.taigamobile.domain.repositories.IWikiRepository
import io.eugenethedev.taigamobile.ui.utils.MutableResultFlow
import io.eugenethedev.taigamobile.ui.utils.loadOrError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject

class WikiPageViewModel : ViewModel() {

    @Inject
    lateinit var wikiRepository: IWikiRepository

    @Inject
    lateinit var userRepository: IUsersRepository

    private lateinit var pageSlug: String

    val page = MutableResultFlow<WikiPage>()
    val link = MutableResultFlow<WikiLink>()
    val attachments = MutableResultFlow<List<Attachment>>()
    val editWikiPageResult = MutableResultFlow<Unit>()
    val deleteWikiPageResult = MutableResultFlow<Unit>()

    var lastModifierUser = MutableStateFlow<User?>(null)

    fun onOpen(slug: String) {
        pageSlug = slug
        loadData()
    }

    private fun loadData() = viewModelScope.launch {
        page.loadOrError {
            wikiRepository.getProjectWikiPageBySlug(pageSlug).also {

                lastModifierUser.value = userRepository.getUser(it.lastModifier)

                val jobsToLoad = arrayOf(
                    launch {
                        link.loadOrError(showLoading = false) {
                            wikiRepository.getWikiLinks().find { it.ref == pageSlug }
                        }
                    },
                    launch {
                        attachments.loadOrError(showLoading = false) {
                            wikiRepository.getPageAttachments(it.id)
                        }
                    }
                )

                joinAll(*jobsToLoad)
            }
        }
    }

    fun deleteWikiPage() = viewModelScope.launch {
        deleteWikiPageResult.loadOrError {
            val linkId = link.value.data?.id
            val pageId = page.value.data?.id

            pageId?.let { wikiRepository.deleteWikiPage(it) }
            linkId?.let { wikiRepository.deleteWikiLink(it) }
        }
    }

    fun editWikiPage(content: String) = viewModelScope.launch {
        editWikiPageResult.loadOrError {
            page.value.data?.let {
                wikiRepository.editWikiPage(
                    pageId = it.id,
                    content = content,
                    version = it.version
                )

                loadData().join()
            }
        }
    }

    fun deletePageAttachment(attachment: Attachment) = viewModelScope.launch {
        attachments.loadOrError(R.string.permission_error) {
            wikiRepository.deletePageAttachment(
                attachmentId = attachment.id
            )

            loadData().join()
            attachments.value.data
        }
    }

    fun addPageAttachment(fileName: String, inputStream: InputStream) = viewModelScope.launch {
        attachments.loadOrError(R.string.permission_error) {
            page.value.data?.id?.let { pageId ->
                wikiRepository.addPageAttachment(
                    pageId = pageId,
                    fileName = fileName,
                    inputStream = inputStream
                )
                loadData().join()
            }
            attachments.value.data
        }
    }
}