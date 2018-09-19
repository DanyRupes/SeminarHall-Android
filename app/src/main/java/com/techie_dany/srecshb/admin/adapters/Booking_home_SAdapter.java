package com.techie_dany.srecshb.admin.adapters;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.techie_dany.srecshb.R;
import com.techie_dany.srecshb.admin.helpers.BookingHome_Helper;
import com.techie_dany.srecshb.admin.interfaces.ItemListener;

import org.json.JSONArray;

import java.util.ArrayList;

public class Booking_home_SAdapter extends RecyclerView.Adapter<Booking_home_SAdapter.ViewHolder> {


    ArrayList<BookingHome_Helper> bookingHome_help;
    Context context;
    public final String TAG = "book_sa_adapter";
    public String[] timings = {"8.45Am-9.40AM","9.40Am-10.35AM","10.35Am-11.30AM","11.30Am-12.25PM","1.20PM-2.15PM","2.15PM-3.05PM","3.05Pm-4.00PM","4.00PM-4.55PM"};
    protected ItemListener itemListener;

//  Adapter constructor recives context , list of BookingHome_hleper, Listenrer interface touch
    public Booking_home_SAdapter(Context context1, ArrayList<BookingHome_Helper> help, ItemListener itemListener1){
        bookingHome_help = help;
        context = context1;
        itemListener = itemListener1;
    }

// init definitions of viewholder class
    public class ViewHolder extends RecyclerView.ViewHolder  {

        public CardView book_card;
        public TextView book_card_Status;
        public TextView book_timing;
        public CheckBox book_home_checkBox;
        public ViewHolder(View itemView) {
            super(itemView);

//            itemView.setOnClickListener(this);

            book_card = (CardView)itemView.findViewById(R.id.booking_home_card);
            book_card_Status = (TextView) itemView.findViewById(R.id.booking_home_status);
            book_timing = (TextView) itemView.findViewById(R.id.book_timing);
            book_home_checkBox = (CheckBox) itemView.findViewById(R.id.book_home_checkBox);
//            book_home_checkBox.setVisibility(View.INVISIBLE);
        }

//     chaning inside viewholder layout items from get positions from there setting text's..
//     Listener for Click on items on ViewHolder
//        @Override
//        public void onClick(View v) {
//
//            Log.i(TAG, "Clicked v: ");
//            if(itemListener != null){
//                itemListener.onSessionClick(bookingHome_help);
//            }
//
//        }
    }

//    Creating and return a our (Booking_home_SAadaoter).ViewHoler Class layout
    @NonNull
    @Override
    public Booking_home_SAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
// layout for ViewHolder on RecycllerView
        View view = LayoutInflater.from(context).inflate(R.layout.layout_booking_home, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    JSONArray jsaSession = new JSONArray();
//    Binigng values of ViewHolder class values into Recycler View positions
    @Override
    public void onBindViewHolder(@NonNull final Booking_home_SAdapter.ViewHolder holder, final int position) {

       holder.book_card_Status.setText(""+(position+1));
        Log.i(TAG, "  " +bookingHome_help.get(position).getStatus());
        if(bookingHome_help.get(position).getStatus()==0){
            holder.book_home_checkBox.setVisibility(View.VISIBLE);
            holder.book_card.setCardBackgroundColor(Color.parseColor("#ffffff"));

        }else if(bookingHome_help.get(position).getStatus()== -1){

            holder.book_home_checkBox.setVisibility(View.INVISIBLE);
            holder.book_card.setCardBackgroundColor(Color.parseColor("#FFF471"));
        }
        else {
            holder.book_home_checkBox.setVisibility(View.INVISIBLE);
            holder.book_card.setCardBackgroundColor(Color.parseColor("#D2D5D6"));
        }
        holder.book_timing.setText(timings[position]);

        holder.book_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onCheckedChanged: ");
                itemListener.onSessionClick(bookingHome_help.get(position));

            }
        });

        holder.book_home_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.book_home_checkBox.setEnabled(false);
                if(holder.book_home_checkBox.isChecked()){
                    Log.i(TAG, "position "+position+1);
                    itemListener.onCheckedClick(bookingHome_help.get(position),position); //bookingHome_help.get(position)
                }
                else {

                     itemListener.onDeChecked(position);
                }
            }
        });
    }



// Recycler view items count
    @Override
    public int getItemCount() {
        return 8;
    }

}
//        holder.book_home_checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.book_home_checkBox.setEnabled(false); ///finished cannot undo check
//
////                holder.book_home_checkBox.setChecked(false);//imediatesly unchecked
//
//            }
//        });