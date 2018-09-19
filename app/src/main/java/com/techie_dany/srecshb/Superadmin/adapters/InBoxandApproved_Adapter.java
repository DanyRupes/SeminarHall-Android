package com.techie_dany.srecshb.Superadmin.adapters;

import android.support.annotation.NonNull;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techie_dany.srecshb.R;
import com.techie_dany.srecshb.Superadmin.helpers.Inbox_Helper;
import com.techie_dany.srecshb.Superadmin.listeners.Super_Listener;

import java.util.ArrayList;

public class InBoxandApproved_Adapter extends RecyclerView.Adapter<InBoxandApproved_Adapter.ViewHolder>  {


    private static final String TAG = "inboxapproved_adapter";
    ArrayList<Inbox_Helper> inboxHelpers = new ArrayList<>();
    ArrayList<Inbox_Helper> approvedHelper = new ArrayList<>();
    int identity;
    int itemSize;
    protected  Super_Listener super_Listerner_Main;


    public InBoxandApproved_Adapter(){}
    public InBoxandApproved_Adapter(ArrayList<Inbox_Helper> helper, Super_Listener super_inbox_listener){
        Log.i(TAG, "InBoxandApproved_Adapter: Inboxx");
        inboxHelpers = helper;
        itemSize =inboxHelpers.size();
        identity = -1;
        super_Listerner_Main = super_inbox_listener;
    }

    public InBoxandApproved_Adapter(ArrayList<Inbox_Helper> approved_helper, int approvei,Super_Listener super_approved_listener) {//identitiy 2 for approved
        Log.i(TAG, "InBoxandApproved_Adapter Approved ");
        approvedHelper = approved_helper;
        itemSize = approvedHelper.size();
        identity = 1;
        super_Listerner_Main = super_approved_listener;
    }


    @NonNull
    @Override
    public InBoxandApproved_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder: ");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_inbox,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final InBoxandApproved_Adapter.ViewHolder holder, final int position) {
        Log.i(TAG, "onBindViewHolder: ");
        if(identity==1){ //approved

            Log.i(TAG, "itemSize approved "+itemSize);
//            holder.inbox_by_text.setVisibility(View.INVISIBLE);
            holder.inbox_info_text.setText("You have approved "+approvedHelper.get(position).getBy()+" Request at "+approvedHelper.get(position).getDate());
        }
        else {  //inbox
            Log.i(TAG, "itemSize inbox "+itemSize);
            holder.inbox_by_text.setText(inboxHelpers.get(position).getDate());
            holder.inbox_info_text.setText(inboxHelpers.get(position).getBy()+" has Requested Hall");
        }

        holder.inbox_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(identity==1){//approved
                    super_Listerner_Main.onApprovedCardClick(approvedHelper.get(position));

//                    Intent inIntent = new Intent(itemView.getContext(),ViewChannel.class);
//                    Inbox_Helper inbox_help = inboxHelpers.get(pos)
//                    inIntent.putParcelableArrayListExtra("inbox_help",);
                }

                else {
                    super_Listerner_Main.onInboxCardClick(inboxHelpers.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemSize;
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView inbox_info_text,inbox_by_text;
        public CardView inbox_card;
        public ViewHolder(final View itemView) {
            super(itemView);
            Log.i(TAG, "ViewHolder: ");
                inbox_info_text = (TextView) itemView.findViewById(R.id.pendings_info_text);
                inbox_card = (CardView) itemView.findViewById(R.id.inbox_card);
            if(identity!=1){ ////inbox
                inbox_by_text = (TextView) itemView.findViewById(R.id.inbox_by_text);
            }

        }
    }
}
