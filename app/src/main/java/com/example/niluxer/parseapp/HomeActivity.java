package com.example.niluxer.parseapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    String username="";

    @BindView(R.id.tvWelcome)
    TextView tvWelcome;

    @BindView(R.id.lvNotes)
    ListView lvNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            username = currentUser.getUsername();
            tvWelcome.setText("Bienvenido(a)... " + username);
            loadNotes();
        }

    }

    private void loadNotes()
    {
        //final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        //final List<String> listNotes = new ArrayList<String>();
        final List<ParseObject> listNotes = new ArrayList<ParseObject>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Notes");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {
                    for (int i=0; i<objects.size();i++) {
                        ParseObject p = objects.get(i);
                        //listNotes.add(p.getString("note"));
                        listNotes.add(p);
                        System.out.println("Note: " + p.getString("note"));
                    }
                    //adapter.addAll(listNotes);
                    //lvNotes.setAdapter(adapter);
                    lvNotes.setAdapter(new NotesAdapter(HomeActivity.this, listNotes));
                } else {
                    Toast.makeText(HomeActivity.this, "Error reading notes...", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.mnuNotes:
                Intent intentNotes = new Intent(HomeActivity.this, NotesActivity.class);
                startActivity(intentNotes);
                break;
            case R.id.mnuLogout:
                ParseUser.logOut();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadNotes();
    }
}
