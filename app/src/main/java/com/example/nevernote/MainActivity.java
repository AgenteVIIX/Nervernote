package com.example.nevernote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText edtUsername, getEdtPassword;
    private Button buttonLogIn, buttonSignIn;
    DBHelper db = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBHelper db = new DBHelper(this);
        edtUsername = findViewById(R.id.edtUsername);
        getEdtPassword = findViewById(R.id.edtPassword);
        buttonLogIn = findViewById(R.id.buttonConnect);
        buttonSignIn = findViewById(R.id.button2);

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, CadUpdUser.class);
                startActivity(it);
            }
        });

        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("debugging");
                String username = edtUsername.getText().toString();
                String getPassword = getEdtPassword.getText().toString();
                String password  = db.searchPassword(username, getPassword);
                if(getPassword.equals(password) && !username.isEmpty() && !getPassword.isEmpty()){
                    //System.out.println("debugging 2");
                    Intent intent= new Intent(MainActivity.this, ListBook.class);
                    intent.putExtra("key_user", db.searchId(username));
                    startActivity(intent);
                }
                else{
                    Toast toast = Toast.makeText(MainActivity.this,
                            "Usuário ou senha inválido",Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

}