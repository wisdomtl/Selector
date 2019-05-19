package taylor.com.selector2;

import android.view.View;

import java.util.HashSet;
import java.util.Set;

/**
 * it controls the states between several choices which is a {@link Selector},
 * there are two modes by default: {@link #MODE_SINGLE_CHOICE} act as RadioButton + RadioGroup ,{@link #MODE_MULTIPLE_CHOICE } act as CheckBox
 * the advantage of this class is it don't need to be the parent view of several choices, thus you could place the choices whatever your like
 * and choice mode could be extends by implementing {#link #ChoiceAction} interface
 */
public class SelectorGroup {
    public static final int MODE_SINGLE_CHOICE = 1;
    public static final int MODE_MULTIPLE_CHOICE = 2;

    private Set<Selector> selectors = new HashSet<>();
    private ChoiceAction choiceMode;
    private StateListener onStateChangeListener;

    /**
     * customized an choice mode by yourself
     * @param choiceMode
     */
    public void setChoiceMode(ChoiceAction choiceMode) {
        this.choiceMode = choiceMode;
    }

    /**
     * set a default choice mode
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

    void addSelector(Selector selector) {
        selectors.add(selector);
    }

    /**
     * cancel selected state of one Selector when another is selected
     *
     * @param selector  the Selector which is selected right now
     * @param selectors the all Selectors which belongs to a SelectorGroup
     */
    private void cancelPreSelector(View selector, Set<Selector> selectors) {
        for (Selector s : selectors) {
            if (!s.equals(selector) && s.isSelected()) {
                s.setSelected(false);
//                if (onStateChangeListener != null) {
//                    onStateChangeListener.onStateChange(s.getSelectorTag(), false);
//                }
            }
        }
    }

    /**
     * add extra layer which means more complex
     *
     * @param selector
     */
    void onSelectorClick(Selector selector) {
        if (choiceMode != null) {
            choiceMode.onChoose(selectors, selector, onStateChangeListener);
        }
    }

    public void clear() {
        if (selectors != null) {
            selectors.clear();
        }
    }

    public interface ChoiceAction {
        void onChoose(Set<Selector> selectors, Selector selector, StateListener stateListener);
    }

    /**
     * pre-defined choice mode: previous choice will be canceled if there is a new choice
     */
    private class SingleAction implements ChoiceAction {

        @Override
        public void onChoose(Set<Selector> selectors, Selector selector, StateListener stateListener) {
            selector.setSelected(true);
            cancelPreSelector(selector, selectors);
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
        public void onChoose(Set<Selector> selectors, Selector selector, StateListener stateListener) {
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
