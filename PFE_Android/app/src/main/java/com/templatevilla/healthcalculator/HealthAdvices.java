package com.templatevilla.healthcalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.templatevilla.healthcalculator.adapters.LazyAdapter;
import com.templatevilla.healthcalculator.models.RowItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

public class HealthAdvices extends AppCompatActivity implements LazyAdapter.clickInterface {

    private static final String ratings_fileName = "ratingAgain";
    private static Integer[] images = {R.drawable.heartrate,R.drawable.idealweight,R.drawable.idealweight, R.drawable.bmi, R.drawable.heartrate, R.drawable.bloodvol, R.drawable.blood_donate, R.drawable.calorie,
            R.drawable.waterintake, R.drawable.body_fat, R.drawable.bloodalcohol,
            R.drawable.pregnancy_new, R.drawable.ovulation, R.drawable.breath_count, R.drawable.blood_pressure};
    private static Integer[] themes = {R.style.PinkTheme,R.style.CyanTheme,R.style.OrangeTheme, R.style.BlueTheme, R.style.YellowTheme, R.style.CyanTheme, R.style.PinkTheme, R.style.GrayTheme, R.style.OrangeTheme,
            R.style.BlueTheme, R.style.GrayTheme, R.style.PinkTheme, R.style.YellowTheme,
            R.style.CyanTheme, R.style.OrangeTheme};
    private static Integer[] arrows = {R.drawable.arrow_pink,R.drawable.arrow_cyan,R.drawable.arrow_orange, R.drawable.arrow_blue, R.drawable.arrow_yellow, R.drawable.arrow_cyan, R.drawable.arrow_pink,
            R.drawable.arrow_gray, R.drawable.arrow_orange
            , R.drawable.arrow_blue, R.drawable.arrow_gray, R.drawable.arrow_pink, R.drawable.arrow_yellow,
            R.drawable.arrow_cyan, R.drawable.arrow_orange};
    private static Integer[] colors = {R.color.pinkcolorPrimary,R.color.cyancolorPrimary,R.color.orangecolorPrimary, R.color.bluecolorPrimary, R.color.yellowcolorPrimary, R.color.cyancolorPrimary, R.color.pinkcolorPrimary, R.color.graycolorPrimary, R.color.orangecolorPrimary
            , R.color.bluecolorPrimary, R.color.graycolorPrimary, R.color.pinkcolorPrimary, R.color.yellowcolorPrimary,
            R.color.cyancolorPrimary, R.color.orangecolorPrimary
    };
    int primaryColor;
    SharedPreferences ratePrefs;
    List<RowItem> rowItems;
    LazyAdapter lazyAdapter;
    RecyclerView listView;
    Toolbar toolbar;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.health_advices);
        MobileAds.initialize(this);
        init();
        ratePrefs = getSharedPreferences(ratings_fileName, 0);
 
        listView = findViewById(R.id.myadvicesList);
        rowItems = new ArrayList<>();
        /***************Mongo db*******************/
        // Replace the placeholder with your MongoDB deployment's connection string
        String uri = "mongodb+srv://talainimohammed:TALAINImd-658@cluster0.ha9wxl2.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("Smarthealth");
            MongoCollection<Document> collection = database.getCollection("articles");
            List<Document> studentList = collection.find().into(new ArrayList<>());
            System.out.println("Student list with an ArrayList:");
            for (Document student : studentList) {
                System.out.println(student.toJson());
            }
        }
        /**********************************/
        String[] strArr = {getResources().getString(R.string.diseasedetection),getResources().getString(R.string.healthadvices),getResources().getString(R.string.idealweight), getResources().getString(R.string.bmi_title), getResources().getString(R.string.heartrate), getResources().getString(R.string.bloodvol), getResources().getString(R.string.blood_donate), getResources().getString(R.string.calories), getResources().getString(R.string.waterintake),
                getResources().getString(R.string.bodyfat), getResources().getString(R.string.bloodalcohol), getResources().getString(R.string.pregnancy), getResources().getString(R.string.ovulation), getResources().getString(R.string.breath_count), getResources().getString(R.string.blood_pressure)};

        String[] strArr2 = {getResources().getString(R.string.diseasedetection_desc),getResources().getString(R.string.healthadvices_desc),getResources().getString(R.string.idealweight_desc), getResources().getString(R.string.bmi_desc), getResources().getString(R.string.heart_desc), getResources().getString(R.string.bloodvol_desc), getResources().getString(R.string.blood_don_desc), getResources().getString(R.string.calorie_desc), getResources().getString(R.string.waterintake_desc),
                getResources().getString(R.string.bodyfat_desc), getResources().getString(R.string.bloodalcohol_desc),
                getResources().getString(R.string.pregnancy_desc), getResources().getString(R.string.ovulation_desc), getResources().getString(R.string.breath_count_desc), getResources().getString(R.string.calc_bp_val)};
        for (int i = 0; i < strArr.length; i++) {
            this.rowItems.add(new RowItem(images[i], strArr[i], strArr2[i], themes[i], arrows[i], colors[i]));
        }
        lazyAdapter = new LazyAdapter(this.rowItems, this);
        listView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        listView.setAdapter(lazyAdapter);
        lazyAdapter.setListeners(this);
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(null);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        TextView text_title = findViewById(R.id.text_title);
        text_title.setText(getResources().getString(R.string.blood_donate));
        ImageView img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        primaryColor = ContextCompat.getColor(getApplicationContext(), R.color.pinkcolorPrimary);
    }






    @Override
    public void onRecItemClick(View view, int i) {
        switch (i) {
            case 0:
                passIntent(HealthAdvices.class);
                return;
            case 1:
                passIntent(IdealWeightCalc.class);
                return;
            case 2:
                passIntent(BodyMassIndex.class);
                return;
            case 3:
                passIntent(HeartRateCalculator.class);
                return;
            case 4:
                passIntent(BloodVolumeCalc.class);
                return;
            case 5:
                passIntent(BloodDonation.class);
                return;
            case 6:
                passIntent(CalorieCalculator.class);
                return;
            case 7:
                passIntent(WaterIntakeCalc.class);
                return;
            case 8:
                passIntent(BodyFatHome.class);
                return;
            case 9:
                passIntent(BloodAlcoholContent.class);
                return;
            case 10:
                passIntent(PregnancyCalc.class);
                return;
            case 11:
                passIntent(OvulationCalc.class);
                return;
            case 12:
                passIntent(RespirationRate.class);
                return;
            case 13:
                passIntent(BloodPressure.class);
                return;
            default:
        }
    }

    public void passIntent(Class aClass) {
        startActivity(new Intent(this, aClass));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
