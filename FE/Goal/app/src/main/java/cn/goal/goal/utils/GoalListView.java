package cn.goal.goal.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * Created by 97617 on 2017/2/24.
 */

public class GoalListView extends ListView {
    /**
     * 为了解决goal页面每一条记录都能显示出来，写的Mylistview类，解决Scrollview 嵌套listview的问题
     * @param context
     * @param attrs
     * @param defStyle
     */
    public GoalListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public GoalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public GoalListView(Context context) {
        super(context);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}