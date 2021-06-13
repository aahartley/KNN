package com.HartDroid.knn;

import android.annotation.SuppressLint;
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
    Button bt3 = null;
    Button bt4=null;
    EditText et = null;
    EditText et2 = null;
    SecondGraphView view;

    int k = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        bt3 = super.findViewById(R.id.bt3);
        bt4 = super.findViewById(R.id.bt4);
        et = super.findViewById(R.id.et5);
        et2 = super.findViewById(R.id.et6);
        tV5 = super.findViewById(R.id.tV5);
        tv6 = super.findViewById(R.id.tv6);
        tv7 = super.findViewById(R.id.tV7);
        view = super.findViewById(R.id.secondGraph);

        bt3.setOnClickListener(v -> {
            try {
                String x = et.getText().toString();
                String y = et.getText().toString();
                Touch guess = new Touch(Integer.parseInt(x), Integer.parseInt(y));
                if(view.getTouches().size()!=0)
                Classify2(guess);
            }
            catch(Exception e){
            System.out.println(e.getMessage());
            }
        });
        bt4.setOnClickListener(v -> {
            view.erase(true);
        });
    }


    protected double euclideanDistance(Touch one, Touch two) {
        return Math.sqrt(Math.pow(one.getX() - two.getX(), 2)) + (Math.pow((one.getY()) - (two.getY()), 2));
    }

    protected void Classify2(Touch guess) {
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
        Classify(dataset, guess);

    }

    protected void Classify(List<Touch> dataset, Touch guess) {
        //Collections.shuffle(dataset); shuffle for lower k?
        k = (int) Math.sqrt(view.getTouches().size());
        if(k%2==0){
            k--;
        }
        Touch query = view.getTouches().get(view.getTouches().size() - 1);
        view.setQuery(query);
        List<Touch> distances = new ArrayList<>();
        List<Touch> neighbors = new ArrayList<>();
        int count = 0;
        int blue = 0;
        int red = 0;
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
            for (Touch t : neighbors) {
                if (t.getLabel().equals("blue"))
                    blue++;
                if (t.getLabel().equals("red"))
                    red++;
            }
            if (blue > red) {
                System.out.println("HELLO");
                tv6.setText("blue");
            } else if (blue < red) {
                System.out.println("HELLO2");

                tv6.setText("red");
            } else {
                tv6.setText("Could be blue or red");
            }
            classifyGuess(guess, dataset);
        }


    }

    protected void classifyGuess(Touch guess,List<Touch> dataset) {
        k = (int) Math.sqrt(view.getTouches().size());
        if(k%2==0){
            k--;
        }
        view.setGuess(guess);
        List<Touch> distances = new ArrayList<>();
        List<Touch> neighbors = new ArrayList<>();
        int count = 0;
        int blue = 0;
        int red = 0;
        if (k > dataset.size()) {
            tv7.setText(R.string.kLarge);
        } else {
            for (Touch t : dataset) {
                double distance = euclideanDistance(t, guess);
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
            for (Touch t : neighbors) {
                if (t.getLabel().equals("blue"))
                    blue++;
                if (t.getLabel().equals("red"))
                    red++;
            }
            if (blue > red) {
                System.out.println("HELLO");
                tv7.setText("blue");
            } else if (blue < red) {
                System.out.println("HELLO2");

                tv7.setText("red");
            } else {
                tv7.setText("Could be blue or red");
            }
        }
    }
}
