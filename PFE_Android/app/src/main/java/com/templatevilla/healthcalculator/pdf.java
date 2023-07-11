package com.templatevilla.healthcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class pdf extends AppCompatActivity {
    com.github.barteksc.pdfviewer.PDFView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        pdfView = findViewById(R.id.pdfView);
        String SAMPLE_FILE = getIntent().getStringExtra("Path");
        try {
            File file = new File(getCacheDir(), SAMPLE_FILE);
            if (!file.exists()) {

                try {
                    InputStream asset = getAssets().open(SAMPLE_FILE);
                    FileOutputStream output;
                    output = new FileOutputStream(file);
                    final byte[] buffer = new byte[1024];
                    int size;
                    while ((size = asset.read(buffer)) != -1) {
                        output.write(buffer, 0, size);
                    }
                    asset.close();
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            pdfView.fromAsset(SAMPLE_FILE)
                    .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    // allows to draw something on the current page, usually visible in the middle of the screen
//                    .onDraw(onDrawListener)
//                    // allows to draw something on all pages, separately for every page. Called only for visible pages
//                    .onDrawAll(onDrawListener)
//                    .onLoad(onLoadCompleteListener) // called after document is loaded and starts to be rendered
//                    .onPageChange(onPageChangeListener)
//                    .onPageScroll(onPageScrollListener)
//                    .onError(onErrorListener)
//                    .onPageError(onPageErrorListener)
//                    .onRender(onRenderListener) // called after document is rendered for the first time
                    // called on single tap, return true if handled, false to toggle scroll handle visibility
//                    .onTap(onTapListener)
                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                    .password(null)
                    .scrollHandle(null)
                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                    // spacing between pages in dp. To define spacing color, set view background
                    .spacing(0)
                    .invalidPageColor(Color.WHITE) // color of page that is invalid and cannot be loaded
                    .load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}