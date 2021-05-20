//will my heart go on
package com.HartDroid.knn;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {
    TextView title = null;
    Button bt = null;
    TextView tV =null;
    TextView tV2 =null;
    TextView tV3= null;
    TextView tV4 =null;
    EditText et = null;
    EditText et2 =null;
    EditText et3=null;
    HashMap<String,Double> map = new HashMap<>();
    List<String> lines = new ArrayList<>();
    List<People> dataset = new ArrayList<>();
    int k =3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = super.findViewById(R.id.title);
        bt = super.findViewById(R.id.bt);
        tV = super.findViewById(R.id.tV);
        tV2 = super.findViewById(R.id.tV2);
        tV3= super.findViewById(R.id.tV3);
        tV4 = super.findViewById(R.id.tV4);
        et = super.findViewById(R.id.et);
        et2 = super.findViewById(R.id.et2);
        et3= super.findViewById(R.id.et3);

        map.put("first",-0.923);
        map.put("second",0.0214);
        map.put("third",0.965);
        map.put("crew",-1.87);
        map.put("adult",-0.228);
        map.put("child",4.38);
        map.put("male",-1.92);
        map.put("female",0.521);
        map.put("yes",-1.0);
        map.put("no",1.0);

        try {
            readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked");
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
                String classes = et.getText().toString().toLowerCase().trim();
                if(!classes.trim().toLowerCase().equals("first")&&!classes.trim().toLowerCase().equals("second")&&!classes.trim().toLowerCase().equals("third")&&
                        !classes.trim().toLowerCase().equals("crew")){
                    et.setError("must match above");

                }
                double classesD = map.get(classes);
                String age = et2.getText().toString().toLowerCase().trim();
                if(!age.trim().toLowerCase().equals("adult")&&!age.trim().toLowerCase().equals("child")){
                    et2.setError("must match above");
                }
                double ageD = map.get(age);
                String sex = et3.getText().toString().trim().toLowerCase();
                if(!sex.trim().toLowerCase().equals("male")&&!sex.trim().toLowerCase().equals("female")){
                    et3.setError("must match above");
                }
                double sexD = map.get(sex);
                People query = new People(classesD,ageD,sexD,0);
                Classify(query);



            }
        });
    }

    protected double euclideanDistance(People x, People y){
      //  System.out.println(x.toString()+" "+y.toString());
        return (Math.sqrt((Math.pow((x.getClasses())-(y.getClasses()),2))+(Math.pow((x.getAge())-(y.getAge()),2))+
                (Math.pow((x.getSex())-(y.getSex()),2))));
    }
    protected void readFile() throws IOException {
        InputStream is = getResources().openRawResource(R.raw.titanic);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String line;
        while((line =buf.readLine())!=null){
            if(line.contains("@")){
                System.out.println(line +"avoid");
            }
            else{
                System.out.println(line);
                String[] tokens = line.split(",");
                Double[] attr = new Double[4];
                for(int i=0; i<tokens.length;i++){
                    attr[i]= Double.parseDouble(tokens[i]);
                }
                dataset.add(new People(attr[0],attr[1],attr[2],attr[3]));
                //lines.add(line);
            }
        }
    }
    protected void Classify(People query){
        TreeMap<Double,People> distances = new TreeMap<>();
        List<People> neighbors = new ArrayList<>();
        int count=0;
        int survived=0;
        int dead=0;
        if(k>dataset.size()) {
            tV4.setText(R.string.kLarge);
        }
        else{
            for(People p: dataset){
                double distance = euclideanDistance(p,query);

                distances.put(distance,p);

            }


            for(Map.Entry<Double,People> entry: distances.entrySet()){
                if(count!=k){
                    neighbors.add(entry.getValue());

                    System.out.println("Dead "+ entry.getValue().getSurvived()+" Distance "+entry.getKey());
                    count++;
                }

            }
            for(People p: neighbors){
                if(p.getSurvived()==-1.0)
                    survived++;
                if(p.getSurvived()==1.0)
                    dead++;
            }
            if(survived > dead){
                tV4.setText(R.string.live);
            }
            else if(survived < dead){
                tV4.setText(R.string.die);
            }
        }


    }

}