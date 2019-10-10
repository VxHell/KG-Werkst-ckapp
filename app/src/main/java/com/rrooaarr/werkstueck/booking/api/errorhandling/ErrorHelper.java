package com.rrooaarr.werkstueck.booking.api.errorhandling;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.rrooaarr.werkstueck.R;

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

    public static void makeToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 250);
        toast.show();
    }

}
