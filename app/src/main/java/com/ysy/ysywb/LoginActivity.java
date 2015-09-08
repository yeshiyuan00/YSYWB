package com.ysy.ysywb;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ysy.ysywb.ui.MentionsTimeLineActivity;
import com.ysy.ysywb.ui.login.OAuthActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listView;

    private AccountAdapter listAdapter;

    private List<Map<String, String>> listData = new ArrayList<Map<String, String>>();

    private ActionMode mActionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        jumpToHomeLine();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        listAdapter = new AccountAdapter();
        listView = (ListView) findViewById(R.id.listView);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setOnItemClickListener(this);

        listView.setAdapter(listAdapter);

        listView.setMultiChoiceModeListener(multiChoiceModeLinstener);
    }

    private AbsListView.MultiChoiceModeListener multiChoiceModeLinstener = new AbsListView.MultiChoiceModeListener() {
        boolean checkAll = false;

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.login_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            listAdapter.addCheckbox();
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_select_all:
                    if (!checkAll) {
                        listAdapter.selectAll();
                        checkAll = true;
                        item.setIcon(R.drawable.account_select_none);
                    } else {
                        listAdapter.unSelectButRemainCheckBoxAll();
                        checkAll = false;
                        item.setIcon(R.drawable.account_select_all);
                    }
                    return true;
                default:
                    Toast.makeText(LoginActivity.this, "删除", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            listAdapter.unSelectAll();
        }
    };


    private void jumpToHomeLine() {
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        String username = settings.getString("username", "");
        String token = settings.getString("token", "");
        String expires = settings.getString("expires", "");

        boolean haveToken = !TextUtils.isEmpty(token);

        boolean haveUsername = !TextUtils.isEmpty(username);

        if (haveToken) {
            Intent intent = new Intent(LoginActivity.this, MentionsTimeLineActivity.class);
            intent.putExtra("token", token);
            intent.putExtra("expires", expires);

            if (haveUsername) {
                intent.putExtra("username", username);
            }

            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login, menu);
        return true;
    }

    public void addAccount(MenuItem menu) {
        Intent intent = new Intent(this, OAuthActivity.class);

        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Bundle values = data.getExtras();
            String access_token = values.getString("access_token");
            String expires_in = values.getString("expires_in");

            Map<String, String> map = new HashMap<String, String>();

            map.put("token", access_token);
            map.put("expires", expires_in);
            listData.add(map);

            listAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String token = listData.get(i).get("token");
        String expires = listData.get(i).get("expires");

        SharedPreferences settings = getPreferences(MODE_PRIVATE);

        SharedPreferences.Editor editor = settings.edit();

        editor.putString("token", token);
        editor.putString("expires", expires);

        editor.commit();

        Intent intent = new Intent(this, MentionsTimeLineActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("expires", expires);

        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }


    private class AccountAdapter extends BaseAdapter {
        boolean needCheckbox = false;

        boolean allChecked = false;

        @Override
        public int getCount() {
            return listData.size();  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Object getItem(int i) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public long getItemId(int i) {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater layoutInflater = getLayoutInflater();

            View mView = layoutInflater.inflate(R.layout.account_item, viewGroup, false);

            if (needCheckbox) {
                LinearLayout linearLayout = (LinearLayout) mView;

                CheckBox cb = new CheckBox(LoginActivity.this);

//                LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                if (allChecked)
                    cb.setChecked(true);

                linearLayout.addView(cb, 0);

            }

            TextView textView = (TextView) mView.findViewById(R.id.textView);

            textView.setText(listData.get(i).get("token"));
            return mView;
        }

        public void addCheckbox() {
            needCheckbox = true;
            notifyDataSetChanged();
        }

        public void removeCheckbox() {
            needCheckbox = false;
            notifyDataSetChanged();
        }

        public void selectAll() {
            needCheckbox = true;
            allChecked = true;
            notifyDataSetChanged();
        }

        public void unSelectAll() {
            needCheckbox = false;
            allChecked = false;
            notifyDataSetChanged();
        }

        public void unSelectButRemainCheckBoxAll() {
            needCheckbox = true;
            allChecked = false;
            notifyDataSetChanged();
        }
    }
}
