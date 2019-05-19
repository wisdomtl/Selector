package taylor.com.selector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import taylor.com.selector2.Selector;
import taylor.com.selector2.SelectorGroup;

public class MainActivity extends AppCompatActivity {
    private List<String> tags = new ArrayList<>();
    private List<String> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        //multiple-choice
        SelectorGroup multipleGroup = new SelectorGroup();
        multipleGroup.setChoiceMode(SelectorGroup.MODE_MULTIPLE_CHOICE);
        multipleGroup.setStateListener(new MultipleChoiceListener());
        ((Selector) findViewById(R.id.selector_10)).setGroup(multipleGroup);
        ((Selector) findViewById(R.id.selector_20)).setGroup(multipleGroup);
        ((Selector) findViewById(R.id.selector_30)).setGroup(multipleGroup);

        //single-choice
        SelectorGroup singleGroup = new SelectorGroup();
        singleGroup.setChoiceMode(SelectorGroup.MODE_SINGLE_CHOICE);
        singleGroup.setStateListener(new SingleChoiceListener());
        ((Selector) findViewById(R.id.single10)).setGroup(singleGroup);
        ((Selector) findViewById(R.id.single20)).setGroup(singleGroup);
        ((Selector) findViewById(R.id.single30)).setGroup(singleGroup);

        //order-choice
        SelectorGroup orderGroup = new SelectorGroup();
        orderGroup.setStateListener(new OrderChoiceListener());
        orderGroup.setChoiceMode(new OderChoiceMode());
        ((Selector) findViewById(R.id.selector_starters_duck)).setGroup(orderGroup);
        ((Selector) findViewById(R.id.selector_starters_pork)).setGroup(orderGroup);
        ((Selector) findViewById(R.id.selector_starters_springRoll)).setGroup(orderGroup);
        ((Selector) findViewById(R.id.selector_main_pizza)).setGroup(orderGroup);
        ((Selector) findViewById(R.id.selector_main_pasta)).setGroup(orderGroup);
        ((Selector) findViewById(R.id.selector_soup_mushroom)).setGroup(orderGroup);
        ((Selector) findViewById(R.id.selector_soup_scampi)).setGroup(orderGroup);
    }

    /**
     * business logic for single-choice is here
     */
    private class SingleChoiceListener implements SelectorGroup.StateListener {

        @Override
        public void onStateChange(String tag, boolean isSelected) {
            Toast.makeText(MainActivity.this, tag + " is selected", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * business logic for multiple-choice is here
     */
    private class MultipleChoiceListener implements SelectorGroup.StateListener {

        @Override
        public void onStateChange(String tag, boolean isSelected) {
            if (isSelected) {
                tags.add(tag);
            } else {
                tags.remove(tag);
            }
            Toast.makeText(MainActivity.this, tags.toString() + " is selected", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * business logic for order-choice is here
     */
    private class OrderChoiceListener implements SelectorGroup.StateListener {

        @Override
        public void onStateChange(String tag, boolean isSelected) {
            if (isSelected) {
                orders.add(tag);
                Toast.makeText(MainActivity.this, orders.toString() + " is selected", Toast.LENGTH_SHORT).show();
            } else {
                orders.remove(tag);
            }
        }
    }

    /**
     * extends the choice mode of SelectorGroup by implementing SelectorGroup.ChoiceAction
     * the new choice mode is like the behaviour when ordering by western food menu:one choice for one type
     */
    private class OderChoiceMode implements SelectorGroup.ChoiceAction {

        @Override
        public void onChoose(Set<Selector> selectors, Selector selector, SelectorGroup.StateListener stateListener) {
            String tagPrefix = getTagPrefix(selector.getSelectorTag());
            cancelPreSelectorBySameTag(selectors, tagPrefix, stateListener);
            selector.setSelected(true);
            if (stateListener != null) {
                stateListener.onStateChange(selector.getSelectorTag(), true);
            }
        }

        private void cancelPreSelectorBySameTag(Set<Selector> selectors, String tagPrefix, SelectorGroup.StateListener stateListener) {
            for (Selector selector : selectors) {
                String prefix = getTagPrefix(selector.getSelectorTag());
                if (prefix.equals(tagPrefix) && selector.isSelected()) {
                    selector.setSelected(false);
                    if (stateListener != null) {
                        stateListener.onStateChange(selector.getSelectorTag(), false);
                    }
                }
            }
        }

        private String getTagPrefix(String tag) {
            int index = tag.indexOf("_");
            return tag.substring(0, index);
        }
    }
}
