package com.templatevilla.healthcalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.templatevilla.healthcalculator.adapters.LazyAdapter;
import com.templatevilla.healthcalculator.models.RowItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LazyAdapter.clickInterface {

    private static final String ratings_fileName = "ratingAgain";
    private static Integer[] images = {R.drawable.heartrate,R.drawable.idealweight, R.drawable.bmi, R.drawable.heartrate, R.drawable.bloodvol, R.drawable.blood_donate, R.drawable.calorie,
            R.drawable.waterintake, R.drawable.pregnancy_new, R.drawable.blood_pressure};
    private static Integer[] themes = {R.style.PinkTheme,R.style.OrangeTheme, R.style.BlueTheme, R.style.YellowTheme, R.style.CyanTheme, R.style.PinkTheme, R.style.GrayTheme, R.style.OrangeTheme,
            R.style.BlueTheme,  R.style.PinkTheme,
             R.style.OrangeTheme};
    private static Integer[] arrows = {R.drawable.arrow_pink,R.drawable.arrow_orange, R.drawable.arrow_blue, R.drawable.arrow_yellow, R.drawable.arrow_cyan, R.drawable.arrow_pink,
            R.drawable.arrow_gray, R.drawable.arrow_orange
            , R.drawable.arrow_blue, R.drawable.arrow_pink,
             R.drawable.arrow_orange};
    private static Integer[] colors = {R.color.pinkcolorPrimary,R.color.cyancolorPrimary,R.color.orangecolorPrimary, R.color.bluecolorPrimary, R.color.yellowcolorPrimary, R.color.cyancolorPrimary, R.color.pinkcolorPrimary, R.color.graycolorPrimary, R.color.orangecolorPrimary
            , R.color.bluecolorPrimary, R.color.graycolorPrimary, R.color.pinkcolorPrimary, R.color.yellowcolorPrimary,
             R.color.orangecolorPrimary
    };
    SharedPreferences ratePrefs;
    List<RowItem> rowItems;
    LazyAdapter lazyAdapter;
    RecyclerView listView;
    Toolbar toolbar;

    ConnectionDetector cd;
    boolean interstitialCanceled;
    InterstitialAd mInterstitialAd;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this);
        init();
        ratePrefs = getSharedPreferences(ratings_fileName, 0);
        listView = findViewById(R.id.myList);
        rowItems = new ArrayList<>();
        String[] strArr = {getResources().getString(R.string.diseasedetection),getResources().getString(R.string.idealweight), getResources().getString(R.string.bmi_title), getResources().getString(R.string.heartrate), getResources().getString(R.string.bloodvol), getResources().getString(R.string.blood_donate), getResources().getString(R.string.calories), getResources().getString(R.string.waterintake),
                 getResources().getString(R.string.pregnancy), getResources().getString(R.string.blood_pressure)};

        String[] strArr2 = {getResources().getString(R.string.diseasedetection_desc),getResources().getString(R.string.idealweight_desc), getResources().getString(R.string.bmi_desc), getResources().getString(R.string.heart_desc), getResources().getString(R.string.bloodvol_desc), getResources().getString(R.string.blood_don_desc), getResources().getString(R.string.calorie_desc), getResources().getString(R.string.waterintake_desc),

                getResources().getString(R.string.pregnancy_desc), getResources().getString(R.string.calc_bp_val)};
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
        setTitle(getResources().getString(R.string.app_name));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionRate:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id="));
                startActivity(intent);
                break;
            case R.id.actionFeedback:
                sendFeedBack();
                break;

        }
        return super.onOptionsItemSelected(item);

    }

    private void sendFeedBack() {
        Intent localIntent = new Intent(Intent.ACTION_SEND);
        localIntent.putExtra(Intent.EXTRA_EMAIL, R.string.mail_feedback_email);
        localIntent.putExtra(Intent.EXTRA_CC, "");
        String str;
        try {
            str = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            localIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.feedback_msg));
            localIntent.putExtra(Intent.EXTRA_TEXT, "\n\n----------------------------------\n" + getResources().getString(R.string.device_os) +
                    Build.VERSION.RELEASE + "\n" + getResources().getString(R.string.app_version) + str + "\n Device Brand: " + Build.BRAND +
                    "\n" + getResources().getString(R.string.device_model) + Build.MODEL + "\n" + getResources().getString(R.string.manufacturer) + Build.MANUFACTURER);
            localIntent.setType("message/rfc822");
            startActivity(Intent.createChooser(localIntent, getResources().getString(R.string.email_client)));
        } catch (Exception ignored) {
        }
    }

    public void onBackPressed() {
        finishAffinity();
    }


    @Override
    public void onRecItemClick(View view, int i) {
        switch (i) {
            case 0:
                passIntent(DiseaseDetector.class);
                return;
            /*case 1:
                passIntent(HealthAdvices.class);
                return;*/
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
            /*case 9:
                passIntent(BodyFatHome.class);
                return;
            case 10:
                passIntent(BloodAlcoholContent.class);
                return;*/
            case 8:
                passIntent(PregnancyCalc.class);
                return;
            /*case 12:
                passIntent(OvulationCalc.class);
                return;
            case 13:
                passIntent(RespirationRate.class);
                return;*/
            case 9:
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
        mInterstitialAd = null;
        interstitialCanceled = true;
        super.onPause();
    }

    @Override
    protected void onResume() {
        interstitialCanceled = false;
        if (getResources().getString(R.string.ADS_VISIBILITY).equals("YES")) {
            CallNewInsertial();
        }
        super.onResume();
    }

//    private void requestNewInterstitial() {
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mInterstitialAd.loadAd(adRequest);
//    }
    private void CallNewInsertial() {
        cd = new ConnectionDetector(MainActivity.this);
        if (cd.isConnectingToInternet()) {

            AdRequest adRequest = new AdRequest.Builder().build();

            InterstitialAd.load(this,getResources().getString(R.string.interestial_ad_id), adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd;
//                    Log.i(TAG, "onAdLoaded");
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
//                    Log.i(TAG, loadAdError.getMessage());
                    mInterstitialAd = null;
                }
            });


        }



    }
//    private void CallNewInsertial() {
//        cd = new ConnectionDetector(MainActivity.this);
//        if (cd.isConnectingToInternet()) {
//            mInterstitialAd = new InterstitialAd(MainActivity.this);
//            mInterstitialAd.setAdUnitId(getString(R.string.interestial_ad_id));
//            requestNewInterstitial();
////            mInterstitialAd.setAdListener(new AdListener() {
////                public void onAdClosed() {
//////                    ContinueIntent();
////                    startActivity(new Intent(this, aClass));
////                }
////            });
//
//        }
//    }
}
