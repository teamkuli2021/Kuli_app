package com.example.kuli_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;

public class IndoorMap extends AppCompatActivity implements View.OnClickListener {

    SensorManager sm = null;
    TextView accText = null;
    TextView gravText = null;
    Sensor accSens = null;
    List list;
    List list_g;
    LinearLayout layout_map,layout_dir;
    TextView textView_directions,textView_directions_1_1,textView_directions_1_2,textView_directions_1_3,textView_directions_1_4,textView_directions_1_5 = null;
    Button pointerButton;
    float values_acc[] = new float[3];
    float values_acc_new[] = new float[3];
    float diff_acc,diff_acc_1;
    int pos_x = 750,pos_y = 200;
    /* This responds to sensor events */
    final SensorEventListener sel = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            /* Isn't required for this example */

        }

        public void onSensorChanged(SensorEvent event) {
            Random r = new Random();

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                values_acc_new = event.values;
//                Ignoring z- direction
                float diff_acc_x = (float) values_acc_new[0] - values_acc[0];
                float diff_acc_y = (float) values_acc_new[1] - values_acc[1];

                diff_acc = abs(diff_acc_x) + abs(diff_acc_y);

              if (diff_acc > 5) {
//Randomly changing the position of the marker (dummy implementation)
//                    We can use the accelorometer , step sensor etc. to move the pointer
                    if ((pos_x >1000) | (pos_y>750)) {
//                        pos_x = 750;
//                        pos_y = 200;
                        pos_x = r.nextInt(1000);
                        pos_y = r.nextInt(750);
                    }
                    pos_x = (int) (pos_x + 10*abs(diff_acc_x));
                    pos_y = (int) (pos_y + 10*abs(diff_acc_y));
                    pointerButton.setTranslationX(pos_x);
                    pointerButton.setTranslationY(pos_y);
                    values_acc = values_acc_new.clone();

                }

//                if (diff_acc > 50) {
//                    Toast.makeText(getApplicationContext(),
//                            "'You've started walking'",
//                            Toast.LENGTH_SHORT).show();
//                }
            }

//            if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
//                values_grav = event.values;
//                gravText.setText("\nGravity Sensor\nx: " + values_grav[0] + "\ny: " + values_grav[1] + "\nz: " + values_grav[2]);
//            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_map);

//        From the last screen, get the selected indoor location and announce what is selected
        Intent caller = getIntent();
        String sel_indoor = caller.getStringExtra("selected_spinner");

        Toast.makeText(getApplicationContext(),
                "You're now near Pantaloons in " + sel_indoor,
                Toast.LENGTH_SHORT).show();


//* Get a SensorManager instance */
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        /* This corresponds to a TextView element in main.xml with android:id="@+id/accText" */
        accText = (TextView) findViewById(R.id.textView_acc);
        gravText = (TextView) findViewById(R.id.textView_grav);

        Sensor sensor_acc = (Sensor) sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(sel, sensor_acc, SensorManager.SENSOR_DELAY_NORMAL);
//
//        Sensor sensor_g = (Sensor) sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
//        sm.registerListener(sel, sensor_g, SensorManager.SENSOR_DELAY_NORMAL);

//        Sensor sensor_step = (Sensor) sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//        sm.registerListener(sel, sensor_step, SensorManager.SENSOR_DELAY_NORMAL);

        //Create the drop down menus to select source and destination inside the layout
        Spinner from_spinner = (Spinner) findViewById(R.id.from_spinner);
        ArrayAdapter<CharSequence> adapter_from = ArrayAdapter.createFromResource(this,
                R.array.from_array, android.R.layout.simple_list_item_activated_1);
        adapter_from.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        from_spinner.setAdapter(adapter_from);
        from_spinner.setSelection(0);

        Spinner to_spinner = (Spinner) findViewById(R.id.to_spinner);
        ArrayAdapter<CharSequence> adapter_to = ArrayAdapter.createFromResource(this,
                R.array.to_array, android.R.layout.simple_list_item_activated_1);
        adapter_to.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        to_spinner.setAdapter(adapter_to);
        to_spinner.setSelection(0);

        pointerButton = findViewById(R.id.pointerButton);
        pointerButton.setTranslationX(pos_x);
        pointerButton.setTranslationY(pos_y);
        pointerButton.setOnClickListener(this);



        layout_map =(LinearLayout)findViewById(R.id.layout_indoormap);
        layout_dir =(LinearLayout)findViewById(R.id.layout_directions);
        layout_map.setBackground(ContextCompat.getDrawable(this, R.drawable.layout1));

//        Directions through textview - in real implementation these can be replaced with dynamic textview creation
        textView_directions = findViewById(R.id.textView_directions_1);
        textView_directions_1_1 = findViewById(R.id.textView_directions_1_1);
        textView_directions_1_2 = findViewById(R.id.textView_directions_1_2);
        textView_directions_1_3 = findViewById(R.id.textView_directions_1_3);
        textView_directions_1_4 = findViewById(R.id.textView_directions_1_4);
        textView_directions_1_5 = findViewById(R.id.textView_directions_1_5);


    }

    public void route_ind(View v){

        layout_dir.setVisibility(View.GONE);
        layout_map.setBackground(ContextCompat.getDrawable(this, R.drawable.layout1_route1));
        layout_map.setVisibility(View.VISIBLE);

    }

    public void directions_ind(View v){


        layout_map.setBackgroundResource(0);
//        layout_map.setBackgroundColor(Color.TRANSPARENT);
        layout_map.setVisibility(View.GONE);
        layout_dir.setVisibility(View.VISIBLE);

        //        These directions can be computed from direction finding module
//        textView_directions.setText("\n 1.Walk Straight 10 steps \n\n 2.Take right \n\n 3.Walk 100 steps \n\n 4.Take a left \n\n 5.Walk 20 steps \n\n\n   You're destinations is on your right!");
        textView_directions.setText("1.Walk Straight 10 steps \n\n");
        textView_directions_1_1.setText("2.Take right \n\n");
        textView_directions_1_2.setText("3.Walk 100 steps \n\n");
        textView_directions_1_3.setText("4.Take a left \n\n");
        textView_directions_1_4.setText("5.Walk 20 steps \n\n");
        textView_directions_1_5.setText("You're destination is on your right!");

    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getApplicationContext(),
                "'You're near Pantaloons'",
                Toast.LENGTH_SHORT).show();

    }
}