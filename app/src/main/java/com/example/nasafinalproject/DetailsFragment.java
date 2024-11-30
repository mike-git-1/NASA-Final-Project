package com.example.nasafinalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

//**
// * A simple {@link Fragment} subclass.
// * Use the {@link DetailsFragment#newInstance} factory method to
// * create an instance of this fragment.
// */

public class DetailsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle dataFromActivity = getArguments();

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.fragment_details, container, false);

        //show the name
        TextView textViewTitle = result.findViewById(R.id.textView_titleFill);
        textViewTitle.setText(dataFromActivity.getString(ListActivity.IMG_TITLE));

        //show the date
        TextView textViewDate = result.findViewById(R.id.textView_dateFill);
        textViewDate.setText(dataFromActivity.getString(ListActivity.IMG_DATE));

        //show the description
        TextView textViewDesc = result.findViewById(R.id.textView_descFill);
        textViewDesc.setText(dataFromActivity.getString(ListActivity.IMG_DESC));
        textViewDesc.setMovementMethod(new ScrollingMovementMethod());

        //show the image
        ImageView imageView = result.findViewById(R.id.imageView);
        String imageFile = dataFromActivity.getString(ListActivity.IMG_FILE);

        if (imageFile != null ) {
            // decode the img
            Bitmap nasaImg = BitmapFactory.decodeFile(imageFile);
            imageView.setImageBitmap(nasaImg);
        } else {
            Bitmap placeholder = BitmapFactory.decodeResource(getResources(), R.drawable.image);
            imageView.setImageBitmap(placeholder);
        };



        // Inflate the layout for this fragment
        return result;
    }
}