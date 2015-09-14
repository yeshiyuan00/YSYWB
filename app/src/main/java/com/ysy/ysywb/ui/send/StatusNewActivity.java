package com.ysy.ysywb.ui.send;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ysy.ysywb.dao.StatusNewMsgDao;

/**
 * User: ysy
 * Date: 2015/9/9
 * Time: 10:38
 */
public class StatusNewActivity extends AbstractSendActivity {

    @Override
    protected void executeTask(String content) {
        new StatusNewTask(content).execute();
    }



    class StatusNewTask extends AsyncTask<Void, String, String> {

        String content;

        StatusNewTask(String content) {
            this.content = content;
        }

        ProgressFragment progressFragment = new ProgressFragment();

        @Override
        protected void onPreExecute() {
            progressFragment.onCancel(new DialogInterface() {
                @Override
                public void cancel() {
                    StatusNewTask.this.cancel(true);
                }

                @Override
                public void dismiss() {
                    StatusNewTask.this.cancel(true);
                }
            });
            progressFragment.show(getFragmentManager(), "");
        }

        @Override
        protected String doInBackground(Void... params) {
            new StatusNewMsgDao(token).sendNewMsg(content);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressFragment.dismissAllowingStateLoss();
            finish();
            Toast.makeText(StatusNewActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
            super.onPostExecute(s);
        }
    }

}
