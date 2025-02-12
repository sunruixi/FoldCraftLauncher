package com.tungsten.fcl.ui.account;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.MainActivity;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.fakefx.collections.ObservableListBase;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLCommonUI;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

public class AccountUI extends FCLCommonUI implements View.OnClickListener {

    private LinearLayoutCompat addOfflineAccount;
    private LinearLayoutCompat addMicrosoftAccount;
    private LinearLayoutCompat addExternalAccount;

    private ListView listView;
    private AccountListAdapter accountListAdapter;

    public AccountUI(Context context, FCLUILayout parent, int id) {
        super(context, parent, id);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        addOfflineAccount = findViewById(R.id.offline);
        addMicrosoftAccount = findViewById(R.id.microsoft);
        addExternalAccount = findViewById(R.id.add_login_server);
        addOfflineAccount.setOnClickListener(this);
        addMicrosoftAccount.setOnClickListener(this);
        addExternalAccount.setOnClickListener(this);

        listView = findViewById(R.id.list);
    }

    @Override
    public void onStart() {
        super.onStart();
        refresh().start();
    }

    @Override
    public Task<?> refresh(Object... param) {
        if (accountListAdapter == null) {
            ObservableList<AccountListItem> list = new ObservableListBase<AccountListItem>() {
                @Override
                public AccountListItem get(int i) {
                    return new AccountListItem(getContext(), Accounts.getAccounts().get(i));
                }

                @Override
                public int size() {
                    return Accounts.getAccounts().size();
                }
            };
            accountListAdapter = new AccountListAdapter(getContext(), list);
            listView.setAdapter(accountListAdapter);
        } else {
            accountListAdapter.notifyDataSetChanged();
        }
        return Task.runAsync(() -> {
            UIManager.getInstance().getMainUI().refresh(accountListAdapter.getSelectedItem() == null ? null : accountListAdapter.getSelectedItem().skinProperty(), accountListAdapter.getSelectedItem() == null ? null : accountListAdapter.getSelectedItem().capeProperty()).start();
            MainActivity.getInstance().refresh(accountListAdapter.getSelectedItem() == null ? null : accountListAdapter.getSelectedItem().imageProperty()).start();
        });
    }

    @Override
    public void onClick(View view) {
        if (view == addOfflineAccount) {
            CreateAccountDialog dialog = new CreateAccountDialog(getContext(), Accounts.FACTORY_OFFLINE);
            dialog.show();
        }
        if (view == addMicrosoftAccount) {
            CreateAccountDialog dialog = new CreateAccountDialog(getContext(), Accounts.FACTORY_MICROSOFT);
            dialog.show();
        }
        if (view == addExternalAccount) {
            CreateAccountDialog dialog = new CreateAccountDialog(getContext(), Accounts.FACTORY_AUTHLIB_INJECTOR);
            dialog.show();
        }
    }

}
