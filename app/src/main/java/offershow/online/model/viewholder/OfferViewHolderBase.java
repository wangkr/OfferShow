package offershow.online.model.viewholder;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

import offershow.online.common.util.log.LogUtil;
import offershow.online.config.Constants;
import offershow.online.R;
import offershow.online.activity.OfferDetailActivity;
import offershow.online.model.OfferInfo;

/**
 * Created by Kairong on 2016/10/30.
 * mail:wangkrhust@gmail.com
 */
public abstract class OfferViewHolderBase extends UltimateRecyclerviewViewHolder<OfferInfo> {
    protected OfferInfo item;

    protected TextView company_tv;
    protected TextView city_tv;
    protected TextView position_tv;
    protected TextView score_tv;
    protected TextView number_tv;
    protected TextView time_tv;
    protected TextView tag_tv;
    protected View eye_v;
    public interface EventListener{

    }

    // 在该接口中根据layout对各控件成员变量赋值
    abstract protected void inflateContentView();

    // 将消息数据项与内容的view进行绑定
    abstract protected void bindContentView();


    public OfferViewHolderBase(View itemView) {
        super(itemView);
        inflate();
    }

    // 内容区域点击事件响应处理。
    protected void onItemClick() {
        if (item.isTag()) return;
        OfferDetailActivity.start(getContext(), item);
    }

    protected final void inflate(){
        company_tv = findViewByIdEfficient(R.id.offer_item_company);
        position_tv = findViewByIdEfficient(R.id.offer_item_position);
        city_tv = findViewByIdEfficient(R.id.offer_item_city);
        score_tv = findViewByIdEfficient(R.id.offer_item_score);
        number_tv = findViewByIdEfficient(R.id.offer_item_number);
        time_tv = findViewByIdEfficient(R.id.offer_item_time);
        tag_tv = findViewByIdEfficient(R.id.offer_item_tag);
        eye_v = findViewByIdEfficient(R.id.offer_detail_eye);
        inflateContentView();
    }

    protected final void refresh(Object _item){
        this.item = (OfferInfo)_item;
        if (!item.isTag() && (item.getMask() & Constants.company_mask) != 0) {
            company_tv.setVisibility(View.VISIBLE);
            company_tv.setText(item.getCompany());
        } else {
            company_tv.setText("");
            company_tv.setVisibility(View.GONE);
        }

        if (!item.isTag() && (item.getMask() & Constants.position_mask) != 0) {
            position_tv.setVisibility(View.VISIBLE);
            position_tv.setText(item.getPosition());
        } else {
            position_tv.setVisibility(View.GONE);
            position_tv.setText("");
        }

        if (!item.isTag() && (item.getMask() & Constants.city_mask) != 0) {
            city_tv.setVisibility(View.VISIBLE);
            city_tv.setText(item.getCity());
        } else {
            city_tv.setVisibility(View.GONE);
            city_tv.setText("");
        }

        if (!item.isTag()) {
            ((CardView)findViewByIdEfficient(R.id.offer_item_cv)).setCardBackgroundColor(getResources().getColor(R.color.white));
            score_tv.setText(item.getScore()+"");
            number_tv.setText(item.getNumber()+"");
            time_tv.setText(item.getTime());
            score_tv.setVisibility(View.VISIBLE);
            number_tv.setVisibility(View.VISIBLE);
            time_tv.setVisibility(View.VISIBLE);
            eye_v.setVisibility(View.VISIBLE);
            tag_tv.setVisibility(View.GONE);
        } else {
            if ((item.getMask() & Constants.position_mask) != 0) {
                tag_tv.setText(item.getPosition());
            } else if ((item.getMask() & Constants.company_mask) != 0) {
                tag_tv.setText(item.getCompany());
            } else if ((item.getMask() & Constants.city_mask) != 0) {
                tag_tv.setText(item.getCity());
            }
            score_tv.setText("");
            number_tv.setText("");
            time_tv.setText("");
            score_tv.setVisibility(View.GONE);
            number_tv.setVisibility(View.GONE);
            time_tv.setVisibility(View.GONE);
            eye_v.setVisibility(View.GONE);
            tag_tv.setVisibility(View.VISIBLE);
            ((CardView)findViewByIdEfficient(R.id.offer_item_cv)).setCardBackgroundColor(getResources().getColor(R.color.color_b3b3b3));
        }

        bindContentView();
        getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick();
            }
        });
    }

    public void refreshCurrentItem(OfferInfo data) {
        if (data != null) {
            refresh(data);
        }
    }
}
