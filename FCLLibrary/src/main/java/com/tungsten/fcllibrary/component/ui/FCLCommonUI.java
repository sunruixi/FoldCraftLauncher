package com.tungsten.fcllibrary.component.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;

import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.anim.DisplayAnimUtils;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

public abstract class FCLCommonUI extends FCLBaseUI {

    private final FCLUILayout parent;

    private UILoadingCallback callback;

    public FCLCommonUI(Context context, FCLUILayout parent, @LayoutRes int id) {
        super(context);
        this.parent = parent;
        setContentView(id, () -> {
            onCreate();
            if (callback != null) {
                callback.onLoad();
            }
        });
    }

    @Override
    public boolean isShowing() {
        return getContentView().getVisibility() == View.VISIBLE;
    }

    @Override
    public abstract Task<?> refresh(Object... param);

    @Override
    public void onCreate() {
        super.onCreate();
        getContentView().setVisibility(View.GONE);
        parent.addView(getContentView(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayAnimUtils.showViewWithAnim(getContentView(), R.anim.ui_show);
    }

    @Override
    public void onStop() {
        super.onStop();
        DisplayAnimUtils.hideViewWithAnim(getContentView(), R.anim.ui_hide);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        parent.removeView(getContentView());
    }

    public void addLoadingCallback(UILoadingCallback callback) {
        this.callback = callback;
    }

    public interface UILoadingCallback {
        void onLoad();
    }
}
