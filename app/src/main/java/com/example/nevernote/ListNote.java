package com.example.nevernote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListNote extends AppCompatActivity {
    private ListView listNotes;
    DBHelper db;
    Note note;
    ArrayList<Note> arrayListNotes;
    ArrayAdapter<Note> arrayAdapterNotes;
    public Button addNote;
    private Book altBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_note);

        addNote = findViewById(R.id.buttonAddNote);
        listNotes = findViewById(R.id.listNote);
        registerForContextMenu(listNotes);

        Intent it = getIntent();
        altBook = (Book) it.getSerializableExtra("key_book");

        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Note note = (Note) arrayAdapterNotes.getItem(i);
                Intent it = new Intent(ListNote.this, CadUpdNote.class);
                it.putExtra("key_note", note);
                startActivity(it);
            }
        });

        listNotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                note = arrayAdapterNotes.getItem(i);
                return false;
            }
        });

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ListNote.this, CadUpdNote.class);
                it.putExtra("key_book", altBook);
                startActivity(it);
            }
        });
    }

    public void preencheLista() {
        db = new DBHelper(ListNote.this);
        arrayListNotes = db.searchNotes(altBook.getId());
        db.close();
        if (listNotes != null) {
            arrayAdapterNotes = new ArrayAdapter<Note>(
                    ListNote.this,
                    android.R.layout.simple_list_item_1,
                    arrayListNotes
            );
            listNotes.setAdapter(arrayAdapterNotes);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        preencheLista();
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem mDelete = menu.add(Menu.NONE, 1, 1, "Deletar");
        MenuItem mNovo = menu.add(Menu.NONE, 2, 2, "Visualizar");
        mDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                long retornoDB = 1;
                db = new DBHelper(ListNote.this);
                try {
                    retornoDB = db.deleteNote(note);
                    db.close();
                    if (retornoDB == -1) {
                        alert("Erro de exclusão!");
                    } else {
                        alert("Caderno excluído com sucesso!");
                    }
                    preencheLista();
                } catch (Exception e){
                    alert("Você não pode excluir um Caderno que ainda tenha Notas!");
                }
                preencheLista();
                return false;
            }
        });
        mNovo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
                int positionSelected = menuInfo.position;
                Note note = (Note) arrayAdapterNotes.getItem(positionSelected);
                Intent it = new Intent(ListNote.this, ListNote.class);
                it.putExtra("key_book", note);
                startActivity(it);
                return false;
            }
        });
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    public void alert(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
    }