package com.taller2.droidclient.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.taller2.droidclient.R;

import io.github.kbiakov.codeview.CodeView;
import io.github.kbiakov.codeview.adapters.Options;
import io.github.kbiakov.codeview.highlight.ColorTheme;
import io.github.kbiakov.codeview.highlight.Font;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class SnippetDialog {
    public void showDialog(Activity activity) {
        Dialog dialog = new Dialog(activity);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.message_snippet);

        dialog.show();

        final TextView numbers = dialog.findViewById(R.id.code_number);
        final EditText code = dialog.findViewById(R.id.code_view);

        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int lines = code.getLineCount();
                StringBuilder linesText = new StringBuilder("");

                for (int i = 1; i <= lines; i++) {
                    //linesText.concat("\n" + i);
                    linesText.append(i).append("\n");
                }

                numbers.setText(linesText.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
