package com.HartDroid.knn;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static TextView tV5 = null;
    TextView tv6 = null;
    TextView tv7=null;
    TextView tvE =null;
    Button bt3 = null;
    Button bt4=null;
    Button bt5=null;
    Button bt6=null;
    EditText et6 = null;
    SecondGraphView view;
    private boolean predict = false;
    private boolean two = false;
    private boolean three = false;

    int k = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        bt3 = super.findViewById(R.id.bt3);
        bt4 = super.findViewById(R.id.bt4);
        bt6 = super.findViewById(R.id.bt6);
        bt5= super.findViewById(R.id.bt5);
        tV5 = super.findViewById(R.id.tV5);
        tv6 = super.findViewById(R.id.tv6);
        tvE = super.findViewById(R.id.tvE);
        et6 = super.findViewById(R.id.et6);
        view = super.findViewById(R.id.secondGraph);
        tvE.setText("Yellow: Users dots | Blue: Cluster | Red: Cluster | Green: Cluster | Pink: Prediction dot"+"\n"+
                "The first two dots or three dots are the initial clusters, your last dot will be used as the prediciton dot");
        bt6.setOnClickListener(v -> {
            try {
                view.setDrawNeighbors(false);
                int dots = view.getTouches().size();
                if(et6.getText().toString().equals("2")&&dots>=3) {
                    Classify2();
                    two=true;
                    et6.setError(null);

                }
                else if(et6.getText().toString().equals("3")&&dots>=3) {
                    Classify3();
                    three=true;
                    et6.setError(null);
                }
                else
                    et6.setError("must be 2 or 3");
            }
            catch(Exception e){
            System.out.println(e.getMessage());
            }
        });
        bt3.setOnClickListener(v -> {
            predict = true;
            if(two){
                Classify2();
                two=false;
            }
            else if(three){
                Classify3();
                three=false;
            }
        });
        bt4.setOnClickListener(v -> {
            view.erase(true);
        });
        bt5.setOnClickListener(v -> {
            startNew();
        });
    }

    protected void startNew(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    protected double euclideanDistance(Touch one, Touch two) {
        return (Math.sqrt((Math.pow((one.getX())-(two.getX()),2))+(Math.pow((one.getY())-(two.getY()),2))));
    }

    protected void Classify2() {
        List<Touch> dataset = new ArrayList<>();

        for (int i = 0; i < view.getTouches().size() - 1; i++) {
            dataset.add(view.getTouches().get(i));
        }
        view.setDrawCluster();
        System.out.println("in classify2");
        List<Touch> distances = new ArrayList<>();
        List<Touch> cl1 = new ArrayList<>();
        List<Touch> cl2 = new ArrayList<>();

        Touch clMean;
        double meanX = 0;
        double meanY = 0;

        double mean2X = 0;
        double mean2Y = 0;

        //            distances.sort(Comparator.comparingDouble(People::getDistance));
        Touch cl2Mean;
        Touch cluster1 = view.getCluster1();
        Touch cluster2 = view.getCluster2();
        System.out.println(dataset.size());
        for (int i = 0; i < dataset.size(); i++) {
            double distance1 = euclideanDistance(dataset.get(i), cluster1);
            double distance2 = euclideanDistance(dataset.get(i), cluster2);
            if (distance1 < distance2) {
                dataset.get(i).setLabel("blue");
                dataset.get(i).setDistance(distance1);
                cl1.add(dataset.get(i));
                distances.add(dataset.get(i));
                meanX += dataset.get(i).getX();
                meanY += dataset.get(i).getY();


            } else {
                dataset.get(i).setLabel("red");
                dataset.get(i).setDistance(distance2);
                distances.add(dataset.get(i));
                cl2.add(dataset.get(i));
                mean2X += dataset.get(i).getX();
                mean2Y += dataset.get(i).getY();

            }
        }
        System.out.println(meanX + " " + mean2X);
        int counter = 0;
        while (counter < 10) {
            counter++;
            System.out.println("COUNTER " + counter);
            clMean = new Touch(meanX / cl1.size(), meanY / cl1.size());
            cl2Mean = new Touch(mean2X / cl2.size(), mean2Y / cl2.size());
            distances.clear();
            meanX = 0;
            meanY = 0;

            mean2X = 0;
            mean2Y = 0;

            cl1.clear();
            cl2.clear();
            for (Touch t : dataset) {
                double distance1 = euclideanDistance(t, clMean);
                double distance2 = euclideanDistance(t, cl2Mean);
                if (distance1 < distance2) {
                    t.setLabel("blue");
                    t.setDistance(distance1);
                    cl1.add(t);
                    distances.add(t);
                    meanX += t.getX();
                    meanY += t.getY();


                } else {
                    t.setLabel("red");
                    t.setDistance(distance2);
                    cl2.add(t);
                    distances.add(t);
                    mean2X += t.getX();
                    mean2Y += t.getY();


                }
            }
            System.out.println(meanX + " " + mean2X);


            System.out.println("GROUP ONE");
            for (Touch t : cl1) {
                //System.out.println(p.toString());
            }
            System.out.println(cl1.size());
            System.out.println("\nGROUP TWO");
            for (Touch t : cl2) {
                // System.out.println(p.toString());
            }
            System.out.println(cl2.size());
        }

        view.setCluster1List(cl1);
        view.setCluster2List(cl2);
        Touch query = view.getTouches().get(view.getTouches().size() - 1);
        view.setQuery(query);
        if(predict)
            Classify(dataset);

    }
    protected void Classify3(){
        List<Touch> dataset = new ArrayList<>();
        for (int i = 0; i < view.getTouches().size() - 1; i++) {
            dataset.add(view.getTouches().get(i));
        }
        view.setDrawCluster();
        System.out.println("in classify2");
        List<Touch> distances = new ArrayList<>();
        List<Touch> cl1 = new ArrayList<>();
        List<Touch> cl2 = new ArrayList<>();
        List<Touch> cl3 = new ArrayList<>();

        Touch clMean = null;
        double meanX = 0;
        double meanY = 0;

        double mean2X = 0;
        double mean2Y = 0;

        double mean3X = 0;
        double mean3Y = 0;


        //            distances.sort(Comparator.comparingDouble(People::getDistance));
        Touch cl2Mean = null;
        Touch cl3Mean = null;
        Touch cluster1 = view.getCluster1();
        Touch cluster2 = view.getCluster2();
        Touch cluster3 = view.getCluster3();
        for (int i = 0; i < dataset.size(); i++) {
            double distance1 = euclideanDistance(dataset.get(i), cluster1);
            double distance2 = euclideanDistance(dataset.get(i), cluster2);
            double distance3 = euclideanDistance(dataset.get(i), cluster3);


            if (distance1 < distance2 && distance1 < distance3) {

                dataset.get(i).setLabel("blue");
                dataset.get(i).setDistance(distance1);
                cl1.add(dataset.get(i));
                distances.add(dataset.get(i));
                meanX += dataset.get(i).getX();
                meanY += dataset.get(i).getY();



            }
            else if(distance2 < distance1 && distance2<distance3) {

                dataset.get(i).setLabel("red");
                dataset.get(i).setDistance(distance2);
                distances.add(dataset.get(i));
                cl2.add(dataset.get(i));
                mean2X += dataset.get(i).getX();
                mean2Y += dataset.get(i).getY();


            }
            else{

                dataset.get(i).setLabel("green");
                dataset.get(i).setDistance(distance3);
                distances.add(dataset.get(i));
                cl3.add(dataset.get(i));
                mean3X += dataset.get(i).getX();
                mean3Y += dataset.get(i).getY();


            }
        }
        System.out.println(cl1.size()+" "+cl2.size()+" "+cl3.size());
        System.out.println(meanX + " " + mean2X+" "+mean3X);
        int counter = 0;
        while (counter < 10) {
            counter++;
            System.out.println("COUNTER " + counter);
            clMean = new Touch(meanX / cl1.size(), meanY/ cl1.size());
            cl2Mean = new Touch(mean2X / cl2.size(), mean2Y / cl2.size());
            cl3Mean = new Touch(mean3X / cl3.size(), mean3Y / cl3.size());

            distances.clear();
            meanX = 0;
            meanY = 0;

            mean2X = 0;
            mean2Y = 0;

            mean3X = 0;
            mean3Y = 0;
            cl1.clear();
            cl2.clear();
            cl3.clear();
            for (Touch t : dataset) {
                double distance1 = euclideanDistance(t, clMean);
                double distance2 = euclideanDistance(t, cl2Mean);
                double distance3 = euclideanDistance(t, cl3Mean);
                if (distance1 < distance2 && distance1 < distance3) {
                    t.setLabel("blue");
                    t.setDistance(distance1);
                    cl1.add(t);
                    distances.add(t);
                    meanX += t.getX();
                    meanY += t.getY();


                }
                else if(distance2 < distance1 && distance2<distance3) {

                    t.setLabel("red");
                    t.setDistance(distance2);
                    distances.add(t);
                    cl2.add(t);
                    mean2X +=t.getX();
                    mean2Y += t.getY();

                }
                else{

                    t.setLabel("green");
                    t.setDistance(distance3);
                    distances.add(t);
                    cl3.add(t);
                    mean3X +=t.getX();
                    mean3Y += t.getY();


                }


            }
            System.out.println(meanX + " " + mean2X);
/*
            for (Player p : distances) {
                if (p.getLabel() == 1) {
                    cl1.add(p);

                }
                if (p.getLabel() == 2) {

                    cl2.add(p);
                }
                if (p.getLabel() == 3) {

                    cl3.add(p);
                }
            }*/
            // System.out.println(dataset.size() + " " + distances.size());
            System.out.println("GROUP ONE");
            for (Touch t : cl1) {
                //System.out.println(p.toString());
            }
            System.out.println(cl1.size());
            System.out.println("\nGROUP TWO");
            for (Touch t : cl2) {
                // System.out.println(p.toString());
            }
            System.out.println(cl2.size());
            System.out.println("\nGROUP THREE");
            for (Touch t : cl3) {
                // System.out.println(p.toString());
            }
            System.out.println(cl3.size());
        }
        view.setCluster1List(cl1);
        view.setCluster2List(cl2);
        view.setCluster3List(cl3);
        Touch query = view.getTouches().get(view.getTouches().size() - 1);
        view.setQuery(query);
        if(predict){
            Classify(dataset);
        }

    }

    protected void Classify(List<Touch> dataset) {
        predict=false;
        //Collections.shuffle(dataset); shuffle for lower k?
        k = (int) Math.sqrt(view.getTouches().size());
        if(k%2==0){
            k--;
        }
        tvE.setText("K for number of neighbors is "+k+"\n data is dynamic after knn prediction the dot will be put into the k means algorithm where it may not match the knn prediction");
        Touch query = view.getTouches().get(view.getTouches().size() - 1);
        //view.labelQuery(query);
        List<Touch> distances = new ArrayList<>();
        List<Touch> neighbors = new ArrayList<>();
        int count = 0;
        int blue = 0;
        int red = 0;
        int green=0;
        if (k > dataset.size()) {
            tv6.setText(R.string.kLarge);
        } else {
            for (Touch t : dataset) {
                double distance = euclideanDistance(t, query);
                t.setDistance(distance);
                distances.add(t);

            }
            System.out.println(distances.size());

            distances.sort(Comparator.comparingDouble(Touch::getDistance));

            for (Touch t : distances) {
                if (count != k) {
                    neighbors.add(t);

                    System.out.println("neighbor " + count + " " + t.toString());
                    count++;
                }

            }
            view.setNeighbors(neighbors);
            view.setDrawNeighbors(true);
            for (Touch t : neighbors) {
                if (t.getLabel().equals("blue"))
                    blue++;
                if (t.getLabel().equals("red"))
                    red++;
                if(t.getLabel().equals("green"))
                    green++;
            }
            if (blue > red && blue > green) {
                System.out.println("HELLO");
                tv6.setText("Prediction dot (pink) will be blue");
            } else if (blue < red && green < red) {
                System.out.println("HELLO2");

                tv6.setText("Preidctin dot (pink) will be red");
            }else if(green > red && green > blue){
                tv6.setText("green");
            }
            else {
                tv6.setText("Could be blue or red or green");
            }
        }


    }


}
