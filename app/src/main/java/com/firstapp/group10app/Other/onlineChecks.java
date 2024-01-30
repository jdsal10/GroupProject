package com.firstapp.group10app.Other;

import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class onlineChecks {

    public static void checkNavigationBar(BottomNavigationView view) {
        if ((!Session.dbStatus) || (!Session.signedIn)) {
            view.getMenu().findItem(R.id.goToHistory).setEnabled(false);
        }
    }
}
