package com.duyi.onenightlang.holder

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.duyi.onenightlang.R
import com.duyi.onenightlang.data.CardData
import com.duyi.onenightlang.utils.DataUtils

/**
 * Created by duyi on 2021-05-10
 */

class CardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        const val layoutId = R.layout.holder_card

        fun getHolder(context: Context): RecyclerView.ViewHolder {
            return CardHolder(View.inflate(context, layoutId, null))
        }

    }


    fun onBindViewHolder(
        cardData: CardData,
        position: Int,
        isPlayerOrPublic: Boolean,
        startNum: Int, dmNum: Int,
        checkCallback: ((Int, Int, CardData) -> Unit)? = null
    ) {
        val tv_card_name = itemView.findViewById<TextView>(R.id.tv_card_name)
        val tv_player_num = itemView.findViewById<TextView>(R.id.tv_player_num)
        val v_bg = itemView.findViewById<View>(R.id.v_bg)
        tv_card_name.text = cardData.cardName
        val player_num = if (isPlayerOrPublic) {
            DataUtils.getPlayerNum(position, startNum, dmNum)
        } else {
            position + 1
        }
        tv_player_num.text = player_num.toString()

        v_bg.isSelected = cardData.isSeleted
        itemView.setOnClickListener {
            checkCallback?.invoke(position, player_num, cardData)
        }
    }
}