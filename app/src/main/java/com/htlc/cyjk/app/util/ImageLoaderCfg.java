package com.htlc.cyjk.app.util;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * 配置ImageLoader加载图片的设置
 */
public class ImageLoaderCfg {


	public static DisplayImageOptions options = new DisplayImageOptions.Builder()
//			.showImageOnLoading(R.drawable.service_item_default)
//			.showImageForEmptyUri(R.drawable.service_item_default)
//			.showImageOnFail(R.drawable.service_item_default).cacheInMemory(true)
			.cacheOnDisk(true).considerExifParams(false)
			.displayer(new SimpleBitmapDisplayer()).build();

	public static DisplayImageOptions fade_options = new DisplayImageOptions.Builder()
//	.showImageOnLoading(R.drawable.service_item_default)
//	.showImageForEmptyUri(R.drawable.service_item_default)
//	.showImageOnFail(R.drawable.service_item_default).cacheInMemory(true)
	.cacheOnDisk(true).considerExifParams(false)
	.displayer(new FadeInBitmapDisplayer(300)).build();

}
