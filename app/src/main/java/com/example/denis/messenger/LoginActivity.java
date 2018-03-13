package com.example.denis.messenger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Denis on 06.03.2018.
 */

public class LoginActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_to_Registration = 0;
    public static final int REQUEST_CODE_to_anonim = 1;

    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.registration)
    TextView registartion;
    @BindView(R.id.anonim)
    TextView anonim;
    @BindView(R.id.logIn)
    Button logIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(LoginActivity.this);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn.setClickable(false);

                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Вход");
                progressDialog.setCancelable(false);
                progressDialog.show();

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                signIn(email.getText().toString(), password.getText().toString());
                                progressDialog.dismiss();
                            }
                        }, 1500);
            }
        });

        anonim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent AnonimLogin = new Intent(LoginActivity.this, AnonimLoginActivity.class);
                startActivityForResult(AnonimLogin, REQUEST_CODE_to_anonim);
            }
        });

        registartion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Registration = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivityForResult(Registration, REQUEST_CODE_to_Registration);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_to_Registration :
                if (resultCode == RESULT_OK) {
                    email.setText(data.getStringExtra("email"));
                    password.setText(data.getStringExtra("password"));
                    Toast.makeText(getBaseContext(), "Регистрация завершена!", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_to_anonim :
                if (resultCode == RESULT_OK) {
                    finish();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
    }

    private void signIn(String email_, String password_) {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email_).matches()) {
            if (!password_.isEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email_, password_)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent Main = new Intent(LoginActivity.this, MainActivity.class);
                                    finish();
                                    startActivity(Main);
                                } else {
                                    ToastMakeText("Неверная почта или пароль!");
                                }
                            }
                        });
            } else {
                ToastMakeText("Введите пароль!");
            }
        } else {
            ToastMakeText("Неверно введен email!");
        }
    }

    private void ToastMakeText(String error) {
        Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
        logIn.setClickable(true);
    }
}
