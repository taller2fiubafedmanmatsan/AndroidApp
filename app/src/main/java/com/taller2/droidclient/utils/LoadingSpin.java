package com.taller2.droidclient.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.taller2.droidclient.R;

public class LoadingSpin {
    Dialog dialog;
    //..we need the context else we can not create the dialog so get context in constructor
    public LoadingSpin() { }

    public void showDialog(Activity activity) {

        dialog  = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(false);
        //...that's the layout i told you will inflate later
        dialog.setContentView(R.layout.loading_spin);
        //...finaly show it
        dialog.show();
    }

    //..also create a method which will hide the dialog when some work is done
    public void hideDialog(){
        dialog.dismiss();
    }

}
