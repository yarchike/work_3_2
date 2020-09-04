package com.yarchike.work_3_1

import android.app.AlertDialog
import android.content.Context
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.create_repost.*
import java.util.regex.Pattern

fun isValidPassword(password: String) =
    Pattern.compile("(?=.*[A-Z])(?!.*[^a-zA-Z0-9])(.{6,15})\$").matcher(password).matches()
fun isValidUsername(username: String) =
    Pattern.compile("(.{1,10})\$").matcher(username).matches()
fun isFirstTime(context: Context) =
    context.getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).getLong(
        LAST_TIME_VISIT_SHARED_KEY, 0
    ) == 0L

fun setNotFirstTime(context: Context) =
    context.getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE)
        .edit()
        .putBoolean(API_SHARED_FILE, false)
