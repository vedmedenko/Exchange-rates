package com.vedmedenko.exchangerates.ui.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vedmedenko.exchangerates.R;
import com.vedmedenko.exchangerates.ui.activities.MainActivity;
import com.vedmedenko.exchangerates.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DateRatesFragment extends BaseFragment {

    @BindView(R.id.card_button)
    CardView cardButton;

    @BindView(R.id.tv_click_request) TextView tv_click_request;


    @BindView(R.id.card_eur)
    CardView cardEur;

    @BindView(R.id.tv_label_eur) TextView tv_eur;

    @BindView(R.id.tv_nbu_eur) TextView tv_nbu_eur;

    @BindView(R.id.tv_pb_eur) TextView tv_pb_eur;


    @BindView(R.id.card_rur)
    CardView cardRur;

    @BindView(R.id.tv_label_rur) TextView tv_rur;

    @BindView(R.id.tv_nbu_rur) TextView tv_nbu_rur;

    @BindView(R.id.tv_pb_rur) TextView tv_pb_rur;


    @BindView(R.id.card_usd)
    CardView cardUsd;

    @BindView(R.id.tv_label_usd) TextView tv_usd;

    @BindView(R.id.tv_nbu_usd) TextView tv_nbu_usd;

    @BindView(R.id.tv_pb_usd) TextView tv_pb_usd;

    private String date_label;

    private ArrayList<String> eur, rur, usd;

    public static DateRatesFragment newInstance() {
        return new DateRatesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_date_rates, container, false);
        ButterKnife.bind(this, fragmentView);

        final Context context = getContext();

        cardButton.setOnClickListener((view -> {
            Calendar c = Calendar.getInstance();
            final int year = c.get(Calendar.YEAR);
            final int month = c.get(Calendar.MONTH) + 1;
            final int day = c.get(Calendar.DAY_OF_MONTH);

            showTvs(false);

            new DatePickerDialog(context, (v, y, m, d) -> {
                m = m + 1;

                String date = ((d < 10) ? "0" + d : d) + "." + ((m < 10) ? "0" + m : m) + "." + y;

                date_label = date;
                tv_click_request.setText(date);

                if ((y < 2015) || ((y == year) && (month == m) && (d > day - 4))) {
                    showHint(R.string.error_no_data);
                    return;
                }

                if (NetworkUtils.isNetworkConnected(context)) {
                    ((MainActivity) getContext()).getPresenter().loadDateCurrency(date);
                } else {
                    showHint(R.string.error_no_internet);
                    tv_click_request.setText(getString(R.string.click_to_make_a_request));
                }
            }, year, month - 1, day).show();
        }));

        showTvs(false);

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();

        tv_click_request.setText(date_label);

        if (eur == null && rur == null && usd == null) {
            tv_click_request.setText(getString(R.string.click_to_make_a_request));
            showTvs(false);
        } else {
            showTvs(true);
        }

        if (eur != null) {
            tv_nbu_eur.setText(getString(R.string.nbu_string, eur.get(0), eur.get(1)));
            tv_pb_eur.setText(getString(R.string.pb_string, eur.get(2), eur.get(3)));
        }

        if (rur != null) {
            tv_nbu_rur.setText(getString(R.string.nbu_string, rur.get(0), rur.get(1)));
            tv_pb_rur.setText(getString(R.string.pb_string, rur.get(2), rur.get(3)));
        }

        if (usd != null) {
            tv_nbu_usd.setText(getString(R.string.nbu_string, usd.get(0), usd.get(1)));
            tv_pb_usd.setText(getString(R.string.pb_string, usd.get(2), usd.get(3)));
        }
    }

    public void setStrings(@NonNull ArrayList<String> eur,
                           @NonNull ArrayList<String> rur,
                           @NonNull ArrayList<String> usd) {
        this.eur = eur;
        this.rur = rur;
        this.usd = usd;
    }

    public void showHint(@StringRes int res) {
        tv_eur.setText(getString(res));
        cardEur.setVisibility(View.VISIBLE);
        tv_nbu_eur.setVisibility(View.GONE);
        tv_pb_eur.setVisibility(View.GONE);
    }

    private void showTvs(boolean show) {
        if (show) {
            cardEur.setVisibility(View.VISIBLE);
            tv_eur.setText(getString(R.string.eur));
            tv_nbu_eur.setVisibility(View.VISIBLE);
            tv_pb_eur.setVisibility(View.VISIBLE);

            cardRur.setVisibility(View.VISIBLE);
            cardUsd.setVisibility(View.VISIBLE);
        } else {
            cardEur.setVisibility(View.GONE);
            cardRur.setVisibility(View.GONE);
            cardUsd.setVisibility(View.GONE);
        }
    }

}
