package com.example.admin.spit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import fragments.AboutAppFragment;
import fragments.AboutOfficeFragment;
import fragments.LoginFragment;
import fragments.SignInFragment;

public class LoginActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_login);

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        LoginFragment loginFragment=new LoginFragment();
        fragmentTransaction.replace(R.id.login_fragment_container,loginFragment);
        fragmentTransaction.commit();

        DrawerLayout drawerLayout=(DrawerLayout)findViewById(R.id.navigation_login);
        toolbar=(Toolbar)findViewById(R.id.login_toolbar_id);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = (NavigationView)findViewById(R.id.login_drawer);
        navigationView.setNavigationItemSelectedListener(this);
    }


    public boolean onNavigationItemSelected(MenuItem item)
    {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

        switch (item.getItemId())
        {
            case R.id.action_loginIn:
                LoginFragment loginFragment=new LoginFragment();
                fragmentTransaction.replace(R.id.login_fragment_container,loginFragment);
                break;
            case R.id.action_signIn:
                SignInFragment signInFragment=new SignInFragment();
                fragmentTransaction.replace(R.id.login_fragment_container,signInFragment);
                break;
            case R.id.action_aboutApp:
                AboutAppFragment aboutAppFragment=new AboutAppFragment();
                fragmentTransaction.replace(R.id.login_fragment_container,aboutAppFragment);
                break;
            case R.id.action_aboutOffice:
                AboutOfficeFragment aboutOfficeFragment=new AboutOfficeFragment();
                fragmentTransaction.replace(R.id.login_fragment_container,aboutOfficeFragment);
                break;
        }

        fragmentTransaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.navigation_login);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.navigation_login);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
