/**********************************************************************
 * AUTHOR：YOLANDA
 * DATE：2015年4月8日上午9:03:54
 * Copyright © 56iq. All Rights Reserved
 *======================================================================
 * EDIT HISTORY
 *----------------------------------------------------------------------
 * |  DATE      | NAME       | REASON       | CHANGE REQ.
 *----------------------------------------------------------------------
 * | 2015年4月8日    | YOLANDA    | Created      |
 *
 * DESCRIPTION：create the File, and add the content.
 *
 ***********************************************************************/
package com.galaxybruce.component.util.bitmap;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.webkit.MimeTypeMap;

/**
 * Date: 2017/3/29
 * Author: bruce.zhang
 * Description: 图片扫描
 *
 * Modification  History:
 */
public class AppMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

	/**
	 * 扫描对象
	 */
	private MediaScannerConnection mediaScanConn = null;

	public AppMediaScanner(Context context) {
		mediaScanConn = new MediaScannerConnection(context, this);
	}

	/**文件路径集合**/
	private String[] filePaths;
	/**文件MimeType集合**/
	private String[] mimeTypes;

	private int scanTimes = 0;

	/**
	 * 扫描文件
	 * @param filePaths
	 * @param mimeTypes
	 */
	public void scanFiles(String[] filePaths, String[] mimeTypes) {
		this.filePaths = filePaths;
		this.mimeTypes = mimeTypes;
		mediaScanConn.connect();//连接扫描服务
	}

	/**
	 *  return MimeType of the given url.
	 * @param url
	 * @return
     */
	public String getMimeTypeFromUrl(String url)
	{
		String extension = MimeTypeMap.getFileExtensionFromUrl(url);
		return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
	}

	@Override
	public void onMediaScannerConnected() {
		if(filePaths == null) return;

		String mimeType = null;
		for (int i = 0; i < filePaths.length; i++) {
			if(mimeTypes != null && mimeTypes.length > i)
			{
				mimeType = mimeTypes[i];
			}
			if(mimeType == null)
			{
				mimeType = getMimeTypeFromUrl(filePaths[i]);
			}
			mediaScanConn.scanFile(filePaths[i], mimeType);//服务回调执行扫描
		}
	}

	/**
	 * 扫描一个文件完成
	 * 如果扫描完了全部文件 断开扫描服务 复位计数
	 *
	 * @param path
	 * @param uri
	 */
	@Override
	public void onScanCompleted(String path, Uri uri) {
		scanTimes ++;
		if(scanTimes == filePaths.length) {
			mediaScanConn.disconnect();
			scanTimes = 0;
		}
	}

	public static void scanMedia(Context context, String path)
	{
//		File appFold = FileUtils.getSaveFolder(null, true);
//		File nomedia = new File(appFold.getAbsolutePath() + "/.nomedia");
//		if(nomedia.exists())
//		{
//			nomedia.delete();
//		}

		AppMediaScanner mediaScanner = new AppMediaScanner(context.getApplicationContext());
		String[] filePaths = new String[]{path};
		mediaScanner.scanFiles(filePaths, null);


//		MediaScannerConnection.scanFile(mContext, new String[]{imagePath}, null, null);

//		Uri contentUri = Uri.fromFile(new File(imagePath));
//		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//		mediaScanIntent.setData(contentUri);
//		sendBroadcast(mediaScanIntent);
	}
}
