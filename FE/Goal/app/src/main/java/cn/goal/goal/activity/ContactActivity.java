package cn.goal.goal.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.util.ArrayList;

import cn.goal.goal.dialog.LoadingDialog;
import cn.goal.goal.adapter.ContactAdapter;
import cn.goal.goal.R;
import cn.goal.goal.services.MessageSerivce;
import cn.goal.goal.services.object.Message;
import cn.goal.goal.services.object.User;
import cn.goal.goal.utils.Meta;
import cn.goal.goal.utils.NetWorkUtils;

public class ContactActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener,AdapterView.OnItemClickListener, ContactAdapter.Callback {
    private ListView listView;
    private ArrayList<Message> list;
    private ImageButton comeback;
    private TextView hide_down;
    private Button send_comment;
    private RelativeLayout rl_comment;
    private EditText content_of_reply;

    private User receiver; // 标记接收消息的人
    private Message willDeleteMessage; // 标记要删除的消息
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        rl_comment = (RelativeLayout) findViewById(R.id.rl_comment);
        send_comment= (Button) findViewById(R.id.comment_send_message);
        send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 发送消息
                if (NetWorkUtils.isNetworkConnected(ContactActivity.this)) {
                    new SendMessageTask(receiver, content_of_reply.getText().toString()).execute();
                } else {
                    Toast.makeText(ContactActivity.this, "当前环境无网络连接", Toast.LENGTH_SHORT).show();
                }
            }
        });
        content_of_reply= (EditText) findViewById(R.id.comment_content);
        hide_down= (TextView) findViewById(R.id.hide_down);
        hide_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_comment.setVisibility(View.GONE);
                // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
                InputMethodManager im = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(content_of_reply.getWindowToken(), 0);
            }
        });

        comeback= (ImageButton) findViewById(R.id.comeback_from_contact);
        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 获取所需目标信息
        if (NetWorkUtils.isNetworkConnected(this)) {
            new FecthMessageTask().execute();
        } else {
            Toast.makeText(this, "当前环境无网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 响应ListView中item的点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
//        Intent intent=new Intent(this,DetailOfRecordActivity.class);
//        startActivity(intent);
    }

    /**
     * 接口方法，响应ListView按钮点击事件
     */
    public void click(View v) {
        User user = list.get((int)v.getTag()).getSender();
        switch (v.getId()) {
            case R.id.user_head_photo_for_contact:
            case R.id.username_for_contact:
                Meta.otherUser = user;
                Intent intent=new Intent(this,EveryUserActivity.class);
                startActivity(intent);
                break;
            case R.id.content_of_contact:
                receiver = user;
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                rl_comment.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        willDeleteMessage = list.get(i);
        String username = willDeleteMessage.getSender().getUsername();
        content_of_reply.setHint("回复"+username+"内容");
        showDialog_delete();
        return true;
    }
    private void showDialog_delete() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setMessage("删除该信息？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            if (NetWorkUtils.isNetworkConnected(ContactActivity.this)) {
                new DeleteMessageTask().execute();
            } else {
                Toast.makeText(ContactActivity.this, "当前环境无网络连接", Toast.LENGTH_SHORT).show();
            }
            }
        });
        builder.show();
    }

    private void render(ArrayList<Message> messageArrayList) {
        list = messageArrayList;

        listView = (ListView)findViewById(R.id.list_for_contact_mes);
        listView.setOnItemLongClickListener(this);
        ContactAdapter myadater=new ContactAdapter(this,messageArrayList,this);
        listView.setAdapter(myadater);
    }

    class FecthMessageTask extends AsyncTask<Void, Void, ArrayList<Message>> {
        LoadingDialog mLoadingDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(ContactActivity.this);
        }

        @Override
        protected ArrayList<Message> doInBackground(Void... params) {
            return MessageSerivce.getMessages();
        }

        @Override
        protected void onPostExecute(ArrayList<Message> messageArrayList) {
            super.onPostExecute(messageArrayList);
            cancelDialog();
            if (messageArrayList != null) {
                render(messageArrayList);
            } else {
                Toast.makeText(ContactActivity.this, "获取消息失败", Toast.LENGTH_SHORT).show();
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

    class SendMessageTask extends AsyncTask<Void, Void, String> {
        LoadingDialog mLoadingDialog;
        private User user;
        private String content;

        public SendMessageTask(User user, String content) {
            super();
            this.user = user;
            this.content = content;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(ContactActivity.this);
            rl_comment.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(Void... params) {
            return MessageSerivce.sendMessage(user,  content);
        }

        @Override
        protected void onPostExecute(String err) {
            super.onPostExecute(err);
            cancelDialog();
            if (err != null) {
                Toast.makeText(ContactActivity.this, err, Toast.LENGTH_SHORT).show();
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                rl_comment.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(ContactActivity.this, "发送消息成功", Toast.LENGTH_SHORT).show();
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

    class DeleteMessageTask extends AsyncTask<Void, Void, String> {
        LoadingDialog mLoadingDialog;

        public DeleteMessageTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(ContactActivity.this);
        }

        @Override
        protected String doInBackground(Void... params) {
            return MessageSerivce.deleteMessage(willDeleteMessage);
        }

        @Override
        protected void onPostExecute(String err) {
            super.onPostExecute(err);
            cancelDialog();
            if (err != null) {
                Toast.makeText(ContactActivity.this, err, Toast.LENGTH_SHORT).show();
            } else {
                // 删除消息成功,刷新数据
                new FecthMessageTask().execute();
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
