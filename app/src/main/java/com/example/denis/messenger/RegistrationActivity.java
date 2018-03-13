package com.example.denis.messenger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.denis.messenger.Models.Chats;
import com.example.denis.messenger.Models.Message;
import com.example.denis.messenger.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Denis on 06.03.2018.
 */

public class RegistrationActivity extends AppCompatActivity {
    @BindView(R.id.email_registration)
    EditText email;
    @BindView(R.id.name_registration)
    EditText name;
    @BindView(R.id.password_registration)
    EditText password;
    @BindView(R.id.password2_registration)
    EditText password2;
    @BindView(R.id.registr)
    Button registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        ButterKnife.bind(RegistrationActivity.this);

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registration.setClickable(false);

                final ProgressDialog progressDialog = new ProgressDialog(RegistrationActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Регистрация...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // On complete call either onLoginSuccess or onLoginFailed
                                registration();
                                // onLoginFailed();
                                progressDialog.dismiss();
                            }
                        }, 3000);

            }
        });
    }

    private void registration() {
        String email_ = email.getText().toString();
        final String name_ = name.getText().toString();
        String password_ = password.getText().toString();
        String password2_ = password2.getText().toString();

        if (name_.length() >=4 && name_.length() <=15) {
            if (password.length() >= 6 && password.length() <= 20) {
                if (password_.equals(password2_)) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(email_).matches()) {
                        FirebaseAuth.getInstance()
                                .createUserWithEmailAndPassword(email_, password_)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(name_)
                                                    .build();
                                            user.updateProfile(profileUpdates);

                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("Users")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .setValue(new User(name_));

                                            Map BaseChat = new HashMap<String, Message>();
                                            BaseChat.put("BASE_CHAT", new Message("0", "BASE_CHAT", "0"));
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .child("BASE_CHAT")
                                                    .setValue(new Chats(BaseChat));

                                            FirebaseAuth.getInstance().signOut();

                                            finishActivityForResult();
                                        } else {
                                            ToastMakeText("Несуществующая почта или она уже используется!");
                                        }
                                    }
                                });
                    } else {
                        ToastMakeText("Неверно введена почта!");
                    }
                } else {
                    ToastMakeText("Не совпадают введенные пароли!");
                }
            } else {
                ToastMakeText("Пароль должен быть длиной от 6 до 20 символов!");
            }
        } else {
            ToastMakeText("Имя должно быть длиной от 4 до 15 символов!");
        }
    }

    private void finishActivityForResult() {
        Intent result = new Intent();
        result.putExtra("email", email.getText().toString());
        result.putExtra("password", password.getText().toString());
        setResult(RESULT_OK, result);
        finish();
    }

    private void ToastMakeText(String error) {
        Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
        registration.setClickable(true);
    }
}
