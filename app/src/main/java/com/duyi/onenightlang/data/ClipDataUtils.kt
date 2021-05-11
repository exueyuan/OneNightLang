package com.duyi.onenightlang.data

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast

/**
 * Created by duyi on 2021-05-11
 */

class ClipDataUtils {
    companion object {
        fun clipData(context: Context, content: String) {
            //获取剪贴板管理器：
            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            // 创建普通字符型ClipData
            val mClipData = ClipData.newPlainText("Label", content)
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData)
            Toast.makeText(context, "复制成功", Toast.LENGTH_LONG).show()
        }
    }
}