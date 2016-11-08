package edu.cornell.chatter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.cornell.chatter.HelperClasses.ChatMessage;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.chat_message)
    EditText mMessage;

    @BindView(R.id.chat_send)
    Button mSend;

    @BindView(R.id.chat_messageList)
    RecyclerView mMessageList;

    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mSend.setOnClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(false);
        mMessageList.setLayoutManager(manager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ChatMessage, MessageViewHolder> adapter = new FirebaseRecyclerAdapter<ChatMessage, MessageViewHolder>(
                ChatMessage.class, android.R.layout.two_line_list_item, MessageViewHolder.class, myRef
        ) {
            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, ChatMessage model, int position) {
                viewHolder.mText1.setText(model.getMessage());
                String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(model.getTimestamp()));
                viewHolder.mText2.setText("Sent: " + dateString);
            }
        };
        mMessageList.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_send:
                ChatMessage chat = new ChatMessage("name", mMessage.getText().toString());
                myRef.push().setValue(chat);
                mMessage.setText("");
                break;
        }
    }

    private static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView mText1;
        TextView mText2;

        public MessageViewHolder(View v) {
            super(v);
            mText1 = (TextView) v.findViewById(android.R.id.text1);
            mText2 = (TextView) v.findViewById(android.R.id.text2);

        }
    }
}
