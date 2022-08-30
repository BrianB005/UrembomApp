package com.brianbett.urembom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.brianbett.urembom.retrofit.Preferences;
import com.brianbett.urembom.retrofit.RetrofitHandler;
import com.brianbett.urembom.retrofit.User;
import com.brianbett.urembom.retrofit.UserInterface;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    AppCompatEditText usernameView,emailView,passwordView,confirmPasswordView;
    MaterialButton registerButton;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent intent=getIntent();
        String email=intent.getStringExtra("email");

        registerButton=findViewById(R.id.register_btn);

        usernameView=findViewById(R.id.register_username);
        emailView=findViewById(R.id.register_email);
        passwordView=findViewById(R.id.register_password);
        confirmPasswordView=findViewById(R.id.register_password_confirm);

        if(!email.equals("")){
            emailView.setText(email);
        }

        registerButton.setOnClickListener(view->{
            String inputtedEmail= Objects.requireNonNull(emailView.getText()).toString();
            String username= Objects.requireNonNull(usernameView.getText()).toString();
            String password= Objects.requireNonNull(passwordView.getText()).toString();
            String passwordConfirm= Objects.requireNonNull(confirmPasswordView.getText()).toString();
            if(inputtedEmail.equals("")){
                emailView.setError("Email field cannot be empty");
            }else if(username.equals("")){
                usernameView.setError("Username field cannot be empty");
            }else if(password.equals("")){
                passwordView.setError("Password field cannot be empty");
            }else if(passwordConfirm.equals("")){
                confirmPasswordView.setError("Confirm password  field cannot be empty");
            }else if(password.length()<8){
                passwordView.setError("Password must be longer than 8 characters");
            }
            else if(!password.equals(passwordConfirm)){
                confirmPasswordView.setError("Passwords do not match!");
            }
            else {
                registerButton.setEnabled(false);
                registerButton.setText("Hold on...");
                HashMap<String,String> userDetails=new HashMap<>();
                userDetails.put("name",username);
                userDetails.put("email",inputtedEmail);
                userDetails.put("password",password);
                RetrofitHandler.registerUser(getApplicationContext(), userDetails, new UserInterface() {
                    @Override
                    public void success(User currentUser) {
                        Toast.makeText(getApplicationContext(),"Welcome"+currentUser.getUserDetails().getUsername(),Toast.LENGTH_SHORT).show();
                        Preferences.saveItemToSP(getApplicationContext(),"username",currentUser.getUserDetails().getUsername());
                        Preferences.saveItemToSP(getApplicationContext(),"token",currentUser.getToken());
                        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                        finish();
                    }
                    @Override
                    public void errorExists(String  errorMessage){
                        registerButton.setEnabled(true);
                        registerButton.setText("Register");

                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void failure(Throwable throwable) {
                        registerButton.setEnabled(true);
                        registerButton.setText("Register");
                        Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

}