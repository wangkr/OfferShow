package offershow.online.model.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.marshalchen.ultimaterecyclerview.SwipeableUltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

import java.util.List;

import offershow.online.R;
import offershow.online.model.OfferInfo;
import offershow.online.model.viewholder.OfferItemViewHolder;
import offershow.online.model.viewholder.OfferViewHolderBase;

/**
 * Created by Kairong on 2016/10/30.
 * mail:wangkrhust@gmail.com
 */
public class OfferItemAdapter extends SwipeableUltimateViewAdapter<OfferInfo> {
    private List<OfferInfo> data;
    private OfferViewHolderBase.EventListener eventListener;
    private LayoutInflater inflater;

    public OfferItemAdapter(Context context, List<OfferInfo> list, OfferViewHolderBase.EventListener eventListener) {
        super(list);
        this.eventListener = eventListener;
        this.data = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    protected void withBindHolder(UltimateRecyclerviewViewHolder holder, OfferInfo data, int position) {
        if (position < getAdapterItemCount()) {
            ((OfferViewHolderBase)holder).refreshCurrentItem(getItem(position));
        }
    }

    @Override
    protected UltimateRecyclerviewViewHolder newViewHolder(View view) {
        return new OfferItemViewHolder(view);
    }

    @Override
    public UltimateRecyclerviewViewHolder newFooterHolder(View view) {
        return new OfferItemViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder newCustomViewHolder(View view) {
        View mView = inflater.inflate(R.layout.layout_offerinfo_item, null);
        return new OfferItemViewHolder(mView);
    }

    @Override
    protected int getNormalLayoutResId() {
        return R.layout.layout_offerinfo_item;
    }


    @Override
    public int getAdapterItemCount() {
        return data.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPES.NORMAL;
    }

    protected OfferInfo getItem(final int pos) {
        synchronized (mLock) {
            return data.get(pos);
        }
    }
}
