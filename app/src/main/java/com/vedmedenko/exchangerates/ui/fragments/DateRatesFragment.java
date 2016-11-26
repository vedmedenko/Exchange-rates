package com.vedmedenko.exchangerates.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vedmedenko.exchangerates.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DateRatesFragment extends BaseFragment {

    @BindView(R.id.tv_click_request) TextView tv_click_request;


    @BindView(R.id.tv_eur) TextView tv_eur;

    @BindView(R.id.tv_nbu_eur) TextView tv_nbu_eur;

    @BindView(R.id.tv_pb_eur) TextView tv_pb_eur;


    @BindView(R.id.tv_rur) TextView tv_rur;

    @BindView(R.id.tv_nbu_rur) TextView tv_nbu_rur;

    @BindView(R.id.tv_pb_rur) TextView tv_pb_rur;


    @BindView(R.id.tv_usd) TextView tv_usd;

    @BindView(R.id.tv_nbu_usd) TextView tv_nbu_usd;

    @BindView(R.id.tv_pb_usd) TextView tv_pb_usd;

    private String tv_date_label;

    private String eur_nbu_sell, eur_nbu_buy, eur_pb_sell, eur_pb_buy;

    private String rur_nbu_sell, rur_nbu_buy, rur_pb_sell, rur_pb_buy;

    private String usd_nbu_sell, usd_nbu_buy, usd_pb_sell, usd_pb_buy;

    public static DateRatesFragment newInstance() {
        return new DateRatesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tv_click_request.setOnClickListener((view -> {

        }));
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_date_rates, container, false);
        ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    private void showTvs(boolean show) {
        if (show) {
            tv_eur.setVisibility(View.VISIBLE);
            tv_nbu_eur.setVisibility(View.VISIBLE);
            tv_pb_eur.setVisibility(View.VISIBLE);

            tv_rur.setVisibility(View.VISIBLE);
            tv_nbu_rur.setVisibility(View.VISIBLE);
            tv_pb_rur.setVisibility(View.VISIBLE);

            tv_usd.setVisibility(View.VISIBLE);
            tv_nbu_usd.setVisibility(View.VISIBLE);
            tv_pb_usd.setVisibility(View.VISIBLE);
        } else {
            tv_eur.setVisibility(View.GONE);
            tv_nbu_eur.setVisibility(View.GONE);
            tv_pb_eur.setVisibility(View.GONE);

            tv_rur.setVisibility(View.GONE);
            tv_nbu_rur.setVisibility(View.GONE);
            tv_pb_rur.setVisibility(View.GONE);

            tv_usd.setVisibility(View.GONE);
            tv_nbu_usd.setVisibility(View.GONE);
            tv_pb_usd.setVisibility(View.GONE);
        }
    }

}
