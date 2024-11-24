package com.example.nasafinalproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
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
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, NavigationView.OnNavigationItemSelectedListener  {
    private ArrayList<NasaImage> storedImages = new ArrayList<>();
    //    private MyListAdapter myAdapter;
    //    public static final String SW_NAME = "NAME";
    //    public static final String SW_HEIGHT = "HEIGHT";
    //    public static final String SW_MASS = "MASS";
    private TextView textView;
    private ProgressBar progressBar;
    private Button saveBtn;
    private TextView textView_date ;
    private TextView textView_url ;
    private TextView textView_hdUrl ;
    private TextView textView_title ;
    private TextView textView_explanation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


//        ListView theList = findViewById(R.id.theList);

        // check if the FrameLayout is loaded
        // If findViewById returns not null, then you are on a tablet.
        // If findViewById returns null, then you are running on a phone.
//        boolean isTablet = findViewById(R.id.fragmentLocation) != null;

//        myAdapter = new MyListAdapter();
//        theList.setAdapter(myAdapter);

//        theList.setOnItemClickListener((list, item, position, id) -> {
//            //Create a bundle to pass data to the new fragment
//            Bundle dataToPass = new Bundle();
//            dataToPass.putString(SW_NAME, swCharacters.get(position).getName());
//            dataToPass.putString(SW_HEIGHT, swCharacters.get(position).getHeight());
//            dataToPass.putString(SW_MASS, swCharacters.get(position).getMass());
//
//
//            //replacing a fragment (for tablets)
//            if(isTablet)
//            {
//                DetailsFragment dFragment = new DetailsFragment(); //add a DetailsFragment
//                dFragment.setArguments( dataToPass ); //pass it a bundle for information
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
//                        .commit(); //actually load the fragment.
//            }
//            //starting a new activity (for phones)
//            else //isPhone
//            {
//                Intent nextActivity = new Intent(MainActivity.this, EmptyActivity.class);
//                nextActivity.putExtras(dataToPass); //send data to next activity
//                startActivity(nextActivity); //make the transition
//            }
//        });

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
            finish();
//            Intent dadJokePage = new Intent(this, DadJoke.class);
//            startActivity(dadJokePage);
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
        private NasaImage nasaImage;


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
//              explanation = NasaData.getString("explanation");
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
                // Trigger the image download task when the button is clicked
                nasaImage = new NasaImage(date,explanation,hdUrl,url,title);
                storedImages .add(nasaImage);
                new DownloadImageTask().execute(nasaImage);
            });

        }
    }
    private class DownloadImageTask extends AsyncTask<NasaImage, Void, Boolean> {

        @Override
        protected Boolean doInBackground(NasaImage... params) {
            String imageUrl = params[0].getUrl();
            String title = params[0].getTitle();
            Bitmap newImg = null;

            try {
                // Open a connection to the image URL
                URL imgURL = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) imgURL.openConnection();

                // Decode the image from the input stream
                newImg = BitmapFactory.decodeStream(connection.getInputStream());

                if (newImg == null) {
                    Log.e("DownloadImageTask", "Failed to decode the image.");
                    return false;
                }

                // Generate a filename based on the title
                String fileName = title.replace(" ", "-");
                File imageFile = new File(getFilesDir(), fileName + ".jpg");

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


//    private class MyListAdapter extends BaseAdapter {
//
//        public int getCount() { return nasaImages.size(); }
//        public String getItem(int position) { return nasaImages.get(position).getTitle();}
//        public long getItemId(int position) { return (long) position; }
//
//        public View getView(int position, View old, ViewGroup parent) {
//
//            View newView = old;
//            LayoutInflater inflater = getLayoutInflater();
//
//            //make a new row:
//            if(newView == null) {
//                newView = inflater.inflate(R.layout.row_layout, parent, false);
//
//            }
//
//            //set what the text should be for this row:
//            TextView tView = newView.findViewById(R.id.nasa_img);
//            tView.setText(getItem(position));
//
//            //return it to be put in the table
//            return newView;
//        }
//    }

    private static class NasaImage {

        private String date;
        private String explanation;
        private String hdUrl;
        private String url;
        private String title;

        public NasaImage(String date, String explanation, String hdUrl, String url, String title) {
            this.date = date;
            this.explanation = explanation;
            this.hdUrl = hdUrl;
            this.url = url;
            this.title = title;
        }

        public String getDate() {
            return this.date;
        }

        public String getExplanation() {
            return this.explanation;
        }

        public String getHdUrl() {
            return this.hdUrl;
        }

        public String getUrl() {
            return this.url;
        }

        public String getTitle() {
            return this.title;
        }



    }
}