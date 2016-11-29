package com.vedmedenko.exchangerates.ui.fragments;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.vedmedenko.exchangerates.R;
import com.vedmedenko.exchangerates.core.services.DateCurrencyService;
import com.vedmedenko.exchangerates.ui.activities.MainActivity;
import com.vedmedenko.exchangerates.ui.views.CustomBarChart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import timber.log.Timber;

public class ChartsFragment extends BaseFragment {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.bar_chart)
    CustomBarChart barChart;

    @BindView(R.id.tv_label_help)
    TextView tvHelp;

    @BindView(R.id.card_button_reload)
    CardView btnReload;

    @BindView(R.id.card_help)
    CardView cardHelp;

    private static final float GROUP_SPACE = 0.4f;
    private static final float BAR_SPACE = 0f;
    private static final float BAR_WIDTH = 0.3f;

    private static final int COLOR_WHITE = Color.rgb(255, 255, 255);
    private static final int COLOR_EUR = Color.rgb(0, 170, 160);
    private static final int COLOR_USD = Color.rgb(255, 122, 90);

    private static final float TEXT_SIZE = 16f;
    private static final float TEXT_VALUE_SIZE = 12f;
    private static float TEXT_SIZE_AXIS_X = 10f;

    public static final int DAY_COUNT = 30;
    public static final int DAY_OFFSET = 5; // API can't return data for the days that is close to today.

    private String[] dates = new String[DAY_COUNT];

    private Float[] dataEur = new Float[DAY_COUNT];
    private Float[] dataUsd = new Float[DAY_COUNT];

    private enum STATE {
        LOADED, LOADING, ERROR
    }

    private STATE state;

    public static ChartsFragment newInstance() {
        return new ChartsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        state = STATE.LOADING;

        Calendar cal = new GregorianCalendar();

        for (int i = 0; i < ChartsFragment.DAY_COUNT; i++) {
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_MONTH, (-i -ChartsFragment.DAY_OFFSET));
            String date = new SimpleDateFormat("dd.MM.yyyy", Locale.US).format(cal.getTime());
            dates[i] = date;
        }

        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, -ChartsFragment.DAY_OFFSET);
        String date = new SimpleDateFormat("dd.MM.yyyy", Locale.US).format(cal.getTime());
        ((MainActivity) getContext()).getPresenter().loadChartData(date);

        checkTextSize();
    }

    private void checkTextSize() {
        Display display = ((MainActivity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if (size.x <= 480)
            TEXT_SIZE_AXIS_X = 6f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_charts, container, false);
        ButterKnife.bind(this, fragmentView);

        formatChart();

        btnReload.setOnClickListener((view -> {
            state = STATE.LOADING;
            DateCurrencyService.clearData();

            Calendar cal = new GregorianCalendar();

            for (int i = 0; i < ChartsFragment.DAY_COUNT; i++) {
                cal.setTime(new Date());
                cal.add(Calendar.DAY_OF_MONTH, (-i -ChartsFragment.DAY_OFFSET));
                String date = new SimpleDateFormat("dd.MM.yyyy", Locale.US).format(cal.getTime());
                dates[i] = date;
            }

            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_MONTH, -ChartsFragment.DAY_OFFSET);
            String date = new SimpleDateFormat("dd.MM.yyyy", Locale.US).format(cal.getTime());
            ((MainActivity) getContext()).getPresenter().loadChartData(date);

            showProgress();
        }));

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        modifyUI();
    }

    private void formatChart() {
        barChart.setPager(((MainActivity) getContext()).getViewPager());
        barChart.setDescription(null);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setTextColor(COLOR_WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(TEXT_SIZE_AXIS_X);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                if (value >= 0.0f && value < DAY_COUNT) {
                    int index = Math.round(value);

                    if (index == DAY_COUNT)
                        index = index - 1;

                    return dates[DAY_COUNT - 1 - index];
                }

                return "";
            }
        });

        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setTextSize(TEXT_SIZE);
        yAxisLeft.setTextColor(COLOR_WHITE);
        yAxisLeft.setAxisMinimum(0.0f);

        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setTextSize(TEXT_SIZE);
        yAxisRight.setTextColor(COLOR_WHITE);
        yAxisRight.setAxisMinimum(0.0f);

        barChart.getLegend().setTextColor(COLOR_WHITE);
        barChart.getLegend().setTextSize(TEXT_SIZE);

        barChart.setScaleMinima(6f, 1f);
    }

    private void showChart() {
        List<BarEntry> entriesEur = new ArrayList<>();
        List<BarEntry> entriesUsd = new ArrayList<>();

        for (int i = 0; i < DAY_COUNT; i++) {
            entriesEur.add(new BarEntry(i, dataEur[i]));
            entriesUsd.add(new BarEntry(i, dataUsd[i]));
        }

        BarDataSet dataSetEur = new BarDataSet(entriesEur, "EUR");
        dataSetEur.setColor(COLOR_EUR);
        dataSetEur.setValueTextColor(COLOR_WHITE);
        dataSetEur.setValueTextSize(TEXT_VALUE_SIZE);

        BarDataSet dataSetUsd = new BarDataSet(entriesUsd, "USD");
        dataSetUsd.setColor(COLOR_USD);
        dataSetUsd.setValueTextColor(COLOR_WHITE);
        dataSetUsd.setValueTextSize(TEXT_VALUE_SIZE);

        BarData data = new BarData(dataSetEur, dataSetUsd);
        data.setBarWidth(BAR_WIDTH);
        barChart.setData(data);
        barChart.groupBars(- BAR_WIDTH - (GROUP_SPACE / 2), GROUP_SPACE, BAR_SPACE);
        barChart.invalidate();

        barChart.setVisibility(View.VISIBLE);
        cardHelp.setVisibility(View.GONE);
        tvHelp.setText(getString(R.string.base_help));
        progressBar.setVisibility(View.GONE);
        btnReload.setVisibility(View.VISIBLE);
    }

    public void showError() {
        barChart.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        cardHelp.setVisibility(View.VISIBLE);
        btnReload.setVisibility(View.VISIBLE);
        tvHelp.setText(getString(R.string.error_no_internet));
        state = STATE.ERROR;
    }

    public void showProgress() {
        barChart.setVisibility(View.GONE);
        cardHelp.setVisibility(View.GONE);
        btnReload.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        state = STATE.LOADING;
    }

    private void modifyUI() {
        switch (state) {
            case LOADING:
                showProgress();
                return;
            case LOADED:
                showChart();
                return;
            case ERROR:
                showError();
        }
    }

    public void setChartData(@NonNull ArrayList<String> eur, @NonNull ArrayList<String> usd) {
        for (int i = 0; i < DAY_COUNT; i++) {
            dataEur[i] = Float.valueOf(eur.get(i));
            dataUsd[i] = Float.valueOf(usd.get(i));
        }

        state = STATE.LOADED;
    }

}
