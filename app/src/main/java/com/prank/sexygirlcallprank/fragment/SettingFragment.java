package com.prank.sexygirlcallprank.fragment;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prank.sexygirlcallprank.BuildConfig;
import com.prank.sexygirlcallprank.R;
import com.prank.sexygirlcallprank.constant.ConstantMethod;
import com.prank.sexygirlcallprank.model.Feedback;


public class SettingFragment extends Fragment implements View.OnClickListener {
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout linearShare = view.findViewById(R.id.linearShare);
        LinearLayout linearRate = view.findViewById(R.id.linearRate);
        LinearLayout linearPrivacy = view.findViewById(R.id.linearPrivacy);


        LinearLayout linearFeedback =view.findViewById(R.id.linearFeedback);
        LinearLayout linearFeedbackTwo = view.findViewById(R.id.linearFeedbackTwo);


        linearFeedback.setOnClickListener(this);
        linearFeedbackTwo.setOnClickListener(this);
        linearRate.setOnClickListener(this);
        linearPrivacy.setOnClickListener(this);
        linearShare.setOnClickListener(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearShare:
                try {
                    String shareBody = String.format("%s https://play.google.com/store/apps/details?id=%s", getString(R.string.lbl_app_share_message), BuildConfig.APPLICATION_ID);
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.lbl_share_app));
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, getString(R.string.lbl_share_app)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.linearFeedbackTwo:
                sendFeedback();

                break;
            case R.id.linearRate:
                try {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(goToMarket);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Unable to find PlayStore app", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.linearPrivacy:
                try {
                    String url = getString(R.string.lbl_setting_privacy_policy_url);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "You don't have any browser to open web page", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.linearFeedback:

                try {
                    Uri uri2 = Uri.parse(getString(R.string.store_url));
                    Intent goToMarket2 = new Intent(Intent.ACTION_VIEW, uri2);
                    goToMarket2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(goToMarket2);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "Unable to find PlayStore app", Toast.LENGTH_LONG).show();
                }
                break;
        }

    }
    private void sendFeedback() {

        final Dialog dlg = new Dialog(getActivity());
        dlg.setContentView(R.layout.alert_dialog_feedback);
        dlg.setCanceledOnTouchOutside(false);

        Rect displayRectangle = new Rect();
        dlg.getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(displayRectangle);
        dlg.getWindow().setLayout((int) (displayRectangle.width() * 0.9f),
                ViewGroup.LayoutParams.WRAP_CONTENT);

        Button btnSubmit = dlg.findViewById(R.id.btnSubmit);
        EditText editFeedback = dlg.findViewById(R.id.editFeedback);
        EditText editEmail = dlg.findViewById(R.id.editEmail);

        ImageView imageClose = dlg.findViewById(R.id.imageClose);
        btnSubmit.setOnClickListener(v -> {
            try {
                if(!ConstantMethod.isEmpty(editEmail.getText().toString())){
                    if(ConstantMethod.isValidEmail(editEmail.getText().toString())){
                        Feedback note = new Feedback();
                        String userId = database.child(getString(R.string.lbl_key)).push().getKey();
                        note.setEmail(editEmail.getText().toString().trim());
                        note.setFeedback(editFeedback.getText().toString().trim());
                        if (userId != null) {
                            database.child(getString(R.string.lbl_key)).child(userId).setValue(note);
                        }

                        Toast.makeText(getActivity(), "Feedback submit successfully", Toast.LENGTH_LONG).show();
                        dlg.cancel();
                    }else {
                        Toast.makeText(getActivity(), "Please enter valid email", Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(getActivity(), "Please enter email address", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        });
        imageClose.setOnClickListener(v -> dlg.dismiss());
        dlg.show();
    }
}
