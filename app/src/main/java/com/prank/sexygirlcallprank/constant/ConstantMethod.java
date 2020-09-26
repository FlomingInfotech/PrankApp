package com.prank.sexygirlcallprank.constant;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ImageView;

import androidx.annotation.IntRange;


public class ConstantMethod {

    private static String TAG = "ConstantMethod";

    public static String ADMOB_ID = "ADMOB_ID";
    public static String BANNER_ID = "BANNER_ID";
    public static String INTRESTIAL_ID = "INTRESTIAL_ID";
    public static final String PREF_SELECTED_CELEBRITY_LIST = "pref_selected_celebrity";
    public static final String PREF_CELEBRITY_LIST = "pref_celebrity_list";
    public static final String KEY_VIDEO_URI = "video_id";
    public static final String KEY_VIDEO_AVTAR = "avtar";
    public static final String KEY_VIDEO_NAME = "celb_name";
    public static final String KEY_NUMBER = "celb_number";

    public static String capitalizeFirstLetter(String text) {

        try {
            StringBuilder str = new StringBuilder();
            String[] tokens = text.split("\\s");// Can be space,comma or hyphen

            for (String token : tokens) {
                str.append(Character.toUpperCase(token.charAt(0))).append(token.substring(1)).append(" ");
            }
            str.toString().trim(); // Trim trailing space

            return str.toString();
        } catch (Exception e) {

            return "";

        }


    }
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }


    @IntRange(from = 0, to = 3)
    public static int isConnected(Context context) {
        int result = 0; // Returns connection type. 0: none; 1: mobile data; 2: wifi
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (cm != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = 2;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = 1;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                        result = 3;
                    }
                }
            }
        } else {
            if (cm != null) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        result = 2;
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        result = 1;
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_VPN) {
                        result = 3;
                    }
                }
            }
        }
        return result;
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static void setImageBackground(final Context mContext, int picture, final ImageView imageView) {
        if (mContext != null && imageView != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imageView.setImageDrawable(mContext.getResources().getDrawable(picture, mContext.getApplicationContext().getTheme()));

                } else {
                    imageView.setImageDrawable(mContext.getResources().getDrawable(picture));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }





}
