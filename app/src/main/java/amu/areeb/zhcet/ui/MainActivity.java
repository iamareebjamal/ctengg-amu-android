package amu.areeb.zhcet.ui;

import amu.areeb.zhcet.R;
import amu.areeb.zhcet.ui.fragment.AttendanceFragment;
import amu.areeb.zhcet.ui.fragment.ResultFragment;
import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private String NAV_ITEM_ID = "selected";
    private int mNavItemId;
    private AttendanceFragment af;
    private ResultFragment rf;
    private DrawerLayout drawer;
    private NavigationView navView;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tool);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Home");
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.nav_view);
        if (navView != null) {
            setupNavigation(navView);
        }
        splash(savedInstanceState);
        if (savedInstanceState == null) {
            af = new AttendanceFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, af);
            ft.commit();
            navView.getMenu().getItem(0).setChecked(true);
            getSupportActionBar().setTitle(navView.getMenu().getItem(0).getTitle());
        } else {
            mNavItemId = savedInstanceState.getInt(NAV_ITEM_ID);
            MenuItem m = navView.getMenu().findItem(mNavItemId);
            m.setChecked(true);
            getSupportActionBar().setTitle(m.getTitle());
        }
    }

    private void splash(final Bundle savedInstanceState) {

        final LinearLayout splash = (LinearLayout) findViewById(R.id.splash);

        splash.post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int x = (splash.getRight() + splash.getLeft()) / 2;
                    int y = (splash.getTop() + splash.getBottom()) / 2;
                    float radius = (float) Math.hypot(splash.getMeasuredHeight(), splash.getMeasuredWidth());
                    Animator anim = ViewAnimationUtils.createCircularReveal(splash, x, y, radius, 0);
                    anim.setDuration(500);
                    anim.setInterpolator(new AccelerateDecelerateInterpolator());
                    //anim.setInterpolator(new FastOutLinearInInterpolator());
                    anim.addListener(new Animator.AnimatorListener() {

                        @Override
                        public void onAnimationStart(Animator p1) {
                            // TODO: Implement this method


                        }

                        @Override
                        public void onAnimationEnd(Animator p1) {
                            // TODO: Implement this method
                            splash.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator p1) {
                            // TODO: Implement this method
                        }

                        @Override
                        public void onAnimationRepeat(Animator p1) {
                            // TODO: Implement this method
                        }
                    });
                    anim.start();
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    ValueAnimator va = ObjectAnimator.ofInt(splash, "backgroundColor", getResources().getColor(R.color.purple_dark), Color.parseColor("#ffffff"));
                    va.setEvaluator(new ArgbEvaluator());
                    va.setRepeatCount(0);
                    va.setRepeatMode(ValueAnimator.REVERSE);
                    va.addListener(new ValueAnimator.AnimatorListener() {

                        @Override
                        public void onAnimationStart(Animator p1) {
                            // TODO: Implement this method


                        }

                        @Override
                        public void onAnimationEnd(Animator p1) {
                            // TODO: Implement this method
                            splash.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator p1) {
                            // TODO: Implement this method
                        }

                        @Override
                        public void onAnimationRepeat(Animator p1) {
                            // TODO: Implement this method
                        }


                    });
                    va.setDuration(1000);
                    va.start();

                } else {
                    splash.setVisibility(View.GONE);
                }
            }
        });

    }

    public void setUsername(String username) {
        TextView user = (TextView) navView.getHeaderView(0).findViewById(R.id.username);
        user.setVisibility(View.VISIBLE);
        user.setText(username);
    }

    public void setDetail(String detail) {
        TextView detailtv = (TextView) navView.getHeaderView(0).findViewById(R.id.detail);
        detailtv.setVisibility(View.VISIBLE);
        detailtv.setText(detail);
    }

    private void setupNavigation(NavigationView navView) {
        // TODO: Implement this method
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menu) {
                // TODO: Implement this method
                menu.setChecked(true);
                //Toast.makeText(getApplicationContext(), menu.getTitle(), Toast.LENGTH_SHORT).show();
                int id = menu.getItemId();

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                switch (id) {
                        /*case R.id.nav_home:
                            HomeFragment hf = new HomeFragment();
							ft.replace(R.id.frame, hf);
							ft.commit();
							mNavItemId = id;
							getSupportActionBar().setTitle(menu.getTitle());
							break;*/
                    case R.id.nav_attendance:
                        if (af == null)
                            af = new AttendanceFragment();
                        ft.replace(R.id.frame, af);
                        ft.commit();
                        mNavItemId = id;
                        getSupportActionBar().setTitle("Attendance");
                        break;
                    case R.id.nav_result:
                        if (rf == null)
                            rf = new ResultFragment();
                        ft.replace(R.id.frame, rf);
                        ft.commit();
                        mNavItemId = id;
                        getSupportActionBar().setTitle("Result");
                        break;
                    case R.id.nav_feedback:
                        final TextInputLayout til = new TextInputLayout(MainActivity.this);
                        til.setHint("Enter your feedback");
                        final AppCompatEditText edt = new AppCompatEditText(MainActivity.this);
                        til.addView(edt);
                        til.setPadding(20, 20, 20, 20);
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
                        alert.setTitle("Send Feedback");
                        alert.setView(til);

                        alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface p1, int p2) {
                                // TODO: Implement this method

                            }


                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface p1, int p2) {
                                // TODO: Implement this method
                                p1.dismiss();
                            }


                        });
                        final AlertDialog dialog = alert.create();
                        dialog.show();
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View p1) {
                                // TODO: Implement this method
                                String feed = edt.getText().toString();
                                if (feed == null || feed.length() < 10) {
                                    til.setError("Write at least 10 letters");
                                } else {
                                    Intent mailIntent = new Intent(Intent.ACTION_SEND);
                                    mailIntent.setType("plain/text");
                                    mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"jamal.areeb@gmail.com"});
                                    mailIntent.putExtra(Intent.EXTRA_SUBJECT, "ZHCET App Feedback");
                                    mailIntent.putExtra(Intent.EXTRA_TEXT, feed);
                                    try {
                                        startActivity(mailIntent);
                                    } catch (ActivityNotFoundException e) {
                                        Toast.makeText(getApplicationContext(), "No email app found on device!", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.dismiss();
                                }
                            }
                        });
                        menu.setChecked(false);
                        break;

                    case R.id.nav_app:
                        startActivity(new Intent(getApplicationContext(), AppDetailActivity.class));
                        menu.setChecked(false);
                        break;
                    case R.id.nav_about:
                        startActivity(new Intent(getApplicationContext(), DetailActivity.class));
                        menu.setChecked(false);
                        return true;
                }
                drawer.closeDrawers();
                return false;
            }


        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAV_ITEM_ID, mNavItemId);
    }

}
