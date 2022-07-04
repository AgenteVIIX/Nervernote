package com.example.nevernote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CadUpdNote extends AppCompatActivity {

    DBHelper db = new DBHelper(this);
    private EditText edtDesc;
    private EditText edtNome;
    private Button salvar;
    private Note note, altNote;
    private Book altBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_upd_note);

        edtDesc = findViewById(R.id.edtDesc);
        edtNome = findViewById(R.id.edtNome);
        salvar = findViewById(R.id.button3);

        Intent it = getIntent();
        altBook = (Book) it.getSerializableExtra("key_book");
        altNote = (Note) it.getSerializableExtra("key_note");
        note = new Note();

        if (altNote != null) {
            salvar.setText("ALTERAR");
            edtDesc.setText(altNote.getDesc());
            edtNome.setText(altNote.getNome());
            note.setId(altNote.getId());
        } else {
            salvar.setText("CADASTRAR");
        }

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String desc = edtDesc.getText().toString();
                String name = edtNome.getText().toString();

                note.setDesc(desc);
                note.setNome(name);
                if(salvar.getText().toString().equals("CADASTRAR")) {
                    db.insertNote(note, altBook.getId());
                    Toast toast = Toast.makeText(CadUpdNote.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT);
                    toast.show();
                    System.out.println(db.searchNotes(altBook.getId()));
                } else {
                    db.updateNote(note);
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