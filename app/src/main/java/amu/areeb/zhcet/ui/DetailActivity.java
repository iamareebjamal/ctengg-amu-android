package amu.areeb.zhcet.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import amu.areeb.zhcet.R;


public class DetailActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
        }

        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ctl.setTitle("Areeb Jamal");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "Contact", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        setupDetail();
    }

    private void setupDetail() {

        findViewById(R.id.card3).setVisibility(View.GONE);

        TextView title1 = (TextView) findViewById(R.id.title1);
        TextView title2 = (TextView) findViewById(R.id.title2);

        title1.setText("About");
        title2.setText("Other Projects");

        TextView content1 = (TextView) findViewById(R.id.content1);
        TextView content2 = (TextView) findViewById(R.id.content2);

        content1.setText(R.string.areeb_about_detail);
        content2.setText(R.string.areeb_dev_detail);
        content2.setLinkTextColor(Color.parseColor("#1de9b6"));
        content2.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void showMenu(View view) {
        PopupMenu pop = new PopupMenu(this, fab);
        MenuInflater inflater = pop.getMenuInflater();
        inflater.inflate(R.menu.contact_menu, pop.getMenu());
        pop.setOnMenuItemClickListener(this);
        pop.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // TODO: Implement this method
        int id = item.getItemId();
        Intent i = new Intent(Intent.ACTION_VIEW);
        switch (id) {
            case R.id.menu_facebook:
                i.setData(Uri.parse("https://www.facebook.com/iamareebjamal"));
                break;
            case R.id.menu_twitter:
                i.setData(Uri.parse("https://twitter.com/iamareebjamal"));
                break;
            case R.id.menu_xda:
                i.setData(Uri.parse("http://forum.xda-developers.com/member.php?u=4782403"));
                break;
            case R.id.menu_email:
                i = new Intent(Intent.ACTION_SEND);
                i.setType("plain/text");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"jamal.areeb@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "ZHCET Feedback");
                i.putExtra(Intent.EXTRA_TEXT, "Hello Areeb, I'd like to give some feedback regarding your app.");
                break;
            default:
        }
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }

}
