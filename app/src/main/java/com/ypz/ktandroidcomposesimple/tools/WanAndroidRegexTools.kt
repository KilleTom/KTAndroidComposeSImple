package com.ypz.ktandroidcomposesimple.tools

import java.text.SimpleDateFormat

class WanAndroidRegexTools private constructor() {

    fun symbolClear(text: String?): String {
//    val pattern = Pattern.compile()
        if (text.isNullOrEmpty()) {
            return ""
        }
        val regex = Regex("<[a-z]+>|</[a-z]+>|<[a-z]+/>")
        if (text.contains(regex)) {
            return text.replace(regex, "")
        }
        return text
    }

    fun timestamp(time: String?): String? {
        time ?: return null
        return kotlin.runCatching {
            SimpleDateFormat("yyyy-MM-dd HH:mm").parse(time)
            time.substring(0, time.indexOf(" "))
        }.getOrDefault(time)
    }

    companion object {
        val instance by lazy { WanAndroidRegexTools() }
    }
}