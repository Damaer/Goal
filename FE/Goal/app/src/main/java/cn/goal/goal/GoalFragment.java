package cn.goal.goal;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import java.util.ArrayList;
import java.util.List;

public class GoalFragment extends Fragment implements RecyclerAdapter.OnItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    public RecyclerAdapter adapter;
    public View view;
    public GoalFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_goal, null);
        initView();
        return view;
    }
    public void initView(){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.goallistview);
        //

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置布局管理器
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
         adapter = new RecyclerAdapter(this.getContext(), getData());
        mRecyclerView.setAdapter(adapter);
        //
        ItemTouchHelper helper = new ItemTouchHelper(new MyItemTouchCallback(adapter));
        helper.attachToRecyclerView(mRecyclerView);
        startAnimation(R.animator.scale);
        adapter.setOnItemClickListener(this);
    }
    public List<Testgoal> getData() {
        List<Testgoal> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Testgoal model = new Testgoal();
            data.add(model);
        }
        return data;
    }
    public void onItemClick(int position, Testgoal model) {
        Log.e(TAG, "onItemClick: " + position);
    }
    /**
     * 开启动画
     */
    private void startAnimation(int anim) {

        LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(this.getContext(),anim));
        lac.setOrder(LayoutAnimationController.ORDER_RANDOM);
        mRecyclerView.setLayoutAnimation(lac);
        mRecyclerView.startLayoutAnimation();
    }

}
