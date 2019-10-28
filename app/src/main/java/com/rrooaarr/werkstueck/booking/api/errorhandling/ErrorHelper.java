package com.rrooaarr.werkstueck.booking.api.errorhandling;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.rrooaarr.werkstueck.R;
import com.rrooaarr.werkstueck.dialog.ErrorDialogFragment;

public class ErrorHelper {

    public static void doDefaultApiErrorHandling(DataErrorWrapper dataWrapper, Context context) {
        if (dataWrapper.getStatus() == DataErrorWrapper.APIStatus.EXCEPTION) {
            Toast.makeText(context, R.string.errUnknown, Toast.LENGTH_LONG).show();
            ErrorHelper.makeToast(context, R.string.errUnknown);
        } else if (dataWrapper.getStatus() == DataErrorWrapper.APIStatus.WSTNOTFOUND) {
            ErrorHelper.makeToast(context, R.string.errWSTNotFound);
        } else if (dataWrapper.getStatus() == DataErrorWrapper.APIStatus.WSTERROR) {
            ErrorHelper.makeToast(context, dataWrapper.getErrorDetails());
        } else if (dataWrapper.getStatus() == DataErrorWrapper.APIStatus.UNAUTHORIZED) {
            ErrorHelper.makeToast(context, R.string.errUnauthorized);
        } else if (dataWrapper.getStatus() == DataErrorWrapper.APIStatus.NOTFOUND) {
            ErrorHelper.makeToast(context, R.string.errNotfound);
        } else {
            ErrorHelper.makeToast(context, R.string.errNo_service);
        }
    }

    public static void makeToast(Context context, int messageId) {
        Toast toast = Toast.makeText(context, messageId, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 250);
        toast.show();
    }

    public static void onShowErrorDialog(FragmentManager fragmentManager, String title, String message){
        ErrorDialogFragment dialog = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putString(ErrorDialogFragment.ARG_TITLE, title);
        args.putString(ErrorDialogFragment.ARG_MESSAGE, message);
        dialog.setArguments(args);
        dialog.show(fragmentManager, "ErrorDialog");
    }

    public static void makeToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 250);
        toast.show();
    }

}
