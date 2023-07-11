package com.templatevilla.healthcalculator.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import com.templatevilla.healthcalculator.pdf;

public class Constant {
    public static GradientDrawable getShapeDrawble(boolean isStroke,int primaryColor) {


        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[]{8, 8, 8, 8, 8, 8, 8, 8});
        shape.setColor(primaryColor);

        if (isStroke) {
            shape.setStroke(3, primaryColor);
            shape.setColor(Color.WHITE);
        }

        return shape;
    }

    public static void passIntent(Activity activity,String path) {


        Intent intent = new Intent(activity, pdf.class);
        intent.putExtra("Path",path);
        activity.startActivity(intent);
    }

}
