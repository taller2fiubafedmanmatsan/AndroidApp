package com.taller2.droidclient.adapters;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.taller2.droidclient.R;
import com.taller2.droidclient.model.UserMessage;
import com.taller2.droidclient.utils.GlideApp;

import io.github.kbiakov.codeview.CodeView;

import static com.taller2.droidclient.utils.GlideOptions.centerCropTransform;

public class ReceivedSnippetHolder extends MessageListAdapter.MyViewHolder {
    private TextView messageText, timeText, nameText;
    private TextView numbers;
    private EditText code;
    private View context;
    private CodeView codeView;

    ReceivedSnippetHolder(View itemView) {
        super(itemView);

        context = itemView;

        nameText = (TextView) itemView.findViewById(R.id.text_message_name);
        codeView = (CodeView) itemView.findViewById(R.id.code_view);
        /*numbers = (TextView) itemView.findViewById(R.id.code_number);
        code = (EditText) itemView.findViewById(R.id.code_view);

        code.setFocusable(false);
        code.setClickable(true);*/

        /*ScrollView scrollView = (ScrollView) itemView.findViewById(R.id.scrollView_code);

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // Disallow the touch request for parent scroll on touch of child view

                view.getParent().requestDisallowInterceptTouchEvent(false);
                view.onTouch(motionEvent);
                return true;
            }
        });*/
    }

    void bind(UserMessage message) {
        nameText.setText(message.getCreator().getNickname());

        codeView.setCode(message.getMessage(), "py");
        /*code.setText(message.getMessage());

        StringBuilder linesText = new StringBuilder("");

        for (int i = 1; i <= code.getLineCount(); i++) {
            linesText.append(i).append("\n");
        }

        numbers.setText(linesText.toString());*/


    }
}
