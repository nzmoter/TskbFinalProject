package nzmoter.com.nazimoterfinalproject.page;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import nzmoter.com.nazimoterfinalproject.R;
import nzmoter.com.nazimoterfinalproject.model.User;
import nzmoter.com.nazimoterfinalproject.util.IPageUtil;
import nzmoter.com.nazimoterfinalproject.util.SharedPreference;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, IPageUtil {
    private static final int GALERY_INTENT = 1;
    private EditText edFirstName, edLastName, edMail, edPassword;
    private Spinner spnDepartmanAdi;
    private Button btnRegister;
    private ImageView imgProfil;
    private Intent intent;
    private DatabaseReference df;
    private FirebaseDatabase db;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String firstName, lastName, mail, password, departmentName, imageUrl, url = "dsfsdf";
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    ProgressDialog progressDialog;
    SharedPreference sharedPreference = new SharedPreference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        spnDepartmanAdi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                departmentName = spnDepartmanAdi.getSelectedItem() + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void init() {
        edFirstName = (EditText) findViewById(R.id.edFirstName);
        edLastName = (EditText) findViewById(R.id.edLastName);
        edMail = (EditText) findViewById(R.id.edMail);
        edPassword = (EditText) findViewById(R.id.edPassword);
        edFirstName = (EditText) findViewById(R.id.edFirstName);
        spnDepartmanAdi = (Spinner) findViewById(R.id.spnDepartmanAdi);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        imgProfil = (ImageView) findViewById(R.id.imgProfil);
        btnRegister.setOnClickListener(this);
        imgProfil.setOnClickListener(this);
        db = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                firstName = edFirstName.getText().toString();
                lastName = edLastName.getText().toString();
                mail = edMail.getText().toString();
                password = edPassword.getText().toString();
                imageUrl = url;
                if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(mail) || TextUtils.isEmpty(password) || TextUtils.isEmpty(imageUrl) || departmentName.equals("Departman Seçiniz")) {
                    Toast.makeText(this, "Gerekli Alanları Doldurunuz.", Toast.LENGTH_SHORT).show();
                } else {
                    createUser(firstName, lastName, mail, password, departmentName, imageUrl);
                }

                break;
            case R.id.imgProfil:
                intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALERY_INTENT);
                break;
        }
    }

    public void createUser(final String firstName, final String lastName, final String mail, String password, final String departmentName, final String imageUrl) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Kayıt Yapılıyor.");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseUser = firebaseAuth.getCurrentUser();
                    saveUser(firebaseUser.getUid(), firstName, lastName, mail, departmentName, imageUrl);
                    Toast.makeText(RegisterActivity.this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show();
                    //Kayıt işlemlerini yap
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Kayıt Yapılamadı", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void saveUser(final String id, String firstName, String lastName, String mail, String departmentName, String imageUrl) {
        progressDialog.setMessage("Ayarlarınız Yapılıyor.");
        df = db.getReference("Users");
        df.child(id).setValue(new User(id, firstName, lastName, mail, departmentName, imageUrl)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    sharedPreference.saveMyId(RegisterActivity.this, id);
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    Toast.makeText(RegisterActivity.this, "Kayıt Başarılı.", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Kayıt Başarısız.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALERY_INTENT && resultCode == RESULT_OK) {
            final Uri uri = data.getData();
            imgProfil.setImageURI(uri);
            String uuid = UUID.randomUUID().toString();
            saveImage(uri, uuid);
        }
    }

    public void saveImage(final Uri uri, String id) {
        storageReference = firebaseStorage.getReference("Users").child(id);
        storageReference.putFile(uri).addOnSuccessListener(RegisterActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                url = taskSnapshot.getDownloadUrl() + "";
            }
        });

    }
}
