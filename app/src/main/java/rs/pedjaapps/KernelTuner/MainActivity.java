package rs.pedjaapps.KernelTuner;

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import rs.pedjaapps.KernelTuner.fragment.FragmentOverview;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appBarLayout = (AppBarLayout)findViewById(R.id.appBarLayout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle(getTitle());

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        if(savedInstanceState == null)
        {
            onNavigationItemSelected(navigationView.getMenu().findItem(R.id.menuOverview));
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_closed)
        {
            public void onDrawerClosed(View view)
            {
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView)
            {
                supportInvalidateOptionsMenu();
            }
        };

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public void setToolbar(String title, int height)
    {
        collapsingToolbarLayout.setTitle(title);
        ViewGroup.LayoutParams params = appBarLayout.getLayoutParams();
        params.height = height;
        appBarLayout.setLayoutParams(params);

    }

    @Override
    public void setTitle(CharSequence title)
    {
        super.setTitle(title);
        collapsingToolbarLayout.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        switch (item.getItemId())
        {

        }
        return super.onOptionsItemSelected(item);
    }

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

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem)
    {
        Fragment fragment = null;
        switch (menuItem.getItemId())
        {
            case R.id.menuOverview:
                fragment = FragmentOverview.newInstance();
                break;
        }
        if(fragment != null)
        {
            menuItem.setChecked(true);
            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
        }
        mDrawerLayout.closeDrawer(navigationView);
        return false;
    }

    public static int getToolbarHeight()
    {
        int[] abSzAttr = new int[] { android.R.attr.actionBarSize };

        TypedArray a = App.getApp().obtainStyledAttributes(abSzAttr);
        return a.getDimensionPixelSize(0, -1);
    }
}
