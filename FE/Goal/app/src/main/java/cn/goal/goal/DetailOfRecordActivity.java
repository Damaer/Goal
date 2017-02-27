package cn.goal.goal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import cn.goal.goal.services.CommentService;
import cn.goal.goal.services.UserService;
import cn.goal.goal.services.object.Comment;
import cn.goal.goal.services.object.GetBitmapInterface;
import cn.goal.goal.utils.MylistView_for_goal_comment;
import cn.goal.goal.utils.NetWorkUtils;
import cn.goal.goal.utils.Util;

import java.util.ArrayList;

public class DetailOfRecordActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,AbsListView.OnScrollListener,MyAdapter_for_every_comment.Callback {
    private MylistView_for_goal_comment listView;
    private ScrollView scrollView;
    private ImageButton comeback;
    private ImageButton likeButton;
    private TextView sumOfLike;
    private ImageView userImage;
    private TextView username;
    private TextView goalname;
    private TextView timeOfSend;
    private TextView hide_down;
    private TextView content_of_record;
    private EditText content_of_send;
    private Button send_comment;
    private RelativeLayout rl_comment;
    private ImageButton reply;

    private ArrayList<Comment> commentArrayList;
    private Comment record;
    private boolean isLike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_of_record);
        if (getIntent() == null || getIntent().getExtras() == null) {
            Toast.makeText(this, "传入数据错误", Toast.LENGTH_SHORT).show();
            finish();
            return ;
        }
        record = MyAdapter_for_every_record.list.get(getIntent().getExtras().getInt("commentIndex"));

        init_listen();

        // 获取所需目标信息
        if (NetWorkUtils.isNetworkConnected(DetailOfRecordActivity.this)) {
            new FetchCommentsTask(record).execute();
        } else {
            Toast.makeText(DetailOfRecordActivity.this, "当前环境无网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    public void init_listen()
    {
        scrollView= (ScrollView) findViewById(R.id.scrollView_for_comment);
        scrollView.smoothScrollTo(0,0);
        comeback= (ImageButton) findViewById(R.id.comeback_from_detail);
        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        userImage= (ImageView) findViewById(R.id.head_photo);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jump_to_user();
            }
        });
        record.getUser().setAvatarInterface(new GetBitmapInterface() {
            @Override
            public void getImg(Bitmap img) {
                userImage.setImageBitmap(img);
            }

            @Override
            public void error(String errorInfo) {
                Toast.makeText(DetailOfRecordActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
            }
        });
        record.getUser().getAvatarBitmap(this);
        username= (TextView) findViewById(R.id.user_name);
        username.setText(record.getUser().getUsername());
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jump_to_user();
            }
        });
        goalname= (TextView) findViewById(R.id.goal_name_for_recorddetail);
        goalname.setText(record.goal.getTitle());
        goalname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jump_to_goal();
            }
        });
        timeOfSend = (TextView) findViewById(R.id.time_of_send);
        timeOfSend.setText(Util.dateToString(record.getCreateAt()));
        likeButton = (ImageButton) findViewById(R.id.like);
        sumOfLike = (TextView) findViewById(R.id.sum_of_like);
        sumOfLike.setText(String.valueOf(record.getLike().size()));
        isLike = record.getLike().contains(UserService.getUserInfo().get_id());
        likeButton.setBackgroundResource(isLike ? R.mipmap.liked : R.mipmap.like);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点赞
                if (NetWorkUtils.isNetworkConnected(DetailOfRecordActivity.this)) {
                    new MarkLikeTask().execute();
                } else {
                    Toast.makeText(DetailOfRecordActivity.this, "当前环境无网络连接", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sumOfLike = (TextView) findViewById(R.id.sum_of_like);
        rl_comment = (RelativeLayout) findViewById(R.id.rl_comment);
        reply= (ImageButton) findViewById(R.id.reply);
        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.smoothScrollTo(0,0);
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                rl_comment.setVisibility(View.VISIBLE);
            }
        });

        content_of_send= (EditText) findViewById(R.id.comment_content);
        content_of_record = (TextView) findViewById(R.id.content_of_record);
        content_of_record.setText(record.getContent());

        send_comment= (Button) findViewById(R.id.comment_send_message);
        send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 评论
                if (NetWorkUtils.isNetworkConnected(DetailOfRecordActivity.this)) {
                    new CommentTask(content_of_send.getText().toString()).execute();
                } else {
                    Toast.makeText(DetailOfRecordActivity.this, "当前环境无网络连接", Toast.LENGTH_SHORT).show();
                }
            }
        });
        hide_down= (TextView) findViewById(R.id.hide_down);
        hide_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_comment.setVisibility(View.GONE);
                // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
                InputMethodManager im = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(content_of_send.getWindowToken(), 0);
            }
        });

    }
    public void jump_to_user()
    {
        Intent intent=new Intent(this,EveryUserActivity.class);
        startActivity(intent);
    }
    public void jump_to_goal()
    {
        Intent intent4=new Intent(this,EveryGoalActivity.class);
        startActivity(intent4);
    }
    @Override
    public void onScrollStateChanged(AbsListView absListView, int position) {
        switch (position)
        {
            case SCROLL_STATE_FLING:
                //下拉添加数据源
                break;
            default:
                break;
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int position, int i1, int i2) {

    }

    /**
     * 响应ListView中item的点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
        Toast.makeText(this, "listview的item被点击了！，点击的位置是-->" + position,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * 接口方法，响应ListView按钮点击事件
     */
    public void click(View v) {
        switch (v.getId())
        {
            case R.id.commentuser_photo:
            case R.id.user_reply:
            case R.id.user_replyed:
                jump_to_user();
                break;
            case R.id.content_of_comment:
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                rl_comment.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    public void renderCommends(ArrayList<Comment> comments) {
        commentArrayList = comments;
        // 给所有comment添加上goal信息
        for (int i = 0; i < comments.size(); ++i) {
            comments.get(i).goal = record.goal;
        }
        listView= (MylistView_for_goal_comment) findViewById(R.id.listview_for_comment);
        MyAdapter_for_every_comment myadater=new MyAdapter_for_every_comment(this,comments,this, record);
        listView.setAdapter(myadater);
    }

    class FetchCommentsTask extends AsyncTask<Void, Void, ArrayList<Comment>> {
        private LoadingDialog mLoadingDialog;
        private Comment comment;

        public FetchCommentsTask(Comment comment) {
            super();
            this.comment = comment;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(DetailOfRecordActivity.this);
        }

        @Override
        protected ArrayList<Comment> doInBackground(Void... params) {
            return CommentService.getReplyOfComment(comment);
        }

        @Override
        protected void onPostExecute(ArrayList<Comment> s) {
            super.onPostExecute(s);
            cancelDialog();
            if (s == null) { // 获取评论失败
                Toast.makeText(DetailOfRecordActivity.this, "获取回复列表失败", Toast.LENGTH_SHORT).show();
            } else {
                renderCommends(s);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cancelDialog();
        }

        private void cancelDialog() {
            if (mLoadingDialog != null) {
                mLoadingDialog.closeDialog();
            }
        }
    }

    class MarkLikeTask extends AsyncTask<Void, Void, String> {
        private LoadingDialog mLoadingDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(DetailOfRecordActivity.this);
        }

        @Override
        protected String doInBackground(Void... params) {
            return isLike ? CommentService.unlike(record) : CommentService.like(record);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cancelDialog();
            if (s != null) { // like失败
                Toast.makeText(DetailOfRecordActivity.this, s, Toast.LENGTH_SHORT).show();
            } else {
                isLike = !isLike;
                Toast.makeText(DetailOfRecordActivity.this, (isLike ? "" : "取消") + "点赞成功", Toast.LENGTH_SHORT).show();
                // 添加下面这句会导致视图不刷新
//                sumOfLikeViews[index].setText(
//                        String.valueOf(
//                                (Integer.valueOf(sumOfLikeViews[index].getText().toString()) + (like ? 1 : -1))
//                        )
//                );
                likeButton.setBackgroundResource(isLike ? R.mipmap.liked : R.mipmap.like);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cancelDialog();
        }

        private void cancelDialog() {
            if (mLoadingDialog != null) {
                mLoadingDialog.closeDialog();
            }
        }
    }

    class CommentTask extends AsyncTask<Void, Void, String> {
        private LoadingDialog mLoadingDialog;
        private String content;

        public CommentTask(String content) {
            super();
            this.content = content;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(DetailOfRecordActivity.this);
        }

        @Override
        protected String doInBackground(Void... params) {
            return CommentService.reply(record, content);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cancelDialog();
            if (s != null) { // 添加失败
                Toast.makeText(DetailOfRecordActivity.this, "回复失败，请重新尝试", Toast.LENGTH_SHORT).show();
            } else {
                new FetchCommentsTask(record).execute();
                rl_comment.setVisibility(View.GONE);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cancelDialog();
        }

        private void cancelDialog() {
            if (mLoadingDialog != null) {
                mLoadingDialog.closeDialog();
            }
        }
    }
}
