package com.example.jieunlee.chatting;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jieunlee on 2018-05-03.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private List<Friend> mFriend;
    String stEmail;
    Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvEmail;
        public ImageView ivUser;
        public Button btnChat;

        public ViewHolder(View itemView) {
            super(itemView);
            tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
            ivUser = (ImageView) itemView.findViewById(R.id.ivUser);
            btnChat = (Button) itemView.findViewById(R.id.btnChat);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FriendAdapter(List<Friend> mFriend, Context context) {
        this.mFriend = mFriend;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_friend, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tvEmail.setText(mFriend.get(position).getEmail());

        String stPhoto = mFriend.get(position).getPhoto();

        //포토가 비었는지 확인
        if (TextUtils.isEmpty(stPhoto)) {
            Picasso.with(context).load(R.mipmap.ic_defaultphoto).fit().centerInside().into(holder.ivUser);

        } else {
            Picasso.with(context).load(stPhoto).fit().centerInside().into(holder.ivUser);

        }

        holder.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String stFriendId = mFriend.get(position).getKey();
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("friendUid",stFriendId);
                context.startActivity(intent);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mFriend.size();
    }
}
