package com.duyi.onenightlang.data

import java.io.Serializable

/**
 * Created by duyi on 2021-05-10
 */

class CardData(
    val cardName: String = "",
    var isSeleted: Boolean = false,
    var isShowNum: Boolean = true,
    var type: CardType = CardType.None
) : Serializable {

}

enum class CardType(val order: Int, val isQiye:Boolean) {
    None(10000, false),
    Langren(1, true),
    Zhaoya(2, true),
    Yuyanjia(3, true),
    Qiangdao(4, true),
    Daodangui(5, true),
    JiuGui(6, true),
    Shimianzhe(7, true),
    Lieren(8, false),
    Pijiang(9, false),
    Cunmin(10, false)
}