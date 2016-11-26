package com.vedmedenko.exchangerates.core.storage;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import com.vedmedenko.exchangerates.utils.AlarmUtils;

public class TimePreference extends DialogPreference {

    private int lastHour = 0;
    private int lastMinute = 0;

    private TimePicker picker = null;
    private final Context context;

    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }

    @Override
    protected View onCreateDialogView() {
        picker = new TimePicker(getContext());
        return(picker);
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        picker.setCurrentHour(lastHour);
        picker.setCurrentMinute(lastMinute);
    }

    @Override
    protected void onDialogClosed(boolean result) {
        super.onDialogClosed(result);

        if (result) {
            lastHour = picker.getCurrentHour();
            lastMinute = picker.getCurrentMinute();

            String time = ((lastHour < 10) ? "0" : "") + String.valueOf(lastHour) + ":" + ((lastMinute < 10) ? "0" : "") + String.valueOf(lastMinute);

            if (callChangeListener(time)) {
                persistString(time);
                AlarmUtils.cancelEverydayAlarm(context);
                AlarmUtils.setupEverydayAlarm(context);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String time;

        if (restoreValue) {
            if (defaultValue == null) {
                time = getPersistedString("09:00");
            } else {
                time = getPersistedString(defaultValue.toString());
            }
        } else {
            time = defaultValue.toString();
        }

        lastHour = AlarmUtils.getHour(time);
        lastMinute = AlarmUtils.getMinute(time);
    }
}
