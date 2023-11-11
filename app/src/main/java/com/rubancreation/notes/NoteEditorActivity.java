package com.rubancreation.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.gun0912.tedpermission.BuildConfig;

import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {

    int noteId;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.share_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.share_menu){
            saveNote();
            return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_note_editor);

        //enable back button to main activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Setting the title
        getSupportActionBar().setTitle("Write Here");

        //Implement menu Inflater


        EditText editText = (EditText) findViewById(R.id.editText);

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);

        if (noteId != -1){

            editText.setText(MainActivity.notes.get(noteId));

        }else{

            MainActivity.notes.add("");
            noteId = MainActivity.notes.size() - 1;
            MainActivity.arrayAdapter.notifyDataSetChanged();

        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                MainActivity.notes.set(noteId, String.valueOf(charSequence));
                MainActivity.arrayAdapter.notifyDataSetChanged();

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.rubancreation.notes", Context.MODE_PRIVATE);

                HashSet<String> set = new HashSet<>(MainActivity.notes);

                sharedPreferences.edit().putStringSet("notes", set).apply();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
    private void saveNote() {
        EditText editText = findViewById(R.id.editText);
        String noteText = editText.getText().toString();

        if (noteText.trim().isEmpty()) {
            Toast.makeText(this, "Please add some Content", Toast.LENGTH_SHORT).show();
            return;
        }

        MainActivity.notes.set(noteId, noteText);
        MainActivity.arrayAdapter.notifyDataSetChanged();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("notes", Context.MODE_PRIVATE);

        HashSet<String> set = new HashSet<>(MainActivity.notes);
        sharedPreferences.edit().putStringSet("notes", set).apply();

        // Finish the current activity and go back to MainActivity
        finish();
    }

}