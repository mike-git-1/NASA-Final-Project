//package com.example.nasafinalproject;
//
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link DetailsFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class DetailsFragment extends Fragment {
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        Bundle dataFromActivity = getArguments();
//
//        // Inflate the layout for this fragment
//        View result =  inflater.inflate(R.layout.fragment_details, container, false);
//
//        //show the name
//        TextView textView1 = result.findViewById(R.id.fill_me1);
//        textView1.setText(dataFromActivity.getString(MainActivity.SW_NAME));
//
//        //show the name
//        TextView textView2 = result.findViewById(R.id.fill_me2);
//        textView2.setText(dataFromActivity.getString(MainActivity.SW_HEIGHT));
//
//        //show the name
//        TextView textView3 = result.findViewById(R.id.fill_me3);
//        textView3.setText(dataFromActivity.getString(MainActivity.SW_MASS));
//
//        // Inflate the layout for this fragment
//        return result;
//    }
//}