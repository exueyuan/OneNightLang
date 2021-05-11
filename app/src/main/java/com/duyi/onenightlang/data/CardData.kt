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
) : Serializable

enum class CardType {
    None,
    Langren,
    Zhaoya,
    Yuyanjia,
    Daodangui,
    JiuGui,
    Qiangdao,
    Shimianzhe
}