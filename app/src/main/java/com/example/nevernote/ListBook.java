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

public class ListBook extends AppCompatActivity {

    private ListView listBooks;
    DBHelper db;
    Book book;
    ArrayList<Book> arrayListBooks;
    ArrayAdapter<Book> arrayAdapterBook;
    public Button addBook;
    private User altUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_book);

        addBook = findViewById(R.id.button7);
        listBooks = findViewById(R.id.listView2);
        registerForContextMenu(listBooks);

        Intent it = getIntent();
        altUser = (User) it.getSerializableExtra("key_user");


        listBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book book = (Book) arrayAdapterBook.getItem(i);
                Intent it = new Intent(ListBook.this, ListNote.class);
                it.putExtra("key_book", book);
                startActivity(it);
            }
        });

        listBooks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                book = arrayAdapterBook.getItem(i);
                return false;
            }
        });

        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ListBook.this, CadUpdBook.class);
                it.putExtra("key_user", altUser);
                startActivity(it);
            }
        });
    }

    public void preencheLista() {
        db = new DBHelper(ListBook.this);
        arrayListBooks = db.searchBooks(altUser.getId());
        db.close();
        if (listBooks != null) {
            arrayAdapterBook = new ArrayAdapter<Book>(
                    ListBook.this,
                    android.R.layout.simple_list_item_1,
                    arrayListBooks
            );
            listBooks.setAdapter(arrayAdapterBook);
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
        MenuItem mNovo = menu.add(Menu.NONE, 2, 2, "Editar");
        mDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                long retornoDB = 1;
                db = new DBHelper(ListBook.this);
                try {
                    retornoDB = db.deleteBook(book);
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
                Book book = (Book) arrayAdapterBook.getItem(positionSelected);
                Intent it = new Intent(ListBook.this, CadUpdBook.class);
                it.putExtra("key_book", book);
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