package com.yarchike.work_3_1

import android.app.AlertDialog
import android.content.Context
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.create_post.*
import java.util.regex.Pattern

fun isValidPassword(password: String) =
    Pattern.compile("(?=.*[A-Z])(?!.*[^a-zA-Z0-9])(.{6,15})\$").matcher(password).matches()
fun isValidUsername(username: String) =
    Pattern.compile("(.{1,10})\$").matcher(username).matches()
