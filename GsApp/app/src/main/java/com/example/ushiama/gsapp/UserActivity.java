package com.example.ushiama.gsapp;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.FacebookException;
import com.facebook.widget.LoginButton;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiSocialCallBack;
import com.kii.cloud.storage.callback.KiiUserCallBack;
import com.kii.cloud.storage.exception.CloudExecutionException;
import com.kii.cloud.storage.social.KiiFacebookConnect;
import com.kii.cloud.storage.social.KiiSocialConnect;
import com.kii.cloud.storage.social.connector.KiiSocialNetworkConnector;
import com.kii.cloud.storage.utils.Log;


public class UserActivity extends ActionBarActivity {
    //入力するビューです。
    private EditText mUsernameField;
    private EditText mPasswordField;

    private EditText m2UsernameField;
    private EditText m2PasswordField;

 //   private TextView textView;
 //   private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);

        Kii.initialize("165843843913897", "7cab45dc19458d630f9ecb4a92ffd320", null);

        textView = (TextView) findViewById(R.id.textView);

        LoginButton fbloginBtn = (LoginButton) findViewById(R.id.fblogin_button);
        fbloginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.v("FB", "loginSuccess");
                Bundle options = new Bundle();
                String accessToken = loginResult.getAccessToken().getToken();
                options.putString("accessToken", accessToken);
                options.putParcelable("provider", KiiSocialNetworkConnector.Provider.FACEBOOK);
                KiiSocialNetworkConnector conn = (KiiSocialNetworkConnector) Kii.socialConnect(KiiSocialConnect.SocialNetwork.SOCIALNETWORK_CONNECTOR);
                conn.logIn(UserActivity.this, options, new KiiSocialCallBack() {
                    @Override
                    public void onLoginCompleted(KiiSocialConnect.SocialNetwork network, KiiUser user, Exception exception) {
                        if (exception != null) {
                            textView.setText("Failed to Login to Kii! " + exception
                                    .getMessage());
                            return;
                        }
                        textView.setText("Login to Kii! " + user.getID());
                    }
                });
            }

            @Override
            public void onCancel() {
                Log.v("FB", "Cancelled.");
                textView.setText("Facebook Login has been cancelled.");
            }

            @Override
            public void onError(FacebookException error) {
                Log.v("FB", "loginFailed");
                error.printStackTrace();
                textView.setText("Facebook Login has been failed: " + error.getMessage());
            }
        });
**/

        //自動ログインのため保存されているaccess tokenを読み出す。tokenがあればログインできる
        SharedPreferences pref = getSharedPreferences(getString(R.string.save_data_name), Context.MODE_PRIVATE);
        String token = pref.getString(getString(R.string.save_token), "");//保存されていない時は""
        //tokenがないとき。
        if(token == "") {
            //画面を作る
            CreateMyView(savedInstanceState);
        }else {
            //自動ログインをする。
            try {
                //KiiCloudのAccessTokenによるログイン処理。完了すると結果がcallback関数として実行される。
                KiiUser.loginWithToken(callback, token);
            } catch (Exception e) {
                //ダイアログを表示
                showAlert(R.string.operation_failed, e.getLocalizedMessage(), null);
                //画面を作る
                CreateMyView(savedInstanceState);
            }
        }

    }

    //Viewを作る。いつもonCreateでやっていること
    protected void CreateMyView(Bundle savedInstanceState) {

        setContentView(R.layout.activity_user);
        //EditTextのビューを探します
        mUsernameField = (EditText) findViewById(R.id.username_field);
        mPasswordField = (EditText) findViewById(R.id.password_field);

        m2UsernameField = (EditText) findViewById(R.id.e_username_field);
        m2PasswordField = (EditText) findViewById(R.id.e_password_field);
        //パスワードを隠す設定
        mPasswordField.setTransformationMethod(new PasswordTransformationMethod());
        m2PasswordField.setTransformationMethod(new PasswordTransformationMethod());
        //パスワードの入力文字を制限する。参考：http://techbooster.jpn.org/andriod/ui/3857/
        mPasswordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        m2PasswordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //登録ボタン
        Button signupBtn = (Button) findViewById(R.id.signup_button);
        //ログインボタン
        Button loginBtn = (Button) findViewById(R.id.login_button);
        //facebook ログインボタン
        Button fbloginBtn = (Button) findViewById(R.id.fblogin_button);
        //ログインボタンをクリックした時の処理を設定
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ログイン処理
                onLoginButtonClicked(v);
            }
        });

        //facebook ログインボタンをクリックした時の処理を設定
