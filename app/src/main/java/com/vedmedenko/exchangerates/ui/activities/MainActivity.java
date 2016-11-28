package com.vedmedenko.exchangerates.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.vedmedenko.exchangerates.ui.fragments.ChartsFragment;
import com.vedmedenko.exchangerates.ui.fragments.DateRatesFragment;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainMvpView {

    // Views Binding

    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.tabs) TabLayout tabs;

    @BindView(R.id.viewpager) ViewPager viewPager;

    // Injection

    @Inject
    FragmentAdapter fragmentAdapter;

    @Inject
    MainPresenter presenter;

    private ViewPager.OnPageChangeListener viewPagerOnPageChangeListener;

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
        viewPagerOnPageChangeListener = null;
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPager.setAdapter(fragmentAdapter);
        tabs.setupWithViewPager(viewPager);

        viewPagerOnPageChangeListener = new ViewPager.OnPageChangeListener() {
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
        };
        viewPager.addOnPageChangeListener(viewPagerOnPageChangeListener);
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
    public void refreshPage(int page) {
        if (tabs.getSelectedTabPosition() == page) {
            viewPagerOnPageChangeListener.onPageSelected(page);
        }
    }

    @Override
    public void setDateCurrencies(@NonNull ArrayList<String> eur, @NonNull ArrayList<String> rur, @NonNull ArrayList<String> usd) {
        ((DateRatesFragment) fragmentAdapter.getFragment(1)).setStrings(eur, rur, usd);
    }

    @Override
    public void setChartData(@NonNull ArrayList<String> eur, @NonNull ArrayList<String> usd) {
        ((ChartsFragment) fragmentAdapter.getFragment(2)).setChartData(eur, usd);
    }

    @Override
    public void showError(int page) {
        switch (page) {
            case 0:
                return;
            case 1:
                ((DateRatesFragment) fragmentAdapter.getFragment(1)).showHint(R.string.error_no_internet);
                return;
            case 2:
                ((ChartsFragment) fragmentAdapter.getFragment(2)).showError();
            default:
        }
    }

    public MainPresenter getPresenter() {
        return presenter;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }
}
