package com.example.denis.messenger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Denis on 11.03.2018.
 */

public class AnonimLoginActivity extends AppCompatActivity{

    @BindView(R.id.anonimName)
    EditText name;
    @BindView(R.id.logInAnonim)
    Button logIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anonim_login_activity);
        ButterKnife.bind(AnonimLoginActivity.this);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                logIn.setClickable(false);

                if (name.length() >=4 && name.length() <= 15) {
                    final ProgressDialog progressDialog = new ProgressDialog(AnonimLoginActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Вход");
                    progressDialog.show();

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    logIn();
                                    progressDialog.dismiss();
                                }
                            }, 1500);
                } else {
                    Toast.makeText(getBaseContext(), "Имя должно быть длиной от 4 до 15 символов!", Toast.LENGTH_SHORT).show();
                    logIn.setClickable(true);
                }
            }
        });
    }

    private void logIn() {
        final String name_ = name.getText().toString();

        FirebaseAuth.getInstance().signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name_)
                                    .build();
                            user.updateProfile(profileUpdates);
                            Intent Main = new Intent(AnonimLoginActivity.this, MainActivity.class);
                            setResult(RESULT_OK);
                            finish();
                            startActivity(Main);
                        } else {
                            Toast.makeText(getBaseContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                            logIn.setClickable(true);
                        }
                    }
                });
    }
}
