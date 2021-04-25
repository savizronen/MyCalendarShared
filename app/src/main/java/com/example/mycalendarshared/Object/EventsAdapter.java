package com.example.mycalendarshared.Object;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycalendarshared.CallBacks.OnEventDelete;
import com.example.mycalendarshared.DataBase.App;
import com.example.mycalendarshared.DataBase.DataBaseReader;
import com.example.mycalendarshared.DataBase.Utils;
import com.example.mycalendarshared.R;
import com.example.mycalendarshared.fragments.CalendarFragment;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsViewHolder>  {
    private ArrayList<EventData> mEventsList;
    private EventsAdapter.OnItemClickListener mListener;
    private OnEventDelete onEventDelete;
    private Context context;



    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(EventsAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
    public  class EventsViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView eventImg;
        public TextView location;
        public TextView dateAndHour;
        public TextView eventContent;
        public ImageView buttonShare ;
        public ImageView  buttonDel;


        public EventsViewHolder(View itemView, final EventsAdapter.OnItemClickListener listener) {
            super(itemView);

            eventImg = itemView.findViewById(R.id.event_img);
            location = itemView.findViewById(R.id.location);
            dateAndHour = itemView.findViewById(R.id.date_and_hour);
            eventContent = itemView.findViewById(R.id.content_event);
            buttonShare = itemView.findViewById(R.id.button_share);
            buttonDel=itemView.findViewById(R.id.button_del_event);

        }
    }

    public EventsAdapter(ArrayList<EventData> exampleList,OnEventDelete onEventDelete) {
        this.onEventDelete=onEventDelete;
        mEventsList = exampleList;
    }

    public EventsAdapter(ArrayList<EventData> exampleList){
        mEventsList = exampleList;
    }



    @NonNull
    @Override
    public EventsAdapter.EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        context = parent.getContext();
        EventsAdapter.EventsViewHolder evh = new EventsAdapter.EventsViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull EventsAdapter.EventsViewHolder holder, int position) {

        EventData eventItem = mEventsList.get(position);
                holder.buttonShare.setOnClickListener(new View.OnClickListener() {
                @Override
                 public void onClick(View v) {
                        openShareDialog(context, eventItem);
                    Log.d("MyTag",position+"");
                  }
                });
                holder.buttonDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventData currentEvent=mEventsList.get(position);
                        mEventsList.remove(position);
                        changeUserEvents(currentEvent);
                        if(onEventDelete!=null)
                            onEventDelete.deleteEvent(currentEvent.getDate());
                        notifyDataSetChanged();
                        App.toast("This event has been deleted");
                    }
                });

        if(eventItem!=null) {
            if(eventItem.getImageEvent()!=null)
                //buttonShare
            DataBaseReader.getInstance().readProfilePic(holder.eventImg, eventItem.getImageEvent());
            if(eventItem.getEventContent()!=null)
            holder.eventContent.setText(eventItem.getEventContent());
            if(eventItem.getLocation()!=null)
            holder.location.setText(eventItem.getLocation());
            holder.dateAndHour.setText(eventItem.getDate().toLocaleString());
        }
    }

    @Override
    public int getItemCount() {
        return mEventsList.size();
    }

    public void filterList(ArrayList<EventData> filteredList) {
        notifyDataSetChanged();
    }


    //============================================


    private void openShareDialog(Context context, EventData event){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_share_event, null);

        GenericDialog dialogView = new GenericDialog(view)

                .findRecyclerViewById(R.id.shared_list_recycler_view)
                .setRecyclerViewAdapter(new ShareAdapter(event, DataBaseReader.getInstance().getUsersList()));

        dialogView.show();
    }

    public void changeUserEvents(EventData eventData){
        ArrayList<EventData> usersEvent=DataBaseReader.getInstance().getUser().getEventsArrayList();
        EventData temp;
        for(int i=0;i<usersEvent.size();i++) {
            temp = usersEvent.get(i);
            if (temp.equals(eventData)) {
                usersEvent.remove(i);
                DataBaseReader.getInstance().getUser().setEventsArrayList(usersEvent);
                DataBaseReader.getInstance().setEventList(usersEvent);
                return;
            }
        }
    }
}



