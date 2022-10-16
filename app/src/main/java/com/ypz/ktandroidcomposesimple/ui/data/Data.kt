package com.ypz.ktandroidcomposesimple.ui.data

import androidx.paging.PagingData
import com.ypz.ktandroidcomposesimple.net.wanAndroid.bean.Article
import kotlinx.coroutines.flow.Flow

typealias PagingArticle = Flow<PagingData<Article>>