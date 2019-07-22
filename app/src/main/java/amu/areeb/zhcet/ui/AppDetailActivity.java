package amu.areeb.zhcet.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import amu.areeb.zhcet.R;


public class AppDetailActivity extends AppCompatActivity {

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
        ctl.setTitle("ctengg");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.vector_web);
        fab.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "Go to Site", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        setupDetail();
    }

    private void setupDetail() {
        TextView title1 = (TextView) findViewById(R.id.title1);
        TextView title2 = (TextView) findViewById(R.id.title2);
        TextView title3 = (TextView) findViewById(R.id.title3);

        title1.setText("About");
        title2.setText("Libraries/Resources Used");
        title3.setText("Credits");

        TextView content1 = (TextView) findViewById(R.id.content1);
        TextView content2 = (TextView) findViewById(R.id.content2);
        TextView content3 = (TextView) findViewById(R.id.content3);

        content1.setText(R.string.app_about_detail);
        content2.setText(R.string.app_library_detail);
        content3.setText(R.string.app_credit_detail);

    }

    public void showMenu(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("http://ctengg.amu.ac.in"));
        try {
            startActivity(i);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "No Browser found", Toast.LENGTH_SHORT).show();
        }
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

}
