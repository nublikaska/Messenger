package com.example.denis.messenger;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import com.example.denis.messenger.Models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainChatActivity extends Chat {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getIdMessage_text() {
        return R.id.message_text;
    }

    @Override
    protected int getIdMessage_i_text() {
        return R.id.message_i_text;
    }

    @Override
    protected int getIdMessage_user() {
        return R.id.message_user;
    }

    @Override
    protected int getIdMessage_time() {
        return R.id.message_time;
    }

    @Override
    protected void startCreateAdapter() {
        createAdapter(FirebaseDatabase.getInstance().getReference().child("Messages"), R.layout.message);
    }

    @Override
    protected boolean getIsShowToUser() {
        return true;
    }

    @Override
    protected void setTitle() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Общий чат");
    }

    @Override
    protected void setClickForWriteMessage() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!input.getText().toString().trim().isEmpty()) {
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("Messages")
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
