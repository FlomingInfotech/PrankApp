package com.prank.sexygirlcallprank.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prank.sexygirlcallprank.R;
import com.prank.sexygirlcallprank.adapter.CelbListAdapter;
import com.prank.sexygirlcallprank.helper.ItemClickInterface;
import com.prank.sexygirlcallprank.model.GirlsList;

import java.util.ArrayList;

public class AddContactsFragment extends Fragment implements ItemClickInterface {
    private ArrayList<GirlsList> girlsListArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private EditText editTextSearch;
    private CelbListAdapter celbListAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_contacts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewCelebrity);
        editTextSearch = view.findViewById(R.id.editTextSearch);
        setUpData();

    }




    private void setUpData() {
        try {
            girlsListArrayList.clear();
            girlsListArrayList.add(new GirlsList("1", "+91 9654766051", getString(R.string.lbl_lara), R.drawable.vid1, R.raw.vid1));
            girlsListArrayList.add(new GirlsList("2", "+91 8563094628", getString(R.string.lbl_angela), R.drawable.vid2, R.raw.vid2));
            girlsListArrayList.add(new GirlsList("3", "+91 7735289451", getString(R.string.lbl_babita), R.drawable.vid3, R.raw.vid3));
            girlsListArrayList.add(new GirlsList("4", "+91 9654766052", getString(R.string.lbl_daisy), R.drawable.vid4, R.raw.vid4));
            girlsListArrayList.add(new GirlsList("5", "+91 9654766054", getString(R.string.lbl_gigi), R.drawable.vid5, R.raw.vid5));
            girlsListArrayList.add(new GirlsList("6", "+91 6798754735", getString(R.string.lbl_kyra), R.drawable.vid6, R.raw.vid6));
            girlsListArrayList.add(new GirlsList("7", "+91 8888354688", "Kriti Agarwal", R.drawable.vid7, R.raw.vid7));
            girlsListArrayList.add(new GirlsList("8", "+91 7735267934", "Teena Arya", R.drawable.vid8, R.raw.vid8));
            girlsListArrayList.add(new GirlsList("9", "+91 636480966", "Preity Edwin", R.drawable.vid9, R.raw.vid9));
            girlsListArrayList.add(new GirlsList("10", "+91 6473930969", "Rakul Gill", R.drawable.vid10, R.raw.vid10));
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            setAdapter();

            editTextSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    celbListAdapter.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAdapter() {
        celbListAdapter = new CelbListAdapter(girlsListArrayList, getActivity(), this);
        recyclerView.setAdapter(celbListAdapter);
    }

    @Override
    public void passData(int position, View view) {


    }


}
