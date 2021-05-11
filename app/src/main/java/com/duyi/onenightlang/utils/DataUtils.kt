package com.duyi.onenightlang.utils

import java.util.*

/**
 * Created by duyi on 2021-05-10
 */

class DataUtils {
    companion object {
        fun <V> isEmpty(sourceList: ArrayList<V>?): Boolean {
            return sourceList == null || sourceList.size == 0
        }

        /**
         * 打乱ArrayList
         */
        fun <V> randomList(sourceList: ArrayList<V>): ArrayList<V> {
            if (isEmpty<V>(sourceList)) {
                return sourceList
            }

            val randomList = ArrayList<V>(sourceList.size)
            do {
                val randomIndex = Math.abs(Random().nextInt(sourceList.size))
                randomList.add(sourceList.removeAt(randomIndex))
            } while (sourceList.size > 0)

            return randomList
        }

        fun getPlayerNum(arrayNum: Int, startNum: Int, dmNum: Int = -1): Int {
            return if (arrayNum + startNum < dmNum) {
                arrayNum + startNum
            } else {
                arrayNum + startNum + 1
            }
        }

    }
}