package com.firstapp.group10app.Other;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.firstapp.group10app.R;

public class FragmentHolderUpdate {
    public static void updateView(Fragment view, AppCompatActivity a) {
        FragmentManager fragmentManager = a.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentHolder, view);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
