package taylor.com.selector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import taylor.com.selector2.Selector;
import taylor.com.selector2.SelectorGroup;

public class MainActivity extends AppCompatActivity implements SelectorGroup.OnStateChangeListener {
    private SelectorGroup singleSelectorGroup = new SelectorGroup(SelectorGroup.MODE_SINGLE_CHOICE);
    private SelectorGroup multipleSelectorGroup = new SelectorGroup(SelectorGroup.MODE_MULTIPLE_CHOICE);

    private List<String> tags = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        Selector teenageSelector = findViewById(R.id.selector_10);
        Selector manSelector = findViewById(R.id.selector_20);
        Selector oldManSelector = findViewById(R.id.selector_30);

        multipleSelectorGroup.setOnStateChangeListener(new MultipleChoiceListener());
        singleSelectorGroup.setOnStateChangeListener(new SingleChoiceListener());

        /**
         * if you want single choice mode ,just replace SelectorGroup by {@link #singleSelectorGroup}
         */
        teenageSelector.setSelectorGroup(multipleSelectorGroup);
        manSelector.setSelectorGroup(multipleSelectorGroup);
        oldManSelector.setSelectorGroup(multipleSelectorGroup);
    }

    @Override
    public void onSelectorStateChange(String tag, boolean isSelected) {
        if (isSelected) {
            Toast.makeText(this, tag + " is selected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, tag + " is unselected", Toast.LENGTH_SHORT).show();
        }
    }

    private class SingleChoiceListener implements SelectorGroup.OnStateChangeListener {

        @Override
        public void onSelectorStateChange(String tag, boolean isSelected) {

        }
    }

    private class MultipleChoiceListener implements SelectorGroup.OnStateChangeListener {

        @Override
        public void onSelectorStateChange(String tag, boolean isSelected) {
            if (isSelected) {
                tags.add(tag);
            } else {
                tags.remove(tag);
            }
            Log.e("ttaylor", "MultipleChoiceListener.onSelectorStateChange()" + "  tags=" + tags);
        }
    }
}
