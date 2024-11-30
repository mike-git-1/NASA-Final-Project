package com.example.nasafinalproject;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;
import android.text.method.LinkMovementMethod;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, NavigationView.OnNavigationItemSelectedListener  {
    private ArrayList<NasaImage> storedImages = new ArrayList<>();


    private TextView textView;
    private ProgressBar progressBar;
    private Button saveBtn;
    private TextView textView_date ;
    private TextView textView_url ;
    private TextView textView_hdUrl ;
    private TextView textView_title ;
    private TextView textView_explanation;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase();


        // Find the views by their ID's
        Button pickDateButton = findViewById(R.id.pickDate);
        saveBtn = findViewById(R.id.saveBtn);
        textView = findViewById(R.id.textView);
        textView_date = findViewById(R.id.textView_dateFill);
        textView_url = findViewById(R.id.textView_urlFill);
        textView_hdUrl = findViewById(R.id.textView_hdUrlFill);
        textView_title = findViewById(R.id.textView_titleFill);
        //textView_explanation = findViewById(R.id.textView_explanationFill);
//        progressBar = findViewById(R.id.progressBar);

        // Set OnClickListener for the button
        pickDateButton.setOnClickListener( (click) -> {
            // Create a new instance of DatePickerFragment
            DatePickerFragment newFragment = new DatePickerFragment();

            // Show the DatePickerFragment using the FragmentManager
            newFragment.show(getSupportFragmentManager(), "datePicker");

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
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String dateQuery = year + "-" + (month+1) + "-" + day ; // Month is 0-based
        Toast.makeText(MainActivity.this, "Selected date: " + dateQuery, Toast.LENGTH_SHORT).show();
        // Update the textView
        textView.setText(dateQuery);
        // API request
        NasaAPI req = new NasaAPI();
        String base_apiUrl = "https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=";
        req.execute(base_apiUrl + dateQuery);

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


    private class NasaAPI extends AsyncTask<String, Integer, String> {

        private String date ;
        private String explanation ;
        private String hdUrl ;
        private String url ;
        private String title;
        private String fileName;
        long newId;


        public String doInBackground(String... args) {

            try {
                //create a URL object of what server to contact:
                URL apiUrl = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) apiUrl.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();
                //JSON reading:
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                //build the response
                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");

                }
                String result = sb.toString(); //result is the whole string

                // convert string to JSON:
                JSONObject NasaData = new JSONObject(result);

                // get the data from JSON response
                date = NasaData.getString("date");
                explanation = NasaData.getString("explanation");
                hdUrl = NasaData.getString("hdurl");
                url = NasaData.getString("url");
                title = NasaData.getString("title");


                return "Success";

            } catch (Exception e) {
                return "Exception Occurred: " + e.getMessage();
            }

        }


        public void onProgressUpdate(Integer... args) {
            //update progress bar
            progressBar.setProgress(args[0]);
        }

        // separate network operation (downloading files, API calls) from UI threads so that UI thread isnt blocked
        public void onPostExecute(String fromDoInBackground) {
            //myAdapter.notifyDataSetChanged();
            if ("Success".equals(fromDoInBackground)){
                textView_title.setText(title);
                //textView_explanation.setText(explanation);
                textView_date.setText(date);
                textView_url.setText(url);
                // construct the html <a>
                String htmlText = "Visit <a href='" + hdUrl + "'>" + hdUrl + "</a>";
                textView_hdUrl.setText(Html.fromHtml(htmlText));
                // Make the links clickable
                textView_hdUrl.setMovementMethod(LinkMovementMethod.getInstance());
            }

            // Set up button click listener to download the image
            saveBtn.setOnClickListener((click) -> {
                // Save to database and Trigger the image download task when the button is clicked
                ContentValues newRowValues = new ContentValues();
                //put items to their database columns
                newRowValues.put(MyOpener.COL_DATE, date);
                newRowValues.put(MyOpener.COL_EXPLANATION, explanation);
                newRowValues.put(MyOpener.COL_HDURL, hdUrl);
                newRowValues.put(MyOpener.COL_URL, url);
                newRowValues.put(MyOpener.COL_TITLE, title);
                // Generate a filename based on the id
                fileName = title.replace(" ", "-") + "-" + date + ".jpg";
                newRowValues.put(MyOpener.COL_IMG_FILE, fileName);

                //Now insert in the database:
                //insert method returns the new row's ID,
                newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

                Log.i("db", "Saved to db: " + fileName);
                new DownloadImageTask().execute(url, fileName);
            });

        }
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String imageUrl = params[0];
            String fileName = params[1];
            Bitmap newImg = null;

            try {
                // Open a connection to the image URL
                URL imgURL = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) imgURL.openConnection();

                // Decode the image from the input stream
                newImg = BitmapFactory.decodeStream(connection.getInputStream());

                File imageFile = new File(getFilesDir(), fileName );

                // Check if the file already exists
                if (!imageFile.exists()) {
                    try {
                        FileOutputStream outputStream = new FileOutputStream(imageFile);
                        newImg.compress(Bitmap.CompressFormat.JPEG, 80, outputStream); // Save as JPEG
                        outputStream.flush(); // Ensure data is written to file
                        outputStream.close();

                        return true;  // Image saved successfully

                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;  // Error saving image
                    }
                } else {
                    return false;  // Image already exists
                }

            } catch (IOException e) {
                e.printStackTrace();
                return false;  // Error downloading the image
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(MainActivity.this, "Image saved successfully!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "There was an error downloading the image or it's already downloaded.", Toast.LENGTH_LONG).show();
            }
        }
    }







}