package com.ysy.ysywb.ui.send;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

/**
 * User: ysy
 * Date: 2015/9/22
 * Time: 9:44
 */
public class SendProgressFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("发送中");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        return dialog;
    }
}
