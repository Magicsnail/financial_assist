package com.magic.snail.assist.global

import android.app.Application

class App : Application() {

    companion object {
        var me : Application? = null
    }
    override fun onCreate() {
        super.onCreate()
        me = this
    }
}