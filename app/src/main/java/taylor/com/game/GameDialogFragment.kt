package cn.neoclub.uki.home.game

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import taylor.com.layout.*
import taylor.com.selector.R
import taylor.com.slectorkt.Selector
import taylor.com.slectorkt.SelectorGroup

class GameDialogFragment : DialogFragment() {

    private val gameBeans = GameBean(
        Games(
            "选择游戏", listOf(
                GameType("王者荣耀", "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2284617148,198865717&fm=26&gp=0.jpg"),
                GameType("王者荣耀", "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2284617148,198865717&fm=26&gp=0.jpg"),
                GameType("王者荣耀", "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2284617148,198865717&fm=26&gp=0.jpg"),
                GameType("大神陪你玩", "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2284617148,198865717&fm=26&gp=0.jpg")
            )
        ),
        listOf(
            GameAttrs(
                "大区", listOf(
//                    GameAttrName("不限"),
                    GameAttrName("微信"),
                    GameAttrName("QQ")
                )
            ),
            GameAttrs(
                "模式", listOf(
//                    GameAttrName("不限"),
                    GameAttrName("排位赛"),
                    GameAttrName("普通模式"),
                    GameAttrName("娱乐模式"),
                    GameAttrName("游戏交流")
                )
            ),
            GameAttrs(
                "匹配段位", listOf(
//                    GameAttrName("不限"),
                    GameAttrName("青铜白银"),
                    GameAttrName("黄金"),
                    GameAttrName("铂金"),
                    GameAttrName("钻石"),
                    GameAttrName("星耀"),
                    GameAttrName("王者")
                )
            ),
            GameAttrs(
                "组队人数", listOf(
//                    GameAttrName("不限"),
                    GameAttrName("三排"),
                    GameAttrName("五排")
                )
            )
        )
    )

    private val DEFAULT_SELECTS = setOf(
        "微信",
        "排位赛"
    )
    private var matchContent: String = ""

    private var selectedTags = listOf<String>()

    //<editor-fold desc="views and layout">
    private lateinit var gameContainer: LinearLayout

