package com.sakhatech.parkme.Activity.home;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.sakhatech.parkme.Activity.BaseActivity;
import com.sakhatech.parkme.Activity.home.model.Menu;
import com.sakhatech.parkme.Activity.signup.LoginScreen;
import com.sakhatech.parkme.ParkMeApplication;
import com.sakhatech.parkme.util.ParkMeSharedPreference;
import com.sakhatech.parkme.util.Utility;
import com.sakhatech.parkme.Activity.signup.crop.ImageCropActivity;
import com.sakhatech.spotizen.BuildConfig;
import com.sakhatech.spotizen.R;
import com.sakhatech.parkme.util.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Bheema.
 * Company Techjini
 */
public class HomeActivity extends BaseActivity {


    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private HomeMapFragment currentFragment;
    private NavigationMenuAdapter mNavigationMenuAdapter;
    ListView mMenuList;
    ActionBarDrawerToggle toggle;


    ArrayList<Menu> menus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dash_board_layout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);

//        setUpNavDrawer();
        toggle = new ActionBarDrawerToggle(this, mDrawerLayout, R
                .string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setItemIconTintList(null);

        mMenuList = (ListView) findViewById(R.id.navigation_item_listview);
        if (Utility.getProfileFile(this, ImageCropActivity.TEMP_PHOTO_FILE_NAME).exists()) {
            try {
                File f = Utility.getProfileFile(this, ImageCropActivity.TEMP_PHOTO_FILE_NAME);
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                ImageView img = (ImageView) findViewById(R.id.profile_image);
                img.setImageBitmap(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (isLocationPermisionEnabled()) {
            addMapFragment();
        }

    }

    private void addMapFragment() {
        HomeMapFragment fragment = new HomeMapFragment();
        addFragment(fragment);
        setMenuList();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    private void setMenuList() {

        Menu myParking = new Menu();
        myParking.menuId = 1;
        myParking.name = getString(R.string.my_parkings);
        myParking.resId = R.drawable.my_parkings_icon;
        menus.add(myParking);

        Menu payments = new Menu();
        payments.menuId = 2;
        payments.name = getString(R.string.payments);
        payments.resId = R.drawable.payments_icon;
        menus.add(payments);

        Menu rewards = new Menu();
        rewards.menuId = 3;
        rewards.name = getString(R.string.rewards);
        rewards.resId = R.drawable.rewards_icon;
        menus.add(rewards);

        Menu tutorial = new Menu();
        tutorial.menuId = 4;
        tutorial.name = getString(R.string.tutorial);
        tutorial.resId = R.drawable.help;
        menus.add(tutorial);

        Menu refferals = new Menu();
        refferals.menuId = 5;
        refferals.name = getString(R.string.refferals);
        refferals.resId = R.drawable.referrals_icon;
        menus.add(refferals);

        Menu support = new Menu();
        support.menuId = 6;
        support.name = getString(R.string.support);
        support.resId = R.drawable.support_icon;
        menus.add(support);

        Menu general = new Menu();
        general.menuId = 7;
        general.name = getString(R.string.general);
        menus.add(general);

        Menu share = new Menu();
        share.menuId = 8;
        share.name = getString(R.string.share);
        share.resId = R.drawable.share_icon;
        menus.add(share);

        Menu logout = new Menu();
        logout.menuId = 9;
        logout.name = getString(R.string.logout);
        logout.resId = R.drawable.logout_icon;
        menus.add(logout);


        mNavigationMenuAdapter = new NavigationMenuAdapter(this, menus);
        mMenuList.setAdapter(mNavigationMenuAdapter);

        mMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Menu menu = mNavigationMenuAdapter.getItem(position);

                if (!Utility.isOnline(HomeActivity.this)) {
                    showDialog(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismissDailog();
                        }
                    }, getString(R.string.button_ok), getString(R.string.no_network), getString(R.string.check_internet), true);
                    return;
                }

                mNavigationMenuAdapter.notifyDataSetChanged();
                handleMenuClick(menu, view);

            }
        });

    }

    private void unSelectOthers(ArrayList<Menu> menus) {
        for (Menu menu : menus
                ) {
            menu.isSelected = false;
        }
    }

    private void handleMenuClick(Menu menu, View view) {
        switch (menu.menuId) {
            case 0:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                popAllFragmentFromBackStack();
                return;
            case 1:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return;
            case 2:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return;
            case 3:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return;
            case 4:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return;
            case 5:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return;
            case 6:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return;
            case 7:
                return;
            case 8:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return;
            case 9:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                logout();
                return;

            default:
                return;
        }
    }

    private void logout() {
        ParkMeSharedPreference.getInstance(this).clearAll();

        //start Login Screen
        Intent intent = new Intent(HomeActivity.this, LoginScreen.class);
        startActivity(intent);
        HomeActivity.this.finish();

    }


    private void addOtherFragments(Fragment fragment, String tag) {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            getSupportFragmentManager().beginTransaction().add(R.id.nav_contentframe, fragment, tag).hide(currentFragment).addToBackStack(tag).commit();
        } else {
            getSupportFragmentManager().popBackStackImmediate();
            getSupportFragmentManager().beginTransaction().add(R.id.nav_contentframe, fragment, tag).hide(currentFragment).addToBackStack(tag).commit();
        }
    }

    private void popAllFragmentFromBackStack() {
        int entryCount = getSupportFragmentManager().getBackStackEntryCount();
        while (entryCount > 0) {
            getSupportFragmentManager().popBackStack();
            entryCount--;
        }
        getSupportFragmentManager().beginTransaction().show(currentFragment).commit();

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStack();
                if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                    getSupportFragmentManager().beginTransaction().show(currentFragment).commit();
                    resetMenuToDefault();
                }
            } else {
                super.onBackPressed();
                getSupportFragmentManager().popBackStack();
                resetMenuToDefault();
            }
        }
    }


    private void resetMenuToDefault() {
        resetMenuSelection();
        if (mNavigationMenuAdapter != null) {
            mNavigationMenuAdapter.setMenus(menus);
            mNavigationMenuAdapter.notifyDataSetChanged();
        }
    }

    public void resetMenuSelection() {
        for (Menu menu : menus
                ) {
            if (menu.menuId == 0) {
                menu.isSelected = true;
            } else {
                menu.isSelected = false;
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void addFragment(HomeMapFragment fragment) {
        getSupportFragmentManager().beginTransaction().add(R.id.nav_contentframe, fragment, fragment.getClass().getName()).commit();
        currentFragment = fragment;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean isLocationPermisionEnabled() {

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                noSufficientPermission(Constants.FINE_LOCATION_PERMISSION_REQUEST_CODE);

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.FINE_LOCATION_PERMISSION_REQUEST_CODE:

                switch (resultCode) {
                    case Activity.RESULT_OK:
                        addMapFragment();
                        break;
                    case Activity.RESULT_CANCELED:
                        finish();
                        break;
                }
                break;
            case HomeMapFragment.REQUEST_CHECK_SETTINGS:
                currentFragment.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (requestCode == Constants.FINE_LOCATION_PERMISSION_REQUEST_CODE || permissions[0].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION)) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addMapFragment();
                    }
                },3000);

            }

        } else {
            if (requestCode == Constants.FINE_LOCATION_PERMISSION_REQUEST_CODE) {
                finish();
            }
        }


    }


}
