package com.example.test6.constants

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {

     val userToken = stringPreferencesKey("token")
     val isRemembered = booleanPreferencesKey("remember")

}