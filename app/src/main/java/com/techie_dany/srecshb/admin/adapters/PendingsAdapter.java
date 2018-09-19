package com.techie_dany.srecshb.admin.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techie_dany.srecshb.R;
import com.techie_dany.srecshb.admin.helpers.PendingsHelper;
import com.techie_dany.srecshb.admin.interfaces.Admin_Listener;
import com.techie_dany.srecshb.informations.Departments;

import java.util.ArrayList;

public class PendingsAdapter extends RecyclerView.Adapter<PendingsAdapter.ViewHolder> implements Admin_Listener {

    ArrayList<PendingsHelper> pendingsHelper;
    Admin_Listener admin_listener;
    public PendingsAdapter(){}
    public static final String TAG = "pendings_adpater";
    public int itemSize;

    public PendingsAdapter(ArrayList<PendingsHelper> penHelp, Admin_Listener lis){
        pendingsHelper = penHelp;
        admin_listener = lis;
        itemSize = pendingsHelper.size();
    }

    int ident;
    public PendingsAdapter(ArrayList<PendingsHelper> penHelp,int i, Admin_Listener lis){
        pendingsHelper = penHelp;
        admin_listener = lis;
        ident = 1; // pendings
        itemSize = pendingsHelper.size();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_pendings,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }
    Departments departments = new Departments();
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        if(ident==1) { // pendings
            Log.i(TAG, "Department Name : " +departments.getDept(pendingsHelper.get(position).getHall_id()));

            holder.pendings_info_text.setText("You have Requested "+departments.getDept(pendingsHelper.get(position).getHall_id()) +" on "+pendingsHelper.get(position).getDate());

            holder.pendings_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    admin_listener.onPendingsCardClick(pendingsHelper.get(position));
                }
            });
        }

        else { //booked
            Log.i(TAG, "Department Name : " +departments.getDept(pendingsHelper.get(position).getHall_id()));

            holder.pendings_info_text.setText("You have Booked "+departments.getDept(pendingsHelper.get(position).getHall_id()) +" on "+pendingsHelper.get(position).getDate());

            holder.pendings_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    admin_listener.onPendingsCardClick(pendingsHelper.get(position));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return itemSize;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView pendings_info_text;
        public CardView pendings_card;
        public ViewHolder(View itemView) {
            super(itemView);

            pendings_card = (CardView)itemView.findViewById(R.id.pendings_card);
            pendings_info_text = (TextView) itemView.findViewById(R.id.pendings_info_text);

        }
    }

    //trash ...it will be override @Pendings
    public void onPendingsCardClick(PendingsHelper temp_in_help) {}

    public void onbookedCardClick(PendingsHelper temp_app_help) {}


}
