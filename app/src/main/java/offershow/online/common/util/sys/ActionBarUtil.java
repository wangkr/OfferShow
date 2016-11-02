package offershow.online.common.util.sys;

import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import offershow.online.R;
import offershow.online.activity.base.BaseActivity;

public class ActionBarUtil {
	
	public static TextView addRightClickableTextViewOnActionBar(BaseActivity activity, int strResId,
																View.OnClickListener l) {
		return addRightClickableTextViewOnActionBar(activity, activity.getString(strResId), l);
	}
	
	public static TextView addRightClickableTextViewOnActionBar(BaseActivity activity, String text,
																View.OnClickListener l) {
		TextView textView = addRightClickableTextViewOnActionBar(activity, text);
		textView.setOnClickListener(l);
		return textView;
	}
	
	/**
	 * on the right of the actionbar , add a clickable textview
	 * 
	 * @param activity
	 * @param text
	 */
	public static TextView addRightClickableTextViewOnActionBar(BaseActivity activity, String text) {
		ActionBar actionBar = activity.getSupportActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
        View view = LayoutInflater.from(activity).inflate(R.layout.nim_action_bar_right_clickable_tv, null);
		TextView textView = (TextView) view.findViewById(R.id.action_bar_right_clickable_textview);
		textView.setText(text);
		actionBar.setCustomView(view, new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.RIGHT | Gravity.CENTER));
		return textView;
	}
	
	/**
	 * @param activity
	 * @param layoutID
	 * @return
	 */
	public static View addRightCustomViewOnActionBar(BaseActivity activity, int layoutID) {
		return addRightCustomViewOnActionBar(activity, layoutID, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
	}
	
	/**
	 * @param activity
	 * @param layoutID
	 * @param width
	 * @param height
	 * @return
	 */
	public static View addRightCustomViewOnActionBar(BaseActivity activity, int layoutID, int width, int height) {
		ActionBar actionBar = activity.getSupportActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
        View view = LayoutInflater.from(activity).inflate(layoutID, null);
		actionBar.setCustomView(view, new ActionBar.LayoutParams(width,
				height, Gravity.RIGHT | Gravity.CENTER));
		return view;
	}
	
	/**
	 * on the right of the actionbar , add a clickable textview
	 * 
	 * @param activity
	 */
	public static TextView addRightClickableBlueTextViewOnActionBar(BaseActivity activity, int strResId) {
		String text = activity.getResources().getString(strResId);
		return addRightClickableTextViewOnActionBar(activity, text);
	}

	public static void setTextViewEnable(BaseActivity activity, boolean enable) {
		ActionBar actionBar = activity.getSupportActionBar();
		View view = actionBar.getCustomView();

		if (view != null) {
			TextView textView = (TextView) view.findViewById(R.id.action_bar_right_clickable_textview);
			if (textView != null) {
				textView.setEnabled(enable);
			}
		}
	}

	public static void setTextViewVisible(BaseActivity activity, boolean visible) {
		ActionBar actionBar = activity.getSupportActionBar();
		View view = actionBar.getCustomView();

		if (view != null) {
			TextView textView = (TextView) view.findViewById(R.id.action_bar_right_clickable_textview);
			if (textView != null) {
				textView.setVisibility(visible ? View.VISIBLE : View.GONE);
			}
		}
	}

}
