package com.techie_dany.srecshb.admin_superadmin.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.techie_dany.srecshb.peoples.Hall_Cabin;
import com.techie_dany.srecshb.admin.helpers.Halls_Home_Helper;
import com.techie_dany.srecshb.R;

import java.util.ArrayList;

public class Hall_Home_Adapter extends RecyclerView.Adapter<Hall_Home_Adapter.ViewHolder> {


    public ArrayList<Halls_Home_Helper> halls_helper;
    public final static String TAG = "admin_hall_adapter";
    private int itemSize;
    public  Hall_Home_Adapter(ArrayList<Halls_Home_Helper> hallls){
        halls_helper = hallls;
        itemSize = halls_helper.size();
        Log.i(TAG, "Hall_Home_Adapter: " +itemSize);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView common_hall_title; //common_hall_status,common_hall_head,
        public CardView admin_hall_card;
        public ImageView common_hall_image;
        public ViewHolder(View itemView)
        {
             super(itemView);
            common_hall_title= (TextView) itemView.findViewById(R.id.common_hall_title);
//            common_hall_head = (TextView) itemView.findViewById(R.id.common_hall_head);
//            common_hall_status = (TextView) itemView.findViewById(R.id.common_hall_status);
            admin_hall_card = (CardView) itemView.findViewById(R.id.admin_hall_card);
//            common_hall_image = (ImageView) itemView.findViewById(R.id.common_hall_image);


        }



    }
    @NonNull
    @Override
    public Hall_Home_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "holder: ");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_halls_home, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Hall_Home_Adapter.ViewHolder holder, final int position) {

        Log.i(TAG, "ViewHolder: "+position);

        holder.common_hall_title.setText(halls_helper.get(position).getTitle());
//        holder.common_hall_head.setText(halls_helper.get(position).getHead());
//        holder.common_hall_status.setText(halls_helper.get(position).getStatus().toString());
//
        holder.admin_hall_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Hall_Cabin.class);
                int hall_id = halls_helper.get(position).getHall_id();
                Log.i(TAG, "hall_id"+hall_id);
                intent.putExtra("hall_id",hall_id);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemSize;
    }
}
