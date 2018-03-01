package com.i5i58.live.common.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Lee on 2017/4/7.
 */

public class BaseFragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragments;
    private List<String> mTitles;

    public BaseFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments =fragments;
    }

    public BaseFragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        this.mFragments =fragments;
        this.mTitles =titles;
    }

    @Override
    public Fragment getItem(int arg0) {
        return mFragments.get(arg0);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}