package com.duyi.onenightlang.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duyi.onenightlang.data.CardData
import com.duyi.onenightlang.holder.CardHolder

class CardAdapter(
    private val cardDataList: List<CardData>,
    private val isPlayerOrPublic: Boolean = true,
    private val checkCallback: ((Int, Int, CardData) -> Unit)? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var startNum = 1
    var dmNum = -1

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CardHolder.getHolder(viewGroup.context)
    }

    override fun getItemCount(): Int {
        return cardDataList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = cardDataList[position]
        if (holder is CardHolder) {
            holder.onBindViewHolder(data, position, isPlayerOrPublic, startNum, dmNum, checkCallback)
        }
    }

    fun setStartNumDmNum(startNum: Int, dmNum: Int) {
        this.startNum = startNum
        this.dmNum = dmNum
    }
}
