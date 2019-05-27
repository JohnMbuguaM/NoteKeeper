package mbugua.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(NoteListActivity.this, MainActivity.class));
            }
        });

        initializeDisplayContent();


    }

    private void initializeDisplayContent() {
        final ListView ListNotes = (ListView) findViewById(R.id.list_note);

        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        ArrayAdapter<NoteInfo>  adapterNotes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
        ListNotes.setAdapter(adapterNotes);

        ListNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NoteListActivity.this, MainActivity.class);
//                NoteInfo note = (NoteInfo) ListNotes.getItemAtPosition(position);
                intent.putExtra(MainActivity.NOTE_POSITION, position);
                startActivity(intent);
            }
        });


    }

}