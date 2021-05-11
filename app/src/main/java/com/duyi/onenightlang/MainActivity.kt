package com.duyi.onenightlang

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.duyi.onenightlang.adapter.CardAdapter
import com.duyi.onenightlang.data.CardData
import com.duyi.onenightlang.data.CardType
import com.duyi.onenightlang.utils.DataUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    var qiye_order = 0

    private var doing_type: CardType = CardType.None
    private val cardDataList = arrayListOf<CardData>()

    private val startPlayerCardList = arrayListOf<CardData>()
    private val startPublicCardList = arrayListOf<CardData>()

    // 玩家卡牌列表
    private val playerCardDataList = arrayListOf<CardData>()
    private val playerCardAdapter =
        CardAdapter(playerCardDataList, checkCallback = { position, player_num, cardData ->
            when (doing_type) {
                CardType.None -> return@CardAdapter
                CardType.Langren -> return@CardAdapter
                CardType.Yuyanjia -> {
                    if (startPlayerCardList[position].cardName == "预言家") {
                        return@CardAdapter
                    }
                    clearAllCardData()
                    checkPlayerData1 = cardData
                    checkPlayerData1PlayerNum = player_num

                    cardData.isSeleted = true
                    updateAdapter()
                }
                CardType.Daodangui -> {
                    if (startPlayerCardList[position].cardName == "捣蛋鬼") {
                        return@CardAdapter
                    }
                    if (checkPlayerData1 != null && checkPlayerData2 != null) {
                        checkPlayerData1?.isSeleted = false
                        checkPlayerData1 = checkPlayerData2
                        checkPlayerData1PlayerNum = checkPlayerData2PlayerNum
                        checkPlayerData2 = cardData
                        checkPlayerData2PlayerNum = player_num
                    } else if (checkPlayerData1 != null && checkPlayerData2 == null) {
                        checkPlayerData2 = cardData
                        checkPlayerData2PlayerNum = player_num
                    } else if (checkPlayerData1 == null) {
                        checkPlayerData1 = cardData
                        checkPlayerData1PlayerNum = player_num
                    }
                    cardData.isSeleted = true
                    updateAdapter()
                }
                CardType.Qiangdao -> {
                    if (startPlayerCardList[position].cardName == "强盗") {
                        return@CardAdapter
                    }
                    clearPlayerCardData()
                    checkPlayerData1 = cardData
                    checkPlayerData1PlayerNum = player_num
                    cardData.isSeleted = true
                    updateAdapter()
                }
                else -> return@CardAdapter
            }

        })
    // 公共牌卡牌列表
    private val publicCardDataList = arrayListOf<CardData>()
    private val publicCardAdapter =
        CardAdapter(
            publicCardDataList,
            isPlayerOrPublic = false,
            checkCallback = { position, public_num, cardData ->
                when (doing_type) {
                    CardType.None -> return@CardAdapter
                    CardType.Langren -> {
                        clearPublicCardData()
                        cardData.isSeleted = true
                        checkPublicData1 = cardData
                        checkPublicData1PublicNum = public_num
                        updateAdapter()
                    }
                    CardType.Yuyanjia -> {
                        clearPlayerCardData()
                        if (checkPublicData1 != null && checkPublicData2 != null) {
                            checkPublicData1?.isSeleted = false
                            checkPublicData1 = checkPublicData2
                            checkPublicData1PublicNum = checkPublicData2PublicNum
                            checkPublicData2 = cardData
                            checkPublicData2PublicNum = public_num
                        } else if (checkPublicData1 != null && checkPublicData2 == null) {
                            checkPublicData2 = cardData
                            checkPublicData2PublicNum = public_num
                        } else if (checkPublicData1 == null) {
                            checkPublicData1 = cardData
                            checkPublicData1PublicNum = public_num
                        }
                        cardData.isSeleted = true
                        updateAdapter()
                    }
                    CardType.JiuGui -> {
                        clearAllCardData()
                        cardData.isSeleted = true
                        checkPublicData1 = cardData
                        checkPublicData1PublicNum = public_num
                        updateAdapter()
                    }
                    CardType.Daodangui -> return@CardAdapter
                    else -> return@CardAdapter
                }

            })
    var checkPublicData1: CardData? = null
    var checkPublicData1PublicNum: Int = 0
    var checkPublicData2: CardData? = null
    var checkPublicData2PublicNum: Int = 0
    var checkPlayerData1: CardData? = null
    var checkPlayerData1PlayerNum: Int = 0
    var checkPlayerData2: CardData? = null
    var checkPlayerData2PlayerNum: Int = 0

    fun clearAllCardData() {
        clearPlayerCardData()
        clearPublicCardData()
        checkPublicData1 = null
        checkPublicData2 = null
        checkPlayerData1 = null
        checkPlayerData2 = null
    }

    fun clearPlayerCardData() {
        for (playerCardData in playerCardDataList) {
            playerCardData.isSeleted = false
        }
        updateAdapter()
    }

    fun clearPublicCardData() {
        for (publicCardData in publicCardDataList) {
            publicCardData.isSeleted = false
        }
        updateAdapter()
    }

    var start_num = 1
    var dm_num = -1
    val qiyeCardTypeList = arrayListOf<CardType>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 设置玩家卡牌列表
        val player_layoutManager = GridLayoutManager(this, 4)
        rv_player_list.layoutManager = player_layoutManager
        rv_player_list.adapter = playerCardAdapter
        // 设置公共牌列表
        val public_layoutManager = GridLayoutManager(this, 4)
        rv_center_list.layoutManager = public_layoutManager
        rv_center_list.adapter = publicCardAdapter
        start_num = intent.getIntExtra("startNum", 1)
        dm_num = intent.getIntExtra("dmNum", -1)
        val dataList: ArrayList<CardData> =
            intent.getSerializableExtra("cardData") as ArrayList<CardData>

        playerCardAdapter.setStartNumDmNum(start_num, dm_num)

        // 获取datalist
        for (data in dataList) {
            data.isShowNum = true
            data.isSeleted = false
            cardDataList.add(data)
        }

        cardDataList.sortWith(Comparator { card1, card2 ->
            if (card1.type.order - card2.type.order >= 0) {
                1
            } else {
                -1
            }
        })
        var order_show = "角色配置:"
        var qiye_order = "起夜顺序:"
        for (cardData in cardDataList) {
            order_show += cardData.cardName + ","
            if (cardData.type.isQiye) {
                if (cardData.type !in qiyeCardTypeList) {
                    qiye_order += cardData.cardName + ","
                    qiyeCardTypeList.add(cardData.type)
                }
            }
        }
        tv_role_list.text = order_show
        tv_qiye_role.text = qiye_order

