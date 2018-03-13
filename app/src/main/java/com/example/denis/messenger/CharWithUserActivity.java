package com.example.denis.messenger;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.example.denis.messenger.Models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by Denis on 11.03.2018.
 */

public class CharWithUserActivity extends Chat {

    private String myId;
    private String hisId;
    private String hisName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        myId =  getIntent().getStringExtra("myId");
        hisId = getIntent().getStringExtra("hisId");
        hisName = getIntent().getStringExtra("hisName");
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getIdMessage_text() {
        return R.id.message_text_ls;
    }

    @Override
    protected int getIdMessage_i_text() {
        return R.id.message_i_text_ls;
    }

    @Override
    protected int getIdMessage_user() {
        return R.id.message_user_ls;
    }

    @Override
    protected int getIdMessage_time() {
        return R.id.message_time_ls;
    }

    @Override
    protected void startCreateAdapter() {
        createAdapter(FirebaseDatabase.getInstance().getReference().child(myId).child(hisId + "{" + hisName + "}"), R.layout.message_ls);
    }

    @Override
    protected boolean getIsShowToUser() {
        return false;
    }

    @Override
    protected void setTitle() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Чат с " + hisName);
    }

    @Override
    protected void setClickForWriteMessage() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!input.getText().toString().trim().isEmpty()) {
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(myId)
                            .child(hisId + "{" + hisName + "}")
                            .push()
                            .setValue(new Message(
                                    input.getText().toString(),
                                    FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), FirebaseAuth.getInstance().getCurrentUser().getUid())
                            );

                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(hisId)
                            .child(myId + "{" + FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "}")
                            .push()
                            .setValue(new Message(
                                    input.getText().toString(),
                                    FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), FirebaseAuth.getInstance().getCurrentUser().getUid())
                            );

                    input.setText("");
                }
            }
        });
    }
}