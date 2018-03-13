package com.example.denis.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.denis.messenger.Models.Message;
import com.firebase.ui.database.FirebaseListAdapter;
import com.github.library.bubbleview.BubbleTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import static java.text.DateFormat.getDateInstance;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Denis on 11.03.2018.
 */

public abstract class Chat extends AppCompatActivity {
    protected static final int TO_PROFILE = 0;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.input)
    EditText input;
    @BindView(R.id.list_of_messages)
    ListView listOfMessages;

    protected FirebaseListAdapter<Message> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        ButterKnife.bind(this);
        registerForContextMenu(listOfMessages);

        setTitle();

        floatingActionButton.hide();

        setClickForWriteMessage();

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(input.getText().toString().length() > 0) {
                    floatingActionButton.show();
                } else {
                    floatingActionButton.hide();
                }
            }
        });

        startCreateAdapter();

        listOfMessages.setAdapter(adapter);
    }

    protected void createAdapter(DatabaseReference databaseReference, int layout) {
        adapter = new FirebaseListAdapter<Message>(this, Message.class, layout, databaseReference) {
            @Override
            protected void populateView(View v, Message model, int position) {
                BubbleTextView messageText = (BubbleTextView) v.findViewById(getIdMessage_text());
                BubbleTextView messageIText = (BubbleTextView) v.findViewById(getIdMessage_i_text());
                TextView messageUser = (TextView)v.findViewById(getIdMessage_user());
                TextView messageTime = (TextView)v.findViewById(getIdMessage_time());

                messageUser.setText(model.getUser());
                if (new Date().getYear() != new Date(model.getTime()).getYear()) {
                    messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm)", model.getTime()));
                } else if (new Date().getDay() !=  new Date(model.getTime()).getDay()){
                    messageTime.setText(DateFormat.format("dd-MM (HH:mm)", model.getTime()));
                } else {
                    messageTime.setText(DateFormat.format("HH:mm", model.getTime()));
                }

                if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(model.getId())) {
                    messageText.setVisibility(View.GONE);
                    messageIText.setVisibility(View.VISIBLE);
                    messageIText.setText(model.getMessage());
                    messageUser.setVisibility(View.GONE);
                } else {
                    messageIText.setVisibility(View.GONE);
                    messageText.setVisibility(View.VISIBLE);
                    messageText.setText(model.getMessage());
                    if (getIsShowToUser()) {
                        messageUser.setVisibility(View.VISIBLE);
                    } else {
                        messageUser.setVisibility(View.INVISIBLE);
                    }
                }
            }
        };
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, TO_PROFILE, 0, "Перейти к чату с пользователем");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == TO_PROFILE) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            String myId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String hisId = adapter.getItem(info.position).getId();
            String hisName = adapter.getItem(info.position).getUser();

            Intent chat = new Intent(this, CharWithUserActivity.class);
            chat.putExtra("myId", myId);
            chat.putExtra("hisId", hisId);
            chat.putExtra("hisName", hisName);
            startActivity(chat);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    protected abstract int getIdMessage_text();
    protected abstract int getIdMessage_i_text();
    protected abstract int getIdMessage_user();
    protected abstract int getIdMessage_time();

    protected abstract void setClickForWriteMessage();
    protected abstract void startCreateAdapter();

    protected abstract boolean getIsShowToUser();

    protected abstract void setTitle();
}
