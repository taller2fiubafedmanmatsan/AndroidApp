package com.taller2.droidclient.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.view.Window;

import com.taller2.droidclient.R;

public class DownloadDialog {
    private Dialog dialog;
    private String filename;
    private String url;
    //..we need the context else we can not create the dialog so get context in constructor
    public DownloadDialog(String filename, String url) {
        this.filename = filename;
        this.url = url;
    }

    public void showDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage("You will download the file: " + filename)
                .setTitle("Download " + filename + "?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setDescription("This is a file from Hypechat");
                request.setTitle("Hypechat file");
                // in order for this if to run, you must use the android 3.2 to compile your app
                //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                //}
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);

                // get download service and enqueue file
                DownloadManager manager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
                request.setVisibleInDownloadsUi(true);
                manager.enqueue(request);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }
}
