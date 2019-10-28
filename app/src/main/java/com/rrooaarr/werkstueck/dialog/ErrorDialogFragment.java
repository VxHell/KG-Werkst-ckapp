package com.rrooaarr.werkstueck.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.rrooaarr.werkstueck.R;

public class ErrorDialogFragment extends DialogFragment {

    public static final String ARG_TITLE = "ErrDialog.Title";
    public static final String ARG_MESSAGE = "ErrDialog.Message";

    public interface ErrorDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    ErrorDialogListener listener;

    // Override the Fragment.onAttach() method to instantiate the ErrorDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the ErrorDialogListener so we can send events to the host
            listener = (ErrorDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement ErrorDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        String title = args.getString(ARG_TITLE);
        String message = args.getString(ARG_MESSAGE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.dialgo_error);
        builder.setTitle(title);
        builder.setMessage(message)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogPositiveClick(ErrorDialogFragment.this);
                    }
                });
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                         listener.onDialogNegativeClick(ErrorDialogFragment.this);
//                    }
//                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}


