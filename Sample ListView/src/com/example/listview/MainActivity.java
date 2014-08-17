package com.example.listview;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ActionBar;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.listview.io.BaseRequest;
import com.example.listview.io.BaseResponse;
import com.example.listview.io.DataExchangerTask;
import com.example.listview.io.HandleResponse;
import com.example.listview.models.FeedResponseModel;

public class MainActivity extends ListActivity implements HandleResponse {

	private static ExecutorService executor = Executors.newFixedThreadPool(5);
	private Handler handler = new Handler();;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_PROGRESS);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		getListView().setDivider(
				getResources().getDrawable(R.drawable.gradient_bg));
		getListView().setDividerHeight(
				getResources().getDimensionPixelSize(
						R.dimen.list_divider_height));
		setActionBar();
		refreshData();
	}

	private void refreshData() {
		BaseRequest request = new BaseRequest(Constants.FEED_URL,
				Constants.FEED_REQUEST_CODE, this);
		getExecutor()
				.execute(
						new DataExchangerTask(request, FeedResponseModel.class,
								handler));
		// setRefreshing(true);
	}

	/**
	 * get executor
	 * 
	 * @return ExecutorService
	 */
	public static ExecutorService getExecutor() {
		int processorCount = Runtime.getRuntime().availableProcessors();
		if (executor == null || executor.isShutdown()
				|| executor.isTerminated()) {
			executor = Executors.newFixedThreadPool(processorCount + 1);
		}
		return executor;
	}

	@Override
	public void handleResponse(BaseResponse responseData) {
		// TODO Auto-generated method stub
		// setProgressBarIndeterminate(false);
		if (responseData.getStatusCode() == Constants.HTTP_CODE_SUCCESS
				&& responseData.getData() != null) {
			FeedResponseModel feed = (FeedResponseModel) responseData.getData();
			setTitle(feed.getTitle());
			getListView().setAdapter(new ListAdapter(this, feed.getRows()));
		}

	}

	private void setTitle(String title) {
		TextView txtTitle = (TextView) getWindow().findViewById(R.id.txtTitle);
		txtTitle.setText(title);
	}

	/**
	 * Sets a custom action bar with title and refresh button
	 */
	private void setActionBar() {
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);

		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.actionbar_layout, null);

		ImageView refreshImage = (ImageView) mCustomView
				.findViewById(R.id.imgRefresh);
		refreshImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				refreshData();
			}
		});

		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
	}

	private void setRefreshing(boolean refresh) {
		ImageView refreshImage = (ImageView) getWindow().findViewById(
				R.id.imgRefresh);
		if (refresh) {
			RotateAnimation animation = new RotateAnimation(0f, 350f, 15f, 15f);
			animation.setInterpolator(new LinearInterpolator());
			animation.setRepeatCount(Animation.INFINITE);
			animation.setDuration(700);
			refreshImage.setAnimation(animation);
		} else {
			refreshImage.setAnimation(null);
		}
	}

}
