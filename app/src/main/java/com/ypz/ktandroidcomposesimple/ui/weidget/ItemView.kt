package com.ypz.ktandroidcomposesimple.ui.weidget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.ypz.ktandroidcomposesimple.net.wanAndroid.bean.Article
import com.ypz.ktandroidcomposesimple.net.wanAndroid.bean.CollectBean
import com.ypz.ktandroidcomposesimple.net.wanAndroid.bean.WebData
import com.ypz.ktandroidcomposesimple.tools.WanAndroidRegexTools

@Composable
fun ArrowRightListItem(
    iconRes: Any,
    iconTintColor: Color = Color.White,
    arrowTintColor: Color = Color.Gray.copy(0.85f),
    title: String,
    msgCount: Int? = null,
    valueText: String = "",
    showDirverStatus: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 10.dp)
            .clickable {
                onClick.invoke()
            }
    ) {

        when (iconRes) {
            is Painter -> {
                Icon(
                    painter = iconRes,
                    contentDescription = null,
                    tint = iconTintColor,
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterVertically)
                        .padding(end = 10.dp)
                )
            }
            is ImageVector -> {
                Icon(
                    imageVector = iconRes,
                    contentDescription = null,
                    tint = iconTintColor,
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterVertically)
                        .padding(end = 10.dp)
                )
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        ) {
            TextContent(
                text = title,
                modifier = Modifier.align(Alignment.CenterVertically),
                color = Color.White,
            )
            if (msgCount != null) {
                Text(
                    text = "（$msgCount）",
                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
        if (valueText.isNotEmpty()) {
            TextContent(
                text = valueText,
                color = Color.White,
                modifier = Modifier
                    .padding(end = 5.dp)
                    .align(Alignment.CenterVertically)
            )
        }
        Icon(
            Icons.Default.KeyboardArrowRight,
            null,
            tint = arrowTintColor,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
    if (showDirverStatus) {
        Divider(
            Modifier
                .padding(start = 20.dp)
                .background(iconTintColor)
        )
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MultiStateItemView(
    modifier: Modifier = Modifier,
    data: Article,
    isTop: Boolean = false,
    onSelected: (data: WebData) -> Unit = {},
    onCollectClick: (articleId: Int) -> Unit = {},
    onUserClick: (userId: Int) -> Unit = {},
    isLoading: Boolean = false,
) {
    Card(
        modifier = modifier
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .fillMaxWidth()
            .clickable(enabled = !isLoading) {
                onSelected.invoke(WebData(data.title!!, data.link!!))
            },
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.White,
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
        ) {

            val (circleIcon, name, publishIcon, publishTime, title, chip1, chip2, tag, favourite, space) = createRefs()

            createVerticalChain(elements = arrayOf(circleIcon, space, chip1), ChainStyle.Packed)
//            createVerticalChain(elements = arrayOf(name, title), ChainStyle.Packed)

            Box(modifier = Modifier
                .size(48.dp)
                .padding(3.dp)
                .constrainAs(circleIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(space.top)
                    start.linkTo(parent.start)
                }){
                UserIcon(
                    modifier = Modifier
                        .fillMaxSize()
                    , accountUrl = "", roundedCornerRadius = 48.dp
                )
            }


            Spacer(modifier = Modifier
                .size(10.dp)
                .constrainAs(space) {
                    top.linkTo(circleIcon.bottom)
                    bottom.linkTo(chip1.top)
                })

            val titleModifier =
                if (isLoading) Modifier.width(80.dp) else Modifier.wrapContentWidth()

            MediumTitle(
                color = Color.Black,
                title = getAuthorName(data),
                modifier = titleModifier
                    .constrainAs(name) {
                        top.linkTo(circleIcon.top)
                        start.linkTo(circleIcon.end)
                        bottom.linkTo(title.top)
                    }
                    .padding(start = 5.dp)
                    .clickable {
                        onUserClick.invoke(data.userId)
                    }
                    .pointerInteropFilter { false },
                isLoading = isLoading
            )
            val dateModifier =
                if (isLoading) Modifier.width(80.dp) else Modifier.wrapContentWidth()
            MiniTitle(
                color = Color.Black,
                text = WanAndroidRegexTools.instance.timestamp(data.niceDate!!) ?: "1993-11-25",
                modifier = dateModifier
                    .constrainAs(publishTime) {
                        top.linkTo(name.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(name.bottom)
                    },
                isLoading = isLoading
            )
            TimerIcon(
                modifier = Modifier
                    .padding(end = if (isLoading) 5.dp else 3.dp)
                    .constrainAs(publishIcon) {
                        top.linkTo(publishTime.top)
                        end.linkTo(publishTime.start)
                        bottom.linkTo(publishTime.bottom)
                    },
                isLoading = isLoading
            )
            TextContent(
                color = Color.Black,
                text = data.title ?: "unknown Title",
                maxLines = 1,
                modifier = Modifier
                    .padding(start = 5.dp, top = 5.dp)
                    .wrapContentHeight()
                    .constrainAs(title) {
                        top.linkTo(name.bottom)
                        bottom.linkTo(circleIcon.bottom)
                        start.linkTo(circleIcon.end)
                        end.linkTo(publishTime.end)
                        width = Dimension.fillToConstraints
                        verticalChainWeight = 0.5f
                    },
                isLoading = isLoading,
            )
            LabelTextButton(
                text = data.superChapterName ?: "unknown",
                modifier = Modifier
                    .constrainAs(chip1) {
                        top.linkTo(space.bottom)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
                ,
                isLoading = isLoading,
                replaceBackgroundStatus = true
            )

            Box(modifier = Modifier
                .constrainAs(chip2) {
                    top.linkTo(chip1.top)
                    start.linkTo(chip1.end, margin = 5.dp)
                    end.linkTo(favourite.start, margin = 5.dp)
                    width = Dimension.fillToConstraints
                }){
                LabelTextButton(
                    text = data.chapterName ?: "unknown",
                    isLoading = isLoading,
                )
            }

            FavouriteIcon(
                modifier = Modifier.constrainAs(favourite) {
                    top.linkTo(chip1.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(chip1.bottom)
                },
                isFavourite = data.collect,
                onClick = {
                    onCollectClick.invoke(data.id)
                },
                isLoading = isLoading
            )
            Row(
                modifier = Modifier
                    .defaultMinSize(minHeight = 20.dp)
                    .constrainAs(tag) {
                        top.linkTo(parent.top)
                        start.linkTo(name.end, margin = 5.dp)
                    }
            ) {
                if (isTop) {
                    HotIcon()
                }
                if (data.fresh) {
                    TitleTipView(
                        tagText = "最新",
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .align(Alignment.Bottom)
                    )
                }
            }

        }
    }
}

fun getAuthorName(data: Article?): String {
    return if (data?.shareUser.isNullOrEmpty()) {
        data?.author ?: ""
    } else {
        data?.shareUser ?: ""
    }
}

fun getFirstCharFromName(data: Article?): String {
    val author = getAuthorName(data)
    return if (author.isNotEmpty()) author.trim().substring(0, 1) else "?"
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CollectItemView(
    collect: CollectBean,
    isLoading: Boolean = false,
    onClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .fillMaxWidth()
            .wrapContentWidth()
            .clickable(enabled = !isLoading) { onClick.invoke() },
        shape = RoundedCornerShape(28.dp),
        contentColor = Color.White.copy(0.88f)
    ) {
        ConstraintLayout(
            modifier = Modifier.padding(20.dp)
        ) {
            val (name, publishIcon, publishTime, title, delete) = createRefs()
            MediumTitle(
                title = collect.author.ifEmpty { "not name" },
                color = Color.Black,
                modifier = Modifier
                    .defaultMinSize(minWidth = 100.dp)
                    .constrainAs(name) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(publishIcon.start, margin = 5.dp)
                        width = Dimension.fillToConstraints
                    },
                isLoading = isLoading
            )
            MiniTitle(
                text = WanAndroidRegexTools.instance.timestamp(collect.niceDate) ?: "2022",
                color = Color.Black,
                modifier = Modifier
                    .constrainAs(publishTime) {
                        top.linkTo(name.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(name.bottom)
                    }
                    .defaultMinSize(minWidth = 40.dp),
                isLoading = isLoading
            )
            TimerIcon(
                modifier = Modifier
                    .constrainAs(publishIcon) {
                        top.linkTo(publishTime.top)
                        end.linkTo(publishTime.start, margin = 2.dp)
                        bottom.linkTo(publishTime.bottom)
                    },
                isLoading = isLoading
            )
            TextContent(
                text = collect.title,
                maxLines = 3,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 10.dp, bottom = 20.dp)
                    .constrainAs(title) {
                        top.linkTo(name.bottom)
                        end.linkTo(parent.end)
                    },
                isLoading = isLoading
            )
            if (!isLoading) {
                DeleteIcon(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .constrainAs(delete) {
                            top.linkTo(title.bottom)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                    onClick = onDeleteClick
                )
            }
        }
    }
}