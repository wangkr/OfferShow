package offershow.online.model.helper;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

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

public class OfferSuggestion implements SearchSuggestion {

    private String searchContent;
    private int mIsHistory = 0;
    private int searchTimes = 0;

    public OfferSuggestion(String suggestion) {
        this.searchContent = suggestion.toLowerCase();
    }

    public OfferSuggestion(Parcel source) {
        this.searchContent = source.readString();
        this.mIsHistory = source.readInt();
        this.searchTimes = source.readInt();
    }

    public int getSearchTimes() {
        return searchTimes;
    }

    public void setSearchTimes(int searchTimes) {
        this.searchTimes = searchTimes;
    }

    public void  hit(){
        this.searchTimes++;
        this.mIsHistory = 1;
    }

    public void setIsHistory(int isHistory) {
        this.mIsHistory = isHistory;
    }

    public int getIsHistory() {
        return this.mIsHistory;
    }

    @Override
    public String getBody() {
        return searchContent;
    }

    public static final Creator<OfferSuggestion> CREATOR = new Creator<OfferSuggestion>() {
        @Override
        public OfferSuggestion createFromParcel(Parcel in) {
            return new OfferSuggestion(in);
        }

        @Override
        public OfferSuggestion[] newArray(int size) {
            return new OfferSuggestion[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(searchContent);
        dest.writeInt(mIsHistory);
        dest.writeInt(searchTimes);
    }
}