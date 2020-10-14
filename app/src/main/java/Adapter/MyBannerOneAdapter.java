package Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import fragment.SliderBannerFragmentOne;

public class MyBannerOneAdapter extends FragmentPagerAdapter {
    private static int ITEM_MAX_NUM = 10;
    private static int LOOPS_COUNT = 1000;

    private List<String> images;

    public MyBannerOneAdapter(FragmentManager fm, List<String> images) {
        super(fm);
        this.images = images;
        ITEM_MAX_NUM = images.size();
    }

    @Override
    public Fragment getItem(int i) {
        int position = i % ITEM_MAX_NUM;
        return SliderBannerFragmentOne.newInstance(images.get(position));
    }


    @Override
    public float getPageWidth(int position) {
        return super.getPageWidth(position);
    }

    @Override
    public int getCount(){
        return ITEM_MAX_NUM*LOOPS_COUNT;
    }

}
