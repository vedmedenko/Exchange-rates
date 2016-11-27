package com.vedmedenko.exchangerates.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vedmedenko.exchangerates.R;
import com.vedmedenko.exchangerates.ui.activities.MainActivity;
import com.vedmedenko.exchangerates.utils.ConstantsManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class CurrentRatesFragment extends BaseFragment {

    @BindView(R.id.tv_eur)
    TextView tv_eur;

    @BindView(R.id.tv_rur)
    TextView tv_rur;

    @BindView(R.id.tv_usd)
    TextView tv_usd;

    private String eur, rur, usd;

    public static CurrentRatesFragment newInstance(@NonNull String[] currencies) {
        CurrentRatesFragment detailFragment = new CurrentRatesFragment();
        Bundle args = new Bundle();
        args.putString(ConstantsManager.EXTRA_CURRENCY_EUR, currencies[0]);
        args.putString(ConstantsManager.EXTRA_CURRENCY_RUR, currencies[1]);
        args.putString(ConstantsManager.EXTRA_CURRENCY_USD, currencies[2]);
        detailFragment.setArguments(args);
        return detailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eur = getArguments().getString(ConstantsManager.EXTRA_CURRENCY_EUR);
        rur = getArguments().getString(ConstantsManager.EXTRA_CURRENCY_RUR);
        usd = getArguments().getString(ConstantsManager.EXTRA_CURRENCY_USD);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_current_rates, container, false);
        ButterKnife.bind(this, fragmentView);
        tv_eur.setText(eur);
        tv_rur.setText(rur);
        tv_usd.setText(usd);
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();

        String[] currencies = ((MainActivity) getContext()).getPresenter().getCurrentCurrency();
        tv_eur.setText(currencies[0]);
        tv_rur.setText(currencies[1]);
        tv_usd.setText(currencies[2]);
    }
}
