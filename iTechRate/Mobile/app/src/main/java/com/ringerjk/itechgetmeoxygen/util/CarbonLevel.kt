package com.ringerjk.itechgetmeoxygen.util

import android.graphics.Color
import android.support.annotation.ColorInt

/**
 * Created by Yury Kanetski on 6/18/17.
 */
class CarbonLevel {
    companion object{
        @ColorInt
        fun levelColor(lvl: Int?): Int{
            when(lvl){
                1 -> return Color.GREEN
                2 -> return Color.GREEN
                3 -> return Color.TRANSPARENT
                4 -> return Color.YELLOW
                5 -> return Color.RED
                else -> return Color.TRANSPARENT
            }
        }
    }
}