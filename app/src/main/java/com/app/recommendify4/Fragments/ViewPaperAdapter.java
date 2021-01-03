package com.app.recommendify4.Fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.ArrayList;

public class ViewPaperAdapter extends FragmentStateAdapter {

    private final ArrayList<Fragment> listFragments = new ArrayList<>();
    private final ArrayList<String> fragmnetsNames = new ArrayList<>();

    public ViewPaperAdapter(@NonNull FragmentLauncher_History fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return listFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return listFragments.size();
    }

    public void addFragment (Fragment fragment, String title){
        listFragments.add(fragment);
        fragmnetsNames.add(title);
    }

}
