package offershow.online.model;

/**
 * Created by Kairong on 2016/10/30.
 * mail:wangkrhust@gmail.com
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.marshalchen.ultimaterecyclerview.SwipeableUltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.layoutmanagers.ScrollSmoothLineaerLayoutManager;
import com.marshalchen.ultimaterecyclerview.swipe.SwipeItemManagerInterface;

import java.lang.ref.WeakReference;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInRightAnimator;
import offershow.online.config.AppCache;
import offershow.online.OfferShowApp;
import offershow.online.R;
import offershow.online.activity.MainActivity;
import offershow.online.activity.NewOfferActivity;
import offershow.online.common.http.HttpCommClient;
import offershow.online.common.http.ICommProtocol;
import offershow.online.common.util.sys.NetworkUtil;
import offershow.online.database.DBService;
import offershow.online.model.adapter.OfferItemAdapter;
import offershow.online.model.helper.DataHelper;
import offershow.online.view.widget.NestedLinearLayoutManager;


/**
 * Created by Kairong on 2015/11/16.
 * mail:wangkrhust@gmail.com
 */
public class OfferListPanel implements MainActivity.OnGroupMenuSelectListener, MainActivity.OnSortMenuSelectedListener{
    public static final String TAG = OfferListPanel.class.getSimpleName();
    public static final int REQ_NEW_OFFER = 11;
    private int group_type = -1; // -1-none 0-city 1-company 2-position
    private int sort_type = -1;  // -1-none 0-time 1-score   2-number
    // container
    private Activity context;

    // message list view

    private UltimateRecyclerView offerListView;
    private FloatingActionButton floatingActionButton;
    private ScrollSmoothLineaerLayoutManager mLayoutManager;
    private OfferItemAdapter currentAdapter;
    private OfferItemAdapter sortAdapter;
    private OfferItemAdapter cityAdapter;
    private OfferItemAdapter comAdapter;

    private MessageLoader messageLoader;

    // service
    private DBService dbService;

    private Handler uiHandler;

    private boolean isRefreshingLike = false;

    public OfferListPanel(Activity context) {
        this(context, null);
    }

    public OfferListPanel(Activity context, Bundle bundle) {
        this.context = context;
        this.sort_type = 0;
        init(bundle);
    }



    public boolean onBackPressed() {
        dbService.addAllOffers(AppCache.getSort_items());
        uiHandler.removeCallbacks(null);
        return false;
    }

    public void reload(final Bundle bundle) {
        // 重新load
        messageLoader = new MessageLoader(bundle.getBundle("messageLoader"));

        offerListView.setDefaultOnRefreshListener(messageLoader);

        AppCache.sortByShijian();
        currentAdapter = sortAdapter;
        currentAdapter.notifyDataSetChanged();
    }


    private void init(Bundle bundle) {
        this.uiHandler = new Handler();
        initService();
        initListView(bundle);
    }

    private void initService() {
        dbService = new DBService(OfferShowApp.getContext());
    }

    private void initListView(Bundle bundle) {
        sortAdapter = new OfferItemAdapter(context, AppCache.getSort_items(), null);
        cityAdapter = new OfferItemAdapter(context, AppCache.getCity_items(), null);
        comAdapter = new OfferItemAdapter(context, AppCache.getCompany_items(), null);

        // adapters setting
        sortAdapter.setMode(SwipeItemManagerInterface.Mode.Multiple);
        cityAdapter.setMode(SwipeItemManagerInterface.Mode.Multiple);
        comAdapter.setMode(SwipeItemManagerInterface.Mode.Multiple);

        // init current state
        currentAdapter = sortAdapter;
        sort_type = 0;
        group_type = -1;

        mLayoutManager = new ScrollSmoothLineaerLayoutManager(context, LinearLayoutManager.VERTICAL, false, 500);

        offerListView = (UltimateRecyclerView) context.findViewById(R.id.offer_listview_rv);
        offerListView.disableLoadmore();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            offerListView.setNestedScrollingEnabled(true);
        }

