package com.ysy.ysywb.ui.main;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

/**
 * User: ysy
 * Date: 2015/9/16
 * Time: 9:50
 */
public class ProgressFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("刷新中");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);

        return dialog;
    }
}
