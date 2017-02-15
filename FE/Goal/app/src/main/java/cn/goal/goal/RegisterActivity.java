package cn.goal.goal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import cn.goal.goal.services.User;
import org.json.JSONException;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton comeback;
    private Button register;
    private TextView agreement;
    private TextView goto_signin;
    private ImageButton wechat;
    private ImageButton qq;
    private ImageButton weibo;
    private EditText mEmailView;
    private EditText mPasswordView;

    private UserRegisterTask mRegisterTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }
    private  void init()
    {
        comeback=(ImageButton) findViewById(R.id.comeback);
        comeback.setOnClickListener(this);
        register=(Button)findViewById(R.id.register);
        register.setOnClickListener(this);
        agreement= (TextView) findViewById(R.id.agreement);
        agreement.setOnClickListener(this);
        goto_signin=(TextView) findViewById(R.id.go_to_signin);
        goto_signin.setOnClickListener(this);
        wechat=(ImageButton) findViewById(R.id.wechat);
        wechat.setOnClickListener(this);
        qq=(ImageButton) findViewById(R.id.qq);
        qq.setOnClickListener(this);
        weibo=(ImageButton) findViewById(R.id.weibo);
        weibo.setOnClickListener(this);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case  R.id.go_to_signin:
            case R.id.comeback:
                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.register:
                register();
                break;
            case R.id.agreement:
                Intent intent3 = new Intent();
                intent3.setClass(RegisterActivity.this, AgreementActivity.class);
                startActivity(intent3);
                break;
            case  R.id.wechat:
                break;
            case  R.id.qq:
                break;
            case R.id.weibo:
                break;

        }
    }

    private void register() {
        if (mRegisterTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // perform the user register attempt.
            mRegisterTask = new UserRegisterTask(email, password);
            mRegisterTask.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Represents an asynchronous registration task
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, String> {

        private final String mName;
        private final String mPassword;

        UserRegisterTask(String name, String password) {
            mName = name;
            mPassword = password;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                return User.register(mName, mPassword);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "数据解析失败";
        }

        @Override
        protected void onPostExecute(final String err) {
            mRegisterTask = null;

            if (err != null) {
                Toast.makeText(getApplicationContext(), err, 1000).show();
            } else {
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mRegisterTask = null;
        }
    }
}
