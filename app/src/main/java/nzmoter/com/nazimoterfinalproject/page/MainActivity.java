package nzmoter.com.nazimoterfinalproject.page;

import android.content.Intent;
import android.support.annotation.BoolRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import nzmoter.com.nazimoterfinalproject.R;
import nzmoter.com.nazimoterfinalproject.adapter.UserListAdapter;
import nzmoter.com.nazimoterfinalproject.model.User;
import nzmoter.com.nazimoterfinalproject.util.IPageUtil;
import nzmoter.com.nazimoterfinalproject.util.SharedPreference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IPageUtil {
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ListView listView;
    private SharedPreference sharedPreference;
    private Button btnChangeFirstName, btnChangeLastName, btnExit;
    private Spinner spnListele;
    private FirebaseDatabase db;
    private String myid;
    private DatabaseReference df;
    private EditText edFirstName, edLastName;
    private ArrayList<User> userArrayList;
    private ValueEventListener mListener;
    private String userid;
    private String auth;
    private String listItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        mAuthListener();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent ıntent = new Intent(MainActivity.this, UserActivity.class);
                ıntent.putExtra("myid", myid);
                ıntent.putExtra("auth", auth);
                ıntent.putExtra("your_id", userArrayList.get(position).getId());
                ıntent.putExtra("your_firstname", userArrayList.get(position).getFirstName());
                ıntent.putExtra("your_lastname", userArrayList.get(position).getLastName());
                ıntent.putExtra("your_depname", userArrayList.get(position).getDepartmentName());
                ıntent.putExtra("your_imageurl", userArrayList.get(position).getImageUrl());
                startActivity(ıntent);
            }
        });
        spnListele.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listItem = spnListele.getSelectedItem() + "";
                if (!listItem.equals("Departman Seçiniz")) {
                    userListWithFilter(listItem);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void init() {
        listView = (ListView) findViewById(R.id.listView);
        edFirstName = (EditText) findViewById(R.id.edFirstName);
        edLastName = (EditText) findViewById(R.id.edLastName);
        btnChangeFirstName = (Button) findViewById(R.id.btnChangeFirstName);
        spnListele = (Spinner) findViewById(R.id.spnListele);
        btnExit = (Button) findViewById(R.id.btnExit);
        btnChangeLastName = (Button) findViewById(R.id.btnChangeLastName);
        btnChangeFirstName.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnChangeLastName.setOnClickListener(this);
        sharedPreference = new SharedPreference();
        myid = sharedPreference.getMyId(this);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        userArrayList = new ArrayList<>();
    }

    public void mAuthListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null) {
                    startActivity(new Intent(MainActivity.this, DefaultActivity.class));
                    MainActivity.this.finish();
                } else {
                    getUserDetail(myid);
                    userList();
                }
            }
        };
    }

    public void getUserDetail(final String id) {
        df = db.getReference("Users");
        df.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    edFirstName.setText(snapshot.getValue(User.class).getFirstName());
                    edLastName.setText(snapshot.getValue(User.class).getLastName());
                    userid = snapshot.getValue(User.class).getId() + "";
                    userAuth(userid);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void userAuth(String id) {
        if (id.equals("xhzqWMzQd4OKeaegVUwHvQPIQ913")) {
            auth = "admin";
        }else{
            auth = "user";
        }
    }

    public void userListWithFilter(String key) {
        df = db.getReference("Users");
        mListener = df.orderByChild("departmentName").equalTo(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = new User(snapshot.getValue(User.class).getId(), snapshot.getValue(User.class).getFirstName(), snapshot.getValue(User.class).getLastName(), snapshot.getValue(User.class).getMail(), snapshot.getValue(User.class).getDepartmentName(), snapshot.getValue(User.class).getImageUrl());
                    userArrayList.add(user);
                }
                UserListAdapter userListAdapter = new UserListAdapter(userArrayList, MainActivity.this);
                listView.setAdapter(userListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void userList() {
        df = db.getReference("Users");
        mListener = df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = new User(snapshot.getValue(User.class).getId(), snapshot.getValue(User.class).getFirstName(), snapshot.getValue(User.class).getLastName(), snapshot.getValue(User.class).getMail(), snapshot.getValue(User.class).getDepartmentName(), snapshot.getValue(User.class).getImageUrl());
                    userArrayList.add(user);
                }
                UserListAdapter userListAdapter = new UserListAdapter(userArrayList, MainActivity.this);
                listView.setAdapter(userListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnChangeFirstName:
                updateUserDetail("firstName", edFirstName.getText().toString());
                break;
            case R.id.btnChangeLastName:
                updateUserDetail("lastName", edLastName.getText().toString());
                break;
            case R.id.btnExit:
                firebaseAuth.signOut();
                break;
        }
    }

    public void updateUserDetail(String key, String value) {
        db.getReference("Users").child(myid).child(key).setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Güncelleme başarılı bir şekilde yapıldı", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Güncelleme başarısız", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        df.removeEventListener(mListener);
    }
}
