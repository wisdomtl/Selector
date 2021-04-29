package taylor.com.slectorkt

import java.io.Closeable

/**
 * the controller for [Selector]s
 */
class SelectorGroup {
    companion object {
        /**
         * single choice mode, previous [Selector] will be unselected if a new one is selected
         */
        var MODE_SINGLE = { selectorGroup: SelectorGroup, selector: Selector, map: LinkedHashMap<String, MutableSet<Selector>> ->
            selectorGroup.run {
                findLast(selector.groupTag)?.let { setSelected(it, false) }
                setSelected(selector, true)
            }
        }

        /**
         * multiple choice mode, several [Selector] could be selected in one [SelectorGroup]
         */
        var MODE_MULTIPLE = { selectorGroup: SelectorGroup, selector: Selector, map: LinkedHashMap<String, MutableSet<Selector>> ->
            selectorGroup.setSelected(selector, ! selector.isSelecting)
        }
    }

    /**
     * the selected [Selector]
     * key is group tag and value is selected [Selector] in this group
     * the reason why use [LinkedHashMap] is to keep the sequence of groups
     */
    private var selectorMap = LinkedHashMap<String, MutableSet<Selector>>()

    /**
     * the selected tag of [Selector]
     */
    private var selectTagMap = LinkedHashMap<String, MutableSet<String>>()

    /**
     * the selected data of [Selector], which is kept in [Selector.tags]
     */
    private var selectDataMap = LinkedHashMap<String, MutableSet<Closeable>>()

    /**
     * the choice mode of this [SelectorGroup], there are two default choice mode, which is [MODE_SINGLE] and [MODE_MULTIPLE]
     */
    var choiceMode: ((SelectorGroup, Selector, map: LinkedHashMap<String, MutableSet<Selector>>) -> Unit)? = null

    /**
     * if selection in this [SelectorGroup] is changed ,this lambda will be invoked,
     * override this to listen the change of selection, [Selector] will be delivered to business layer
     */
    var selectChangeListener: ((List<Selector>/*selected set*/) -> Unit)? = null

    /**
     * if selection in this [SelectorGroup] is changed ,this lambda will be invoked,
     * override this to listen the change of selection, [Selector.tag] will be delivered to business layer
     */
    var selectTagChangeListener: ((List<String>) -> Unit)? = null

    /**
     * if selection in this [SelectorGroup] is changed ,this lambda will be invoked,
     * override this to listen the change of selection, [Selector.tags] will be delivered to business layer
     */
    private var selectDataChangeListener: ((List<Closeable>) -> Unit)? = null

    /**
     * the key for getting the data of selected [Selector]
     */
    private var key: Selector.Key<*>? = null

    fun <T : Closeable> setSelectDataChangeListener(key: Selector.Key<T>, listener: (List<Closeable>) -> Unit) {
        this.key = key
        selectDataChangeListener = listener
    }

    fun onSelectorClick(selector: Selector) {
        choiceMode?.invoke(this, selector, selectorMap)
    }

    /**
     * find [Selector]s with the same [groupTag]
     */
    fun find(groupTag: String) = selectorMap[groupTag]

    /**
     * find last selected [Selector] of [groupTag]
     */
    fun findLast(groupTag: String) = find(groupTag)?.takeUnless { it.isNullOrEmpty() }?.last()

    fun setSelected(selector: Selector, select: Boolean) {
        if (select) {
            selectTagMap[selector.groupTag]?.also { it.add(selector.tag) } ?: kotlin.run { selectTagMap[selector.groupTag] = mutableSetOf(selector.tag) }
            selectorMap[selector.groupTag]?.also { it.add(selector) } ?: kotlin.run { selectorMap[selector.groupTag] = mutableSetOf(selector) }
            selectDataMap[selector.groupTag]?.also { set -> selector.tags[key]?.let { set.add(it) } } ?: kotlin.run {
                selector.tags[key]?.let {
                    selectDataMap[selector.groupTag] = mutableSetOf(it)
                }
            }
        } else {
            selectTagMap[selector.groupTag]?.also { it.remove(selector.tag) }
            selectorMap[selector.groupTag]?.also { it.remove(selector) }
            selectDataMap[selector.groupTag]?.also { it.remove(selector.tags[key]) }
        }
        selector.showSelectEffect(select)
        selectChangeListener?.invoke(selectorMap.flatMap { it.value })
        selectTagChangeListener?.invoke(selectTagMap.flatMap { it.value })
        selectDataChangeListener?.invoke(selectDataMap.flatMap { it.value })
    }

    fun clear(selector:Selector) {
        selectorMap[selector.groupTag]?.remove(selector)
    }
}
