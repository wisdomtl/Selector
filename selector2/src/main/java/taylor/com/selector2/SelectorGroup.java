package taylor.com.selector2;

import android.view.View;

import java.util.HashSet;
import java.util.Set;

/**
 * it controls the states between several choices which is a {@link Selector},
 * there are two modes: {@link #MODE_SINGLE_CHOICE} act as RadioButton + RadioGroup ,{@link #MODE_MULTIPLE_CHOICE } act as CheckBox
 * the advantage of this class is it don't need to be the parent view of several choices, thus you could place the choices whatever your like
 */
public class SelectorGroup {
    public static final int MODE_SINGLE_CHOICE = 1;
    public static final int MODE_MULTIPLE_CHOICE = 2;

    private Set<Selector> selectors = new HashSet<>();
    private ChoiceAction choiceMode;
    private OnStateChangeListener onStateChangeListener;

    public SelectorGroup(int mode) {
        switch (mode) {
            case MODE_MULTIPLE_CHOICE:
                choiceMode = new MultipleChoiceAction();
                break;
            case MODE_SINGLE_CHOICE:
                choiceMode = new SingleChoiceAction();
                break;
            default:
                choiceMode = new SingleChoiceAction();
        }
    }

    public void setChoiceMode(ChoiceAction choiceMode) {
        this.choiceMode = choiceMode;
    }

    /**
     * toggle or cancel one choice
     *
     * @param selected
     * @param selector
     */
    public void setSelected(boolean selected, Selector selector) {
        if (selector == null) {
            return;
        }
        selector.setSelected(selected);
        if (onStateChangeListener != null) {
            onStateChangeListener.onSelectorStateChange(selector.getSelectorTag(), selected);
        }
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public void addSelector(Selector selector) {
        selectors.add(selector);
    }

    /**
     * cancel selected state of one Selector when another is selected
     *
     * @param selector the Selector which is selected right now
     */
    private void cancelPreSelector(View selector) {
        for (Selector s : selectors) {
            if (!s.equals(selector) && s.isSelected()) {
                s.setSelected(false);
                if (onStateChangeListener != null) {
                    onStateChangeListener.onSelectorStateChange(s.getSelectorTag(), false);
                }
            }
        }
    }

    /**
     * add extra layer which means more complex
     *
     * @param selector
     */
    public void onSelectorClick(Selector selector) {
        if (choiceMode != null) {
            choiceMode.onChoose(selector);
        }
    }

    public void clear() {
        if (selectors != null) {
            selectors.clear();
        }
    }

    private interface ChoiceAction {
        void onChoose(Selector selector);
    }

    /**
     * pre-defined choice mode: previous choice will be canceled if there is a new choice
     */
    private class SingleChoiceAction implements ChoiceAction {

        @Override
        public void onChoose(Selector selector) {
            selector.setSelected(true);
            cancelPreSelector(selector);
            if (onStateChangeListener != null) {
                onStateChangeListener.onSelectorStateChange(selector.getSelectorTag(), true);
            }
        }
    }

    /**
     * pre-defined choice mode: all choices will be preserved
     */
    private class MultipleChoiceAction implements ChoiceAction {

        @Override
        public void onChoose(Selector selector) {
            boolean isSelected = selector.isSelected();
            selector.setSelected(!isSelected);
            if (onStateChangeListener != null) {
                onStateChangeListener.onSelectorStateChange(selector.getSelectorTag(), !isSelected);
            }
        }
    }

    public interface OnStateChangeListener {
        void onSelectorStateChange(String tag, boolean isSelected);
    }
}
