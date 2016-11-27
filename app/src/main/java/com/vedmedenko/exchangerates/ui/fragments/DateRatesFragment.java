package com.vedmedenko.exchangerates.ui.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
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
import timber.log.Timber;

public class DateRatesFragment extends BaseFragment {

    @BindView(R.id.tv_click_request) TextView tv_click_request;


    @BindView(R.id.tv_label_eur) TextView tv_eur;

    @BindView(R.id.tv_nbu_eur) TextView tv_nbu_eur;

    @BindView(R.id.tv_pb_eur) TextView tv_pb_eur;


    @BindView(R.id.tv_label_rur) TextView tv_rur;

    @BindView(R.id.tv_nbu_rur) TextView tv_nbu_rur;

    @BindView(R.id.tv_pb_rur) TextView tv_pb_rur;


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

        tv_click_request.setOnClickListener((view -> {
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
                }
            }, year, month - 1, day).show();
        }));

        showTvs(false);

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (eur != null) {
            tv_click_request.setText(date_label);

            tv_nbu_eur.setText(getString(R.string.nbu_string, eur.get(0), eur.get(1)));
            tv_pb_eur.setText(getString(R.string.pb_string, eur.get(2), eur.get(3)));

            tv_nbu_rur.setText(getString(R.string.nbu_string, rur.get(0), rur.get(1)));
            tv_pb_rur.setText(getString(R.string.pb_string, rur.get(2), rur.get(3)));

            tv_nbu_usd.setText(getString(R.string.nbu_string, usd.get(0), usd.get(1)));
            tv_pb_usd.setText(getString(R.string.pb_string, usd.get(2), usd.get(3)));
        } else {
            tv_click_request.setText(getString(R.string.click_to_make_a_request));
            showTvs(false);
        }
    }

    public void setStrings(@NonNull ArrayList<String> eur,
                           @NonNull ArrayList<String> rur,
                           @NonNull ArrayList<String> usd) {
        this.eur = eur;
        this.rur = rur;
        this.usd = usd;
    }

    private void showHint(@StringRes int res) {
        tv_nbu_eur.setText(getString(res));
        tv_nbu_eur.setVisibility(View.VISIBLE);
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
