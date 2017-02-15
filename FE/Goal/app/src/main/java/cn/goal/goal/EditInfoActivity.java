package cn.goal.goal;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import cn.goal.goal.utils.Utils;

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

    private String avatarUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        // 传入数据错误
        if (getIntent() == null || getIntent().getExtras().getBundle("data") == null) {
            Toast.makeText(this, "传入数据错误", 1000).show();
            finish();
            return ;
        }

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
        Bundle bundle = getIntent().getExtras().getBundle("data");
        avatarUrl = bundle.getString("avatar");
        username.setText(bundle.getString("username"));
        description.setText(bundle.getString("description"));
        email.setText(bundle.getString("email"));
        phone.setText(bundle.getString("phone"));
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
                Intent intent = getIntent();
                Bundle bundle = intent.getExtras();
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putString("username", username.getText().toString());
                bundle.putString("avatar", avatarUrl);
                bundle.putString("description", description.getText().toString());
                bundle.putString("email", email.getText().toString());
                bundle.putString("phone", phone.getText().toString());

                intent.putExtra("data", bundle);
                setResult(REQUEST_CODE, intent);
                finish();
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
        dialogView = LayoutInflater.from(this).inflate(R.layout.edit_dialog_view, null);
        inputLayout = (TextInputLayout) dialogView.findViewById(R.id.input_layout);
        input = (EditText) dialogView.findViewById(R.id.new_value);
    }

    private void handleEditEmail() {
        createDialogView();
        inputLayout.setHint(getResources().getString(R.string.new_email));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle(R.string.edit_email)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("新邮箱" + input.getText());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    private void handleEditPhone() {
        createDialogView();
        inputLayout.setHint(getResources().getString(R.string.new_phone));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle(R.string.edit_phone)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("新手机号" + input.getText());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            ContentResolver cr = getContentResolver();
            try {
                Bitmap image = BitmapFactory.decodeStream(cr.openInputStream(uri));
                avatar.setImageBitmap(Utils.toRoundCorner(image));
                // 上传头像到服务器，获取头像Url
                // ...

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
