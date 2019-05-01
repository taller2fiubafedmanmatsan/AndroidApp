package com.taller2.droidclient.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.taller2.droidclient.R;
import com.taller2.droidclient.model.CallbackUserRequester;
import com.taller2.droidclient.model.PasswordUser;
import com.taller2.droidclient.requesters.UserRequester;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class ChangePasswordActivity extends BasicActivity {

    private EditText new_password;
    private EditText confirm_password;

    private Button button_change_password;
    private Button button_cancel;
    private String token;

    private UserRequester userRequester;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        changeTextActionBar("Change password");

        new_password = findViewById(R.id.password_new);
        confirm_password =findViewById(R.id.password_confirm);
        button_change_password = findViewById(R.id.button_send_password);
        button_cancel = findViewById(R.id.button_cancel);

        token = preference.getToken();//this.getUserToken();

        userRequester = new UserRequester();

        setListeners();
    }

    private void setListeners(){
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //changeActivity(ChangePasswordActivity.this, ProfileActivity.class, token);
                finish();
            }
        });

        button_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password_1 = new_password.getText().toString();
                String password_2 = confirm_password.getText().toString();
                if(password_1.equals(password_2)){
                    PasswordUser pass = new PasswordUser(password_1);
                    changePassword(pass);

                }else{
                    Toast.makeText(ChangePasswordActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void changePassword(PasswordUser pass){
        userRequester.changePasswordUser(pass, token, new CallbackUserRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    //changeActivity(ChangePasswordActivity.this, ProfileActivity.class, token);
                    finish();

                    ChangePasswordActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ChangePasswordActivity.this, "Success!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    ChangePasswordActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ChangePasswordActivity.this, "Invalid new password", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                Log.d("LOG/Change Password", token);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                //Toast.makeText(ChangePasswordActivity.this, "Error : Couldn't change password", Toast.LENGTH_SHORT).show();
                Log.d("ERROR", e.getMessage());
                call.cancel();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
