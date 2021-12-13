package com.galaxybruce.component.ui.jetpack

import androidx.lifecycle.ViewModel

/**
 * @date 2020/8/10  5:43 PM
 * @author
 * @description viewModel 基类
 * <p>
 *
 * add by bruce.zhang start
 *
 * TODO tip：
 * 链路：UI-ViewModel-Request-Repository/或者其他逻辑类
 *
 * UI: 负责UI展示和交互
 * ViewModel: ViewModel 的职责仅限于 状态托管，不建议在此处理 UI 逻辑，每个页面都要单独准备一个 state-ViewModel
 * Request: 逻辑处理的入口类，同时用MutableLiveData保存处理结果状态
 * Repository: 具体业务处理，比如网络请求
 *
 *
 * ViewModel中的属性是选择 ObservaleField 还是 LiveData 还是UnPeekLiveData???
 *
 * "ObservaleField 有防抖的特点，要记住这个特点，然后根据情况选择使用。"
 * 比如 PureMusic 中通知抽屉打开，用 ObservaleField<Boolean> 不合适，而 LiveData 合适，
 * 因为 ObservaleField 防抖，第一次 set true，就有 true 为 value 了，第二次再 set true，就不 notify 视图刷新了（具体见 ObservaleBoolean 的 set 方法实现）
 * 防抖可以避免重复刷新 以减少不必要的性能开销，所以看情况选择 ObservaleField 或 LiveData。
 *
 * UnPeekLiveData可以防止数据倒灌，比如显示对话框、加载框、进入登录界面现在用的就是UnPeekLiveData
 *
 * add by bruce.zhang end
 *
 * modification history:
 */
abstract class JPBaseViewModel : ViewModel() {

    /**
     * onCleared()方法在activity.onDestroy()方法之前调用，横竖屏切换导致的activity.onDestroy()并不会
     * 调用onCleared()方法，因此可以在用mCleared标志来区分页面是否真正关闭并做一些释放工作。
     *
     *  override fun onDestroy() {
     *      // 页面真正关闭时，才停止扫描，横竖屏切换时不扫描
     *      if(mPageViewModel.mCleared) {
     *          // hub配置页面关闭时，停止扫描
     *          hubStateManager.stopScan()
     *      }
     *      super.onDestroy()
     *  }
     */
    @Volatile var mCleared = false

    override fun onCleared() {
        mCleared = true
        super.onCleared()
    }

    /**
     * 返回ViewModel中所有的request
     *
     *  override fun getRequests(): List<JPBaseRequest> {
     *      return listOf(request)
     *  }
     *
     */
    abstract fun getRequests(): List<JPBaseRequest>

}