    private val rootView: ConstraintLayout? by lazy {
        ConstraintLayout {
            layout_width = match_parent
            layout_height = 650
            background_color = "#FFFFFF"

            gameContainer = LinearLayout {
                layout_id = "gameContainer"
                layout_width = match_parent
                layout_height = 573
                padding_start = 20
                padding_end = 5
                orientation = vertical
                top_toTopOf = parent_id
            }

            ImageView {
                layout_width = 22
                layout_height = 22
                top_toTopOf = parent_id
                end_toEndOf = parent_id
                margin_top = 9
                margin_end = 5
                padding = 5
                scaleType = scale_fix_xy
                src = R.drawable.ic_close_black
                onClick = onCloseClick
            }

            when (arguments?.getString("type")) {
                "match" -> {
                    TextView {
                        layout_id = "tvCreateParty"
                        layout_width = 150
                        layout_height = wrap_content
                        textSize = 14f
                        textColor = "#ff3f4658"
                        background_res = R.drawable.bg_game_attr
                        padding_top = 15
                        padding_bottom = 15
                        text = "关闭"
                        horizontal_chain_style = spread
                        gravity = gravity_center
                        start_toStartOf = parent_id
                        end_toStartOf = "tvStartMatch"
                        top_toBottomOf = "gameContainer"
                        bottom_toBottomOf = parent_id
                        onClick = { _ -> }
                    }

                    TextView {
                        layout_id = "tvStartMatch"
                        layout_width = 150
                        layout_height = wrap_content
                        textSize = 14f
                        textColor = "#FFFFFF"
                        background_res = R.drawable.bg_game_attr_select
                        padding_top = 15
                        padding_bottom = 15
                        text = "匹配"
                        gravity = gravity_center
                        horizontal_chain_style = spread
                        start_toEndOf = "tvCreateParty"
                        end_toEndOf = parent_id
                        top_toBottomOf = "gameContainer"
                        bottom_toBottomOf = parent_id
                        onClick = { _ -> }
                    }
                }
                "party" -> {
                    TextView {
                        layout_width = 335
                        layout_height = 60
                        textSize = 14f
                        textColor = "#FFFFFF"
                        gravity = gravity_center
                        text = "创建组队"
                        background_res = R.drawable.bg_game_attr_select
                        center_horizontal = true
                        top_toBottomOf = "gameContainer"
                        bottom_toBottomOf = parent_id
                        onClick = { _ -> }
                    }
                }
                "info" -> {
                    TextView {
                        layout_id = "tvDelete"
                        layout_width = 150
                        layout_height = wrap_content
                        textSize = 14f
                        textColor = "#3F4658"
                        background_res = R.drawable.bg_game_attr
                        padding_top = 15
                        padding_bottom = 15
                        horizontal_chain_style = spread
                        text = "关闭"
                        gravity = gravity_center
                        start_toStartOf = parent_id
                        end_toStartOf = "tvSave"
                        top_toBottomOf = "gameContainer"
                        bottom_toBottomOf = parent_id
                        onClick = { _ -> }
                    }

                    TextView {
                        layout_id = "tvSave"
                        layout_width = 150
                        layout_height = wrap_content
                        textSize = 14f
                        textColor = "#FFFFFF"
                        background_res = R.drawable.bg_game_attr_select
                        padding_top = 15
                        padding_bottom = 15
                        gravity = gravity_center
                        horizontal_chain_style = spread
                        text = "保存"
                        start_toEndOf = "tvDelete"
                        end_toEndOf = parent_id
                        top_toBottomOf = "gameContainer"
                        bottom_toBottomOf = parent_id
                        onClick = { _ -> }
                    }
                }
                else -> {
                    TextView {
                        layout_width = 320
                        layout_height = 60
                        textSize = 14f
                        textColor = "#FFFFFF"
                        gravity = gravity_center
                        text = "创建组队"
                        background_res = R.drawable.bg_game_attr_select
                        center_horizontal = true
                        top_toBottomOf = "gameContainer"
                        bottom_toBottomOf = parent_id
                        onClick = { _ -> }
                    }
                }
            }
        }
    }


    private val titleView: TextView?
        get() = TextView {
            layout_width = wrap_content
            layout_height = wrap_content
            textSize = 14f
            textColor = "#ff3f4658"
            textStyle = bold
        }

    private val gameView: ConstraintLayout?
        get() = ConstraintLayout {
            layout_width = 60
            layout_height = 81

            View {
                layout_id = "vGameSelected"
                layout_width = 60
                layout_height = 60
                top_toTopOf = parent_id
                center_horizontal = true
                visibility = invisible
                background_res = R.drawable.game_selected
            }

            ImageView {
                layout_id = "ivGame"
                layout_width = 50
                layout_height = 50
                align_horizontal_to = "vGameSelected"
                align_vertical_to = "vGameSelected"
                scaleType = scale_fix_xy
            }

            TextView {
                layout_id = "tvGame"
                layout_width = wrap_content
                layout_height = wrap_content
                bottom_toBottomOf = parent_id
                textColor = "#747E8B"
                textSize = 12f
                center_horizontal = true
            }
        }

    private val gameAttrView: TextView?
        get() = TextView {
            layout_id = "tvGameAttrName"
            layout_width = 70
            layout_height = 32
            textSize = 12f
            textColor = "#ff3f4658"
            background_res = R.drawable.bg_game_attr
            gravity = gravity_center
            padding_top = 7
            padding_bottom = 7
        }

    //</editor-fold>

    //<editor-fold desc="actions of views">
    private val onCloseClick = { _: View ->
        dismiss()
    }

    private val onGameTypeChange = { selector: Selector, select: Boolean ->
        selector.find<View>("vGameSelected")?.visibility = if (select) visible else invisible
        selector.find<TextView>("tvGame")?.textColor = if (select) "#FF5183" else "#747E8B"
    }

