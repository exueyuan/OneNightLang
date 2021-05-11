package com.duyi.onenightlang

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.duyi.onenightlang.adapter.CardAdapter
import com.duyi.onenightlang.data.CardData
import com.duyi.onenightlang.data.CardType
import kotlinx.android.synthetic.main.activity_checkcard.*
import java.io.Serializable
import java.lang.Exception

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
        CardData("猎人", type = CardType.Lieren, isShowNum = false),
        CardData("皮匠", type = CardType.Pijiang, isShowNum = false),
        CardData("强盗", type = CardType.Qiangdao, isShowNum = false),
        CardData("村民", type = CardType.Cunmin, isShowNum = false),
        CardData("村民", type = CardType.Cunmin, isShowNum = false)
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
            if (checkCardList.size < 6) {
                Toast.makeText(this, "请至少选择6张以上的牌！", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            var start_num = 1
            var dm_num = -1
            try {
                start_num = et_start_num.text.toString().toInt()
                dm_num = et_dm_num.text.toString().toInt()
            } catch (e: Exception) {

            }

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("cardData", checkCardList as Serializable)
            intent.putExtra("startNum", start_num)
            intent.putExtra("dmNum", dm_num)
            startActivity(intent)
        }
    }

    private fun updateAdapter() {
        roleCardAdapter.notifyDataSetChanged()
        checkCardAdapter.notifyDataSetChanged()
    }
}