package ryulib;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class Fragments {

    private static Fragments obj = null;

    public static void initialize(Activity activity) {
        obj = new Fragments();
        obj.activity = activity;
    }

    public static Fragments getObj() {
        return obj;
    }

    public void add(int layout, Fragment fragment) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(layout, fragment);
        fragmentTransaction.commit();

        currentFragment = fragment;
    }

    public void remove(Fragment fragment) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();

        if (fragment == currentFragment) currentFragment = null;
    }

    public void remove() {
        if (currentFragment == null) return;

        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(currentFragment);
        fragmentTransaction.commit();

        currentFragment = null;
    }

    public void show(int layout, Fragment fragment) {
        remove();
        add(layout, fragment);
    }

    private Activity activity;
    private Fragment currentFragment = null;
}
