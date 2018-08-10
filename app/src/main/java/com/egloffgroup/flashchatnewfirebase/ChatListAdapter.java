package com.egloffgroup.flashchatnewfirebase;

import android.app.Activity;
import android.content.Context;
import android.drm.DrmStore;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {

    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String mDisplayName;
    private ArrayList<DataSnapshot> mSnapshotsList;

    /*
        Firebase listener who checks if anything happens to the database
     */
    private ChildEventListener mListener = new ChildEventListener() {

        /*
            This event triggers when new chat message is added to de Database
         */
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            mSnapshotsList.add(dataSnapshot);
            //Notify the listview that has to refresh
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public ChatListAdapter(Activity activity, DatabaseReference databaseReference, String displayName) {
        mActivity = activity;
        mDatabaseReference = databaseReference.child("messages");
        mDatabaseReference.addChildEventListener(mListener);
        mDisplayName = displayName;
        mSnapshotsList = new ArrayList<>();
    }

    //Class to inflate the row layout
    static class ViewHolder {
        TextView authorName;
        TextView body;
        LinearLayout.LayoutParams params;

    }

    /*
        We change the return type from Object to InstantMessage
     */
    @Override
    public InstantMessage getItem(int position) {
        DataSnapshot snapshot = mSnapshotsList.get(position);
        return snapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /*
        ListView reuses Layouts of offscreen rows that where created before. The method getView is used
        by the Adapter to get a offscreen layout to add new data. If no offscreen layout is available
        it creates (inflates) a new layout.
        The Views elements of the row layout are represented in the static class.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Check if an existing row can be reuse
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_msg_row, parent,false);
            final ViewHolder holder = new ViewHolder();
            holder.authorName = (TextView) convertView.findViewById(R.id.author);
            holder.body = (TextView) convertView.findViewById(R.id.message);
            holder.params = (LinearLayout.LayoutParams) holder.authorName.getLayoutParams();
            convertView.setTag(holder);
        }

        final InstantMessage message = getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();

        boolean isMe = message.getAuthor().equals(mDisplayName);
        setChatRowAppearance(isMe, holder);

        holder.authorName.setText(message.getAuthor());
        holder.body.setText(message.getMessage());

        return convertView;
    }

    @Override
    public int getCount() {
        return mSnapshotsList.size();
    }

    private void setChatRowAppearance (boolean isItMe, ViewHolder holder) {
        if (isItMe) {
            holder.params.gravity = Gravity.END;
            holder.authorName.setTextColor(Color.GREEN);
            holder.body.setBackgroundResource(R.drawable.bubble2);
        } else {
            holder.params.gravity = Gravity.START;
            holder.authorName.setTextColor(Color.BLUE);
            holder.body.setBackgroundResource(R.drawable.bubble1);
        }

        holder.authorName.setLayoutParams(holder.params);
        holder.body.setLayoutParams(holder.params);
    }

    public void cleanup() {
        mDatabaseReference.removeEventListener(mListener);
    }
}
