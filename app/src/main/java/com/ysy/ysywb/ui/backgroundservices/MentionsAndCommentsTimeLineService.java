package com.ysy.ysywb.ui.backgroundservices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * User: ysy
 * Date: 2015/9/10
 * Time: 15:47
 */
public class MentionsAndCommentsTimeLineService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);
    }


}
