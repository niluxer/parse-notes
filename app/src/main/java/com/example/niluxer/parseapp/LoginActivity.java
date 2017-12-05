package com.example.niluxer.parseapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etUsername)
    EditText etUsername;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @OnClick(R.id.btnLogIn)
    public void Login()
    {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Abrir HomeActivity
                    redirectHome();
                } else {

                    Toast.makeText(LoginActivity.this, "Username and/or password incorrect.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @OnClick(R.id.btnRegister)
    public void Register()
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        verifySession();
    }

    private  void verifySession()
    {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            currentUser.getUsername();
            redirectHome();
        }
    }

    private void redirectHome()
    {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
    }

}
