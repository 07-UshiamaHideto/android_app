package com.example.ushiama.gsapp;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiUserCallBack;
import com.kii.cloud.storage.exception.CloudExecutionException;

public class SettingActivity extends AppCompatActivity {

    private EditText oldPasswordField;
    private EditText newPasswordField;
    private EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Userで追加ここから
        //KiiCloudでのログイン状態を取得します。nullの時はログインしていない。
        KiiUser user = KiiUser.getCurrentUser();
        //自動ログインのため保存されているaccess tokenを読み出す。tokenがあればログインできる
        SharedPreferences pref = getSharedPreferences(getString(R.string.save_data_name), Context.MODE_PRIVATE);
        String token = pref.getString(getString(R.string.save_token), "");//保存されていない時は""
        //ログインしていない時はログインのactivityに遷移.SharedPreferencesが空の時もチェックしないとLogOutできない。
        if (user == null || token == "") {
            // Intent のインスタンスを取得する。getApplicationContext()でViewの自分のアクティビティーのコンテキストを取得。遷移先のアクティビティーを.classで指定
            Intent intent = new Intent(getApplicationContext(), UserActivity.class);
            // 遷移先の画面を呼び出す
            startActivity(intent);
            //戻れないようにActivityを終了します。
            finish();
        }
        //Userで追加ここまで

        setContentView(R.layout.activity_setting);

    }

    //ダイアログを表示する
    void showAlert(int titleId, String message, AlertDialogFragment.AlertDialogListener listener ) {
        DialogFragment newFragment = AlertDialogFragment.newInstance(titleId, message, listener);
        newFragment.show(getFragmentManager(), "dialog");
    }

    //Viewを作る。いつもonCreateでやっていること
    protected void CreateMyView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
        //EditTextのビューを探します
        newPasswordField = (EditText) findViewById(R.id.oldpassword_field);
        oldPasswordField = (EditText) findViewById(R.id.newpassword_field);
        //パスワードを隠す設定
        newPasswordField.setTransformationMethod(new PasswordTransformationMethod());
        oldPasswordField.setTransformationMethod(new PasswordTransformationMethod());
        //パスワードの入力文字を制限する。参考：http://techbooster.jpn.org/andriod/ui/3857/
        newPasswordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        oldPasswordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //変更ボタン
        Button changeBtn = (Button) findViewById(R.id.change_button);
        //ボタンをクリックした時の処理を設定
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //変更処理
                onChangeButtonClicked(v);
            }
        });
    }

    //変更処理
    public void onChangeButtonClicked(View v) {
        //IMEを閉じる
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        //入力文字を得る
        final String oldpassword = oldPasswordField.getText().toString();
        final String newpassword = newPasswordField.getText().toString();

        KiiUser users = KiiUser.getCurrentUser();
        String username = users.toString();
        //String username =  "ushiama";

        KiiUser.logIn(new KiiUserCallBack() {

            @Override
            public void onLoginCompleted(int token, KiiUser user, Exception exception) {
                if (exception != null) {
                    if (exception instanceof CloudExecutionException)
                        //KiiCloud特有のエラーメッセージを表示
                        showAlert(R.string.operation_failed, Util.generateAlertMessage((CloudExecutionException) exception), null);
                    else
                        //一般的なエラーを表示
                        showAlert(R.string.operation_failed, exception.getLocalizedMessage(), null);
                    return;
                }
                user.changePassword(new KiiUserCallBack() {
                    @Override
                    public void onChangePasswordCompleted(int token, Exception exception) {
                        if (exception != null) {
                            // Error handling
                            return;
                        }
                    }
                }, newpassword, oldpassword);
            }
        }, username, oldpassword);

    }

    //メニュー関係：
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

 /*   @Override
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

        //Userで追加ここから
        //ログアウト処理.KiiCloudにはログアウト機能はないのでAccesTokenを削除して対応。

        if (id == R.id.log_out) {
            //自動ログインのため保存されているaccess tokenを消す。
           SharedPreferences pref = getSharedPreferences(getString(R.string.save_data_name), Context.MODE_PRIVATE);
            pref.edit().clear().apply();
            //ログイン画面に遷移
            // Intent のインスタンスを取得する。getApplicationContext()でViewの自分のアクティビティーのコンテキストを取得。遷移先のアクティビティーを.classで指定
            Intent intent = new Intent(getApplicationContext(), UserActivity.class);
            // 遷移先の画面を呼び出す
            startActivity(intent);
            //戻れないようにActivityを終了します。
            finish();
            return true;
        }
        //Userで追加ここまで

        return super.onOptionsItemSelected(item);
    } */
}