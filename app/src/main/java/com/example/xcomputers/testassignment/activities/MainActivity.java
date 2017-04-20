package com.example.xcomputers.testassignment.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.example.xcomputers.testassignment.R;
import com.example.xcomputers.testassignment.screens.OnBackPressedDelegate;
import com.example.xcomputers.testassignment.screens.browsing.BrowsingViewFragment;


/**
 * Created by xComputers on 01/04/2017.
 */

/**
 * The main container activity for the screens
 */
public class MainActivity extends BaseActivity implements NavigationHandler {

    private static final int DEFAULT_DOUBLE_TAP_WAIT = 2000; //milliseconds

    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing);

        openView(BrowsingViewFragment.class, true, null);
        onBackPressedDelegate = new OnBackPressedDelegate(getSupportFragmentManager(), R.id.container);
    }

    public void openView(Class<? extends Fragment> fragment, boolean addToBackStack, Bundle args) {

        if (fragment == null) {
            return;
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ftx = fm.beginTransaction();

        String fragmentName = fragment.getName();
        Fragment fragmentToOpen = Fragment.instantiate(this, fragmentName);
        fragmentToOpen.setArguments(args);
        ftx.replace(R.id.container, fragmentToOpen, fragmentName);

        String backStackTag = addToBackStack ? fragmentName : null;
        ftx.addToBackStack(backStackTag);

        ftx.commit();
        fm.executePendingTransactions();
    }

    private void popBackStack() {

        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() <= 1) {
            System.exit(0);
        } else {
            fm.popBackStackImmediate();
        }
    }

    @Override
    public void onBackPressed() {

        if (onBackPressedDelegate.onBackPressed()) {
            return;
        }

        if (doubleBackToExitPressedOnce) {
            popBackStack();
            return;
        }

        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.double_back_exit_message, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, DEFAULT_DOUBLE_TAP_WAIT);
    }
}

