package offershow.online.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import offershow.online.R;
import offershow.online.activity.base.BaseActivity;
import offershow.online.common.util.sys.NetworkUtil;
import offershow.online.config.Extras;
import offershow.online.model.OfferInfo;
import offershow.online.model.adapter.SearchResultsListAdapter;
import offershow.online.model.helper.OfferInfoWrapper;

/**
 * Created by Kairong on 2016/10/30.
 * mail:wangkrhust@gmail.com
 */
public class SearchResultActivity extends BaseActivity {

    public static void start(Context context, ArrayList<OfferInfoWrapper> oiws) {
        Intent intent = new Intent();
        intent.setClass(context, SearchResultActivity.class);
        intent.putParcelableArrayListExtra(Extras.EXTRA_QUERY_STRING, oiws);
        context.startActivity(intent);
    }

    private ArrayList<OfferInfoWrapper> oiws;

    private RecyclerView mSearchResultsList;

    private SearchResultsListAdapter mSearchResultsAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        oiws = getIntent().getParcelableArrayListExtra(Extras.EXTRA_QUERY_STRING);
        mSearchResultsList = (RecyclerView)findViewById(R.id.search_results_list);
        setupResultsList();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }




    private void setupResultsList() {
        mSearchResultsAdapter = new SearchResultsListAdapter();
        mSearchResultsAdapter.swapData(oiws);
        mSearchResultsList.setAdapter(mSearchResultsAdapter);
        mSearchResultsList.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultsAdapter.setItemsOnClickListener(new SearchResultsListAdapter.OnItemClickListener() {
            @Override
            public void onClick(OfferInfoWrapper colorWrapper) {
                if (NetworkUtil.isNetAvailable(SearchResultActivity.this)){
                    OfferInfo oi = new OfferInfo();
                    oi.setId(colorWrapper.getId());
                    oi.setCompany(colorWrapper.getCompany());
                    oi.setCity(colorWrapper.getCity());
                    oi.setPosition(colorWrapper.getPosition());
                    OfferDetailActivity.start(SearchResultActivity.this, oi);
                }
            }
        });
    }
}
