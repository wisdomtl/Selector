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
                findLast(selector.groupTag)?.let { setSelected(it, false) }
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
    private var selectorMap = HashMap<String, MutableSet<Selector>>()

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
     * find [Selector]s with the same [groupTag]
     */
    fun find(groupTag: String) = selectorMap[groupTag]

    /**
     * find [Selector] according to [groupTag] and [tag]
     */
    fun findLast(groupTag: String) = find(groupTag)?.takeUnless { it.isNullOrEmpty() }?.last()

    fun setSelected(selector: Selector, select: Boolean) {
        if (select) {
            selectorMap[selector.groupTag]?.also { it.add(selector) } ?: also { selectorMap[selector.groupTag] = mutableSetOf(selector) }
        } else {
            selectorMap[selector.groupTag]?.also { it.remove(selector) }
        }
        selector.showSelectEffect(select)
        if (select) {
            selectChangeListener?.invoke(selectorMap[selector.groupTag] ?: emptySet(), null)
        } else {
            selectChangeListener?.invoke(selectorMap[selector.groupTag] ?: emptySet(), selector)
        }
    }

    fun clear() {
        selectorMap.clear()
    }
}