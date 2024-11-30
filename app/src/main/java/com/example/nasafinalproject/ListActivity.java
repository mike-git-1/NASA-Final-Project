package com.example.nasafinalproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.ImageView;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class ListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<NasaImage> storedImages = new ArrayList<>();
    private MyListAdapter myAdapter;
    private SQLiteDatabase db;

    public static final String IMG_TITLE = "TITLE";
    public static final String IMG_DATE = "DATE";
    public static final String IMG_DESC = "DESCRIPTION";
    public static final String IMG_FILE = "FILE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//      check if the FrameLayout is loaded
//      If findViewById returns not null, then you are on a tablet.
//      If findViewById returns null, then you are running on a phone.
        boolean isTablet = findViewById(R.id.fragmentLocation) != null;

        //create an adapter object and send it to the listVIew
        myAdapter = new MyListAdapter();

        loadDataFromDatabase(); //get any previously saved images

        ListView myList = findViewById(R.id.theListView);

        myList.setAdapter(myAdapter);

        myList.setOnItemClickListener((list, item, position, id) -> {
            Log.i("test", "onCreate: ");
            //Create a bundle to pass data to the new fragment
            Bundle dataToPass = new Bundle();
            dataToPass.putString(IMG_TITLE, storedImages.get(position).getTitle());
            dataToPass.putString(IMG_DATE, storedImages.get(position).getDate());
            dataToPass.putString(IMG_DESC, storedImages.get(position).getExplanation());

            // get the file and pass to bundle

            File file = new File(getFilesDir(), storedImages.get(position).getImageFile());
            String imageFile;
            if (file.exists()) {
                imageFile = file.getAbsolutePath();
            } else {
                imageFile = null;
            }
            dataToPass.putString(IMG_FILE, imageFile);


            //replacing a fragment (for tablets)
            if(isTablet)
            {
                DetailsFragment dFragment = new DetailsFragment(); //add a DetailsFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment.
            }
            //starting a new activity (for phones)
            else //isPhone
            {
                Intent nextActivity = new Intent(ListActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }
        });

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
                MyOpener.COL_URL,
                MyOpener.COL_IMG_FILE
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
        int imgFileColIndex = results.getColumnIndex(MyOpener.COL_IMG_FILE);
        int i = 0;
        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            long id = results.getLong(idColIndex);
            String date = results.getString(dateColIndex);
            String title = results.getString(titleColIndex);
            String explanation = results.getString(explanationColIndex);
            String hdUrl = results.getString(hdurlColIndex);
            String url = results.getString(urlColIndex);
            String imgFile = results.getString(imgFileColIndex);


            //add the new Contact to the array list:
            storedImages.add(new NasaImage(id, title, date,explanation,hdUrl,url,imgFile));
            Log.i("Title", "loadDataFromDatabase: " + storedImages.get(i).getId() +" " + storedImages.get(i).getImageFile() );
            i++;
        }


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
            View newView = old;
            LayoutInflater inflater = getLayoutInflater();

            //make a new row:
            if(newView == null) {
                newView = inflater.inflate(R.layout.row_layout, parent, false);

            }

            //set what the text should be for this row:
            TextView tView_title = newView.findViewById(R.id.item_title);
            TextView tView_date = newView.findViewById(R.id.item_date);
            ImageView trashBtn = newView.findViewById(R.id.trash);

            tView_title.setText(getItem(position).getTitle());
            tView_date.setText(getItem(position).getDate());

            trashBtn.setOnClickListener(c -> {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListActivity.this);
                alertDialogBuilder.setTitle("Are you sure you want to delete this?")

                        //What is the message:
                        .setMessage("You are deleting:\n\n["+ position + "]  " +  storedImages.get(position).getTitle())

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



