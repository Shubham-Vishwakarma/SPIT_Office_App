package com.example.admin.spit;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import fragments.AboutAppFragment;
import fragments.AboutMeFragment;
import fragments.AboutOfficeFragment;
import fragments.AdminUploadFragment;
import fragments.CertificateFragment;
import fragments.ConcessionFragment;
import fragments.ExaminationFragment;
import fragments.NoticeFragment;

public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    NavigationView navigationView;
    Toolbar toolbar;

    final static String TAG="ADMIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_admin);

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        NoticeFragment noticeFragment=new NoticeFragment();
        fragmentTransaction.replace(R.id.admin_fragment_container,noticeFragment);
        fragmentTransaction.commit();

        DrawerLayout drawerLayout=(DrawerLayout)findViewById(R.id.navigation_admin);
        toolbar=(Toolbar)findViewById(R.id.admin_toolbar_id);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView=(NavigationView)findViewById(R.id.admin_drawer);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.navigation_admin);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finishAffinity();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
        switch(item.getItemId())
        {
            case R.id.action_upload:
                AdminUploadFragment adminUploadFragment=new AdminUploadFragment();
                fragmentTransaction.replace(R.id.admin_fragment_container,adminUploadFragment);
                break;
            case R.id.action_notice:
                NoticeFragment noticeFragment=new NoticeFragment();
                fragmentTransaction.replace(R.id.admin_fragment_container,noticeFragment);
                break;
            case R.id.action_examination:
                ExaminationFragment examinationFragment=new ExaminationFragment();
                fragmentTransaction.replace(R.id.admin_fragment_container,examinationFragment);
                break;
            case R.id.action_concession:
                ConcessionFragment concessionFragment=new ConcessionFragment();
                fragmentTransaction.replace(R.id.admin_fragment_container,concessionFragment);
                break;
            case R.id.action_certificate:
                CertificateFragment certificateFragment=new CertificateFragment();
                fragmentTransaction.replace(R.id.admin_fragment_container,certificateFragment);
                break;
            case R.id.action_signOut:
                signOutDialog();
                break;
            case R.id.action_aboutApp:
                AboutAppFragment aboutAppFragment=new AboutAppFragment();
                fragmentTransaction.replace(R.id.admin_fragment_container,aboutAppFragment);
                break;
            case R.id.action_aboutOffice:
                AboutOfficeFragment aboutOfficeFragment=new AboutOfficeFragment();
                fragmentTransaction.replace(R.id.admin_fragment_container,aboutOfficeFragment);
                break;
            case R.id.action_aboutMe:
                AboutMeFragment aboutMeFragment=new AboutMeFragment();
                fragmentTransaction.replace(R.id.admin_fragment_container,aboutMeFragment);
                break;
        }

        fragmentTransaction.commit();
        DrawerLayout drawerLayout=(DrawerLayout)findViewById(R.id.navigation_admin);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    void signOutDialog()
    {
        final AlertDialog.Builder builder=new AlertDialog.Builder(AdminActivity.this);
        builder.setMessage("Do you want to Sign Out").setTitle("Sign Out");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try
                {
                    if(isInternetConnected()) {
                        FirebaseAuth.getInstance().signOut();
                        finish();
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(AdminActivity.this,"Error",Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog dialog=builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorWindowBackground);
    }

    public boolean isInternetConnected()
    {
        ConnectivityManager connectivityManager=(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();

        if(networkInfo==null || !networkInfo.isConnected() || !networkInfo.isAvailable())
        {
            Toast.makeText(AdminActivity.this,"No Internet Connectivity",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}