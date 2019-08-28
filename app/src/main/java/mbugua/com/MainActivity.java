package mbugua.com;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NoteRecyclerAdapter mNoteRecyclerAdapter;
    private RecyclerView mRecyclerItems;
    private LinearLayoutManager mNotesLayoutManger;
    private CourseRecyclerAdapter mCourseRecyclerAdapter;
    private GridLayoutManager mCoursesLayoutManager;
    private NoteKeeperOpenHelper mDbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDbOpenHelper = new NoteKeeperOpenHelper(this);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NoteActivity.class));

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        initializeDisplayContent();

    }

    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mAdapterNotes.notifyDataSetChanged();
        mNoteRecyclerAdapter.notifyDataSetChanged();
    }

    private void initializeDisplayContent() {

        mRecyclerItems = (RecyclerView) findViewById(R.id.list_items);
        mNotesLayoutManger = new LinearLayoutManager(this);




        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        mNoteRecyclerAdapter = new NoteRecyclerAdapter(this, notes);
        mCoursesLayoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.course_grid_span));

        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        mCourseRecyclerAdapter = new CourseRecyclerAdapter(this, courses);
        DisplayNotes();


    }

    private void DisplayNotes() {
        mRecyclerItems.setLayoutManager(mNotesLayoutManger);
        mRecyclerItems.setAdapter(mNoteRecyclerAdapter);

        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

        selectNavigationMenuItem(R.id.nav_notes);

    }

    private void selectNavigationMenuItem(int id) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu= navigationView.getMenu();
        menu.findItem(id).setChecked(true);
    }

    private  void displayCourses() {
        mRecyclerItems.setLayoutManager(mCoursesLayoutManager);
        mRecyclerItems.setAdapter(mCourseRecyclerAdapter);
        selectNavigationMenuItem(R.id.nav_courses);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notes) {
            DisplayNotes();



        } else if (id == R.id.nav_courses) {
            displayCourses();

        } else if (id == R.id.nav_share) {
            handleSelection((R.string.nav_shared_enough));

        } else if (id == R.id.nav_send) {
            handleSelection((R.string.send_nav));

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleSelection(int message_id) {
        View view = findViewById(R.id.list_items);
        Snackbar.make(view, message_id, Snackbar.LENGTH_LONG).show();
    }
}
