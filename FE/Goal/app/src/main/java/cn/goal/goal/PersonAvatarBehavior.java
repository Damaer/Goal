package cn.goal.goal;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by chenlin on 14/02/2017.
 */
public class PersonAvatarBehavior extends CoordinatorLayout.Behavior<ImageButton> {
    public PersonAvatarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageButton child, View dependency) {
        System.out.println(dependency.getAccessibilityClassName());
//        System.out.println("layout depends on");
        return false;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageButton child, View dependency) {
        child.setScaleX(0.8f);
        child.setScaleY(0.8f);

        return true;
    }
}
