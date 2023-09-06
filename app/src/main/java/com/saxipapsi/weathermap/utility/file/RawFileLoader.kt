package com.saxipapsi.weathermap.utility.file

import android.content.Context
import androidx.annotation.RawRes
import java.io.InputStream
import java.util.*

class RawFileLoader(private val context: Context) {

    fun getRawString(@RawRes resId: Int): String {
        return readStream(context.resources.openRawResource(resId))
    }

    private fun readStream(inputStream: InputStream): String {
        return Scanner(inputStream).useDelimiter("\\A").let {
            if (it.hasNext()) {
                it.next()
            } else {
                ""
            }
        }
    }
}