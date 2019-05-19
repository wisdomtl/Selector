package taylor.com.selector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import taylor.com.selector2.Selector;
import taylor.com.selector2.SelectorGroup;

public class MainActivity extends AppCompatActivity implements SelectorGroup.onStateChangeListener{
    private SelectorGroup selectorGroup = new SelectorGroup(SelectorGroup.MODE_SINGLE_CHOICE);

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

        teenageSelector.setSelectorGroup(selectorGroup);
        manSelector.setSelectorGroup(selectorGroup);
        oldManSelector.setSelectorGroup(selectorGroup);
    }

    @Override
    public void onSelectorStateChange(String tag, boolean isSelected) {
        if (isSelected) {
            Toast.makeText(this, tag + " is selected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, tag + " is unselected", Toast.LENGTH_SHORT).show();
        }
    }
}
