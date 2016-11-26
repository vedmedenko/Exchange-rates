package com.vedmedenko.exchangerates.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.ViewGroup;

import com.vedmedenko.exchangerates.core.DataManager;
import com.vedmedenko.exchangerates.injection.ActivityContext;
import com.vedmedenko.exchangerates.ui.activities.MainActivity;
import com.vedmedenko.exchangerates.ui.fragments.ChartsFragment;
import com.vedmedenko.exchangerates.ui.fragments.CurrentRatesFragment;
import com.vedmedenko.exchangerates.ui.fragments.DateRatesFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import timber.log.Timber;

public class FragmentAdapter extends FragmentPagerAdapter {

    private SparseArray<String> fragmentTags;

    private final FragmentManager fragmentManager;
    private final Context context;

    @Inject
    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull @ActivityContext Context context) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
        this.context = context;
        fragmentTags = new SparseArray<>();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return CurrentRatesFragment.newInstance(((MainActivity) context).getPresenter().getCurrentCurrency());
            case 1:
                DateRatesFragment tab2 = new DateRatesFragment();
                return tab2;
            case 2:
                ChartsFragment tab3 = new ChartsFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Current";
            case 1:
                return "By date";
            case 2:
                return "Charts";
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object object = super.instantiateItem(container, position);
        if (object instanceof CurrentRatesFragment)
            fragmentTags.put(position, ((CurrentRatesFragment) object).getTag());
        return object;
    }

    public Fragment getFragment(int position) {
        if (fragmentTags.get(position) != null) {
            return fragmentManager.findFragmentByTag(fragmentTags.get(position));
        } else {
            return null;
        }
    }
}
