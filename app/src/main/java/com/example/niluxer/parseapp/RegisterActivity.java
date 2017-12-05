package com.example.niluxer.parseapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.etRegisterUsername)
    EditText etRegisterUsername;

    @BindView(R.id.etRegisterPassword)
    EditText etRegisterPassword;

    @BindView(R.id.etRegisterEmail)
    EditText etRegisterEmail;

    @OnClick(R.id.btnRegister)
    public void register()
    {
        String username = etRegisterUsername.getText().toString();
        String password = etRegisterPassword.getText().toString();
        String email    = etRegisterEmail.getText().toString();

        if (username.trim().length()>0 &&
            password.trim().length()>0 &&
            email.trim().length()>0) {

            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.put("assigned", false);

            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error registering account.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @OnClick(R.id.btnCancel)
    public void cancel()
    {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }
}
