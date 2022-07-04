package com.example.nevernote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "nevernote.db";

    // table books
    private static final String TABLE_BOOKS = "books";
    private static final String COL_ID_BOOKS = "id";
    private static final String COL_NAME_BOOK = "name";
    private static final String COL_DESC_BOOK = "descricao";
    private static final String COL_IDUSER = "id_user";

    // table notes
    private static final String TABLE_NOTES = "notes";
    private static final String COL_ID_NOTES = "id";
    private static final String COL_NAME_NOTE = "name";
    private static final String COL_DESC_NOTE = "descricao";
    private static final String COL_IDBOOK = "id_book";
    private static final String TAG = "tag";

    //table user
    private static final String TABLE_USER = "users";
    private static final String COL_ID_USERS = "id";
    private static final String COL_EMAIL = "email";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";

    SQLiteDatabase db;
    private static final String TABLE_NOTES_CREATE = "create table " + TABLE_NOTES + "("+COL_ID_NOTES+
            " integer primary key autoincrement, "+COL_IDBOOK+ " integer not null, "+COL_NAME_NOTE+ " text not null, " +COL_DESC_NOTE+ " text not null, FOREIGN KEY ("+COL_IDBOOK+") REFERENCES "+TABLE_BOOKS+" ("+COL_ID_BOOKS+") ON DELETE RESTRICT);";

    private static final String TABLE_BOOKS_CREATE = "create table " +TABLE_BOOKS+ "(" +COL_ID_BOOKS+ " integer primary key autoincrement, "
           +COL_NAME_BOOK+ " text not null, "+COL_DESC_BOOK+ " text not null, "+COL_IDUSER+ " integer not null, FOREIGN KEY ("+COL_IDUSER+") REFERENCES "+TABLE_USER+"("+COL_ID_USERS+") ON DELETE RESTRICT);";

    private static final String TABLE_USERS_CRATE = "create table "+TABLE_USER+ "("+COL_ID_USERS+ " integer primary key autoincrement, "
            +COL_EMAIL+ " text not null unique, "+COL_USERNAME+ " text not null unique, "+COL_PASSWORD+ " text not null);";

    private static final String CREATE_ADMIN_USER = "insert into users (email, username, password) VALUES ('admin@gmail.com', 'admin', 'admin');";

    private static final String CREATE_BOOK = "insert into books (name, descricao, id_user) VALUES ('livro', 'livro de notas', 1)";

    private static final String drop = "drop table books";
    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_USERS_CRATE);
        db.execSQL(TABLE_BOOKS_CREATE);
        db.execSQL(TABLE_NOTES_CREATE);
        db.execSQL(CREATE_ADMIN_USER);
        db.execSQL(CREATE_BOOK);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String query1 = "DROP TABLE IF EXISTS " +TABLE_USER;
        String query2 = "DROP TABLE IF EXISTS " +TABLE_BOOKS;
        String query3 = "DROP TABLE IF EXISTS " +TABLE_NOTES;
        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
        this.onCreate(db);
    }


    //CREATING DATA
    public void insertUser(User u) {
        db = this.getWritableDatabase();
        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            values.put(COL_EMAIL,u.getEmail());
            values.put(COL_USERNAME, u.getUsername());
            values.put(COL_PASSWORD, u.getPassword());
            db.insertOrThrow(TABLE_USER,null, values);
            db.setTransactionSuccessful();
        }catch (Exception e) {
            Log.d(TAG, "Erro ao cadastrar o Usuário");
        }finally {
            db.endTransaction();
        };
    }

    public void insertBook(Book b, int id) {
        db = this.getWritableDatabase();
        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            values.put(COL_NAME_BOOK, b.getNome());
            values.put(COL_DESC_BOOK, b.getDesc());
            values.put(COL_IDUSER, id);
            db.insertOrThrow(TABLE_BOOKS,null, values);
            db.setTransactionSuccessful();
        }catch (Exception e) {
            Log.d(TAG, "Erro ao criar um novo Caderno");
        }finally {
            db.endTransaction();
        };
    }

    public void insertNote(Note n, int id) {
        db = this.getWritableDatabase();
        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            values.put(COL_NAME_NOTE, n.getNome());
            values.put(COL_DESC_NOTE, n.getDesc());
            values.put(COL_IDBOOK, id);
            db.insertOrThrow(TABLE_NOTES,null, values);
            db.setTransactionSuccessful();
            System.out.println("sim");
        }catch (Exception e) {
            Log.d(TAG, "Erro ao adicionar uma nova Nota");
            System.out.println("nao");
        }finally {
            db.endTransaction();
        };
    }

    //READING DATA
    public ArrayList<Book> searchBooks(int id) {
        String[] colunas = {COL_ID_BOOKS, COL_IDUSER, COL_NAME_BOOK, COL_DESC_BOOK};
        String s = ""+id;
        String[] colunas2 = {s};
        Cursor c = getReadableDatabase().query(TABLE_BOOKS, colunas, COL_IDUSER+"=?", colunas2, null, null,
                "upper(name)", null);
        ArrayList<Book> list = new ArrayList<Book>();
        while(c.moveToNext()) {
            Book b = new Book();
            b.setId(c.getInt(0));
            b.setId_user(c.getInt(1));
            b.setNome(c.getString(2));
            b.setDesc(c.getString(3));
            list.add(b);
        }
        return list;
    }

    public User searchId(String username) {
        //String[] colunas = {COL_ID_USERS};
        //String[] query = {username};
        User u = new User();
        Cursor c = getReadableDatabase().query(TABLE_USER, null, null, null, null, null,
                null, null);
        while (c.moveToNext()) {
            //System.out.println(username.equals(cursor.getString(2)) && pass.equals(cursor.getString(3)));
            if (username.equals(c.getString(2))) {
                u.setId(c.getInt(0));
                u.setEmail(c.getString(1));
                u.setUsername(c.getString(2));
                u.setPassword(c.getString(3));
            }
        }
        return u;
    }



    public ArrayList<Note> searchNotes(int id) {
        String[] colunas = {COL_ID_NOTES, COL_IDBOOK, COL_NAME_NOTE, COL_DESC_NOTE};
        String s = ""+id;
        String[] colunas2 = {s};
        Cursor c = getReadableDatabase().query(TABLE_NOTES, colunas, COL_IDBOOK+"=?", colunas2, null, null,
                "upper(name)", null);
        ArrayList<Note> list = new ArrayList<Note>();
        while(c.moveToNext()) {
            Note n = new Note();
            n.setId(c.getInt(0));
            n.setId_book(c.getInt(1));
            n.setNome(c.getString(2));
            n.setDesc(c.getString(3));
            list.add(n);
        }
        return list;
    }

    public String searchPassword(String username, String pass){
        String[] colunas = {COL_PASSWORD};
        String[] query = {username};
        String password="não encontrado";
        db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            Cursor cursor = db.query(TABLE_USER, null, null, null, null, null,
                    null, null);
            //System.out.println(cursor.getCount() + "c");
            try {
                while (cursor.moveToNext()) {
                    //System.out.println(username.equals(cursor.getString(2)) && pass.equals(cursor.getString(3)));
                    if (username.equals(cursor.getString(2)) && pass.equals(cursor.getString(3))) {
                        password = cursor.getString(3);
                        db.setTransactionSuccessful();
                        break;
                    }
                }
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        }catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return password;
    }

    //DELETING DATA
    public long deleteBook(Book b) {
        long retornoDB;
        db = this.getWritableDatabase();
        String[] args = {String.valueOf(b.getId())};
        retornoDB = db.delete(TABLE_BOOKS, COL_ID_BOOKS+"=?",  args);
        return retornoDB;
    }

    public long deleteNote(Note n) {
        long retornoDB;
        db = this.getWritableDatabase();
        String[] args = {String.valueOf(n.getId())};
        retornoDB = db.delete(TABLE_NOTES, COL_ID_NOTES+"=?",  args);
        return retornoDB;
    }

    //UPDATING DATA
    public long upadateUser(User u) {
        long retornoDB;
        db= this.getWritableDatabase();
        String[] args = {String.valueOf(u.getId())};
        ContentValues values = new ContentValues();
        values.put(COL_EMAIL,u.getEmail());
        values.put(COL_USERNAME, u.getUsername());
        values.put(COL_PASSWORD, u.getPassword());
        retornoDB = db.update(TABLE_USER, values, "id=?", args);
        db.close();
        return retornoDB;
    }

    public long updateBook(Book b) {
        long retornoDB;
        System.out.println(b.getNome() + b.getDesc());
        db= this.getWritableDatabase();
        String[] args = {String.valueOf(b.getId())};
        ContentValues values = new ContentValues();
        System.out.println(b.getNome());
        System.out.println(b.getId());
        values.put(COL_NAME_BOOK,b.getNome());
        values.put(COL_DESC_BOOK, b.getDesc());
        //values.put(COL_NASC, j.getNasc());
        //values.put(COL_IDUSER, b.getId_user());
        retornoDB = db.update(TABLE_BOOKS, values, "id=?", args);
        db.close();
        return retornoDB;
    }

    public long updateNote(Note n) {
        long retornoDB;
        db= this.getWritableDatabase();
        String[] args = {String.valueOf(n.getId())};
        ContentValues values = new ContentValues();
        values.put(COL_NAME_NOTE, n.getNome());
        values.put(COL_DESC_NOTE, n.getDesc());
        retornoDB = db.update(TABLE_NOTES, values, "id=?", args);
        db.close();
        return retornoDB;
    }
}
