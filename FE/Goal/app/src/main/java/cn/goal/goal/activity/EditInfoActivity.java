package cn.goal.goal.activity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import cn.goal.goal.dialog.LoadingDialog;
import cn.goal.goal.R;
import cn.goal.goal.services.UserService;
import cn.goal.goal.services.object.GetBitmapInterface;
import cn.goal.goal.services.object.User;
import cn.goal.goal.utils.NetWorkUtils;
import cn.goal.goal.utils.RoundCorner;
import cn.goal.goal.utils.Util;

import java.io.FileNotFoundException;

/**
 * Created by chenlin on 12/02/2017.
 */
public class EditInfoActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 2;
    public static final int PICK_PICTURE = 3;

    private ImageButton close;
    private ImageButton confirm;

    private ImageButton avatar;
    private EditText username;
    private EditText description;
    private TextView email;
    private TextView phone;
    private Button editEmail;
    private Button editPhone;

    private View dialogView;
    private TextInputLayout inputLayout;
    private EditText input;

    private User user;
    private String newAvatar; // 用户选择头像文件路径

    AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        user = UserService.getUserInfo();

        close = (ImageButton) findViewById(R.id.button_close);
        confirm = (ImageButton) findViewById(R.id.button_confirm);
        avatar = (ImageButton) findViewById(R.id.avatar);
        username = (EditText) findViewById(R.id.username);
        description = (EditText) findViewById(R.id.description);
        email = (TextView) findViewById(R.id.email);
        phone = (TextView) findViewById(R.id.phone);
        editEmail = (Button) findViewById(R.id.edit_email);
        editPhone = (Button) findViewById(R.id.edit_phone);

        renderInitialData();
        addListener();
    }

    private void renderInitialData() {
        // 获取头像
        user.setAvatarInterface(new GetBitmapInterface() {
            @Override
            public void getImg(Bitmap avatarBitmap) {
                avatar.setImageBitmap(avatarBitmap);
            }
            @Override
            public void error(String errorInfo) {
                Toast.makeText(EditInfoActivity.this, "获取头像失败", Toast.LENGTH_SHORT);
            }
        });
        user.getAvatarBitmap(EditInfoActivity.this);

        username.setText(user.getUsername());
        description.setText(user.getDescription());
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
    }

    private void addListener() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = username.getText().toString();
                String newDescription = description.getText().toString();
                if (newDescription.equals(user.getDescription()) && newUsername.equals(user.getUsername()) && newAvatar == null) {
                    // 用户信息为改变则直接finish
                    finish();
                    return ;
                }
                if (NetWorkUtils.isNetworkConnected(EditInfoActivity.this)) {
                    new UpdateUserInfoTask(newUsername, newDescription).execute();
                } else {
                    Toast.makeText(EditInfoActivity.this, "保存失败,当前网络不可用", Toast.LENGTH_SHORT);
                }
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 选择新头像
                Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
                intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
                intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
                startActivityForResult(intent, PICK_PICTURE);
            }
        });

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleEditEmail();
            }
        });

        editPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleEditPhone();
            }
        });
    }

    private void createDialogView() {
        dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_view, null);
        inputLayout = (TextInputLayout) dialogView.findViewById(R.id.input_layout);
        input = (EditText) dialogView.findViewById(R.id.new_value);
    }

    private void handleEditEmail() {
        createDialogView();
        inputLayout.setHint(getResources().getString(R.string.new_email));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle(R.string.edit_email)
                .setPositiveButton(R.string.confirm, null)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog = builder.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = input.getText().toString();
                if (!Util.checkEmailFormate(email)) {
                    input.setError("格式不正确");
                    return ;
                }
                dialog.dismiss();
                if (NetWorkUtils.isNetworkConnected(EditInfoActivity.this)) {
                    new UpdateEmailTask(email).execute();
                } else {
                    Toast.makeText(EditInfoActivity.this, "当前环境无网络连接", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handleEditPhone() {
        createDialogView();
        inputLayout.setHint(getResources().getString(R.string.new_phone));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle(R.string.edit_phone)
                .setPositiveButton(R.string.confirm, null)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog = builder.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = input.getText().toString();
                if (!Util.checkPhoneFormate(phone)) {
                    input.setError("格式不正确");
                    return ;
                }
                dialog.dismiss();
                if (NetWorkUtils.isNetworkConnected(EditInfoActivity.this)) {
                    new UpdatePhoneTask(phone).execute();
                } else {
                    Toast.makeText(EditInfoActivity.this, "当前环境无网络连接", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            ContentResolver cr = getContentResolver();
            try {
                Bitmap image = BitmapFactory.decodeStream(cr.openInputStream(uri));
                avatar.setImageBitmap(RoundCorner.toCircle(image));
                newAvatar = Util.getRealFilePath(this, uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class UpdateUserInfoTask extends AsyncTask<Void, Void, String> {
        private LoadingDialog dialog;
        private String username;
        private String description;
        private String avatarUrl = null;

        public UpdateUserInfoTask(String username, String description) {
            super();
            this.username = username;
            this.description = description;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new LoadingDialog().showLoading(EditInfoActivity.this);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.closeDialog();
            if (s != null) { // 发生错误
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditInfoActivity.this);
                builder.setMessage("更新信息失败，是否重新尝试?");
                builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        new UpdateUserInfoTask(username, description).execute();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            } else {
                finish();
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            ContentResolver cr = getContentResolver();
            if (newAvatar != null) {
                // 用户选择了新头像则先上传新头像
                avatarUrl = UserService.uploadAvatar(newAvatar);
                if (avatarUrl == null) {
                    //上传头像失败
                    return "上传头像失败";
                }
            }
            return UserService.updateUserInfo(username, avatarUrl, description);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.closeDialog();
        }
    }

    class UpdateEmailTask extends AsyncTask<Void, Void, String> {
        LoadingDialog mLoadingDialog;
        String newEmail;

        public UpdateEmailTask(String email) {
            super();
            this.newEmail = email;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(EditInfoActivity.this);
        }

        @Override
        protected String doInBackground(Void... params) {
            return UserService.updateEmail(newEmail);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cancelDialog();
            if (s != null) {
                Toast.makeText(EditInfoActivity.this, s, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                email.setText(newEmail);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cancelDialog();
        }

        private void cancelDialog() {
            if (mLoadingDialog != null)
                mLoadingDialog.closeDialog();
        }
    }

    class UpdatePhoneTask extends AsyncTask<Void, Void, String> {
        LoadingDialog mLoadingDialog;
        String newPhone;

        public UpdatePhoneTask(String phone) {
            super();
            this.newPhone = phone;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(EditInfoActivity.this);
        }

        @Override
        protected String doInBackground(Void... params) {
            return UserService.updatePhone(newPhone);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cancelDialog();
            if (s != null) {
                Toast.makeText(EditInfoActivity.this, s, Toast.LENGTH_SHORT).show();
            } else {
                phone.setText(newPhone);
                Toast.makeText(EditInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cancelDialog();
        }

        private void cancelDialog() {
            if (mLoadingDialog != null)
                mLoadingDialog.closeDialog();
        }
    }
}
