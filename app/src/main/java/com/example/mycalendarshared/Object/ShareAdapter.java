package com.example.mycalendarshared.Object;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycalendarshared.DataBase.App;
import com.example.mycalendarshared.DataBase.DataBaseReader;
import com.example.mycalendarshared.DataBase.Utils;
import com.example.mycalendarshared.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ShareViewHolder> {

    private View view;
    private ArrayList<User> users;
    private EventData event;


    public ShareAdapter(EventData event, ArrayList<User> users){
        this.users = users;
        this.event = event;
        signUsersAtEvent();
    }

    private void signUsersAtEvent() {
        int userListSize=users.size();
        for(int i=0;i<userListSize;i++) {
            if (users.get(i).checkIfEventExist(event))
                users.get(i).markCheckBox();
                else
                    users.get(i).unMarkCheckBox();
            notifyDataSetChanged();
        }
    }

    //=============================

    @Override
    public int getItemCount() { return users.size(); }

    @NonNull
    @Override
    public ShareAdapter.ShareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
        return new ShareAdapter.ShareViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShareAdapter.ShareViewHolder holder, int position) {
        User user               = users.get(position);
        holder.userName                .setText(user.getName());
        holder.userCardView.setOnClickListener(v -> {
            invite(position);
            signUsersAtEvent();
        });
        DataBaseReader.getInstance().readProfilePic( holder.userPhoto, user.getProfilePicFilePath());
        holder.checkBox.setImageResource(user.getCheckBoxResource());

    }

    private void invite(int position){
        User user = DataBaseReader.getInstance().getUsersList().get(position);
        if(!(user.checkIfEventExist(event))) {
            user.getEventsArrayList().add(event);
            user.markCheckBox();
            notifyDataSetChanged();
            saveUser(user);
        }
      else
     App.toast("You have already invited this friend");
    }

    private void saveUser(User user) {
        DataBaseReader.getUsersRef().child(user.getUserId()).setValue(user);
       App.toast("This friend has been invited");
    }

    //==================================================

    public class ShareViewHolder extends RecyclerView.ViewHolder {


        private CircleImageView userPhoto;
        private TextView userName;
        private MaterialCardView userCardView;
        private ImageView checkBox;
        ShareViewHolder(View itemView) {
            super(itemView);
            findViews();
        }

        //==================================================

        void findViews(){
            userCardView=itemView.findViewById(R.id.userCardView);
            userPhoto = itemView.findViewById(R.id.user_IMG_profilePic);
            userName = itemView.findViewById(R.id.user_TXT_name);
            checkBox=itemView.findViewById(R.id.user_IMG_checkBox);
        }

        //==================================================

    }
}
