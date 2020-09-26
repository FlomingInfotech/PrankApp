package com.prank.sexygirlcallprank.adapter;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.prank.sexygirlcallprank.R;
import com.prank.sexygirlcallprank.helper.ItemClickInterface;
import com.prank.sexygirlcallprank.model.GirlsList;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<GirlsList> girlsListArrayList;
    private Context context;
    private ItemClickInterface itemClickInterface;
    private long mLastClickTime = 0;
    public ContactAdapter(ArrayList<GirlsList> girlsListArrayList, Context context, ItemClickInterface itemClickInterface) {
        this.girlsListArrayList = girlsListArrayList;
        this.context = context;
        this.itemClickInterface = itemClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_contacts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        final GirlsList girlsList = girlsListArrayList.get(position);
        try {
            holder.textViewName.setText(girlsList.getName());
            holder.txtContact.setText(girlsList.getContact());
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.user_placeholder)
                    .priority(Priority.HIGH);
            Glide.with(context)
                    .load(girlsList.getAvatar())
                    .apply(options)
                    .thumbnail(0.9f)
                    .error(R.drawable.user_placeholder)
                    .into(holder.imageViewFeed);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return girlsListArrayList == null ? 0 : girlsListArrayList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewName,txtContact;
        private ImageView imageViewFeed;


        private ViewHolder(View view) {
            super(view);


            LinearLayout layoutMain = view.findViewById(R.id.layoutMain);
            txtContact = view.findViewById(R.id.txtContact);
            imageViewFeed = view.findViewById(R.id.imageViewFeed);
            textViewName = view.findViewById(R.id.textViewName);
            layoutMain.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            if (view.getId() == R.id.layoutMain) {
                itemClickInterface.passData(getAdapterPosition(), view);
            }
        }
    }

}
