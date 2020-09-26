package com.prank.sexygirlcallprank.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prank.sexygirlcallprank.R;
import com.prank.sexygirlcallprank.constant.ConstantMethod;
import com.prank.sexygirlcallprank.helper.ItemClickInterface;
import com.prank.sexygirlcallprank.helper.Prefs;
import com.prank.sexygirlcallprank.model.GirlsList;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CelbListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private ArrayList<GirlsList> girlsListArrayList;
    private ArrayList<GirlsList> girlsListArrayListFiltered;
    private Context context;
    private ItemClickInterface itemClickInterface;

    public CelbListAdapter(ArrayList<GirlsList> girlsListArrayList, Context context, ItemClickInterface itemClickInterface) {
        this.girlsListArrayList = girlsListArrayList;
        this.girlsListArrayListFiltered = girlsListArrayList;
        this.context = context;
        this.itemClickInterface = itemClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_celb_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        final GirlsList girlsList = girlsListArrayListFiltered.get(position);

        try {
            holder.textViewName.setText(girlsList.getName());
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
            if (isAddCelebrity(girlsList.getId())) {
                holder.imageAdd.setImageResource(R.drawable.ic_remove);
            } else {
                holder.imageAdd.setImageResource(R.drawable.ic_add);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public boolean isAddCelebrity(String id) {
        return Prefs.getBoolean(id, false);
    }

    @Override
    public int getItemCount() {
        return girlsListArrayListFiltered == null ? 0 : girlsListArrayListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    girlsListArrayListFiltered = girlsListArrayList;
                } else {
                    ArrayList<GirlsList> filteredList = new ArrayList<>();
                    for (GirlsList row : girlsListArrayList) {

                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    girlsListArrayListFiltered =  filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = girlsListArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                girlsListArrayListFiltered = (ArrayList<GirlsList>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewName;
        private ImageView imageViewFeed, imageAdd;

        private ViewHolder(View view) {
            super(view);

            imageAdd = view.findViewById(R.id.imageAdd);

            imageViewFeed = view.findViewById(R.id.imageViewFeed);
            textViewName = view.findViewById(R.id.textViewName);
            LinearLayout linearLayoutList = view.findViewById(R.id.linearLayoutList);
            imageAdd.setOnClickListener(this);
            linearLayoutList.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imageAdd:
                case R.id.linearLayoutList:

                    GirlsList girlsList = girlsListArrayListFiltered.get(getAdapterPosition());
                    if (!isAddCelebrity(girlsList.getId())) {
                       addCelebrityId(girlsList.getId());
                       setSelectedCelebrityList(girlsList);
                        imageAdd.setImageResource(R.drawable.ic_remove);
                        itemClickInterface.passData(getAdapterPosition(),view);
                    } else {
                        deleteValues(girlsList.getId());
                        removeSelectedCelebrityList(girlsList);
                        imageAdd.setImageResource(R.drawable.ic_add);
                    }

                    break;
            }
        }
    }
    public void addCelebrityId(String name) {

        Prefs.putBoolean(name, true);
    }
    public void setSelectedCelebrityList(GirlsList girlsList) {
        ArrayList<GirlsList> list = getSelectedCelebrityList();
        list.add(girlsList);
        Gson gson = new Gson();
        String json = gson.toJson(list);
        Prefs.putString(ConstantMethod.PREF_SELECTED_CELEBRITY_LIST, json);

    }
    public ArrayList<GirlsList> getSelectedCelebrityList() {
        try {
            ArrayList<GirlsList> playersList;
            String json = Prefs.getString(ConstantMethod.PREF_SELECTED_CELEBRITY_LIST, "");
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<GirlsList>>() {
            }.getType();
            playersList = gson.fromJson(json, type);
            if (playersList != null) {
                return playersList;
            } else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    public void removeSelectedCelebrityList(GirlsList girlsList) {
        ArrayList<GirlsList> list = getSelectedCelebrityList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equalsIgnoreCase(girlsList.getId())) {
                list.remove(i);
                break;
            }
        }
        Gson gson = new Gson();
        String json = gson.toJson(list);
        Prefs.putString(ConstantMethod.PREF_SELECTED_CELEBRITY_LIST, json);

    }


    public void deleteValues(String name) {
        Prefs.remove(name);
    }



}
