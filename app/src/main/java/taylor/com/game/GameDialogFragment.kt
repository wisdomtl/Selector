package cn.neoclub.uki.home.game

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
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
                GameType("王者荣耀", "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2284617148,198865717&fm=26&gp=0.jpg")
            )
        ),
        listOf(
            GameAttrs(
                "大区", listOf(
                    "不限",
                    "微信",
                    "QQ"
                )
            ),
            GameAttrs(
                "模式", listOf(
                    "不限",
                    "排位赛",
                    "普通模式",
                    "娱乐模式",
                    "游戏交流"
                )
            ),
            GameAttrs(
                "匹配段位", listOf(
                    "青铜白银",
                    "黄金",
                    "铂金",
                    "钻石",
                    "星耀",
                    "王者"
                )
            ),
            GameAttrs(
                "组队人数", listOf(
                    "不限",
                    "三排",
                    "五排"
                )
            )
        )
    )

    private lateinit var gameContainer: LinearLayout

    private val rootView by lazy {
        ConstraintLayout {
            layout_width = match_parent
            layout_height = 633
            background_color = "#FFFFFF"

            gameContainer = LinearLayout {
                layout_width = match_parent
                layout_height = 553
                orientation = vertical
                top_toTopOf = parent_id
            }
        }
    }


    private val titleView: TextView?
        get() = TextView {
            layout_width = wrap_content
            layout_height = wrap_content
            textSize = 14f
            textColor = "#ff3f4658"
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

    private val onGameTypeSelectChange = { selector: Selector, select: Boolean ->
        selector.find<View>("vGameSelected")?.visibility = if (select) visible else invisible
        selector.find<TextView>("tvGame")?.textColor = if (select) "#FF5183"  else "#747E8B"
    }

    private val gameSelectorGroup by lazy {
        SelectorGroup().apply {
            choiceMode = { selectorGroup, selector ->
                selectorGroup.apply {
//                    findSelectors(selector.groupTag)?.let { setSelected(it, false) }
                }
                selectorGroup.setSelected(selector, true)
            }
            selectChangeListener = { selectors, select -> }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return rootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buildGame(gameBeans)
    }

    private fun buildGame(gameBeans: GameBean) {
        buildGameType(gameBeans.games)
        buildGameAttr(gameBeans.gameAttrs)
    }

    private fun buildGameAttr(gameAttrs: List<GameAttrs>?) {

    }

    private fun buildGameType(games: Games?) {
        gameContainer.apply {

            titleView?.apply {
                text = games?.title
            }.also { addView(it) }

            LineFeedLayout {
                layout_width = match_parent
                layout_height = 100
                games?.gameTypes?.filter { !it.name.isNullOrEmpty() }?.forEach { gameType ->
                    Selector {
                        layout_id = gameType.name!!
                        tag = gameType.name!!
                        groupTag = "gameTypes"
                        group = gameSelectorGroup
                        contentView = gameView
                        onSelectChange = onGameTypeSelectChange
                        layout_width = 60
                        layout_height = 81
                        margin_end = 20
                        tags[gameTypeKey] = gameType
                    }.apply {
                        find<TextView>("tvGame")?.text = gameType.name
//                        find<ImageView>("ivGame")?.let {
//                            UkiImgLoader.from(context!!)
//                                .setSource(gameType.img)
//                                .into(it)
//                        }
                    }
                }
            }
        }
    }

    private val gameTypeKey = object : Selector.Key<GameType> {}

}
