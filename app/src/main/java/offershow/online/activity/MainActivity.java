package offershow.online.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;

import java.util.ArrayList;
import java.util.List;

import offershow.online.R;
import offershow.online.activity.base.BaseActivity;
import offershow.online.common.ui.dialog.CustomAlertDialog;
import offershow.online.model.OfferListPanel;
import offershow.online.model.helper.DataHelper;
import offershow.online.model.helper.OfferInfoWrapper;
import offershow.online.model.helper.OfferSuggestion;

public class MainActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener{
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;
    private OfferListPanel offerListPanel;
    private FloatingSearchView mSearchView;
    private AppBarLayout mAppBar;
    private Handler handler = new Handler();

    private boolean mIsDarkSearchTheme = true;

    private String mLastQuery = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        offerListPanel = new OfferListPanel(MainActivity.this);

        mSearchView = (FloatingSearchView)findViewById(R.id.floating_search_view);
        mAppBar = (AppBarLayout)findViewById(R.id.appBar);
        mAppBar.addOnOffsetChangedListener(this);

        setupFloatingSearch();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    private void showSortChoiceDlg(){
        CustomAlertDialog alertDialog = new CustomAlertDialog(this);
        alertDialog.addItem("按时间(由近至远)", new CustomAlertDialog.onSeparateItemClickListener() {
            @Override
            public void onClick() {
                offerListPanel.onShijianSelect();
            }
        });
        alertDialog.addItem("按可信度(由高到低)", new CustomAlertDialog.onSeparateItemClickListener() {
            @Override
            public void onClick() {
                offerListPanel.onKexinduSelect();
            }
        });
        alertDialog.addItem("按浏览量(由多到少)", new CustomAlertDialog.onSeparateItemClickListener() {
            @Override
            public void onClick() {
                offerListPanel.onLiulanSelect();
            }
        });
        alertDialog.setTitle("");
        alertDialog.show();
    }

