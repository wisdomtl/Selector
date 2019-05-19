package taylor.com.selector2;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * it is a customized view acts like a checkbox.
 * it can be selected or unselected, the background will change accordingly. wrapping this business logic into a single view for clean code in Fragment
 */
public abstract class Selector extends FrameLayout implements View.OnClickListener {
    /**
     * the unique tag for a selector
     */
    private String tag;
    /**
     * the group which this Selector belongs to
     */
    private SelectorGroup selectorGroup;

    public Selector(Context context) {
        super(context);
        initView(context, null);
    }

    public Selector(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public Selector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        //read declared attributes
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Selector);
            int tagResId = typedArray.getResourceId(R.styleable.Selector_tag, 0);
            tag = context.getString(tagResId);
            onObtainAttrs(typedArray);
            typedArray.recycle();
        } else {
            tag = "default tag";
        }
        //inflate views
        View view = onCreateView();
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(view, params);
        this.setOnClickListener(this);

    }

    public void onObtainAttrs(TypedArray typedArray) {
    }

    /**
     * add this Selector into a SelectorGroup
     *
     * @param selectorGroup
     * @return
     */
    public Selector setGroup(SelectorGroup selectorGroup) {
        this.selectorGroup = selectorGroup;
        selectorGroup.addSelector(this);
        return this;
    }

    /**
     * design how the selector looks like
     *
     * @return
     */
    protected abstract View onCreateView();

    public String getSelectorTag() {
        return tag;
    }

    @Override
    public void setSelected(boolean selected) {
        boolean isPreSelected = isSelected();
        super.setSelected(selected);
        if (isPreSelected != selected) {
            onSwitchSelected(selected);
        }
    }

    @Override
    public void onClick(View v) {
        if (selectorGroup != null) {
            selectorGroup.onSelectorClick(this);
        }
    }

    /**
     * it will be invoked when select state changes
     *
     * @param isSelect
     */
    protected abstract void onSwitchSelected(boolean isSelect);

    @Override
    public int hashCode() {
        return this.tag.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Selector) {
            return ((Selector) obj).tag.equals(this.tag);
        }
        return false;
    }
}
