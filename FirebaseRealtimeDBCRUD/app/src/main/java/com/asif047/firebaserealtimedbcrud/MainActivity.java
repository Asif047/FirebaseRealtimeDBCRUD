package com.asif047.firebaserealtimedbcrud;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnSave;
    private EditText etUser;
    private ListView listViewUsers;
    private DatabaseReference databaseReference;

    private List<User> users;
    public static String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        users = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        btnSave = findViewById(R.id.btnSave);
        etUser = findViewById(R.id.edtName);
        listViewUsers = findViewById(R.id.listViewUsers);

       btnSave.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String name = etUser.getText().toString();

               if(TextUtils.isEmpty(userId)) {
                   String id = databaseReference.push().getKey();
                   User user = new User(id, name);
                   databaseReference.child(id).setValue(user);

                   Toast.makeText(MainActivity.this, "user created successfully!",
                           Toast.LENGTH_SHORT).show();
               } else {
                   databaseReference.child(userId).child("name").setValue(name);
                   Toast.makeText(MainActivity.this, "User Updated Successfully",
                           Toast.LENGTH_SHORT).show();
               }

               etUser.setText(null);
           }
       });

    }


    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();


                for(DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    User user = postSnapShot.getValue(User.class);
                    users.add(user);
                }

                UserList userAdapter = new UserList(MainActivity.this, users,
                        databaseReference, etUser);
                listViewUsers.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {



            }
        });
    }
}
