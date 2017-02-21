package cn.goal.goal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import cn.goal.goal.services.UserService;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private TextView register;
    private TextView forgetPassword;
    private ImageButton wechat;
    private ImageButton qq;
    private ImageButton weibo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.email_sign_in_button || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        register= (TextView) findViewById(R.id.register);
        register.setOnClickListener(new OnClickListener() {
            @Override
            /**
             * 注册新账户匿名内部类监听事件
             */
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        forgetPassword= (TextView) findViewById(R.id.forget_password);
        forgetPassword.setOnClickListener(new OnClickListener() {
            @Override
            /**
             * 忘记密码监听事件
             */
            public void onClick(View view) {

            }
        });
        wechat=(ImageButton) findViewById(R.id.wechat);
        wechat.setOnClickListener(new OnClickListener() {
            @Override
            /**
             *微信登陆
             */
            public void onClick(View view) {

            }
        });
        qq=(ImageButton) findViewById(R.id.qq);
        qq.setOnClickListener(new OnClickListener() {
            @Override
            /**
             *qq登陆
             */
            public void onClick(View view) {

            }
        });
        weibo=(ImageButton) findViewById(R.id.weibo);
        weibo.setOnClickListener(new OnClickListener() {
            @Override
            /**
             *微博登陆
             */
            public void onClick(View view) {

            }
        });
    }

    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
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
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mEmail;
        private final String mPassword;
        private LoadingDialog mLoadingDialog;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            mLoadingDialog = new LoadingDialog().showLoading(LoginActivity.this);
        }

        @Override
        protected String doInBackground(Void... params) {
            String err = UserService.login(mEmail, mPassword);
            if (err != null) return err;
            // TODO 获取用户所有信息
            if (UserService.getUserInfo() == null) {
                return "获取用户信息失败";
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String err) {
            mAuthTask = null;

            if (mLoadingDialog != null) {
                mLoadingDialog.closeDialog();
            }

            if (err != null) {
                Toast.makeText(getApplicationContext(), err, Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(getApplicationContext(), BaseActivity.class));
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            if (mLoadingDialog != null) {
                mLoadingDialog.closeDialog();
            }
        }
    }
}