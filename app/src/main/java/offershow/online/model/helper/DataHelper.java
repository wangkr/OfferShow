package offershow.online.model.helper;

/**
 * Copyright (C) 2015 Ari C.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import offershow.online.OfferShowApp;
import offershow.online.R;
import offershow.online.config.AppCache;
import offershow.online.database.DBService;
import offershow.online.model.OfferInfo;

public class DataHelper {

    private static final int MAX_SUGGEST_COUNT = 60;

    private static List<OfferInfoWrapper> sOfferInfoWrappers = new ArrayList<>();

    private static LinkedList<OfferSuggestion> sOfferSuggestions = new LinkedList<>();

    private static DBService dbService = new DBService(OfferShowApp.getContext());

    public static void init(){
        if (OfferShowApp.isFirstUse()) {
            String[] comm_companys = OfferShowApp.getResource().getStringArray(R.array.comm_companys);
            for(String c : comm_companys) sOfferSuggestions.add(new OfferSuggestion(c));

            String[] comm_positions = OfferShowApp.getResource().getStringArray(R.array.comm_positions);
            for(String c : comm_positions) sOfferSuggestions.add(new OfferSuggestion(c));

            String[] comm_cities = OfferShowApp.getResource().getStringArray(R.array.comm_cities);
            for(String c : comm_cities) sOfferSuggestions.add(new OfferSuggestion(c));

            dbService.addAllOfferSuggestions(sOfferSuggestions);
            OfferShowApp.setFirstUse();
        } else {
            sOfferSuggestions.addAll(dbService.findAllOfferSuggestions());
        }
    }

    public static void  save() {
        dbService.addAllOfferSuggestions(sOfferSuggestions);
    }

    public interface OnFindOfferListener {
        void onResults(List<OfferInfoWrapper> results);
    }

    public interface OnFindSuggestionsListener {
        void onResults(List<OfferSuggestion> results);
    }

    public static List<OfferSuggestion> getHistory(Context context, int count) {

        List<OfferSuggestion> suggestionList = new ArrayList<>();
        OfferSuggestion offerSuggestion;
        for (int i = 0; i < sOfferSuggestions.size(); i++) {
            offerSuggestion = sOfferSuggestions.get(i);
            offerSuggestion.setIsHistory(1);
            suggestionList.add(offerSuggestion);
            if (suggestionList.size() == count) {
                break;
            }
        }
        return suggestionList;
    }

    public static void hitOrAddSuggestion(String sugg){
        boolean contains = false;
        int index = -1;
        for(OfferSuggestion os : sOfferSuggestions) {
            index++;
            if (os.getBody().equals(sugg)) {
                contains = true;
                os.hit();
                break;
            }
        }
        if (contains) {
            OfferSuggestion os = sOfferSuggestions.remove(index);
            sOfferSuggestions.addFirst(os);
        } else if (sOfferSuggestions.size() < MAX_SUGGEST_COUNT) {
            OfferSuggestion os = new OfferSuggestion(sugg);
            os.hit();
            sOfferSuggestions.addFirst(os);
        } else {
            OfferSuggestion os = new OfferSuggestion(sugg);
            os.hit();
            sOfferSuggestions.removeLast();
            sOfferSuggestions.addFirst(os);
        }
    }

    public static void resetSuggestionsHistory() {
        for (OfferSuggestion offerSuggestion : sOfferSuggestions) {
            offerSuggestion.setIsHistory(0);
        }
    }

    public static void findSuggestions(Context context, String query, final int limit, final long simulatedDelay,
                                       final OnFindSuggestionsListener listener) {
        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                try {
                    Thread.sleep(simulatedDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                DataHelper.resetSuggestionsHistory();
                List<OfferSuggestion> suggestionList = new ArrayList<>();
                if (!(constraint == null || constraint.length() == 0)) {

                    for (OfferSuggestion suggestion : sOfferSuggestions) {
                        if (suggestion.getBody().toUpperCase()
                                .startsWith(constraint.toString().toUpperCase())) {

                            suggestionList.add(suggestion);
                            if (limit != -1 && suggestionList.size() == limit) {
                                break;
                            }
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (listener != null) {
                    listener.onResults((List<OfferSuggestion>) results.values);
                }
            }
        }.filter(query);

    }


    public static void findOffers(Context context, String query, final OnFindOfferListener listener) {
        initOfferInfoWrapperList(context);

        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {


                List<OfferInfoWrapper> suggestionList = new ArrayList<>();

                if (!(constraint == null || constraint.length() == 0)) {

                    for (OfferInfoWrapper oiw : sOfferInfoWrappers) {
                        if (oiw.getCompany().toUpperCase().startsWith(constraint.toString().toUpperCase()) ||
                                oiw.getCity().toUpperCase().startsWith(constraint.toString().toUpperCase())||
                                oiw.getPosition().toUpperCase().startsWith(constraint.toString().toUpperCase())) {
                            suggestionList.add(oiw);
                        }
                    }

                }

                FilterResults results = new FilterResults();
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (listener != null) {
                    listener.onResults((ArrayList<OfferInfoWrapper>) results.values);
                }
            }
        }.filter(query);

    }

    private static void initOfferInfoWrapperList(Context context) {
        if (sOfferInfoWrappers.isEmpty()) {
            if (AppCache.getSort_items().size() > 0){
                for (OfferInfo oi :
                        AppCache.getSort_items()) {
                    sOfferInfoWrappers.add(new OfferInfoWrapper(oi.getId(), oi.getCompany(), oi.getPosition(), oi.getCity()));
                }
            }
        }
    }

    public static void  updateOfferInfoWrapperList(List<OfferInfo> ois){
        if (ois.size() > sOfferInfoWrappers.size()) {
            int oiwSize = sOfferInfoWrappers.size();
            for(int i = 0;i < ois.size() - oiwSize;i++){
                OfferInfo oi = ois.get(i);
                sOfferInfoWrappers.add(new OfferInfoWrapper(oi.getId(), oi.getCompany(), oi.getPosition(), oi.getCity()));
            }
        }
    }



}