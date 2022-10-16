package com.ypz.ktandroidcomposesimple.tools

import android.content.res.Resources

object SizeTools {
    fun dp2px(dpValue: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}