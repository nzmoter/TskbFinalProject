package nzmoter.com.nazimoterfinalproject.page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import nzmoter.com.nazimoterfinalproject.R;
import nzmoter.com.nazimoterfinalproject.util.IPageUtil;

/**
 * Created by Bhct on 26.12.2017.
 */

public class UserActivity extends Activity implements IPageUtil, View.OnClickListener {
    private Button btnDelete;
    private TextView txtFirstName, txtLastName, txtDepName;
    private FirebaseDatabase db;
    private ImageView imgProfil;
    private String myid, your_id, your_firstname, your_lastname, your_depname, your_imageurl, auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        init();
        getUserDetail();

    }

    @Override
    public void init() {
        btnDelete = (Button) findViewById(R.id.btnDelete);
        txtFirstName = (TextView) findViewById(R.id.txtFirstName);
        txtLastName = (TextView) findViewById(R.id.txtLastName);
        txtDepName = (TextView) findViewById(R.id.txtDepName);
        imgProfil = (ImageView) findViewById(R.id.imgProfil);
        btnDelete.setOnClickListener(this);
        db = FirebaseDatabase.getInstance();
    }

    public void getUserDetail() {
        Intent ıntent = getIntent();
        myid = ıntent.getStringExtra("myid");
        your_id = ıntent.getStringExtra("your_id");
        your_firstname = ıntent.getStringExtra("your_firstname");
        your_lastname = ıntent.getStringExtra("your_lastname");
        your_depname = ıntent.getStringExtra("your_depname");
        your_imageurl = ıntent.getStringExtra("your_imageurl");
        auth = ıntent.getStringExtra("auth");
        authControl(auth);
        writeUserDetail(your_firstname, your_lastname, your_depname);
        Picasso.with(UserActivity.this).load(your_imageurl).centerCrop().fit().into(imgProfil);
    }

    public void authControl(String auth) {
        if (auth.equals("admin")) {
            btnDelete.setVisibility(View.VISIBLE);
        }
    }

    public void writeUserDetail(String your_firstname, String your_lastname, String your_depname) {
        txtFirstName.setText("Firstname:" + your_firstname);
        txtLastName.setText("Lastname:" + your_lastname);
        txtDepName.setText("Department:" + your_depname);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDelete:
                deleteUser(your_id);
                break;
        }
    }

    public void deleteUser(String yourid) {
        db.getReference("Users").child(yourid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UserActivity.this, "Kullanıcı Silindi", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserActivity.this, "Kullanıcı Silinemedi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
