package taylor.com.selector;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import taylor.com.selector2.Selector;

/**
 * a Selector which shows an heart image on the right top when it is selected
 */
public class OrderSelector extends Selector {
    private TextView tvTitle;
    private ImageView ivIcon;
    private ImageView ivSelector;
    private ValueAnimator alphaAnimator;
    private ValueAnimator scaleAnimator;
    private String text;
    private int iconResId;
    private int indicatorResId;
    private int textColor;
    private int textSize;

    public OrderSelector(Context context) {
        super(context);
    }

    public OrderSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void onBindView(String text, int iconResId, int indicatorResId, int textColor, int textSize) {
        if (tvTitle != null) {
            tvTitle.setText(text);
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            tvTitle.setTextColor(textColor);
        }
        if (ivIcon != null) {
            ivIcon.setImageResource(iconResId);
        }
        if (ivSelector != null) {
            ivSelector.setImageResource(indicatorResId);
            ivSelector.setAlpha(0);
        }
    }

    @Override
    public void onObtainAttrs(TypedArray typedArray) {
        text = typedArray.getString(R.styleable.Selector_text);
        iconResId = typedArray.getResourceId(R.styleable.Selector_img, 0);
        indicatorResId = typedArray.getResourceId(R.styleable.Selector_indicator, 0);
        textColor = typedArray.getColor(R.styleable.Selector_text_color, Color.parseColor("#FF222222"));
        textSize = typedArray.getInteger(R.styleable.Selector_text_size, 15);
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.order_selector, null);
        tvTitle = view.findViewById(R.id.tv_title);
        ivIcon = view.findViewById(R.id.iv_icon);
        ivSelector = view.findViewById(R.id.iv_selector);
        onBindView(text, iconResId, indicatorResId, textColor, textSize);
        return view;
    }

    @Override
    protected void onSwitchSelected(boolean isSelect) {
        if (isSelect) {
            playSelectedAnimation();
        } else {
            playUnselectedAnimation();
        }
    }

    private void playUnselectedAnimation() {
        if (ivSelector == null) {
            return;
        }
        if (alphaAnimator != null) {
            alphaAnimator.reverse();
        }
    }

    private void playSelectedAnimation() {
        if (ivSelector == null) {
            return;
        }
        AnimatorSet set = new AnimatorSet();
        alphaAnimator = ValueAnimator.ofInt(0, 255);
        alphaAnimator.setDuration(200);
        alphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ivSelector.setAlpha((int) animation.getAnimatedValue());
            }
        });

        scaleAnimator = ValueAnimator.ofFloat(1f, 1.3f, 1f);
        scaleAnimator.setDuration(500);
        scaleAnimator.setInterpolator(new AnticipateOvershootInterpolator());
        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ivSelector.setScaleX(((Float) animation.getAnimatedValue()));
                ivSelector.setScaleY(((Float) animation.getAnimatedValue()));
            }
        });
        set.playTogether(alphaAnimator, scaleAnimator);
        set.start();
    }
}
