package com.verodigital.androidtask.util

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.verodigital.androidtask.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun getProgressDrawable(context: Context): CircularProgressDrawable {
        return CircularProgressDrawable(context).apply {
            strokeWidth = 10f
            centerRadius = 50f
            start()
        }
    }

    fun ImageView.loadImage(photoURL: String?, progressDrawable: CircularProgressDrawable) {
        val requestOptions = RequestOptions()
            .placeholder(progressDrawable)
            .error(R.mipmap.ic_launcher_round)
        Glide.with(this.context).setDefaultRequestOptions(requestOptions)
            .load(photoURL)
            .into(this)
    }

    fun StringtoDate(date: String?): Date {
        var date1: Date? = null
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        try {
            date1 = format.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date1!!
    }

    fun getDateDiff(date1: Date, date2: Date, timeUnit: TimeUnit): Long {
        val diffInMillies = date2.time - date1.time
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS)
    }
    fun getCurrentDate(): String? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        dateFormat.timeZone = TimeZone.getTimeZone("GMT+5:00")
        val today = Calendar.getInstance().time
        return dateFormat.format(today)
    }

