package com.example.nevernote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CadUpdUser extends AppCompatActivity {
    DBHelper db = new DBHelper(this);
    private EditText edtEmail, edtUsername, edtPassword, edtConfirmPassword;
    private Button salvar;
    private User user, altUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_upd_user);

        edtEmail = findViewById(R.id.edtEmail);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        salvar = findViewById(R.id.button4);

        Intent it = getIntent();
        altUser = (User) it.getSerializableExtra("key_user");
        user = new User();

        if (altUser != null) {
            salvar.setText("ALTERAR");
            edtEmail.setText(altUser.getEmail());
            edtUsername.setText(altUser.getUsername());
            edtPassword.setText(altUser.getPassword());
            user.setId(altUser.getId());
        } else {
            salvar.setText("CADASTRAR");
        }

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                String confirmPassword = edtConfirmPassword.getText().toString();

                user.setEmail(email);
                user.setUsername(username);
                user.setPassword(password);

                if(salvar.getText().toString().equals("CADASTRAR")) {
                    System.out.println(password.equals(confirmPassword) && !email.isEmpty() && !username.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty());
                    if (password.equals(confirmPassword) && !email.isEmpty() && !username.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
                        db.insertUser(user);
                        Toast toast = Toast.makeText(CadUpdUser.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT);
                        toast.show();
                        //System.out.println(db.searchUser());
                    } else {
                        System.out.println("2");
                        Toast toast = Toast.makeText(CadUpdUser.this, "Não foi possível cadastrar o usuário", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                } else if(salvar.getText().toString().equals("ALTERAR")) {
                    db.upadateUser(user);
                    Toast toast = Toast.makeText(CadUpdUser.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    //db.updateUser(user);
                    db.close();
                }
                limpar();
                finish();
            }
        });
    }


    public void limpar(){
        edtEmail.setText("");
        edtUsername.setText("");
        edtPassword.setText("");
    }

    public void cancelar(View view) {
        finish();
    }
}