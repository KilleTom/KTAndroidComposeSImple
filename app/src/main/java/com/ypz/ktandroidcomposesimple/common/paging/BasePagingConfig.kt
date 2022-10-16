package com.ypz.ktandroidcomposesimple.common.paging

import androidx.paging.PagingConfig

data class BasePagingConfig(
    val pageSize: Int = 20,
    val initialLoadSize: Int = 20,
    val prefetchDistance:Int = 1,
    val maxSize:Int = PagingConfig.MAX_SIZE_UNBOUNDED,
    val enablePlaceholders:Boolean = false
)