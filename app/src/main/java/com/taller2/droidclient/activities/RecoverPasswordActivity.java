package com.taller2.droidclient.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.taller2.droidclient.R;
import com.taller2.droidclient.model.CallbackUserRequester;
import com.taller2.droidclient.model.EmailRecoverUser;
import com.taller2.droidclient.model.PasswordUser;
import com.taller2.droidclient.requesters.UserRequester;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class RecoverPasswordActivity extends BasicActivity {

    private Button button_recovery;
    private EditText email_recovery;

    private UserRequester userRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recover_password);

        button_recovery = findViewById(R.id.button_email_recovery);
        email_recovery = findViewById(R.id.email_recover);

        userRequester = new UserRequester();

        setListeners();
    }

    private void setListeners(){

        button_recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_recovery.getText().toString();

                if(email.contains("@")){
                    EmailRecoverUser emailRecover = new EmailRecoverUser(email);
                    sendEmail(emailRecover);

                }else{
                    Toast.makeText(RecoverPasswordActivity.this, "Incorrect Email", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void sendEmail(EmailRecoverUser email){
        userRequester.recoverPassword(email, new CallbackUserRequester() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    changeActivity(RecoverPasswordActivity.this, MainActivity.class);

                    RecoverPasswordActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(RecoverPasswordActivity.this, "Email send to your inbox", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    RecoverPasswordActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(RecoverPasswordActivity.this, "There has been an error in the reocvery procces", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                Log.d("LOG/Recover Password", response.body().string());
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ERROR RECOVERY", e.getMessage());
                call.cancel();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                changeActivity(RecoverPasswordActivity.this, MainActivity.class);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