//
//        cardDataList.add(CardData("狼人"))
//        cardDataList.add(CardData("狼人"))
//        cardDataList.add(CardData("爪牙"))
//        cardDataList.add(CardData("预言家"))
//        cardDataList.add(CardData("强盗"))
//        cardDataList.add(CardData("捣蛋鬼"))
//        cardDataList.add(CardData("酒鬼"))
//        cardDataList.add(CardData("失眠者"))
//        cardDataList.add(CardData("皮匠"))


        startGame(cardDataList)


        bt_next.setOnClickListener {
            clearAllCardData()
            setGone()
            this.qiye_order += 1
            if(this.qiye_order < qiyeCardTypeList.size) {
                checkQiyeType(qiyeCardTypeList[this.qiye_order])
            } else {
                checkNone()
            }
        }

        tv_restart.setOnClickListener {
            startGame(cardDataList)
        }

        tv_back.setOnClickListener {
            finish()
        }

    }


    override fun onBackPressed() {

    }

    private fun startGame(cardDataList: ArrayList<CardData>) {
        val randomDataList = DataUtils.randomList(cardDataList)
        cardDataList.addAll(randomDataList)

        playerCardDataList.clear()
        publicCardDataList.clear()
        startPlayerCardList.clear()
        startPublicCardList.clear()
        val length = randomDataList.size
        for (i in 0 until length - 3) {
            playerCardDataList.add(randomDataList[i])
        }
        for (i in length - 3 until length) {
            publicCardDataList.add(randomDataList[i])
        }
        startPlayerCardList.addAll(playerCardDataList)
        startPublicCardList.addAll(publicCardDataList)

        setGone()

        updateAdapter()

        qiye_order = 0
        if(qiye_order < qiyeCardTypeList.size) {
            checkQiyeType(qiyeCardTypeList[qiye_order])
        }
    }

    private fun checkQiyeType(cardType: CardType) {
        when(cardType) {
            CardType.Langren -> checkLangren()
            CardType.Zhaoya -> checkZhaoya()
            CardType.Yuyanjia -> checkYuyanjia()
            CardType.Qiangdao -> checkQiangdao()
            CardType.Daodangui -> checkDaoangui()
            CardType.JiuGui -> checkJiuGui()
            CardType.Shimianzhe -> checkShimianzhe()
            else -> checkNone()
        }
    }

    private fun checkShimianzhe() {
        doing_type = CardType.Shimianzhe
        tv_name.text = "失眠者"
        var shimianzheCardData: CardData? = null
        var startShaimianzhePosition = 0
        for (i in 0 until startPlayerCardList.size) {
            if (startPlayerCardList[i].cardName == "失眠者") {
                shimianzheCardData = startPlayerCardList[i]
                startShaimianzhePosition = i
            }
        }
        if (shimianzheCardData == null) {
            tv_desc.text = "场上无失眠者"
            bt_next.visibility = View.VISIBLE
            bt_ok.visibility = View.GONE
        } else {
            tv_name.text =
                "失眠者:${DataUtils.getPlayerNum(startShaimianzhePosition, start_num, dm_num)}"
            val cardName = playerCardDataList[startShaimianzhePosition].cardName
            tv_desc.text = "你最后查看自己的角色为：$cardName"
            bt_next.visibility = View.VISIBLE
            bt_ok.visibility = View.GONE
        }

    }

    private fun checkQiangdao() {
        doing_type = CardType.Qiangdao
        tv_name.text = "强盗"
        var qiangdaoCardData: CardData? = null
        var startQiangDaoPosition = 0
        for (i in 0 until startPlayerCardList.size) {
            if (startPlayerCardList[i].cardName == "强盗") {
                qiangdaoCardData = startPlayerCardList[i]
                startQiangDaoPosition = i
            }
        }

        if (qiangdaoCardData == null) {
            tv_desc.text = "场上无强盗"
            bt_next.visibility = View.VISIBLE
            bt_ok.visibility = View.GONE
        } else {
            tv_name.text = "强盗:${DataUtils.getPlayerNum(startQiangDaoPosition, start_num, dm_num)}"
            tv_desc.text = "请选择要交换的卡牌的号码"
            bt_next.visibility = View.GONE
            bt_ok.visibility = View.VISIBLE
        }

        bt_ok.setOnClickListener {
            if (checkPlayerData1 == null) {
                Toast.makeText(this, "请选择要查验的号码", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            bt_ok.isEnabled = false


            val change_position_1 = startQiangDaoPosition
            var change_position_2 = 0
            for (i in 0 until playerCardDataList.size) {
                if (playerCardDataList[i] == checkPlayerData1) {
                    change_position_2 = i
                }
            }
            val temp = playerCardDataList[change_position_1]
            playerCardDataList[change_position_1] = checkPlayerData1!!
            playerCardDataList[change_position_2] = temp

            tv_result.text = "这张牌是${checkPlayerData1?.cardName}"
            tv_result.visibility = View.VISIBLE
            bt_next.visibility = View.VISIBLE

            clearAllCardData()
        }

    }

    private fun checkNone() {
        doing_type = CardType.None
        tv_name.text = "请所有玩家睁眼进行推理"
        tv_desc.text = ""
        bt_next.visibility = View.GONE
        bt_ok.visibility = View.GONE
    }


    private fun checkJiuGui() {
        doing_type = CardType.JiuGui
        tv_name.text = "酒鬼"
        var jiuguiCardData: CardData? = null
        var startJiuGuiPosition = 0
        for (i in 0 until startPlayerCardList.size) {
            if (startPlayerCardList[i].cardName == "酒鬼") {
                jiuguiCardData = startPlayerCardList[i]
                startJiuGuiPosition = i
            }
        }
        if (jiuguiCardData == null) {
            tv_desc.text = "场上无酒鬼"
            bt_next.visibility = View.VISIBLE
            bt_ok.visibility = View.GONE
        } else {
            tv_name.text = "酒鬼:${DataUtils.getPlayerNum(startJiuGuiPosition, start_num, dm_num)}"
            tv_desc.text = "请选择要换的中间牌"
            bt_next.visibility = View.GONE
            bt_ok.visibility = View.VISIBLE
        }
        bt_ok.setOnClickListener {
            if (checkPublicData1 == null) {
                Toast.makeText(this, "请选择要交换的号码", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            bt_ok.isEnabled = false
            val change_player_position = startJiuGuiPosition
            var change_public_position = 0
            for (i in 0 until publicCardDataList.size) {
                if (publicCardDataList[i] == checkPublicData1) {
                    change_public_position = i
                }
            }
            val temp = playerCardDataList[change_player_position]
            playerCardDataList[change_player_position] = checkPublicData1!!
            publicCardDataList[change_public_position] = temp
            clearAllCardData()

            tv_result.visibility = View.VISIBLE
            bt_next.visibility = View.VISIBLE
            tv_result.text = "已经交换"
        }
    }

    private fun checkYuyanjia() {
        doing_type = CardType.Yuyanjia
        tv_name.text = "预言家"
        var yuyanjiaCardData: CardData? = null
        var startYuyanjiaPosition = 0
        for (i in 0 until startPlayerCardList.size) {
            if (startPlayerCardList[i].cardName == "预言家") {
                yuyanjiaCardData = startPlayerCardList[i]
                startYuyanjiaPosition = i
            }
        }
        if (yuyanjiaCardData == null) {
            tv_desc.text = "场上无预言家"
            bt_next.visibility = View.VISIBLE
            bt_ok.visibility = View.GONE
        } else {
            tv_name.text = "预言家:${DataUtils.getPlayerNum(startYuyanjiaPosition, start_num, dm_num)}"
            tv_desc.text = "请选择要查验的预言家的号码"
            bt_next.visibility = View.GONE
            bt_ok.visibility = View.VISIBLE
        }

        bt_ok.setOnClickListener {
            if (checkPlayerData1 == null && checkPublicData1 == null) {
                Toast.makeText(this, "请选择要查验的号码", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            bt_ok.isEnabled = false
            var showText = ""
            if (checkPlayerData1 != null) {
                showText = "这张牌是${checkPlayerData1?.cardName}"
            }
            if (checkPublicData1 != null) {
                showText =
                    "这两张牌是${checkPublicData1PublicNum}:${checkPublicData1?.cardName}, ${checkPublicData2PublicNum}:${checkPublicData2?.cardName}"
            }

            tv_result.visibility = View.VISIBLE
            bt_next.visibility = View.VISIBLE
            tv_result.text = showText
        }

    }

    private fun checkDaoangui() {
        doing_type = CardType.Daodangui
        tv_name.text = "捣蛋鬼"
        var daodanguiCardData: CardData? = null
        var startDaodanguiPosition = 0
        for (i in 0 until startPlayerCardList.size) {
            if (startPlayerCardList[i].cardName == "捣蛋鬼") {
                daodanguiCardData = startPlayerCardList[i]
                startDaodanguiPosition = i
            }
        }
        if (daodanguiCardData == null) {
            tv_desc.text = "场上无捣蛋鬼"
            bt_next.visibility = View.VISIBLE
            bt_ok.visibility = View.GONE
        } else {
            tv_name.text =
                "捣蛋鬼:${DataUtils.getPlayerNum(startDaodanguiPosition, start_num, dm_num)}"
            tv_desc.text = "请选择要交换的其他玩家的号码"
            bt_next.visibility = View.GONE
            bt_ok.visibility = View.VISIBLE
        }
        bt_ok.setOnClickListener {
            if (checkPlayerData1 == null || checkPlayerData2 == null) {
                Toast.makeText(this, "请选择要交换的号码", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            bt_ok.isEnabled = false
            var change_position_1 = 1
            var change_position_2 = 2
            for (i in 0 until playerCardDataList.size) {
                if (playerCardDataList[i] == checkPlayerData1) {
                    change_position_1 = i
                }
                if (playerCardDataList[i] == checkPlayerData2) {
                    change_position_2 = i
                }
            }
            val temp = playerCardDataList[change_position_1]
            playerCardDataList[change_position_1] = checkPlayerData2!!
            playerCardDataList[change_position_2] = temp
            clearAllCardData()
            tv_result.visibility = View.VISIBLE
            bt_next.visibility = View.VISIBLE
            tv_result.text = "已经进行交换"
        }

    }

    private fun checkZhaoya() {
        doing_type = CardType.Zhaoya
        tv_name.text = "爪牙"
        var zhaoyaCardData: CardData? = null
        var startZhaoyaPosition = 0
        for (i in 0 until startPlayerCardList.size) {
            if (startPlayerCardList[i].cardName == "爪牙") {
                zhaoyaCardData = startPlayerCardList[i]
                startZhaoyaPosition = i
            }
        }
        if (zhaoyaCardData == null) {
            tv_desc.text = "场上无爪牙"
            bt_next.visibility = View.VISIBLE
            bt_ok.visibility = View.GONE
        } else {
            tv_name.text =
                "爪牙:${DataUtils.getPlayerNum(startZhaoyaPosition, start_num, dm_num)}"
            val langrenPositionList = arrayListOf<Int>()
            var showText = ""
            for (i in 0 until startPlayerCardList.size) {
                if (startPlayerCardList[i].cardName.contains("狼")) {
                    langrenPositionList.add(i)
                    showText += DataUtils.getPlayerNum(i, start_num, dm_num).toString() + ","
                }
            }
            tv_desc.text = "场上的狼人为：$showText"
            bt_next.visibility = View.VISIBLE
            bt_ok.visibility = View.GONE
        }

    }

    private fun checkLangren() {
        doing_type = CardType.Langren
        tv_name.text = "狼人"
        val langrenList = arrayListOf<CardData>()
        val startLangrenPositionList = arrayListOf<Int>()
        for (i in 0 until startPlayerCardList.size) {
            if (startPlayerCardList[i].cardName.contains("狼")) {
                val langrenCardData = startPlayerCardList[i]
                langrenList.add(langrenCardData)
                startLangrenPositionList.add(i)
            }
        }
        if (langrenList.isEmpty()) {
            tv_desc.text = "场上无狼人"
            bt_next.visibility = View.VISIBLE
            bt_ok.visibility = View.GONE
        } else {
            var showText = ""
            for (position in startLangrenPositionList) {
                showText += DataUtils.getPlayerNum(position, start_num, dm_num).toString() + ","
            }
            tv_name.text = "狼人：$showText"
        }
        if (langrenList.size >= 2) {
            tv_desc.text = "场上有两个以上狼人，无需查验"
            bt_next.visibility = View.VISIBLE
            bt_ok.visibility = View.GONE
        }
        if (langrenList.size == 1) {
            tv_desc.text = "请选择要查验的狼人的号码"
            bt_next.visibility = View.GONE
            bt_ok.visibility = View.VISIBLE
        }
        bt_ok.setOnClickListener {
            if (checkPublicData1 == null) {
                Toast.makeText(this, "请选择要查验的号码", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            bt_ok.isEnabled = false
            tv_result.visibility = View.VISIBLE
            bt_next.visibility = View.VISIBLE
            tv_result.text = "选择的牌为:${checkPublicData1?.cardName ?: ""}"
        }
    }

    fun setGone() {
        tv_result.visibility = View.GONE
        bt_ok.isEnabled = true
    }


    private fun updateAdapter() {
        playerCardAdapter.notifyDataSetChanged()
        publicCardAdapter.notifyDataSetChanged()
    }
}
