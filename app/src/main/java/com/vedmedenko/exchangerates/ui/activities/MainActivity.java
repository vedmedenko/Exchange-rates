package com.vedmedenko.exchangerates.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.vedmedenko.exchangerates.R;
import com.vedmedenko.exchangerates.ui.activities.base.BaseActivity;
import com.vedmedenko.exchangerates.ui.activities.presenters.MainPresenter;
import com.vedmedenko.exchangerates.ui.activities.settings.SettingsActivity;
import com.vedmedenko.exchangerates.ui.activities.views.MainMvpView;
import com.vedmedenko.exchangerates.ui.adapters.FragmentAdapter;
import com.vedmedenko.exchangerates.ui.fragments.CurrentRatesFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements MainMvpView, ViewPager.OnPageChangeListener {

    // Views Binding

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    // Injection

    @Inject
    FragmentAdapter fragmentAdapter;

    @Inject
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        presenter.attach(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPager.setAdapter(fragmentAdapter);
        tabs.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void refreshCurrentCurrency() {
        if (tabs.getSelectedTabPosition() == 0) {
            onPageSelected(0);
        }
    }

    public MainPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Fragment fragment = fragmentAdapter.getFragment(position);

        if (fragment != null) {
            fragment.onResume();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
