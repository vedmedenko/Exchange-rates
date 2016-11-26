package com.vedmedenko.exchangerates.ui.activities.base;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.lang.ref.WeakReference;

import com.vedmedenko.exchangerates.ExchangeRatesApplication;
import com.vedmedenko.exchangerates.injection.components.ActivityComponent;
import com.vedmedenko.exchangerates.injection.components.DaggerActivityComponent;
import com.vedmedenko.exchangerates.injection.modules.ActivityModule;

public class BaseActivity extends AppCompatActivity {

    private ActivityComponent mActivityComponent;
    private WeakReference<Context> weakReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weakReference = new WeakReference<>(getBaseContext());
    }

    public ActivityComponent activityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(ExchangeRatesApplication.get(weakReference).getComponent())
                    .build();
        }
        return mActivityComponent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else {
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        weakReference.clear();
        super.onDestroy();
        watchForLeaks();
    }

    private void watchForLeaks() {
        ExchangeRatesApplication.getRefWatcher(this).watch(this);
    }
}
