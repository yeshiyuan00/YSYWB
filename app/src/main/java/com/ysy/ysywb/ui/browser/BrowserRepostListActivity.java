package com.ysy.ysywb.ui.browser;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ysy.ysywb.R;
import com.ysy.ysywb.ui.Abstract.AbstractAppActivity;

/**
 * User: ysy
 * Date: 2015/9/14
 * Time: 15:43
 */
public class BrowserRepostListActivity extends AbstractAppActivity {

    private String token = "";
    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.browsercommentlistactivity_layout);

        token = getIntent().getStringExtra("token");
        id = getIntent().getStringExtra("id");

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RepostsByIdTimeLineFragment fragment = new RepostsByIdTimeLineFragment(token, id);
        fragmentTransaction.add(R.id.listViewFragment, fragment);
        fragmentTransaction.commit();
    }
}
