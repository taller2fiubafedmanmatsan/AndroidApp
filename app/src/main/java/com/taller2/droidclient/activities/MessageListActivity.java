package com.taller2.droidclient.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.taller2.droidclient.R;
import com.taller2.droidclient.adapters.MessageListAdapter;
import com.taller2.droidclient.model.BaseMessage;

import java.util.LinkedList;
import java.util.List;

public class MessageListActivity extends BasicActivity {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        //Lista de mensajes

        List<BaseMessage> messageList = new LinkedList<BaseMessage>();

        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new MessageListAdapter(this, messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
    }
}