        floatingActionButton = (FloatingActionButton) context.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewOfferActivity.start(context, REQ_NEW_OFFER);
            }
        });

        offerListView.setHasFixedSize(false);
        offerListView.setLayoutManager(mLayoutManager);
        offerListView.setItemAnimator(new FadeInRightAnimator());
        offerListView.requestDisallowInterceptTouchEvent(true);
        offerListView.setRefreshing(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            offerListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }


        enableEmptyViewPolicy();
        // adapter
        offerListView.setAdapter(currentAdapter);

        if (bundle != null) {
            reload(bundle);
        } else {
            messageLoader = new MessageLoader(null);
            offerListView.setDefaultOnRefreshListener(messageLoader);
        }

        offerListView.setRefreshing(true);

        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                messageLoader.isRefreshing = true;
                messageLoader.onRefresh();
            }
        },500);
    }

    private MyHandler handler = new MyHandler(this);

    private static class MyHandler extends Handler{
        private WeakReference<OfferListPanel> listPanelWeakReference;
        public MyHandler(OfferListPanel fragment) {
            this.listPanelWeakReference = new WeakReference<OfferListPanel>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {

        }
    }

    public boolean addOfferInfo2Server(final OfferInfo oi) {
        // send message to server and saveAll to db
        HttpCommClient.getInstance().addOfferInfo(oi, new ICommProtocol.CommCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                messageLoader.onRefresh();
            }

            @Override
            public void onFailed(String code, String errorMsg) {
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    protected void enableEmptyViewPolicy() {
        //  ultimateRecyclerView.setEmptyView(R.layout.empty_view, UltimateRecyclerView.EMPTY_KEEP_HEADER_AND_LOARMORE);
        //    ultimateRecyclerView.setEmptyView(R.layout.empty_view, UltimateRecyclerView.EMPTY_KEEP_HEADER);
        //  ultimateRecyclerView.setEmptyView(R.layout.empty_view, UltimateRecyclerView.EMPTY_SHOW_LOADMORE_ONLY);
        offerListView.setEmptyView(R.layout.empty_view, UltimateRecyclerView.EMPTY_KEEP_HEADER_AND_LOARMORE);
    }

    // 刷新消息列表
    public void refreshMessageList() {
        context.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                currentAdapter.notifyDataSetChanged();
            }
        });
    }

    public void scrollToTop(){
        mLayoutManager.smoothScrollToPosition(offerListView.mRecyclerView, null, 0);
    }

    public void scrollToItem(final int position) {
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                offerListView.scrollVerticallyToPosition(position);
            }
        }, 50);
    }


    /**
     * 刷新单条消息
     *
     * @param index
     */
    private void refreshViewHolderByIndex(final int index) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (index < 0) {
                    return;
                }
                currentAdapter.notifyItemChanged(index);
            }
        });
    }

    private int getItemIndex(int id) {
        for (int i = 0; i < AppCache.getSort_items().size(); i++) {
            OfferInfo oi = AppCache.getSort_items().get(i);
            if (oi.getId() == id) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 菜单业务流程
     */
    /*分类业务*/
    @Override
    public void onCompanySelect() {
        if (group_type == 1) return;

        AppCache.groupByCompany();
        currentAdapter = comAdapter;
        offerListView.swapAdapter(currentAdapter,false);
        currentAdapter.notifyDataSetChanged();
        scrollToItem(0);
        sort_type = -1;
        group_type = 1;
    }

    @Override
    public void onCitySelect() {
        if (group_type == 0) return;

        AppCache.groupByCity();
        currentAdapter = cityAdapter;
        offerListView.swapAdapter(currentAdapter,false);
        currentAdapter.notifyDataSetChanged();
        scrollToItem(0);
        sort_type = -1;
        group_type = 0;
    }

    @Override
    public void onPosiSelect() {
//        if (group_type == 2) return;
//
//        AppCache.groupByPosition();
//        currentAdapter = posAdapter;
//        offerListView.swapAdapter(currentAdapter,false);
//        currentAdapter.notifyDataSetChanged();
//        sort_type = -1;
//        group_type = 2;
    }

    /*排序业务*/
    @Override
    public void onShijianSelect() {
        if (sort_type == 0) return;

        AppCache.sortByShijian();
        currentAdapter = sortAdapter;
        offerListView.swapAdapter(currentAdapter, false);
        currentAdapter.notifyDataSetChanged();
        scrollToItem(0);
        sort_type = 0;
        group_type = -1;
    }

    @Override
    public void onKexinduSelect() {
        if (sort_type == 1) return;

        AppCache.sortByKexindu();
        currentAdapter = sortAdapter;
        offerListView.swapAdapter(currentAdapter, false);
        currentAdapter.notifyDataSetChanged();
        scrollToItem(0);
        sort_type = 1;
        group_type = -1;
    }

    @Override
    public void onLiulanSelect() {
        if (sort_type == 2) return;

        AppCache.sortByLiulan();
        currentAdapter = sortAdapter;
        offerListView.swapAdapter(currentAdapter, false);
        currentAdapter.notifyDataSetChanged();
        scrollToItem(0);
        sort_type = 2;
        group_type = -1;
    }

    private class MessageLoader implements SwipeRefreshLayout.OnRefreshListener/*, UltimateRecyclerView.OnLoadMoreListener */{

        private boolean firstLoad = true;

        public boolean isLastRow = false;

        public volatile boolean isRefreshing = false;


        public MessageLoader(Bundle bundle) {
            if (bundle != null) {
                this.isLastRow = bundle.getBoolean("isLastRow");
            } else {
                loadFromLocal();
                AppCache.sortByShijian();
                currentAdapter.notifyDataSetChanged();
            }
        }

        public Bundle getBundle() {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isLastRow", isLastRow);
            return bundle;
        }

        private void loadFromDB() {
            if (AppCache.getSort_items().size() > 0) {
                AppCache.clear();
            }

            // 加载缓存
            List<OfferInfo> _items = dbService.findAllByIds();
            AppCache.addAll(_items);
        }

        private void loadFromLocal() {
            loadFromDB();
            if (firstLoad) {
                firstLoad = false;
            }
        }


        // 从服务器加载
        private void loadFromRemote() {
            HttpCommClient.getInstance().getAllOfferInfo(new ICommProtocol.CommCallback<List<OfferInfo>>() {
                @Override
                public void onSuccess(List<OfferInfo> offerInfos) {
                    if (offerInfos.size() == 0) return;

                    DataHelper.updateOfferInfoWrapperList(offerInfos);
                    dbService.addAllOffers(offerInfos);
                    AppCache.clear();
                    AppCache.addAll(offerInfos);
                    AppCache.sortByShijian();

                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (currentAdapter != sortAdapter) {
                                currentAdapter = sortAdapter;
                                offerListView.swapAdapter(currentAdapter, false);
                            }
                            currentAdapter.notifyDataSetChanged();
                            sort_type = 0;
                            group_type = -1;
                        }
                    });

//                    if (AppCache.getSize() == 0){ // 本地没有数据
//                        AppCache.addAll(offerInfos);
//                        context.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (currentAdapter != sortAdapter) {
//                                    currentAdapter = sortAdapter;
//                                    offerListView.swapAdapter(currentAdapter, false);
//                                }
//                                currentAdapter.notifyDataSetChanged();
//                            }
//                        });
//                    } else { // 已经有数据了
//                        int first_id = AppCache.getItems().getFirst().getId();
//                        int l = -1;
//                        while (++l < offerInfos.size() && offerInfos.get(l).getId() > first_id) {
//                            AppCache.addItem(offerInfos.get(l));
//                        }
//                        if (l > 0) {
//                            final int count = l;
//                            context.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (currentAdapter != sortAdapter) {
//                                        currentAdapter = sortAdapter;
//                                        offerListView.swapAdapter(currentAdapter, false);
//                                    }
//                                    currentAdapter.notifyItemRangeInserted(0, count);
//                                }
//                            });
//                        }
//                    }

                    uiHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onLoad();
                        }
                    }, 100);
                }

                @Override
                public void onFailed(String code, final String errorMsg) {
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }

        // 加载/刷新完成调用函数
        private void onLoad() {
            isRefreshing = false;
            offerListView.setRefreshing(false);
        }


        /**
         * *************** OnRefreshListener ***************
         */

        /**
         * 刷新后必须尽可能让用户最近发表的新鲜事能看到
         */
        @Override
        public void onRefresh() {
            if (isNetworkOk()) {
                loadFromRemote();
            }
        }
    }

    /**
     * 检查网络连接
     * @return
     */
    private boolean isNetworkOk() {
        // 提示网络无连接
        if (!NetworkUtil.isNetAvailable(context)) {
            uiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, context.getString(R.string.network_is_not_available), Toast.LENGTH_SHORT).show();
                    messageLoader.onLoad();
                }
            }, 100);
            return false;
        }

        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

}
