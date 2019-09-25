package com.rrooaarr.werkstueck.util;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.rrooaarr.werkstueck.R;
import com.rrooaarr.werkstueck.view.FragmentBase;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Utils {
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static int compareLongDesc(long lhs, long rhs) {
        return lhs > rhs ? -1 : (lhs == rhs ? 0 : 1);
    }

    public static int convertDpToPixels(float dp, final Context context) {
        if (context != null) {
            final Resources resources = context.getResources();
            return (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dp,
                    resources.getDisplayMetrics()
            );
        } else {
            return 0;
        }
    }

    public static boolean isNullOrEmpty(final Collection<?> c) {
        return c == null || c.isEmpty();
    }

    public static boolean isNullOrEmpty(final Map<?, ?> m) {
        return m == null || m.isEmpty();
    }

    public static boolean isNullOrEmpty(final String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isNotNullOrEmpty(final Collection<?> c) {
        return !isNullOrEmpty(c);
    }

    public static boolean isNotNullOrEmpty(final Map<?, ?> m) {
        return !isNullOrEmpty(m);
    }

    public static boolean isNotNullOrEmpty(final String s) {
        return !isNullOrEmpty(s);
    }

    public static Fragment getVisibleFragment(final AppCompatActivity activity) {
        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        final List<Fragment> fragments = fragmentManager.getFragments();
        if (Utils.isNotNullOrEmpty(fragments)) {
            for (final Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    public static boolean isNetworkAvailable(final Context context) {
        if (context == null) {
            return false;
        }

        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static String getDeviceName() {
        final String manufacturer = Build.MANUFACTURER;
        final String deviceModel = Build.MODEL;
        return deviceModel.startsWith(manufacturer) ? deviceModel : manufacturer + " " + deviceModel;
    }

    public static void doErrorHandling(int stringRes, Context applicationContext) {
        final Dialog webserviceResponseDialog;
        if (applicationContext != null) {
            webserviceResponseDialog = createWebserviceResponseDialog(applicationContext.getResources().getString(stringRes), applicationContext);
            if (webserviceResponseDialog != null) {
                try {
                    webserviceResponseDialog.show();
                } catch (Exception e) {
                    Log.e("Utils", "Exception while trying to display errordialog");
                }
            }
        }
    }

    public static Dialog createWebserviceResponseDialog(final String faultString, final Context context) {
        if (context != null) {
            final Dialog webserviceDialog = new Dialog(context);
            // Opacity for dialog
            Drawable d = new ColorDrawable(0xCCffffff);
//        d.setAlpha(180);
            Window window = webserviceDialog.getWindow();

            if (window != null) {
                window.setBackgroundDrawable(d);
            }

//            webserviceDialog.setContentView(R.layout.webservice_dialog);
//            final RelativeLayout okButton = (RelativeLayout) webserviceDialog.findViewById(R.id.webserviceOk);
//            final TextView textView = (TextView) webserviceDialog.findViewById(R.id.faultText);
//            textView.setText(faultString);
            webserviceDialog.setCancelable(false);
            webserviceDialog.setCanceledOnTouchOutside(false);
//            okButton.setClickable(true);
            final View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webserviceDialog.dismiss();
                }
            };
//            FragmentHelper.addButtonToView(okButton, R.string.ok, R.drawable.success_png, webserviceDialog, 0.8f, 15, clickListener);

            return webserviceDialog;
        } else {
            return null;
        }
    }

    public static Integer convertToInt(final String s) {
        return Utils.isNullOrEmpty(s) ? Integer.valueOf(0) : Integer.valueOf(s);
    }

    public static FragmentManager getFragmentManager(Context context) {
        return ((AppCompatActivity) context).getSupportFragmentManager();
    }

    public static void replaceFragment(final Fragment frag, final boolean addTOBackstack, final FragmentManager fragmentManager, final int replacementFrame) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String fragmentTag = "replaceFragment";
        try {
        if (frag instanceof FragmentBase) {
            fragmentTag = ((FragmentBase) frag).getFragmentTag();
            transaction.replace(replacementFrame, frag, fragmentTag);
            if (addTOBackstack) {
                transaction.addToBackStack(fragmentTag);
            }
        } else {
            transaction.replace(replacementFrame, frag);
        }

        transaction.commit();
        } catch (IllegalStateException ise) {
            Log.e(fragmentTag, "Cannot replace fragment: " + frag.getTag());
        }
    }

}