    private val onGameAttrChange = { selector: Selector, select: Boolean ->
        selector.find<TextView>("tvGameAttrName")?.apply {
            background_res = if (select) R.drawable.bg_game_attr_select else R.drawable.bg_game_attr
            textColor = if (select) "#FFFFFF" else "#3F4658"
        }
        Unit
    }

    private val gameSelectorGroup by lazy {
        SelectorGroup().apply {
            choiceMode = { selectorGroup, selector ->
                if (selector.groupTag != "匹配段位") {
                    selectorGroup.apply {
                        findLast(selector.groupTag)?.let { setSelected(it, false) }
                    }
                    selectorGroup.setSelected(selector, true)
                } else {
                    selectorGroup.setSelected(selector, !selector.isSelecting)
                }
            }
            selectChangeListener = { selecteds ->
                selectedTags = selecteds.map { it.tag }
                Log.v("ttaylor", "tag=asdf, GameDialogFragment.()  selectors = ${selecteds.print { it.tag }}")
                Log.d("ttaylor", "tag=asdf, GameDialogFragment.()  selectors = ${selectedTags.print { it }}")
            }
        }
    }
    //</editor-fold>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.BOTTOM
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView?.post {
            buildGameLayout(gameBeans, DEFAULT_SELECTS)
        }
    }

    private fun buildGameLayout(gameBeans: GameBean, defautSelect: Set<String>) {
        val gameTypes = gameBeans.games?.gameTypes?.filter { !it.name.isNullOrEmpty() }
        val gameAttrs = gameBeans.gameAttrs?.filter { !it.title.isNullOrEmpty() }

        gameContainer.apply {
//            // build game type
//            titleView?.apply {
//                text = gameBeans.games?.title
//                margin_top = 15
//                margin_bottom = 10
//            }.also { addView(it) }
//
//            LineFeedLayout {
//                layout_width = match_parent
//                layout_height = wrap_content
//                margin_end = 10
//                horizontal_gap = 10
//                vertical_gap = 10
//
//                gameTypes?.forEach { type ->
//                    Selector {
//                        layout_id = type.name!!
//                        tag = type.name!!
//                        groupTag = "gameTypes"
//                        group = gameSelectorGroup
//                        contentView = gameView
//                        onSelectChange = onGameTypeChange
//                        layout_width = 60
//                        layout_height = 81
//                        bind = Binder(type) { _, _ ->
//                            tags[gameTypeKey] = type
//                            find<TextView>("tvGame")?.text = type.name
//                            find<ImageView>("ivGame")?.let {
//                            }
//                        }
//                    }.takeIf { type.name in defautSelect }?.also { it.setSelect(true) }
//                }
//            }

            // build game attribute
            gameAttrs?.forEach { gameAttr ->
                titleView?.apply {
                    text = gameAttr.title
                    margin_top = 10
                    margin_bottom = 10
                }.also { addView(it) }

                LineFeedLayout {
                    layout_width = match_parent
                    layout_height = wrap_content
                    horizontal_gap = 8
                    vertical_gap = 8

                    gameAttr.attrs?.forEachIndexed { index, attr ->
                        Selector {
                            layout_id = attr.name!!
                            tag = attr.name!!
                            groupTag = gameAttr.title!!
                            group = gameSelectorGroup
                            contentView = gameAttrView
                            onSelectChange = onGameAttrChange
                            layout_width = 70
                            layout_height = 32
                            bind = Binder(attr) { _, _ ->
                                tags[gameAttrKey] = attr
                                find<TextView>("tvGameAttrName")?.text = attr.name
                            }
                        }.takeIf { attr.name in defautSelect }?.also { it.setSelect(true) }
                    }
                }
            }
        }
    }

    private val gameTypeKey = object : Selector.Key<GameType> {}
    private val gameAttrKey = object : Selector.Key<GameAttrName> {}
}

/**
 * print collection bean in which you interested defined by [map]
 */
fun <T> Collection<T>.print(map: (T) -> String) =
    StringBuilder("\n[").also { sb ->
        this.forEach { e -> sb.append("\n\t${map(e)},") }
        sb.append("\n]")
    }.toString()
