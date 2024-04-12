package io.eugenethedev.taigamobile.ui.components.lists

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import io.eugenethedev.taigamobile.domain.entities.CommonTask
import io.eugenethedev.taigamobile.ui.components.loaders.DotsLoader
import io.eugenethedev.taigamobile.ui.components.texts.SectionTitle
import io.eugenethedev.taigamobile.ui.utils.NavigateToTask

/**
 * List of tasks with optional title.
 */
fun LazyListScope.SimpleTasksListWithTitle(
    navigateToTask: NavigateToTask,
    commonTasks: List<CommonTask> = emptyList(),
    commonTasksLazy: LazyPagingItems<CommonTask>? = null,
    keysHash: Int = 0,
    @StringRes titleText: Int? = null,
    topPadding: Dp = 0.dp,
    horizontalPadding: Dp = 0.dp,
    bottomPadding: Dp = 0.dp,
    isTasksLoading: Boolean = false,
    showExtendedTaskInfo: Boolean = false,
    navigateToCreateCommonTask: (() -> Unit)? = null
) {
    val isLoading = commonTasksLazy
        ?.run { loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading }
        ?: isTasksLoading

    val lastIndex = commonTasksLazy?.itemCount?.minus(1) ?: commonTasks.lastIndex

    val itemContent: @Composable LazyItemScope.(Int, CommonTask?) -> Unit = lambda@ { index, item ->
        if (item == null) return@lambda

        CommonTaskItem(
            commonTask = item,
            horizontalPadding = horizontalPadding,
            navigateToTask = navigateToTask,
            showExtendedInfo = showExtendedTaskInfo
        )

        if (index < lastIndex) {
            Divider(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = horizontalPadding),
                color = MaterialTheme.colorScheme.outline
            )
        }
    }

    item {
        Spacer(Modifier.height(topPadding))
    }

    titleText?.let {
        item {
            SectionTitle(
                text = stringResource(it),
                horizontalPadding = horizontalPadding,
                onAddClick = navigateToCreateCommonTask
            )
        }
    }

    commonTasksLazy?.let {
/*            itemsIndexed(
                items = it,
                key = { _, item:LazyPagingItems<CommonTask> -> item.id + keysHash },
                itemContent = itemContent
            )*/
    } ?: itemsIndexed(commonTasks, itemContent = itemContent)

    item {
        if (isLoading) {
            DotsLoader()
        }
        Spacer(Modifier.height(bottomPadding))
    }
}
