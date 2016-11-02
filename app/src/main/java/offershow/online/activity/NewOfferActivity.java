package offershow.online.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METLengthChecker;

import offershow.online.R;
import offershow.online.activity.base.BaseActivity;
import offershow.online.common.http.HttpCommClient;
import offershow.online.common.http.ICommProtocol;
import offershow.online.common.util.sys.NetworkUtil;
import offershow.online.model.OfferInfo;

/**
 * Created by Kairong on 2016/10/30.
 * mail:wangkrhust@gmail.com
 */
public class NewOfferActivity extends BaseActivity {
    private MaterialEditText company_met;
    private MaterialEditText position_met;
    private MaterialEditText salary_met;
    private MaterialEditText city_met;
    private MaterialEditText remark_met;

    public static void start(Context context, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, NewOfferActivity.class);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_offer);

        company_met = (MaterialEditText)findViewById(R.id.new_offer_company);
        position_met = (MaterialEditText)findViewById(R.id.new_offer_position);
        salary_met = (MaterialEditText)findViewById(R.id.new_offer_salry);
        city_met = (MaterialEditText)findViewById(R.id.new_offer_city);
        remark_met = (MaterialEditText)findViewById(R.id.new_offer_remark);
    }

    public void onCommitClick(final View v){
        if (TextUtils.isEmpty(company_met.getText().toString()) || TextUtils.isEmpty(position_met.getText().toString())
                || TextUtils.isEmpty(salary_met.getText().toString()) || TextUtils.isEmpty(city_met.getText().toString())) {
            showToast(v, "信息不完整");
            return;
        }

        OfferInfo oi = new OfferInfo();
        oi.setCompany(company_met.getText().toString());
        oi.setSalary(salary_met.getText().toString());
        oi.setCity(city_met.getText().toString());
        oi.setPosition(position_met.getText().toString());

        oi.setRemark(remark_met.getText().toString());
        if (!NetworkUtil.isNetAvailable(this)){
            showToast(v, "网络无连接");
            return;
        }
        HttpCommClient.getInstance().addOfferInfo(oi, new ICommProtocol.CommCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailed(String code, String errorMsg) {
                showToast(v, errorMsg);
            }
        });
    }
}
