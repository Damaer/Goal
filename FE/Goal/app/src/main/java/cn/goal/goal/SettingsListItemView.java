package cn.goal.goal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by chenlin on 03/05/2017.
 */
public class SettingsListItemView extends RelativeLayout {
    private TextView mTitleView;
    private TextView mSubTitleView;
    private TextView mActionView;
    private LinearLayout mBottomLine;

    public SettingsListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_setting_list_item, this, true);
        mTitleView = (TextView) findViewById(R.id.title);
        mSubTitleView = (TextView) findViewById(R.id.subTitle);
        mActionView = (TextView) findViewById(R.id.action);
        mBottomLine = (LinearLayout) findViewById(R.id.bottomLine);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SettingsListItemView);
        if (attributes != null) {
            // 设置标题
            String title = attributes.getString(R.styleable.SettingsListItemView_title);
            if (TextUtils.isEmpty(title)) {
                mTitleView.setVisibility(GONE);
            } else {
                mTitleView.setText(TextUtils.isEmpty(title) ? "" : title);
                mTitleView.setTextColor(attributes.getInteger(R.styleable.SettingsListItemView_titleColor, Color.BLACK));
            }
            // 设置小标题
            String subTitle = attributes.getString(R.styleable.SettingsListItemView_subTitle);
            if (TextUtils.isEmpty(subTitle)) {
                // 小标题内容为空，则不显示小标题，并设置标题居中显示
                mSubTitleView.setVisibility(GONE);

                LayoutParams layoutParams = (LayoutParams) mTitleView.getLayoutParams();
                layoutParams.addRule(CENTER_VERTICAL);
                mTitleView.setLayoutParams(layoutParams);
            } else {
                mSubTitleView.setText(subTitle);
                mSubTitleView.setTextColor(attributes.getInteger(R.styleable.SettingsListItemView_subTitleColor, R.color.lintgray));
            }
            // 设置action
            String actionTitle = attributes.getString(R.styleable.SettingsListItemView_actionTitle);
            if (TextUtils.isEmpty(actionTitle)) {
                mActionView.setVisibility(GONE);
            } else {
                // 标题均为空，则将action居中显示
                LayoutParams layoutParams = (LayoutParams) mActionView.getLayoutParams();
                if (mTitleView.getVisibility() == GONE && mSubTitleView.getVisibility() == GONE) {
                    layoutParams.addRule(CENTER_IN_PARENT);
                } else {
                    layoutParams.addRule(CENTER_VERTICAL);
                    layoutParams.addRule(ALIGN_PARENT_END);
                }
                mActionView.setLayoutParams(layoutParams);
                mActionView.setText(actionTitle);
                mActionView.setTextColor(attributes.getInteger(R.styleable.SettingsListItemView_actionColor, R.color.purple));
            }
            // 设置底部分割线
            if (!attributes.getBoolean(R.styleable.SettingsListItemView_bottomLine, true)) {
                // 隐藏分割线
                mBottomLine.setVisibility(INVISIBLE);
            }
        }

        // 设置 ripple 效果
        if (Build.VERSION.SDK_INT >= 21) {
            this.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ripple_bg));
        }
    }

    /**
     * 设置 action 监听器
     * @param onClickListener
     */
    public void setActionClickListener(OnClickListener onClickListener) {
        if (onClickListener != null) {
            mActionView.setOnClickListener(onClickListener);
        }
    }

    /**
     * 设置 subTitle 文本
     * @param subTitle
     */
    public void setMSubTitle(String subTitle) {
        mSubTitleView.setText(subTitle);
    }

    /**
     * 设置 title 文本
     * @param title
     */
    public void setMTitle(String title){
        mTitleView.setText(title);
    }

    /**
     * 设置 action 文本
     * @param title
     */
    public void setActionTitle(String title) {
        mActionView.setText(title);
    }
}
