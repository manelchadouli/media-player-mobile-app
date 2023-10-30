package com.example.tp7;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class DownloadHelper extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;


    private EditText editTextUrl;
    private EditText editTextTitle;
   // private EditText editTextDescription;
    private Button buttonDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_helper);

        editTextUrl = findViewById(R.id.editTextUrl);
        editTextTitle = findViewById(R.id.editTextTitle);
       // editTextDescription = findViewById(R.id.editTextDescription);
        buttonDownload = findViewById(R.id.buttonDownload);

    buttonDownload.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url = editTextUrl.getText().toString().trim();
            String title = editTextTitle.getText().toString().trim();
           // String description = editTextDescription.getText().toString().trim();

            //if (url.isEmpty() || title.isEmpty() || description.isEmpty()) {
                //Toast.makeText(DownloadHelper.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
               // return;
            //}

            if (ContextCompat.checkSelfPermission(DownloadHelper.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(DownloadHelper.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
            } else {
                startDownload(url, title);
            }
        }
    });
}

    private void startDownload(String url, String title) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(title);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // Construct the destination path
        String destinationDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        String destinationPath = destinationDir + File.separator + title + ".mp3";
        request.setDestinationUri(Uri.fromFile(new File(destinationPath)));

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }

        Toast.makeText(this, "Download started", Toast.LENGTH_SHORT).show();

        // Set the result for the MainActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("songTitle", title);
        resultIntent.putExtra("songPath", destinationPath);
        setResult(Activity.RESULT_OK, resultIntent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String url = editTextUrl.getText().toString().trim();
                String title = editTextTitle.getText().toString().trim();
              //  String description = editTextDescription.getText().toString().trim();

                startDownload(url, title);
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
