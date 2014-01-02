package rs.pedjaapps.KernelTuner.ui;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.adapter.MenuAdapter;
import rs.pedjaapps.KernelTuner.fragments.CpuFragment;
import rs.pedjaapps.KernelTuner.model.MainOption;
import rs.pedjaapps.KernelTuner.model.MainOptionsCollection;

public class MainActivity extends Activity
{
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private CharSequence mDescription;
    private List<MainOption> menuList;
    private static final int MENU_ITEM_ID_SETTINGS = 1001;
    private static final int MENU_ITEM_ID_SWAP = 1002;
    private static final int MENU_ITEM_ID_COMP_CHECK = 1003;
    MenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        menuList = MainOptionsCollection.getInstance().getMainOptions();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        adapter = new MenuAdapter(this, R.layout.menu_item_layout, menuList);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.on,  /* "open drawer" description for accessibility */
                R.string.off  /* "close drawer" description for accessibility */
        )
        {
            public void onDrawerClosed(View view)
            {
                getActionBar().setTitle(mTitle);
                getActionBar().setSubtitle(getString(R.string.action_bar_subtitle));
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView)
            {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null)
        {
            selectItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(1, MENU_ITEM_ID_SETTINGS, 1, getString(R.string.settings))
                .setIcon(R.drawable.settings_dark)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        menu.add(2, MENU_ITEM_ID_COMP_CHECK, 2, getString(R.string.compatibility_check))
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(3, MENU_ITEM_ID_SWAP, 3, getString(R.string.swap)).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(MENU_ITEM_ID_SETTINGS).setVisible(!drawerOpen);
        menu.findItem(MENU_ITEM_ID_COMP_CHECK).setVisible(!drawerOpen);
        menu.findItem(MENU_ITEM_ID_SWAP).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        if (item.getItemId() == MENU_ITEM_ID_SETTINGS)
        {
            startActivity(new Intent(this, Preferences.class));
        }
        else if (item.getItemId() == MENU_ITEM_ID_COMP_CHECK)
        {
            startActivity(new Intent(this, CompatibilityCheck.class));
        }
        else if (item.getItemId() == MENU_ITEM_ID_SWAP)
        {
            Intent myIntent = new Intent(this, Swap.class);
            startActivity(myIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            selectItem(position);
        }
    }

    private void selectItem(int position)
    {
        // update the main content by replacing fragments
        Fragment fragment = getFragment(position);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(menuList.get(position).getTitle());
        setDescription(menuList.get(position).getDescription());
        mDrawerLayout.closeDrawer(mDrawerList);
        adapter.setSelectedItem(position);
    }

    private Fragment getFragment(int position)
    {
        switch(position)
        {
            case 0:
                return CpuFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public void setTitle(int title)
    {
        mTitle = getString(title);
        getActionBar().setTitle(mTitle);
    }

    private void setDescription(int description)
    {
        mDescription = getString(description);
        getActionBar().setSubtitle(description);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}