package taylor.com.selector2;

import java.util.HashMap;

/**
 * it controls the states between several choices which is a {@link Selector},
 * there are two modes by default: {@link #MODE_SINGLE_CHOICE} act as RadioButton + RadioGroup ,{@link #MODE_MULTIPLE_CHOICE } act as CheckBox
 * the advantage of this class is it don't need to be the parent view of several choices, thus you could place the choices whatever your like
 * and choice mode could be extends by implementing {#link #ChoiceAction} interface
 */
public class SelectorGroup {
    public static final int MODE_SINGLE_CHOICE = 1;
    public static final int MODE_MULTIPLE_CHOICE = 2;

    private ChoiceAction choiceMode;
    private StateListener onStateChangeListener;
    /**
     * a map to keep previous selected selector
     */
    private HashMap<String, Selector> selectorMap = new HashMap<>();

    /**
     * customized an choice mode by yourself
     *
     * @param choiceMode
     */
    public void setChoiceMode(ChoiceAction choiceMode) {
        this.choiceMode = choiceMode;
    }

    /**
     * set a default choice mode
     *
     * @param mode
     */
    public void setChoiceMode(int mode) {
        switch (mode) {
            case MODE_MULTIPLE_CHOICE:
                choiceMode = new MultipleAction();
                break;
            case MODE_SINGLE_CHOICE:
                choiceMode = new SingleAction();
                break;
        }
    }

    public void setStateListener(StateListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    /**
     * get the selector which clicked last time by the specific group tag
     *
     * @param groupTag a tag which the previous selector belongs to
     * @return
     */
    public Selector getPreSelector(String groupTag) {
        return selectorMap.get(groupTag);
    }

    /**
     * cancel selected state of one Selector when another is selected
     *
     * @param selector the Selector which is selected right now
     */
    private void cancelPreSelector(Selector selector) {
        String groupTag = selector.getGroupTag();
        Selector preSelector = selectorMap.get(groupTag);
        if (preSelector != null) {
            preSelector.setSelected(false);
        }
    }

    /**
     * add extra layer which means more complex
     *
     * @param selector
     */
    void onSelectorClick(Selector selector) {
        if (choiceMode != null) {
            choiceMode.onChoose(selector, this, onStateChangeListener);
        }
        //keep click selector in map
        selectorMap.put(selector.getGroupTag(), selector);
    }

    public void clear() {
        if (selectorMap != null) {
            selectorMap.clear();
        }
    }

    public interface ChoiceAction {
        /**
         * invoked when one selector is clicked
         *
         * @param selector      the clicked selector
         * @param selectorGroup
         * @param stateListener
         */
        void onChoose(Selector selector, SelectorGroup selectorGroup, StateListener stateListener);
    }

    /**
     * pre-defined choice mode: previous choice will be canceled if there is a new choice
     */
    private class SingleAction implements ChoiceAction {

        @Override
        public void onChoose(Selector selector, SelectorGroup selectorGroup, StateListener stateListener) {
            selector.setSelected(true);
            cancelPreSelector(selector);
            if (stateListener != null) {
                stateListener.onStateChange(selector.getSelectorTag(), true);
            }
        }
    }

    /**
     * pre-defined choice mode: all choices will be preserved
     */
    private class MultipleAction implements ChoiceAction {

        @Override
        public void onChoose(Selector selector, SelectorGroup selectorGroup, StateListener stateListener) {
            boolean isSelected = selector.isSelected();
            selector.setSelected(!isSelected);
            if (stateListener != null) {
                stateListener.onStateChange(selector.getSelectorTag(), !isSelected);
            }
        }
    }

    public interface StateListener {
        void onStateChange(String tag, boolean isSelected);
    }
}
