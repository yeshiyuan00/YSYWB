package com.ysy.ysywb.ui.send;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.ysy.ysywb.R;

/**
 * User: ysy
 * Date: 2015/9/9
 * Time: 10:38
 */
public class StatusNewActivity extends Activity {
    private static final int CAMERA_RESULT = 0;

    private static final int PIC_RESULT = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statusnewactivity_layout);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.statusnewactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive())
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                onBackPressed();
                break;
            case R.id.menu_add_gps:

                break;
            case R.id.menu_add_pic:
                MyAlertDialogFragment myAlertDialogFragment = MyAlertDialogFragment.newInstance();
                myAlertDialogFragment.show(getFragmentManager(), "");
                break;

            case R.id.menu_send:

                break;
        }
        return true;
    }

    static class MyAlertDialogFragment extends DialogFragment {


        public static MyAlertDialogFragment newInstance() {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            frag.setRetainInstance(true);
            Bundle args = new Bundle();
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String[] items = {getString(R.string.take_camera), getString(R.string.select_pic)};


            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.select))
                    .setItems(items, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            switch (which) {
                                case 0:
                                    Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(i, CAMERA_RESULT);
                                    break;
                                case 1:
                                    Intent choosePictureIntent = new Intent(Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(choosePictureIntent, PIC_RESULT);
                                    break;
                            }

                        }
                    });


            return builder.create();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            Bundle extras = intent.getExtras();
            Bitmap bmp = (Bitmap) extras.get("data");
//                imv = (ImageView) findViewById(R.id.ReturnedImageView);
//                imv.setImageBitmap(bmp);
        }
    }
}
