package com.galaxybruce.component.ui;

import android.os.Bundle;
import android.view.View;

/**
 * @date 2019/3/20 11:25
 * @author bruce.zhang
 * @description 所有的UI都可以实现这个接口 Activity Fragment CustomView
 * <p>
 * modification history:
 */
public interface IUiInit {

	/** 在initView之前初始化数据 **/
	public boolean initData(Bundle savedInstanceState);

	/** 返回布局id **/
	public int getLayoutId();

	/** find view and bind listener**/
	public void initView(View view);

	/** 绑定数据 **/
	public void bindData(Bundle savedInstanceState);
	
}
