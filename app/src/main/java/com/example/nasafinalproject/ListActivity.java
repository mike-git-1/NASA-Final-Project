package com.example.nasafinalproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



import java.util.ArrayList;
import java.util.Arrays;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class ListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<NasaImage> storedImages = new ArrayList<>();
    private MyListAdapter myAdapter;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //create an adapter object and send it to the listVIew

        myAdapter = new MyListAdapter();

        loadDataFromDatabase(); //get any previously saved images

        ListView myList = findViewById(R.id.theListView);
        myList.setAdapter(myAdapter);



        //This gets the toolbar from the layout:
        Toolbar tBar = findViewById(R.id.toolbar);

        //This loads the toolbar, which calls onCreateOptionsMenu below:
        setSupportActionBar(tBar);

        //For NavigationDrawer:
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        int id = item.getItemId();
        if (id == R.id.item_home) {
            message = "You clicked on Home";
        } else if (id == R.id.item_image) {
            message = "You clicked on My Images";
        } else {
            return true;
        }

        //Look at your menu XML file. Put a case for every id in that file:
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;
    }

    // Needed for the OnNavigationItemSelected interface:
    @Override
    public boolean onNavigationItemSelected( MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Intent mainPage = new Intent(this, MainActivity.class);
            startActivity(mainPage);
        } else if (id == R.id.nav_image) {
            Intent storedImagesPage = new Intent(this, ListActivity.class);
            startActivity(storedImagesPage);
        } else if (id == R.id.nav_help) {
            finish();
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void loadDataFromDatabase()
    {
        //get a database connection:
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase();

        //  get all of the columns.
        String [] columns = {
                MyOpener.COL_ID,
                MyOpener.COL_TITLE,
                MyOpener.COL_DATE,
                MyOpener.COL_EXPLANATION,
                MyOpener.COL_HDURL,
                MyOpener.COL_URL
        };
        //query all the results from the database:
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);
        int titleColIndex = results.getColumnIndex(MyOpener.COL_TITLE);
        int dateColIndex = results.getColumnIndex(MyOpener.COL_DATE);
        int explanationColIndex = results.getColumnIndex(MyOpener.COL_EXPLANATION);
        int hdurlColIndex = results.getColumnIndex(MyOpener.COL_HDURL);
        int urlColIndex = results.getColumnIndex(MyOpener.COL_URL);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            long id = results.getLong(idColIndex);
            String date = results.getString(dateColIndex);
            String title = results.getString(titleColIndex);
            String explanation = results.getString(explanationColIndex);
            String hdUrl = results.getString(hdurlColIndex);
            String url = results.getString(urlColIndex);


            //add the new Contact to the array list:
            storedImages.add(new NasaImage(id, title, date,explanation,hdUrl,url));

        }

        Log.i("Title", "loadDataFromDatabase: " + storedImages.get(0).getTitle() +" " + storedImages.get(1).getTitle() );
        results.close();

        myAdapter.notifyDataSetChanged();
//        printCursor(results);
        //At this point, the elements array has loaded every row from the cursor.
    }

    private class MyListAdapter extends BaseAdapter {

        public int getCount() {  return storedImages.size(); }
        public NasaImage getItem(int position) { return storedImages.get(position);}
        public long getItemId(int position) { return (long) position; }

        public View getView(int position, View old, ViewGroup parent) {
            Log.d("ListActivity", "getView called for position: " + position);
            View newView = old;
            LayoutInflater inflater = getLayoutInflater();

            //make a new row:
            if(newView == null) {
                newView = inflater.inflate(R.layout.row_layout, parent, false);

            }

            //set what the text should be for this row:
            TextView tView_title = newView.findViewById(R.id.item_title);
            TextView tView_date = newView.findViewById(R.id.item_date);
            ImageButton trashBtn = newView.findViewById(R.id.trash);

            tView_title.setText(getItem(position).getTitle());
            tView_date.setText(getItem(position).getDate());

            trashBtn.setOnClickListener(c -> {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListActivity.this);
                alertDialogBuilder.setTitle("Do you want to delete this?")

                        //What is the message:
                        .setMessage("The selected item is:\n\n["+ position + "]  " +  storedImages.get(position).getTitle())

                        //what the Yes button does:
                        .setPositiveButton("Yes", (click, arg) -> {
                            //delete from database - parameterized queries
                            db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {Long.toString(storedImages.get(position).getId())});
                            //delete from array
                            storedImages.remove(position);
                            myAdapter.notifyDataSetChanged();
                            Snackbar.make(trashBtn, "Image deleted successfully!", Snackbar.LENGTH_LONG).show();

                        })

                        //What the No button does:
                        .setNegativeButton("No", (click, arg) -> {
                        })

                        //Show the dialog
                        .create().show();

            });
            //return it to be put in the table
            return newView;
        }
    }

}



