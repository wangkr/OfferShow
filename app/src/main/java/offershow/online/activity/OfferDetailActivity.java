package offershow.online.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import offershow.online.R;
import offershow.online.activity.base.BaseActivity;
import offershow.online.common.http.HttpCommClient;
import offershow.online.common.http.ICommProtocol;
import offershow.online.common.ui.dialog.EasyEditDialog;
import offershow.online.common.util.sys.TimeUtil;
import offershow.online.config.Extras;
import offershow.online.database.DBService;
import offershow.online.model.MessageInfo;
import offershow.online.model.OfferInfo;
import offershow.online.model.adapter.MessageItemAdapter;

/**
 * Created by Kairong on 2016/10/30.
 * mail:wangkrhust@gmail.com
 */
public class OfferDetailActivity extends BaseActivity {
    private OfferInfo oi;
    private boolean fromDatabase;
    private ListView messageList;
    private MessageItemAdapter adapter;
    private LinkedList<MessageInfo> items;
    private DBService dbService;
    public static void start(Context context, OfferInfo oi) {
        start(context, oi, false);
    }

    public static void start(Context context, OfferInfo oi, boolean fromDatabase) {
        Intent intent = new Intent();
        intent.setClass(context, OfferDetailActivity.class);
        intent.putExtra(Extras.EXTRA_OFFER_INFO, oi);
        intent.putExtra(Extras.EXTRA_FROM_DB, fromDatabase);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_offer_detail);
        dbService = new DBService(this);

        oi = (OfferInfo)getIntent().getSerializableExtra(Extras.EXTRA_OFFER_INFO);
        fromDatabase = getIntent().getBooleanExtra(Extras.EXTRA_FROM_DB, true);

        // 不是来自本地且为空时说明发生了错误，立即返回
        if (!fromDatabase && oi == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    OfferDetailActivity.this.finish();
                }
            }, 100);
            return;
        }

        final View headerView = LayoutInflater.from(this).inflate(R.layout.layout_detail_listheader, null);

        final LinearLayout company_ll = (LinearLayout)headerView.findViewById(R.id.detail_company);
        final LinearLayout position_ll = (LinearLayout)headerView.findViewById(R.id.detail_position);
        final LinearLayout city_ll = (LinearLayout)headerView.findViewById(R.id.detail_city);
        final LinearLayout salary_ll = (LinearLayout)headerView.findViewById(R.id.detail_salary);
        final LinearLayout score_ll = (LinearLayout)headerView.findViewById(R.id.detail_score);
        final LinearLayout time_ll = (LinearLayout)headerView.findViewById(R.id.detail_time);

        ((TextView)company_ll.findViewById(R.id.offer_detail_attr)).setText(getString(R.string.company));
        ((TextView)position_ll.findViewById(R.id.offer_detail_attr)).setText(getString(R.string.position));
        ((TextView)city_ll.findViewById(R.id.offer_detail_attr)).setText(getString(R.string.city));
        ((TextView)salary_ll.findViewById(R.id.offer_detail_attr)).setText(getString(R.string.salary));
        ((TextView)score_ll.findViewById(R.id.offer_detail_attr)).setText(getString(R.string.kexindu));
        ((TextView)time_ll.findViewById(R.id.offer_detail_attr)).setText(getString(R.string.create_time));

        messageList = (ListView)findViewById(R.id.detail_messages);
        items = new LinkedList<>();
        adapter = new MessageItemAdapter(items, this);
        messageList.addHeaderView(headerView);
        messageList.setAdapter(adapter);

        if (fromDatabase) {
            oi = dbService.findOfferInfo(oi.getId());
            items = dbService.getMessages(oi.getId());

            if (oi == null) finish();
            ((TextView)company_ll.findViewById(R.id.offer_detail_value)).setText(oi.getCompany());
            ((TextView)position_ll.findViewById(R.id.offer_detail_value)).setText(oi.getPosition());
            ((TextView)city_ll.findViewById(R.id.offer_detail_value)).setText(oi.getCity());
            ((TextView)salary_ll.findViewById(R.id.offer_detail_value)).setText(oi.getSalary());
            ((TextView)time_ll.findViewById(R.id.offer_detail_value)).setText(oi.getTime());
            ((TextView)score_ll.findViewById(R.id.offer_detail_value)).setText(oi.getScore()+"");
            ((TextView)headerView.findViewById(R.id.detail_remark)).setText(getString(R.string.salary_note)+oi.getRemark());
            adapter.notifyDataSetChanged();
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
                    ((TextView) headerView.findViewById(R.id.detail_remark)).setText(getString(R.string.salary_note) + newoi.getRemark());
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
                    ((TextView) headerView.findViewById(R.id.detail_remark)).setText(getString(R.string.salary_note) + oi.getRemark());
                    setTitle("详情(浏览数:" + oi.getNumber() + ")");
                }
            });
            HttpCommClient.getInstance().getMessages(oi.getId(), new ICommProtocol.CommCallback<List<MessageInfo>>() {
                @Override
                public void onSuccess(List<MessageInfo> messageInfos) {
                    dbService.addOfferMessages(messageInfos);
                    items.addAll(messageInfos);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailed(String code, String errorMsg) {
                    showToast(city_ll, errorMsg);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_message, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_detail_addmsg) {
            final EasyEditDialog dlg = new EasyEditDialog(this);
            dlg.setTitle("添加留言");
            dlg.setEditHint("offer补充或疑问,格式学历+补充内容");
            dlg.setEditTextMaxLines(5);
            dlg.setEditTextMaxLength(140);
            dlg.addPositiveButtonListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if(!TextUtils.isEmpty(dlg.getEditMessage())){
                        HttpCommClient.getInstance().addMessage(oi.getId(), dlg.getEditMessage(), new ICommProtocol.CommCallback<String>() {
                            @Override
                            public void onSuccess(String s) {
                                MessageInfo mi = new MessageInfo();
                                mi.setTime(TimeUtil.getNowDateTime("yyyy-MM-dd HH:mm"));
                                mi.setContent(dlg.getEditMessage());
                                mi.setOfferid(oi.getId());
                                items.addFirst(mi);
                                adapter.notifyDataSetChanged();
                                dlg.dismiss();
                                showToast(messageList, s);
                            }

                            @Override
                            public void onFailed(String code, String errorMsg) {
                                showToast(messageList, errorMsg);
                            }
                        });
                    }
                }
            });

            dlg.addNegativeButtonListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlg.dismiss();
                }
            });

            dlg.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
