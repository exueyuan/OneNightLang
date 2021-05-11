package com.duyi.onenightlang

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.duyi.onenightlang.adapter.CardAdapter
import com.duyi.onenightlang.data.CardData
import com.duyi.onenightlang.data.CardType
import kotlinx.android.synthetic.main.activity_checkcard.*

/**
 * Created by duyi on 2021-05-11
 */

class CheckCardActivity : AppCompatActivity() {
    private val roleCardList = arrayListOf<CardData>(
        CardData("狼人", type = CardType.Langren, isShowNum = false),
        CardData("狼人", type = CardType.Langren, isShowNum = false),
        CardData("爪牙", type = CardType.Zhaoya, isShowNum = false),
        CardData("预言家", type = CardType.Yuyanjia, isShowNum = false),
        CardData("酒鬼", type = CardType.JiuGui, isShowNum = false),
        CardData("捣蛋鬼", type = CardType.Daodangui, isShowNum = false),
        CardData("猎人", type = CardType.None, isShowNum = false),
        CardData("皮匠", type = CardType.None, isShowNum = false),
        CardData("强盗", type = CardType.Qiangdao, isShowNum = false),
        CardData("村民", type = CardType.None, isShowNum = false),
        CardData("村民", type = CardType.None, isShowNum = false)
    )
    private val roleCardAdapter = CardAdapter(roleCardList, checkCallback = { _, _, cardData ->
        if (cardData.isSeleted) {
            cardData.isSeleted = false
            for (card in checkCardList) {
                if (card == cardData) {
                    checkCardList.remove(card)
                    break
                }
            }
        } else {
            cardData.isSeleted = true
            checkCardList.add(cardData)
        }
        updateAdapter()
    })

    private val checkCardList = arrayListOf<CardData>()
    private val checkCardAdapter = CardAdapter(checkCardList)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkcard)
        // 设置role
        val rolelayoutManager = GridLayoutManager(this, 4)
        rv_role_list.layoutManager = rolelayoutManager
        rv_role_list.adapter = roleCardAdapter
        // 设置check card
        val checkCardlayoutManager = GridLayoutManager(this, 4)
        rv_check_role_list.layoutManager = checkCardlayoutManager
        rv_check_role_list.adapter = checkCardAdapter

        updateAdapter()

        bt_ok.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateAdapter() {
        roleCardAdapter.notifyDataSetChanged()
        checkCardAdapter.notifyDataSetChanged()
    }
}