    private void showGroupChoiceDlg(){
        CustomAlertDialog alertDialog = new CustomAlertDialog(this);
        alertDialog.addItem("按公司名称", new CustomAlertDialog.onSeparateItemClickListener() {
            @Override
            public void onClick() {
                offerListPanel.onCompanySelect();
            }
        });
        alertDialog.addItem("按工作城市", new CustomAlertDialog.onSeparateItemClickListener() {
            @Override
            public void onClick() {
                offerListPanel.onCitySelect();
            }
        });
        alertDialog.setTitle("");
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_menu_sort:
                showSortChoiceDlg();
                break;
            case R.id.menu_main_menu_group:
                showGroupChoiceDlg();
                break;
            case R.id.menu_main_menu_about:
                AboutActivity.start(this);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == OfferListPanel.REQ_NEW_OFFER) {
            showToast(findViewById(R.id.fab), "爆料成功");
        }
    }

    private void setupFloatingSearch() {
        //demonstrate setting colors for items
        mSearchView.setBackgroundColor(Color.parseColor("#484848"));
        mSearchView.setViewTextColor(Color.parseColor("#e9e9e9"));
        mSearchView.setHintTextColor(Color.parseColor("#e9e9e9"));
        mSearchView.setActionMenuOverflowColor(Color.parseColor("#e9e9e9"));
        mSearchView.setMenuItemIconColor(Color.parseColor("#e9e9e9"));
        mSearchView.setLeftActionIconColor(Color.parseColor("#e9e9e9"));
        mSearchView.setClearBtnColor(Color.parseColor("#e9e9e9"));
        mSearchView.setDividerColor(Color.parseColor("#BEBEBE"));
        mSearchView.setLeftActionIconColor(Color.parseColor("#e9e9e9"));
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {

                    //this shows the top left circular progress
                    //you can call it where ever you want, but
                    //it makes sense to do it when loading something in
                    //the background.
                    mSearchView.showProgress();

                    //simulates a query call to a data source
                    //with a new query.

                    DataHelper.findSuggestions(MainActivity.this, newQuery, 5,
                            FIND_SUGGESTION_SIMULATED_DELAY, new DataHelper.OnFindSuggestionsListener() {

                                @Override
                                public void onResults(List<OfferSuggestion> results) {

                                    //this will swap the data and
                                    //render the collapse/expand animations as necessary
                                    mSearchView.swapSuggestions(results);

                                    //let the users know that the background
                                    //process has completed
                                    mSearchView.hideProgress();
                                }
                            });
                }

                Log.d(TAG, "onSearchTextChanged()");
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                mSearchView.showProgress();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DataHelper.hitOrAddSuggestion(searchSuggestion.getBody());

                        OfferSuggestion colorSuggestion = (OfferSuggestion) searchSuggestion;
                        DataHelper.findOffers(MainActivity.this, colorSuggestion.getBody(),
                                new DataHelper.OnFindOfferListener() {

                                    @Override
                                    public void onResults(List<OfferInfoWrapper> results) {
                                        mSearchView.hideProgress();
                                        SearchResultActivity.start(MainActivity.this, (ArrayList<OfferInfoWrapper>)results);
                                    }

                                });
                    }
                },200);

                Log.d(TAG, "onSuggestionClicked()");

                mLastQuery = searchSuggestion.getBody();
            }

            @Override
            public void onSearchAction(String query) {
                mLastQuery = query;
                DataHelper.hitOrAddSuggestion(query);

                DataHelper.findOffers(MainActivity.this, query,
                        new DataHelper.OnFindOfferListener() {

                            @Override
                            public void onResults(List<OfferInfoWrapper> results) {
                                SearchResultActivity.start(MainActivity.this, (ArrayList<OfferInfoWrapper>)results);
                            }

                        });
                Log.d(TAG, "onSearchAction()");
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //show suggestions when search bar gains focus (typically history suggestions)
                        mSearchView.swapSuggestions(DataHelper.getHistory(MainActivity.this, 5));
                    }
                },200);

                Log.d(TAG, "onFocus()");
            }

            @Override
            public void onFocusCleared() {

                //set the title of the bar so that when focus is returned a new query begins
                mSearchView.setSearchBarTitle(mLastQuery);
                //you can also set setSearchText(...) to make keep the query there when not focused and when focus returns
                //mSearchView.setSearchText(searchSuggestion.getBody());

                Log.d(TAG, "onFocusCleared()");
            }
        });


        //handle menu clicks the same way as you would
        //in a regular activity
        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {

                if (item.getItemId() == R.id.menu_main_menu_sort) {
                    showSortChoiceDlg();
                } else if(item.getItemId() == R.id.menu_main_menu_group) {
                    //just print action
                    showGroupChoiceDlg();
                } else if (item.getItemId() == R.id.menu_main_menu_about) {
                    AboutActivity.start(MainActivity.this);
                }

            }
        });

        //use this listener to listen to menu clicks when app:floatingSearch_leftAction="showHome"
        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {

                Log.d(TAG, "onHomeClicked()");
            }
        });

        /*
         * Here you have access to the left icon and the text of a given suggestion
         * item after as it is bound to the suggestion list. You can utilize this
         * callback to change some properties of the left icon and the text. For example, you
         * can load the left icon images using your favorite image loading library, or change text color.
         *
         *
         * Important:
         * Keep in mind that the suggestion list is a RecyclerView, so views are reused for different
         * items in the list.
         */
        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition) {
                OfferSuggestion colorSuggestion = (OfferSuggestion) item;

                String textColor = mIsDarkSearchTheme ? "#ffffff" : "#000000";
                String textLight = mIsDarkSearchTheme ? "#bfbfbf" : "#787878";

                if (colorSuggestion.getIsHistory() == 1) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_history_black_24dp, null));

                    Util.setIconColor(leftIcon, Color.parseColor(textColor));
                    leftIcon.setAlpha(.36f);
                } else {
                    leftIcon.setAlpha(0.0f);
                    leftIcon.setImageDrawable(null);
                }

                textView.setTextColor(Color.parseColor(textColor));
                String text = colorSuggestion.getBody()
                        .replaceFirst(mSearchView.getQuery(),
                                "<font color=\"" + textLight + "\">" + mSearchView.getQuery() + "</font>");
                textView.setText(Html.fromHtml(text));
            }

        });

        //listen for when suggestion list expands/shrinks in order to move down/up the
        //search results list
        mSearchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
            @Override
            public void onSuggestionsListHeightChanged(float newHeight) {

            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        mSearchView.setTranslationY(verticalOffset);
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.setSearchFocused(false)) {
            return;
        }
        DataHelper.save();
        offerListPanel.onBackPressed();
        super.onBackPressed();
    }

    public interface OnSortMenuSelectedListener {
        void onShijianSelect();
        void onKexinduSelect();
        void onLiulanSelect();
    }

    public interface OnGroupMenuSelectListener {
        void onCompanySelect();
        void onCitySelect();
        void onPosiSelect();
    }
}
