//package com.example.nasafinalproject;
//
//import android.os.Bundle;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class EmptyActivity extends AppCompatActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_empty);
//
//        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from MainActivity
//
//        DetailsFragment dFragment = new DetailsFragment();
//        dFragment.setArguments( dataToPass ); //pass data to the the fragment
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragmentLocation, dFragment)
//                .commit();
//    }
//}