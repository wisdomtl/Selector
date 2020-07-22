package taylor.com.slectorkt


/**
 * the controller for [Selector]s
 */
class SelectorGroup {
    companion object {
        /**
         * single choice mode, previous [Selector] will be unselected if a new one is selected
         */
        var MODE_SINGLE = { selectorGroup: SelectorGroup, selector: Selector ->
            selectorGroup.run {
                find(selector.groupTag)?.let { setSelected(it, false) }
                setSelected(selector, true)
            }
        }

        /**
         * multiple choice mode, several [Selector] could be selected in one [SelectorGroup]
         */
        var MODE_MULTIPLE = { selectorGroup: SelectorGroup, selector: Selector ->
            selectorGroup.setSelected(selector, !selector.isSelecting)
        }
    }

    /**
     * the selected [Selector]s in this [SelectorGroup]
     */
    private var selectors = mutableSetOf<Selector>()

    /**
     * the choice mode of this [SelectorGroup], there are two default choice mode, which is [MODE_SINGLE] and [MODE_MULTIPLE]
     */
    var choiceMode: ((SelectorGroup, Selector) -> Unit)? = null

    /**
     * if selection in this [SelectorGroup] is changed ,this lambda will be invoked,
     * override this to listen the change of selection
     */
    var selectChangeListener: ((Set<Selector>/*selected set*/, Selector?/*unselected*/) -> Unit)? = null

    fun onSelectorClick(selector: Selector) {
        choiceMode?.invoke(this, selector)
    }

    /**
     * find [Selector] according to tag
     */
    fun find(groupTag: String) = selectors.find { it.groupTag == groupTag }

    fun setSelected(selector: Selector, select: Boolean) {
        if (selector.isSelecting == select) return
        if (select) selectors.add(selector) else selectors.remove(selector)
        selector.showSelectEffect(select)
        if (select) {
            selectChangeListener?.invoke(selectors, null)
        } else {
            selectChangeListener?.invoke(selectors, selector)
        }
    }

    fun clear() {
        selectors.clear()
    }
}