package com.vedmedenko.exchangerates.ui.fragments;

import android.support.v4.app.Fragment;

import com.vedmedenko.exchangerates.injection.components.DaggerFragmentComponent;
import com.vedmedenko.exchangerates.injection.components.FragmentComponent;
import com.vedmedenko.exchangerates.injection.modules.FragmentModule;
import com.vedmedenko.exchangerates.ui.activities.base.BaseActivity;

public class BaseFragment extends Fragment {

    private FragmentComponent mFragmentComponent;

    protected FragmentComponent fragmentComponent() {
        if (mFragmentComponent == null) {
            mFragmentComponent = DaggerFragmentComponent.builder()
                    .fragmentModule(new FragmentModule(this))
                    .activityComponent(((BaseActivity) getActivity()).activityComponent())
                    .build();
        }
        return mFragmentComponent;
    }

}
