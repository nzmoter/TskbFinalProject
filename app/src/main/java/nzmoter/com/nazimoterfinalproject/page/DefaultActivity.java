package nzmoter.com.nazimoterfinalproject.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import nzmoter.com.nazimoterfinalproject.R;
import nzmoter.com.nazimoterfinalproject.util.IPageUtil;

public class DefaultActivity extends AppCompatActivity implements IPageUtil, View.OnClickListener {
    private Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);
        init();
    }


    @Override
    public void init() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                startActivity(new Intent(DefaultActivity.this, LoginActivity.class));
                break;
            case R.id.btnRegister:
                startActivity(new Intent(DefaultActivity.this, RegisterActivity.class));
                break;
        }
    }
}
