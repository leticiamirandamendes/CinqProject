package com.example.letic.cinqproject;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.letic.cinqproject.database.DatabaseHelper;
import com.example.letic.cinqproject.models.User;
import com.example.letic.cinqproject.utils.InputValidation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_email)
    EditText etEmail;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.btn_login)
    Button btnLogin;

    @BindView(R.id.tv_create_account)
    TextView createAccount;

    @BindView(R.id.login)
    ConstraintLayout login;

    private final AppCompatActivity activity = LoginActivity.this;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        databaseHelper = new DatabaseHelper(this);
        inputValidation = new InputValidation(this);
    }


    @OnClick(R.id.btn_login)
    public void login(){
        doLoginSQLite();
    }

    @OnClick(R.id.tv_create_account)
    public void register(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void doLoginSQLite(){
        if(!inputValidation.isEditTextFilled(etEmail, getString(R.string.error_email))){
            return;
        }
        if(!inputValidation.isInputEditTextEmail(etEmail, getString(R.string.error_email))){
            return;
        }
        if (!inputValidation.isEditTextFilled(etPassword, getString(R.string.error_password))) {
            return;
        }
        if (databaseHelper.checkUser(etEmail.getText().toString().trim()
                , etPassword.getText().toString().trim())) {
            User user = databaseHelper.getUser(etEmail.getText().toString().trim());
            Intent accountsIntent = new Intent(activity, HomeActivity.class);
            accountsIntent.putExtra("EMAIL", user.getEmail());
            accountsIntent.putExtra("NAME", user.getName());
            startActivity(accountsIntent);
        } else {
            Snackbar.make(login, getString(R.string.error_password_email), Snackbar.LENGTH_LONG).show();
        }
    }
}
