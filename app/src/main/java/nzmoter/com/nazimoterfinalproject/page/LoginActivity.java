package nzmoter.com.nazimoterfinalproject.page;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nzmoter.com.nazimoterfinalproject.R;
import nzmoter.com.nazimoterfinalproject.model.User;
import nzmoter.com.nazimoterfinalproject.util.IPageUtil;
import nzmoter.com.nazimoterfinalproject.util.SharedPreference;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, IPageUtil {
    private Button btnLogin;
    private FirebaseAuth firebaseAuth;
    private EditText edMail, edPassword;
    private String mail, password;
    private FirebaseDatabase db;
    private DatabaseReference df;
    private FirebaseUser firebaseUser;
    private SharedPreference sharedPreference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                mail = edMail.getText().toString();
                password = edPassword.getText().toString();
                if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(password)) {
                    Toast.makeText(this, "Boş alan Bırakmayınız", Toast.LENGTH_SHORT).show();
                } else {
                    login(mail, password);
                }

                break;
        }
    }

    @Override
    public void init() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        edMail = (EditText) findViewById(R.id.edMail);
        edPassword = (EditText) findViewById(R.id.edPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        sharedPreference = new SharedPreference();

    }

    public void login(String mail, String password) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Giriş Yapılıyor");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseUser = firebaseAuth.getCurrentUser();
                    getUserDetail(firebaseUser.getUid());
                } else {
                    progressDialog.dismiss();
                }
            }
        });
    }

    public void getUserDetail(String id) {
        progressDialog.setMessage("Ayarlarınız Yapılıyor.");
        df = db.getReference("Users");
        df.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("id", snapshot.getValue(User.class).getId());
                    Log.d("firstName", snapshot.getValue(User.class).getFirstName());
                    sharedPreference.saveMyId(LoginActivity.this, snapshot.getValue(User.class).getId());
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }
}
