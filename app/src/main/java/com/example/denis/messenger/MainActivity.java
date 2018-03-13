package com.example.denis.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.denis.messenger.Models.*;
import com.firebase.ui.database.FirebaseListAdapter;
import com.github.library.bubbleview.BubbleTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Denis on 12.03.2018.
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.list_of_chats)
    ListView chats;

    private FirebaseListAdapter<Chats> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Диалоги");

        //FirebaseAuth.getInstance().signOut();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            Intent Login = new Intent(this, LoginActivity.class);
            finish();
            startActivity(Login);
        } else {
            createAdapter();
            chats.setAdapter(adapter);
        }

        chats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView name = (TextView)view.findViewById(R.id.chat_user) ;
                TextView id = (TextView)view.findViewById(R.id.id_user);

                if (id.getText().toString().isEmpty()) {
                    Intent mainChat = new Intent(MainActivity.this, MainChatActivity.class);
                    startActivity(mainChat);
                } else {
                    Intent chatWithUser = new Intent(MainActivity.this, CharWithUserActivity.class);

                    chatWithUser.putExtra("myId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    chatWithUser.putExtra("hisId", id.getText().toString());
                    chatWithUser.putExtra("hisName", name.getText().toString());
                    startActivity(chatWithUser);
                }
            }
        });
    }

    private void createAdapter() {
        adapter = new FirebaseListAdapter<Chats>(this, Chats.class, R.layout.chats, FirebaseDatabase.getInstance().getReference()
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
        ) {
            @Override
            protected void populateView(final View v, Chats model, int position) {
                TextView chatUser = (TextView) v.findViewById(R.id.chat_user);
                TextView id_User = (TextView) v.findViewById(R.id.id_user);
                String idUser = adapter.getRef(position).getKey();


                if (!idUser.equals("BASE_CHAT")) {
                    String[] str = idUser.split("\\{", 3);
                    id_User.setText(str[0]);
                    chatUser.setText(str[1].substring(0,str[1].length()-1));
                } else {
                    chatUser.setText("Общий чат");
                }
            }
        };
    }

    private void setText(TextView s, String s2) {
        s.setText(s2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.exit : FirebaseAuth.getInstance().signOut();
                Intent login = new Intent(this, LoginActivity.class);
                finish();
                startActivity(login);
                break;
        }
        return true;
    }
}
//
////                chatUser.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////                        String myId = FirebaseAuth.getInstance().getCurrentUser().getUid();
////                        //String hisId = model.
////                                FirebaseDatabase.getInstance().getReference()
////                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid().
////                        Intent chat = new Intent(this, CharWithUserActivity.class);
////                        chat.putExtra("myId", myId);
////                        chat.putExtra("hisId", hisId);
////                        startActivity(chat);
////                    }
////                });
//            }
//        };

