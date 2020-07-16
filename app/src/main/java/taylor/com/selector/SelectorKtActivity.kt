package taylor.com.selector

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import taylor.com.layout.*
import taylor.com.slectorkt.Selector
import taylor.com.slectorkt.SelectorGroup

class SelectorKtActivity : AppCompatActivity() {

    /**
     * describe how age Selector looks like
     */
    private val ageSelectorView: ConstraintLayout
        get() = ConstraintLayout {
            layout_width = match_parent
            layout_height = match_parent
            ImageView {
                layout_id = "ivSelector"
                layout_width = match_parent
                layout_height = 40
                bottom_toTopOf = "tvTitle"
                top_toTopOf = parent_id
                background_res = R.drawable.age_selctor_shape
                alpha = 0f
                center_horizontal = true
            }

            ImageView {
                layout_id = "ivContent"
                layout_width = match_parent
                layout_height = 30
                center_horizontal = true
                src = R.drawable.man
                top_toTopOf = "ivSelector"
                bottom_toTopOf = "tvTitle"
            }

            TextView {
                layout_id = "tvTitle"
                layout_width = match_parent
                layout_height = wrap_content
                top_toBottomOf = "ivSelector"
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
     * the controller for age selector
     */
    private val ageGroup = SelectorGroup().apply {
        choiceMode = SelectorGroup.MODE_SINGLE
        selectChangeListener = { selectors: List<Selector>, select: Boolean ->
            val sel = selectors.fold(""){acc: String, selector: Selector ->  "${acc} ,${selector.tag}"}
            Log.v("ttaylor","tag=selectkt, SelectorKtActivity.()  ${sel} is $select")
        }
    }

    private val rootView by lazy {
        LinearLayout {
            layout_width = match_parent
            layout_height = match_parent
            orientation = vertical

            Selector {
                layout_id = "sMan"
                tag = "man"
                group = ageGroup
                groupTag = "age"
                layout_width = 90
                layout_height = 120
                contentView = ageSelectorView
                onStateChange = onAgeSelectStateChange
            }.apply {
                find<ImageView>("ivContent")?.setImageResource(R.drawable.man)
                find<TextView>("tvTitle")?.text = "man"
            }
            Selector {
                layout_id = "sOldMan"
                tag = "old-man"
                group = ageGroup
                groupTag = "age"
                layout_width = 90
                layout_height = 120
                contentView = ageSelectorView
                onStateChange = onAgeSelectStateChange
            }.apply {
                find<ImageView>("ivContent")?.setImageResource(R.drawable.old_man)
                find<TextView>("tvTitle")?.text = "old man"
            }
            Selector {
                layout_id = "sTeenager"
                tag = "teenage"
                group = ageGroup
                groupTag = "age"
                layout_width = 90
                layout_height = 120
                contentView = ageSelectorView
                onStateChange = onAgeSelectStateChange
            }.apply {
                find<ImageView>("ivContent")?.setImageResource(R.drawable.teenage)
                find<TextView>("tvTitle")?.text = "teenage"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(rootView)
    }
}