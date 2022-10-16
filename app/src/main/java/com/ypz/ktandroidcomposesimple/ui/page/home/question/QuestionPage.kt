package com.ypz.ktandroidcomposesimple.ui.page.home.question

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.ypz.ktandroidcomposesimple.net.wanAndroid.bean.Article
import com.ypz.ktandroidcomposesimple.tools.RouteTools
import com.ypz.ktandroidcomposesimple.tools.WanAndroidRegexTools
import com.ypz.ktandroidcomposesimple.ui.router.AppRouter
import com.ypz.ktandroidcomposesimple.ui.weidget.MainTitle
import com.ypz.ktandroidcomposesimple.ui.weidget.MiniTitle
import com.ypz.ktandroidcomposesimple.ui.weidget.RefreshWidget
import com.ypz.ktandroidcomposesimple.ui.weidget.TextContent
import com.ypz.ktandroidcomposesimple.view.module.question.QuestionViewModule

@Composable
fun QuestionPage(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: QuestionViewModule = hiltViewModel()
) {
    val viewStates = viewModel.viewStates
    val questionData = viewStates.pagingData.collectAsLazyPagingItems()
    val listState = if (questionData.itemCount > 0) viewStates.listState else LazyListState()



    RefreshWidget(
        lazyPagingItems = questionData,
        listState = listState,
    ) {
        itemsIndexed(questionData) { _, item ->
            DayQAItem(item!!) {
                item.run {

                    if (link!=null){
                        RouteTools.navTo(navCtrl, AppRouter.WEB_VIEW, link)
                    }

                }
            }
        }
    }


}

@Composable
private fun DayQAItem(article: Article, isLoading: Boolean = false, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(enabled = !isLoading) {
                onClick.invoke()
            },
        backgroundColor = Color.White,
        shape = RoundedCornerShape(16.dp),
        elevation = 5.dp
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {

            //标题
            MainTitle(
                title = titleSubstring(article.title) ?: "每日一问",
                color = Color.Black,
                maxLine = 1,
                isLoading = isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            TextContent(
                text = WanAndroidRegexTools.instance.symbolClear(article.desc) ?: "",
                maxLines = 2,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 48.dp),
                isLoading = isLoading
            )

            Row(
                modifier = Modifier
                    .align(alignment = Alignment.End)
                    .padding(vertical = 5.dp, horizontal = 5.dp)
            ) {

                //UserIcon(isLoading = isLoading)
                MiniTitle(
                    text = "作者:${article.author ?: "unknown"}",
                    color = Color.Black,
                    modifier = Modifier
                        .padding(start = if (isLoading) 5.dp else 0.dp)
                        .align(Alignment.CenterVertically),
                    isLoading = isLoading
                )
                //发布时间
                //TimerIcon(isLoading = isLoading)
                MiniTitle(
                    modifier = Modifier
                        .padding(start = if (isLoading) 5.dp else 0.dp, top = 5.dp)
                        .align(Alignment.CenterVertically),
                    text = "日期:${WanAndroidRegexTools.instance.timestamp(article.niceDate)}",
                    color = Color.Black,
                    maxLines = 1,
                    isLoading = isLoading
                )
            }
        }
    }
}

private fun titleSubstring(oldText: String?): String? {
    return oldText?.run {
        var newText = this

        val rep = "\\<.*?\\>|\\(.*?\\)|\\（.*?\\）|\\[.*?\\]|\\【.*?\\】|\\{.*?\\}"
        val regex = Regex(rep)

        if (contains("每日一问 | ")) {
            return@run newText.replaceFirst("每日一问 | ", "").trim()
        }

        if (contains("每日一问|")) {
            return@run newText.replaceFirst("每日一问|", "").trim()
        }

        if (contains("每日一问")) {
            return@run newText.replaceFirst("每日一问", "").trim().replaceFirst("|", "").trim()
        }

        if (contains(regex)) {
            return@run newText.replaceFirst(Regex(rep), "")
        }

        newText
    }
}