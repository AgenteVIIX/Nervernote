package com.example.nevernote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CadUpdBook extends AppCompatActivity {

    DBHelper db = new DBHelper(this);
    private EditText edtDesc;
    private EditText edtNome;
    private Button salvar;
    private Book book, altBook;
    private User altUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_upd_book);

        edtDesc = findViewById(R.id.edtDesc);
        edtNome = findViewById(R.id.edtNome);
        salvar = findViewById(R.id.button3);

        Intent it = getIntent();
        altBook = (Book) it.getSerializableExtra("key_book");
        book = new Book();
        altUser = (User) it.getSerializableExtra("key_user");
        System.out.println(altUser);


        if (altBook != null) {
            salvar.setText("ALTERAR");
            edtNome.setText(altBook.getNome());
            edtDesc.setText(altBook.getDesc());
            book.setId(altBook.getId());
        } else {
            salvar.setText("CADASTRAR");
        }

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String desc = edtDesc.getText().toString();
                String name = edtNome.getText().toString();

                book.setDesc(desc);
                book.setNome(name);
                if(salvar.getText().toString().equals("CADASTRAR")) {
                    db.insertBook(book, altUser.getId());
                    Toast toast = Toast.makeText(CadUpdBook.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT);
                    toast.show();
                    System.out.println(db.searchBooks(altUser.getId()));
                } else {
                    db.updateBook(book);
                    //System.out.println(db.searchBooks(altUser.getId()).toString());
                    db.close();
                }
                limpar();
                finish();
            }
        });
    }


    public void limpar(){
        edtDesc.setText("");
    }

    public void cancelar(View view) {
        finish();
    }
}