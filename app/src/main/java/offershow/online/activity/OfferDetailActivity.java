package offershow.online.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import offershow.online.R;
import offershow.online.activity.base.BaseActivity;
import offershow.online.common.http.HttpCommClient;
import offershow.online.common.http.ICommProtocol;
import offershow.online.common.util.sys.NetworkUtil;
import offershow.online.database.DBService;
import offershow.online.model.OfferInfo;

/**
 * Created by Kairong on 2016/10/30.
 * mail:wangkrhust@gmail.com
 */
public class OfferDetailActivity extends BaseActivity {
    private OfferInfo oi;
    private boolean fromDatabase;

    public static void start(Context context, OfferInfo oi) {
        start(context, oi, false);
    }

    public static void start(Context context, OfferInfo oi, boolean fromDatabase) {
        Intent intent = new Intent();
        intent.setClass(context, OfferDetailActivity.class);
        intent.putExtra("OfferInfo", oi);
        intent.putExtra("fromDatabase", fromDatabase);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);
        oi = (OfferInfo)getIntent().getSerializableExtra("OfferInfo");
        fromDatabase = getIntent().getBooleanExtra("fromDatabase", true);

        if (!fromDatabase && oi == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    OfferDetailActivity.this.finish();
                }
            }, 100);
            return;
        }

        final LinearLayout company_ll = (LinearLayout)findViewById(R.id.detail_company);
        final LinearLayout position_ll = (LinearLayout)findViewById(R.id.detail_position);
        final LinearLayout city_ll = (LinearLayout)findViewById(R.id.detail_city);
        final LinearLayout salary_ll = (LinearLayout)findViewById(R.id.detail_salary);
        final LinearLayout score_ll = (LinearLayout)findViewById(R.id.detail_score);
        final LinearLayout time_ll = (LinearLayout)findViewById(R.id.detail_time);

        ((TextView)company_ll.findViewById(R.id.offer_detail_attr)).setText(getString(R.string.company));
        ((TextView)position_ll.findViewById(R.id.offer_detail_attr)).setText(getString(R.string.position));
        ((TextView)city_ll.findViewById(R.id.offer_detail_attr)).setText(getString(R.string.city));
        ((TextView)salary_ll.findViewById(R.id.offer_detail_attr)).setText(getString(R.string.salary));
        ((TextView)score_ll.findViewById(R.id.offer_detail_attr)).setText(getString(R.string.kexindu));
        ((TextView)time_ll.findViewById(R.id.offer_detail_attr)).setText(getString(R.string.create_time));

        if (fromDatabase && !NetworkUtil.isNetAvailable(this)) {
            DBService dbService = new DBService(this);
            oi = dbService.findOfferInfo(oi.getId());
            if (oi == null) finish();
            ((TextView)company_ll.findViewById(R.id.offer_detail_value)).setText(oi.getCompany());
            ((TextView)position_ll.findViewById(R.id.offer_detail_value)).setText(oi.getPosition());
            ((TextView)city_ll.findViewById(R.id.offer_detail_value)).setText(oi.getCity());
            ((TextView)salary_ll.findViewById(R.id.offer_detail_value)).setText(oi.getSalary());
            ((TextView)time_ll.findViewById(R.id.offer_detail_value)).setText(oi.getTime());
            ((TextView)score_ll.findViewById(R.id.offer_detail_value)).setText(oi.getScore()+"");
            ((TextView)findViewById(R.id.detail_remark)).setText(getString(R.string.salary_note)+oi.getRemark());
        } else {
            HttpCommClient.getInstance().getOfferInfo(oi.getId(), new ICommProtocol.CommCallback<OfferInfo>() {
                @Override
                public void onSuccess(OfferInfo newoi) {
                    ((TextView) company_ll.findViewById(R.id.offer_detail_value)).setText(newoi.getCompany());
                    ((TextView) position_ll.findViewById(R.id.offer_detail_value)).setText(newoi.getPosition());
                    ((TextView) city_ll.findViewById(R.id.offer_detail_value)).setText(newoi.getCity());
                    ((TextView) salary_ll.findViewById(R.id.offer_detail_value)).setText(newoi.getSalary());
                    ((TextView) time_ll.findViewById(R.id.offer_detail_value)).setText(newoi.getTime());
                    ((TextView) score_ll.findViewById(R.id.offer_detail_value)).setText(newoi.getScore() + "");
                    ((TextView) findViewById(R.id.detail_remark)).setText(getString(R.string.salary_note) + newoi.getRemark());
                    setTitle("详情(浏览数:" + newoi.getNumber() + ")");
                }

                @Override
                public void onFailed(String code, String errorMsg) {
                    showToast(city_ll, errorMsg);
                    ((TextView) company_ll.findViewById(R.id.offer_detail_value)).setText(oi.getCompany());
                    ((TextView) position_ll.findViewById(R.id.offer_detail_value)).setText(oi.getPosition());
                    ((TextView) city_ll.findViewById(R.id.offer_detail_value)).setText(oi.getCity());
                    ((TextView) salary_ll.findViewById(R.id.offer_detail_value)).setText(oi.getSalary());
                    ((TextView) time_ll.findViewById(R.id.offer_detail_value)).setText(oi.getTime());
                    ((TextView) score_ll.findViewById(R.id.offer_detail_value)).setText(oi.getScore() + "");
                    ((TextView) findViewById(R.id.detail_remark)).setText(getString(R.string.salary_note) + oi.getRemark());
                    setTitle("详情(浏览数:" + oi.getNumber() + ")");
                }
            });
        }




        findViewById(R.id.detail_dislike).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                HttpCommClient.getInstance().dislikeOfferInfo(oi.getId(), new ICommProtocol.CommCallback<String>() {
                    @Override
                    public void onSuccess(String s) {
                        showToast(v, s);
                    }

                    @Override
                    public void onFailed(String code, String errorMsg) {
                        showToast(v, errorMsg);
                    }
                });
            }
        });

        findViewById(R.id.detail_like).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                HttpCommClient.getInstance().likeOfferInfo(oi.getId(), new ICommProtocol.CommCallback<String>() {
                    @Override
                    public void onSuccess(String s) {
                        showToast(v, s);
                    }

                    @Override
                    public void onFailed(String code, String errorMsg) {
                        showToast(v, errorMsg);
                    }
                });
            }
        });
    }
}
