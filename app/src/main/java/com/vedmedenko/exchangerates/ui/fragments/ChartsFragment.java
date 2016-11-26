package com.vedmedenko.exchangerates.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vedmedenko.exchangerates.R;

import butterknife.ButterKnife;

public class ChartsFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_charts, container, false);
        ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

}
