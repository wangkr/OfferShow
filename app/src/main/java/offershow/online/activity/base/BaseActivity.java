package offershow.online.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import offershow.online.OfferShowApp;
import offershow.online.R;
import offershow.online.common.http.HttpClientBase;
import offershow.online.common.http.HttpCommClient;

/**
 * Created by Kairong on 2016/10/30.
 * mail:wangkrhust@gmail.com
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(displayHomeAsUpEnabled());
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    protected boolean displayHomeAsUpEnabled() {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onNavigateUpClicked();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onNavigateUpClicked() {
        onBackPressed();
    }

    private static int snack_show_count = 0;
    protected void showToast(View view, CharSequence msg, int duration, String actionBtnText, View.OnClickListener clickListener) {
        // 清理内存
        if (snack_show_count >= 8){
            System.gc();
            snack_show_count = 0;
        }
        Snackbar.make(view, msg, duration)
                .setAction(actionBtnText, clickListener)
                .setActionTextColor(getResources().getColor(R.color.colorAccent))
                .show();
        snack_show_count ++;
    }

    protected void showToast(View view, CharSequence msg, int duration) {
        showToast(view, msg, duration, null, null);
    }

    protected void showToast(View view, CharSequence msg){
        showToast(view, msg, Snackbar.LENGTH_SHORT);
    }

    protected void showToast(View view, @StringRes int resId) {
        showToast(view, OfferShowApp.getResource().getString(resId));
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
