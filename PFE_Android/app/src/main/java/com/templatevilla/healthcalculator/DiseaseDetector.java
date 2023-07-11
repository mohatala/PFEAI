package com.templatevilla.healthcalculator;

import static com.templatevilla.healthcalculator.util.Constant.getShapeDrawble;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class DiseaseDetector extends AppCompatActivity {
    Button add;
    AutoCompleteTextView actv;
    TextView symplistviewer,diseaseresult;
    Toolbar toolbar;
    int primaryColor;
    Button calcsymp;
    String[] Symptomes ={"chills", "vomiting", "fatigue", "weight_loss", "cough", "high_fever", "breathlessness", "sweating", "loss_of_appetite", "mild_fever"};
    ArrayList<String> Symptomes_list = new ArrayList<String>();
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(R.style.PinkTheme);
        setContentView(R.layout.disease_detector);
        init();
        symplistviewer=findViewById(R.id.symplistviewer);
        diseaseresult=findViewById(R.id.diseaseresult);
        calcsymp = findViewById(R.id.calcsymp);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        calcsymp.setBackground(getShapeDrawble(false, primaryColor));
        add = findViewById(R.id.addsymp);
        add.setBackground(getShapeDrawble(false, primaryColor));
        /**************************************/
        //Creating the instance of ArrayAdapter containing list of language names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,Symptomes);
        //Getting the instance of AutoCompleteTextView
        actv= (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.RED);
        /***************************************/
        add.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Symptomes_list.add(actv.getText().toString());
                symplistviewer.setText(Symptomes_list.toString());
            }
        });
        calcsymp.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                /********************************************************/
                // Create a socket object
                Socket clientSocket = new Socket();

                try {
                    // Get local machine name
                    String host = java.net.InetAddress.getLocalHost().getHostName();

                    // Connect to the server
                    clientSocket.connect(new java.net.InetSocketAddress("54.204.68.125", 8000));

                    // Send a question to the server
                    String sym="";
                    for (int i=0; i< Symptomes_list.size();i++){
                        sym+= Symptomes_list.get(i)+',';
                    }
                    Log.d("symplist",sym);
                    String s = "chills, vomiting, fatigue, weight_loss, cough, high_fever, breathlessness, sweating, loss_of_appetite, mild_fever, yellowing_of_eyes, swelled_lymph_nodes, malaise, phlegm, chest_pain, blood_in_sputum";
                    OutputStream outputStream = clientSocket.getOutputStream();
                    outputStream.write(sym.getBytes());

                    // Receive the answer from the server
                    byte[] buffer = new byte[1024];
                    InputStream inputStream = clientSocket.getInputStream();
                    int bytesRead = inputStream.read(buffer);
                    String answer = new String(buffer, 0, bytesRead);

                    // Print the answer
                    diseaseresult.setText(answer);
                    Log.d("Disease",answer);
                    // Close the connection with the server
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                 /******************************************************/

                //DiseaseDetector.this.startActivity(new Intent(DiseaseDetector.this, EligibilityCheck.class));
            }
        });

    }

    private void init() {


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(null);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        TextView text_title = findViewById(R.id.text_title);
        text_title.setText(getResources().getString(R.string.diseasedetection));
        ImageView img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        primaryColor = ContextCompat.getColor(getApplicationContext(), R.color.pinkcolorPrimary);
    }
}
