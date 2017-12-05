package com.example.niluxer.parseapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotesActivity extends AppCompatActivity {

    @BindView(R.id.etNote)
    EditText etNote;

    @OnClick(R.id.btnSaveNote)
    public void saveNote()
    {
        String note = etNote.getText().toString();
        if (note.trim().length()>0) {
            ParseObject notes = new ParseObject("Notes");
            notes.put("username", username);
            notes.put("note", note);
            notes.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null) {
                        Toast.makeText(NotesActivity.this, "Note saved successfully", Toast.LENGTH_SHORT).show();
                        //Intent intent = new Intent(NotesActivity.this, HomeActivity.class );
                        //startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(NotesActivity.this, "Error saving note...", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @OnClick(R.id.btnCancelNote)
    public void cancelNote()
    {
        finish();

    }

    String username="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        ButterKnife.bind(this);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            username = currentUser.getUsername();
        } else {
            finish();
        }

    }
}
