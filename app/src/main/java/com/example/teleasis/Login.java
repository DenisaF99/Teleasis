package com.example.teleasis;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login extends AppCompatActivity {
    private Button logIn;
    EditText mailEditTxt, passwordEditTxt;
    Spinner spinnerLogin;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        logIn = findViewById(R.id.logInBtn);
        mailEditTxt = findViewById(R.id.mailTxt);
        passwordEditTxt = findViewById(R.id.passwordTxt);
        spinnerLogin = findViewById(R.id.spinnerfilter);
        fAuth= FirebaseAuth.getInstance();


        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.spinnerfilter, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerLogin.setAdapter(adapter);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent myIntent = new Intent(Login.this, PrimaPagina.class);
//                startActivity(myIntent);
                String mail= mailEditTxt.getText().toString().trim();
                String parolaLogIn = passwordEditTxt.getText().toString().trim();
                String item = spinnerLogin.getSelectedItem().toString();


                if(TextUtils.isEmpty(mail)){
                    mailEditTxt.setError("First Name is required");
                    return;
                }
                if(TextUtils.isEmpty(parolaLogIn)){
                    passwordEditTxt.setError("Password is required");
                    return;
                }

                fAuth.signInWithEmailAndPassword(mail,parolaLogIn).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(item.equals("pacient")){
                                Intent myIntent = new Intent(Login.this, PrimaPagina_Pacient.class);
                                startActivity(myIntent);
                            }else if(item.equals("ingrijitor")){
                                Intent myIntent = new Intent(Login.this, AlegePacient.class);
                                startActivity(myIntent);
                            }
                            //Toast.makeText(Login.this,"User created", Toast.LENGTH_SHORT).show();

                        }else
                        {
                            Toast.makeText(Login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }
}