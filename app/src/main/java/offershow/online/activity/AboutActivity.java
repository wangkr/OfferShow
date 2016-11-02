package offershow.online.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import offershow.online.R;
import offershow.online.activity.base.BaseActivity;
import offershow.online.common.ui.dialog.EasyAlertDialogHelper;

/**
 * Created by Kairong on 2016/10/30.
 * mail:wangkrhust@gmail.com
 */
public class AboutActivity extends BaseActivity {
    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void onClickUseRules(View view){
        EasyAlertDialogHelper.showOneButtonDiolag(this, "使用规则", getString(R.string.use_rules_content), "确定", true, null);
    }

    public void onClickProjectData(View view){
        EasyAlertDialogHelper.showOneButtonDiolag(this, "项目数据", getString(R.string.project_data_content), "确定", true, null);
    }
}
