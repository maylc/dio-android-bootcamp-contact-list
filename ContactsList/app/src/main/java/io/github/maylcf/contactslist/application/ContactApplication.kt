package io.github.maylcf.contactslist.application

import android.app.Application
import io.github.maylcf.contactslist.helpers.HelperDB

class ContactApplication : Application() {

    var helperDB: HelperDB? = null
        private set

    companion object {
        lateinit var instance: ContactApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        helperDB = HelperDB(this)
    }
}