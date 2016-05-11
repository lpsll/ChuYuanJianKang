package com.htlc.cyjk.app.util;

import com.htlc.cyjk.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * 配置ImageLoader加载图片的设置
 */
public class ImageLoaderCfg {


	public static DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.mipmap.default_head)
			.showImageForEmptyUri(R.mipmap.default_head)
			.showImageOnFail(R.mipmap.default_head)
//			.cacheInMemory(true)
//			.cacheOnDisk(true).considerExifParams(false)
//			.displayer(new SimpleBitmapDisplayer())
			.build();

	public static DisplayImageOptions fade_options = new DisplayImageOptions.Builder()
//	.showImageOnLoading(R.drawable.service_item_default)
//	.showImageForEmptyUri(R.drawable.service_item_default)
//	.showImageOnFail(R.drawable.service_item_default).cacheInMemory(true)
	.cacheOnDisk(true).considerExifParams(false)
	.displayer(new FadeInBitmapDisplayer(300)).build();

}
