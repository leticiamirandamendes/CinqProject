package com.example.letic.cinqproject;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.letic.cinqproject.database.DatabaseHelper;
import com.example.letic.cinqproject.models.User;
import com.example.letic.cinqproject.utils.InputValidation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.et_name)
    EditText etName;

    @BindView(R.id.et_email)
    EditText etEmail;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.btn_register)
    Button add;

    @BindView(R.id.register)
    ConstraintLayout register;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private User user;
    private String name;
    private String email;
    private String password;
    private boolean isUpdate = false;
    private boolean isNew = false;
    private String addUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        init();
        checkIsUpdate();
    }

    public void init(){
        inputValidation = new InputValidation(this);
        databaseHelper = new DatabaseHelper(this);
        user = new User();
    }

    public void checkIsUpdate(){
        name = getIntent().getStringExtra("NAME");
        email = getIntent().getStringExtra("EMAIL");
        password = getIntent().getStringExtra("PASSWORD");
        addUser = getIntent().getStringExtra("ADD");

        if(name != null){
            etName.setText(name);
            etEmail.setText(email);
            etPassword.setText(password);
            etEmail.setClickable(false);
            etEmail.setFocusable(false);
            isUpdate = true;
            add.setText("Atualizar");
        }else if(add != null){
            isNew = true;
            add.setText("Adicionar");
        }

    }

    @OnClick(R.id.btn_register)
    public void register(){
        if(!isUpdate) {
            addToSQLite();
        }else{
            updateUser();
        }
    }

    private void addToSQLite() {
        if (!inputValidation.isEditTextFilled(etName, getString(R.string.error_name))) {
            return;
        }
        if (!inputValidation.isEditTextFilled(etEmail, getString(R.string.error_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(etEmail, getString(R.string.error_email))) {
            return;
        }
        if (!inputValidation.isEditTextFilled(etPassword, getString(R.string.error_password))) {
            return;
        }

        if (!databaseHelper.checkUser(etEmail.getText().toString().trim())) {
            user.setName(etName.getText().toString().trim());
            user.setEmail(etEmail.getText().toString().trim());
            user.setPassword(etPassword.getText().toString().trim());
            databaseHelper.createUser(user);
            if(isNew){
                finish();
            }else {
                Intent accountsIntent = new Intent(this, HomeActivity.class);
                accountsIntent.putExtra("EMAIL", etEmail.getText().toString().trim());
                accountsIntent.putExtra("NAME", user.getName());
                startActivity(accountsIntent);
            }
        } else {
            Snackbar.make(register, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
        }
    }

    public void updateUser(){
        User user = new User();
        user.setName(etName.getText().toString().trim());
        user.setEmail(email);
        user.setPassword(etPassword.getText().toString().trim());
        databaseHelper.updateUser(user);
        finish();
    }
}
