package taylor.com.selector

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.neoclub.uki.home.game.GameDialogFragment
import taylor.com.layout.*
import taylor.com.slectorkt.Selector
import taylor.com.slectorkt.SelectorGroup
import java.io.Closeable

class SelectorKtActivity : AppCompatActivity() {

    private val mans = listOf(
        Man(13, "teenager", R.drawable.teenage),
        Man(20, "man", R.drawable.man),
        Man(40, "old-man", R.drawable.old_man)
    )

    private lateinit var multipleModeContainer: LinearLayout
    private val key = object : Selector.Key<Man> {}

    /**
     * describe how age Selector looks like
     */
    private val ageSelectorView: ConstraintLayout
        get() = ConstraintLayout {
            layout_width = match_parent
            layout_height = match_parent
            ImageView {
                layout_id = "ivSelector"
                layout_width = 0
                layout_height = 30
                top_toTopOf = "ivContent"
                bottom_toBottomOf = "ivContent"
                start_toStartOf = "ivContent"
                end_toEndOf = "ivContent"
                background_res = R.drawable.age_selctor_shape
                alpha = 0f
            }

            ImageView {
                layout_id = "ivContent"
                layout_width = match_parent
                layout_height = 30
                center_horizontal = true
                src = R.drawable.man
                top_toTopOf = "ivSelector"
            }

            TextView {
                layout_id = "tvTitle"
                layout_width = match_parent
                layout_height = wrap_content
                bottom_toBottomOf = parent_id
                text = "man"
                gravity = gravity_center_horizontal
            }
        }

    /**
     * the listener for age selectors
     */
    private val onAgeSelectStateChange = { selector: Selector, select: Boolean ->
        selector.find<ImageView>("ivSelector")?.alpha = if (select) 1f else 0f
    }

    /**
     * the single choice mode controller for age selector
     */
    private val singleGroup = SelectorGroup().apply {
        choiceMode = SelectorGroup.MODE_SINGLE
        selectChangeListener = { selectors: List<Selector>->

        }
    }

    private val selectorBindAction = { selector: View, data: Any? ->
        selector.find<ImageView>("ivContent")?.setImageResource((data as Man).res)
        selector.find<TextView>("tvTitle")?.text = (data as Man).title
    }

    /**
     * the multiple choice mode controller for age selector
     */
    private val multipleGroup = SelectorGroup().apply {
        choiceMode = SelectorGroup.MODE_MULTIPLE
        selectChangeListener = { selectors: List<Selector> ->
        }
    }

    /**
     * create Selector non-dynamically just like in xml
     */
    private val rootView by lazy {
        ConstraintLayout {
            layout_width = match_parent
            layout_height = match_parent

            TextView {
                layout_id = "tvSingleMode"
                layout_width = wrap_content
                layout_height = wrap_content
                top_toTopOf = parent_id
                center_horizontal = true
                text = "single choice mode"
                textSize = 20f
            }

            Selector {
                layout_id = "sMan"
                tag = "man"
                group = singleGroup
                groupTag = "age"
                layout_width = 90
                layout_height = 50
                contentView = ageSelectorView
                onSelectChange = onAgeSelectStateChange
                top_toBottomOf = "tvSingleMode"
                center_horizontal = true
                bind = Binder(Man(20, "man", R.drawable.man), selectorBindAction)
            }
            Selector {
                layout_id = "sOldMan"
                tag = "old-man"
                group = singleGroup
                groupTag = "age"
                layout_width = 90
                layout_height = 50
                contentView = ageSelectorView
                onSelectChange = onAgeSelectStateChange
                top_toBottomOf = "sMan"
                start_toStartOf = parent_id
                end_toEndOf = parent_id
                horizontal_bias = 0.2f
                bind = Binder(Man(40, "old-man", R.drawable.old_man), selectorBindAction)
            }
            Selector {
                layout_id = "sTeenager"
                tag = "teenager"
                group = singleGroup
                groupTag = "age"
                layout_width = 90
                layout_height = 50
                contentView = ageSelectorView
                onSelectChange = onAgeSelectStateChange
                top_toBottomOf = "sMan"
                start_toStartOf = parent_id
                end_toEndOf = parent_id
                horizontal_bias = 0.8f
                bind = Binder(Man(13, "teenager", R.drawable.teenage), selectorBindAction)
            }

            TextView {
                layout_id = "tvMultiMode"
                layout_width = wrap_content
                layout_height = wrap_content
                top_toBottomOf = "sTeenager"
                center_horizontal = true
                text = "multiple choice mode"
                textSize = 20f
            }

            multipleModeContainer = LinearLayout {
                layout_id = "multiple-container"
                layout_width = wrap_content
                layout_height = wrap_content
                orientation = horizontal
                center_horizontal = true
                top_toBottomOf = "tvMultiMode"
            }

            Button {
                layout_width = match_parent
                layout_height = wrap_content
                text = "show game selector"
                top_toBottomOf = "multiple-container"
                onClick = {_->
                    GameDialogFragment().show(supportFragmentManager,"game")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(rootView)
        buildMultipleChoiceSelector(mans)
    }

    /**
     * build multiple choice [Selector] dynamically
     */
    private fun buildMultipleChoiceSelector(mans: List<Man>) {
        mans.forEach { man ->
            Selector {
                layout_id = man.title
                tag = man.title
                group = multipleGroup
                groupTag = "multiple-age"
                layout_width = 90
                layout_height = 50
                contentView = ageSelectorView
                onSelectChange = onAgeSelectStateChange
                this[key] = man
                bind = Binder(man){selector,data->
                    find<ImageView>("ivContent")?.setImageResource((data as Man).res)
                    find<TextView>("tvTitle")?.text = (data as Man).title
                }
            }.also { multipleModeContainer.addView(it) }
        }
    }
}

/**
 * data bean for multiple choice [Selector]
 */
data class Man(var age: Int, var title: String, var res: Int) : Closeable {
    override fun close() {
        age = -1
        title = ""
        res = -1
    }
}

/**
 * print collection bean in which you interested defined by [map]
 */
fun <T> Collection<T>.print(map: (T) -> String) =
    StringBuilder("\n[").also { sb ->
        this.forEach { e -> sb.append("\n\t${map(e)},") }
        sb.append("\n]")
    }.toString()