package com.brianbett.urembom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.brianbett.urembom.retrofit.Preferences;
import com.brianbett.urembom.retrofit.RetrofitHandler;
import com.brianbett.urembom.retrofit.User;
import com.brianbett.urembom.retrofit.UserInterface;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    AppCompatEditText emailView,passwordView;
    String email,password;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MaterialButton openRegister,loginButton;
        openRegister=findViewById(R.id.open_register_page);
        loginButton=findViewById(R.id.login_btn);


        emailView=findViewById(R.id.login_email);
        passwordView=findViewById(R.id.login_password);


        openRegister.setOnClickListener(view -> {
            email= Objects.requireNonNull(emailView.getText()).toString();
            password= Objects.requireNonNull(passwordView.getText()).toString();
            Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
            intent.putExtra("email",email);
            startActivity(intent);
            finish();

        });
        loginButton.setOnClickListener(view->{
            email= Objects.requireNonNull(emailView.getText()).toString();
            password= Objects.requireNonNull(passwordView.getText()).toString();
            if(email.equals("")){
                emailView.setError("Email field cannot be empty");
            }else if (password.equals("")){
                passwordView.setError("password field cannot be empty");
            }else if(password.length()<8){
                passwordView.setError("Password must be longer than 8 characters");
            }else{
                HashMap<String,String> loginDetails=new HashMap<>();

                loginDetails.put("email",email);
                loginDetails.put("password",password);
                loginButton.setEnabled(false);
                loginButton.setText("Hold on...");
                RetrofitHandler.loginUser(getApplicationContext(), loginDetails, new UserInterface() {
                    @Override
                    public void success(User currentUser) {
                        Toast.makeText(getApplicationContext(),"Welcome back"+currentUser.getUserDetails().getUsername(),Toast.LENGTH_SHORT).show();
                        Preferences.saveItemToSP(getApplicationContext(),"username",currentUser.getUserDetails().getUsername());
                        Preferences.saveItemToSP(getApplicationContext(),"token",currentUser.getToken());
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();

                    }

                    @Override
                    public void errorExists(String  errorMessage){
                        loginButton.setEnabled(true);
                        loginButton.setText("Login");

                    }
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void failure(Throwable throwable) {
//                        Log.d("Exception",throwable.getMessage());
                        Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        loginButton.setEnabled(true);
                        loginButton.setText("Login");
                    }

                });
            }

        });
    }
}