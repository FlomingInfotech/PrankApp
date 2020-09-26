package com.prank.sexygirlcallprank.fragment;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prank.sexygirlcallprank.BuildConfig;
import com.prank.sexygirlcallprank.R;
import com.prank.sexygirlcallprank.activity.ProfileActivity;
import com.prank.sexygirlcallprank.adapter.ContactAdapter;
import com.prank.sexygirlcallprank.constant.ConstantMethod;
import com.prank.sexygirlcallprank.helper.ItemClickInterface;
import com.prank.sexygirlcallprank.helper.Prefs;
import com.prank.sexygirlcallprank.model.GirlsList;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.Manifest.permission.CAMERA;

public class MyContactsFragment extends Fragment implements ItemClickInterface {

    private RecyclerView recyclerView;
    private ArrayList<GirlsList> girlsListArrayList = new ArrayList<>();
    private final int REQUEST_CODE = 100;
    private String[] neededPermissions = new String[]{CAMERA};
    private TextView textViewEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_contacts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        textViewEmpty = view.findViewById(R.id.textViewEmpty);
        setupData();
    }


    private void setupData() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setAdapter();

    }


    public void setAdapter() {
        try {
            girlsListArrayList = getSelectedCelebrityList();
            if(!CollectionUtils.isEmpty(girlsListArrayList)){
                textViewEmpty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                ContactAdapter contactAdapter = new ContactAdapter(girlsListArrayList, getActivity(), this);
                recyclerView.setAdapter(contactAdapter);
            }else {
                textViewEmpty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


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

    @Override
    public void passData(int position, View view) {
        if(ConstantMethod.isConnected(getActivity())!=0){
            boolean result = checkPermission();
            if (result) {
                Intent mIntent = new Intent(getActivity(), ProfileActivity.class);
                mIntent.putExtra(ConstantMethod.KEY_VIDEO_URI, girlsListArrayList.get(position).getVideo());
                mIntent.putExtra(ConstantMethod.KEY_VIDEO_AVTAR, girlsListArrayList.get(position).getAvatar());
                mIntent.putExtra(ConstantMethod.KEY_VIDEO_NAME, girlsListArrayList.get(position).getName());
                mIntent.putExtra(ConstantMethod.KEY_NUMBER, girlsListArrayList.get(position).getContact());
                startActivity(mIntent);
            }
        }else {
            Toast.makeText(getActivity(), R.string.txt_no_internet, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            ArrayList<String> permissionsNotGranted = new ArrayList<>();
            for (String permission : neededPermissions) {
                if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsNotGranted.add(permission);
                }
            }
            if (permissionsNotGranted.size() > 0) {
                boolean shouldShowAlert = false;
                for (String permission : permissionsNotGranted) {
                    shouldShowAlert = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission);
                }
                if (shouldShowAlert) {
                    showPermissionAlert(permissionsNotGranted.toArray(new String[0]));
                } else {
                    requestPermissions(permissionsNotGranted.toArray(new String[0]));
                }
                return false;
            }
        }
        return true;
    }

    private void showPermissionAlert(final String[] permissions) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle(R.string.permission_required);
        alertBuilder.setMessage(R.string.permission_message);
        alertBuilder.setPositiveButton(android.R.string.yes, (dialog, which) -> requestPermissions(permissions));
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    private void requestPermissions(String[] permissions) {
        requestPermissions( permissions, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {


                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                    alertBuilder.setCancelable(false);
                    alertBuilder.setTitle(R.string.permission_required);
                    alertBuilder.setMessage(R.string.txt_permission);
                    alertBuilder.setPositiveButton(R.string.txt_setting, (dialog, which) -> {
                        Intent intent = new Intent();
                        intent.setAction(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",
                                BuildConfig.APPLICATION_ID, null);
                        intent.setData(uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().startActivity(intent);
                    });
                    alertBuilder.setNegativeButton(R.string.txt_notnow, (dialogInterface, i) -> {

                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                    return;
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


}
