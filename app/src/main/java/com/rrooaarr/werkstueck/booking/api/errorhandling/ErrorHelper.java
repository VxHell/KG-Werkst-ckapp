package com.rrooaarr.werkstueck.booking.api.errorhandling;

import android.content.Context;
import android.widget.Toast;

import com.rrooaarr.werkstueck.R;

public class ErrorHelper {

    public static void doDefaultApiErrorHandling(DataErrorWrapper dataWrapper, Context context){
        if (dataWrapper.getStatus() == DataErrorWrapper.APIStatus.EXCEPTION) {
            Toast.makeText(context, R.string.errUnknown, Toast.LENGTH_LONG).show();
        } else if (dataWrapper.getStatus() == DataErrorWrapper.APIStatus.UNAUTHORIZED) {
            Toast.makeText(context, R.string.errUnauthorized, Toast.LENGTH_LONG).show();
        } else if (dataWrapper.getStatus() == DataErrorWrapper.APIStatus.NOTFOUND) {
            Toast.makeText(context, R.string.errNotfound, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, R.string.errNo_service, Toast.LENGTH_LONG).show();
        }
    }

}
