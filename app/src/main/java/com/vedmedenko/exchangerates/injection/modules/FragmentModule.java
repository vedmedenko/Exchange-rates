package com.vedmedenko.exchangerates.injection.modules;

import android.content.Context;
import android.support.v4.app.Fragment;

import dagger.Module;
import dagger.Provides;
import com.vedmedenko.exchangerates.injection.ActivityContext;

@Module
public class FragmentModule {

    private Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        mFragment = fragment;
    }

    @Provides
    Fragment provideFragment() {
        return mFragment;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mFragment.getContext();
    }

}
