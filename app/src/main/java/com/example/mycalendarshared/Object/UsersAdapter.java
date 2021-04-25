package com.example.mycalendarshared.Object;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycalendarshared.DataBase.DataBaseReader;
import com.example.mycalendarshared.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ExampleViewHolder> {
    private ArrayList<User> mExampleList=DataBaseReader.getInstance().getUsersList() ;
    private OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public ImageView checkBox;


        public ExampleViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.user_IMG_profilePic);
            mTextView1 = itemView.findViewById(R.id.user_TXT_name);
            mTextView2 = itemView.findViewById(R.id.user_TXT_birthday);
            checkBox   = itemView.findViewById(R.id.user_IMG_checkBox);



        }
    }

    public UsersAdapter(ArrayList<User> exampleList) {
        mExampleList = exampleList;
    }


    @NonNull
    @Override
    public UsersAdapter.ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.ExampleViewHolder holder, int position) {

        User currentItem = mExampleList.get(position);
        DataBaseReader.getInstance().readProfilePic(holder.mImageView, currentItem.getProfilePicFilePath());
        holder.mTextView1.setText(currentItem.getName());
        holder.mTextView2.setText(currentItem.getBirthdayString());
        holder.checkBox.setImageResource(currentItem.getBirthdayPic());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public void filterList(ArrayList<User> filteredList) {
        mExampleList = filteredList;
        notifyDataSetChanged();
    }

}