/*        fbloginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //facebook ログイン処理
                onFbLoginButtonClicked(v);
            }
        });

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        KiiSocialConnect connect = (KiiFacebookConnect) Kii.socialConnect(KiiSocialConnect.SocialNetwork.FACEBOOK);
        connect.initialize("165843843913897", "7cab45dc19458d630f9ecb4a92ffd320", null);

*/

        //登録ボタンをクリックした時の処理を設定
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登録処理
                onSignupButtonClicked(v);
            }
        });
    }
    //ログイン処理：参考　http://documentation.kii.com/ja/guides/android/managing-users/sign-in/
    public void onLoginButtonClicked(View v) {
        //IMEを閉じる
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        //入力文字を得る
        String username = mUsernameField.getText().toString();
        String password = mPasswordField.getText().toString();
        try {
            //KiiCloudのログイン処理。完了すると結果がcallback関数として実行される。
            KiiUser.logIn(callback, username, password);
        } catch (Exception e) {
            //ダイアログを表示
            showAlert(R.string.operation_failed, e.getLocalizedMessage(), null);
        }
    }
    //facebookログイン処理
    /*
    public void onFbLoginButtonClicked(View v) {
        KiiSocialConnect connect = (KiiFacebookConnect) Kii.socialConnect(KiiSocialConnect.SocialNetwork.FACEBOOK);
        connect.initialize("165843843913897", "7cab45dc19458d630f9ecb4a92ffd320", null);
        Log.v("FB", "loginSuccess");
        Bundle options = new Bundle();
        Bundle b = connect.getAccessTokenBundle();
        //options.putParcelable(KiiSocialNetworkConnector.PROVIDER, KiiSocialNetworkConnector.Provider.FACEBOOK);

        String accessToken = session.getAccessToken();
        String accessToken = loginResult.getAccessToken().getToken();
        options.putString("accessToken", accessToken);
        options.putParcelable("provider", KiiSocialNetworkConnector.Provider.FACEBOOK);
        KiiSocialNetworkConnector connect = (KiiSocialNetworkConnector) Kii.socialConnect(KiiSocialConnect.SocialNetwork.SOCIALNETWORK_CONNECTOR);

        // Login.
        connect.logIn(UserActivity.this, options, new KiiSocialCallBack() {
            @Override
            public void onLoginCompleted(KiiSocialConnect.SocialNetwork network, KiiUser user, Exception exception) {
                if (exception != null) {
                    // Error handling
                    showAlert(R.string.operation_failed, exception.getLocalizedMessage(), null);
                    return;
                }
            }
        });
    }
    */

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Kii.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Kii.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == KiiSocialNetworkConnector.REQUEST_CODE) {
            Kii.socialConnect(KiiSocialConnect.SocialNetwork.SOCIALNETWORK_CONNECTOR).respondAuthOnActivityResult(
                    requestCode,
                    resultCode,
                    data);
        }
     //   callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    //ダイアログを表示する
    void showAlert(int titleId, String message, AlertDialogFragment.AlertDialogListener listener ) {
        DialogFragment newFragment = AlertDialogFragment.newInstance(titleId, message, listener);
        newFragment.show(getFragmentManager(), "dialog");
    }
    //登録処理
    public void onSignupButtonClicked(View v) {
        //IMEを閉じる
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        //入力文字を得る
        String username = m2UsernameField.getText().toString();
        String password = m2PasswordField.getText().toString();
        try {
            //KiiCloudのユーザ登録処理
            KiiUser user = KiiUser.createWithUsername(username);
            user.register(callback, password);
        } catch (Exception e) {
            showAlert(R.string.operation_failed, e.getLocalizedMessage(), null);
        }
    }
    //新規登録、ログインの時に呼び出されるコールバック関数
    KiiUserCallBack callback = new KiiUserCallBack() {
        //ログインが完了した時に自動的に呼び出される。自動ログインの時も呼び出される
        @Override
        public void onLoginCompleted(int token, KiiUser user, Exception e) {
            // setFragmentProgress(View.INVISIBLE);
            if (e == null) {
                //自動ログインのためにSharedPreferenceに保存。アプリのストレージ。参考：http://qiita.com/Yuki_Yamada/items/f8ea90a7538234add288
                SharedPreferences pref = getSharedPreferences(getString(R.string.save_data_name), Context.MODE_PRIVATE);
                pref.edit().putString(getString(R.string.save_token), user.getAccessToken()).apply();

                // Intent のインスタンスを取得する。getApplicationContext()で自分のコンテキストを取得。遷移先のアクティビティーを.classで指定
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                // 遷移先の画面を呼び出す
                startActivity(intent);
                //戻れないようにActivityを終了します。
                finish();
            } else {
                //eがKiiCloud特有のクラスを継承している時
                if (e instanceof CloudExecutionException)
                    //KiiCloud特有のエラーメッセージを表示。フォーマットが違う
                    showAlert(R.string.operation_failed, Util.generateAlertMessage((CloudExecutionException) e), null);
                else
                    //一般的なエラーを表示
                    showAlert(R.string.operation_failed, e.getLocalizedMessage(), null);
            }
        }
        //新規登録の時に自動的に呼び出される
        @Override
        public void onRegisterCompleted(int token, KiiUser user, Exception e) {
            if (e == null) {
                //自動ログインのためにSharedPreferenceに保存。アプリのストレージ。参考：http://qiita.com/Yuki_Yamada/items/f8ea90a7538234add288
                SharedPreferences pref = getSharedPreferences(getString(R.string.save_data_name), Context.MODE_PRIVATE);
                pref.edit().putString(getString(R.string.save_token), user.getAccessToken()).apply();

                // Intent のインスタンスを取得する。getApplicationContext()で自分のコンテキストを取得。遷移先のアクティビティーを.classで指定
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                // 遷移先の画面を呼び出す
                startActivity(intent);
                //戻れないようにActivityを終了します。
                finish();
            } else {
                //eがKiiCloud特有のクラスを継承している時
                if (e instanceof CloudExecutionException)
                    //KiiCloud特有のエラーメッセージを表示
                    showAlert(R.string.operation_failed, Util.generateAlertMessage((CloudExecutionException) e), null);
                else
                    //一般的なエラーを表示
                    showAlert(R.string.operation_failed, e.getLocalizedMessage(), null);
            }
        }
    };

    //GrowthHackで追加ここから
    @Override
    protected void onStart() {
        super.onStart();
        Tracker t = ((VolleyApplication)getApplication()).getTracker(VolleyApplication.TrackerName.APP_TRACKER);
        t.setScreenName(this.getClass().getSimpleName());
        t.send(new HitBuilders.AppViewBuilder().build());
    }
    //GrowthHackで追加ここまで

    //メニュー関係：未使用
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
