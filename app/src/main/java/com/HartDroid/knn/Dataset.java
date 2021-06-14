package com.HartDroid.knn;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Dataset extends AppCompatActivity {
    TextView tv9=null;
    List<String> lines = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        tv9 = super.findViewById(R.id.tv9);
        tv9.setMovementMethod(new ScrollingMovementMethod());


        lines=MainActivity.allLines;
        for(String s: lines) {
            System.out.println(s);
            tv9.append(s+"\n");
        }
    }

}
