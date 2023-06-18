package com.galaxybruce.component.ui.jetpack

import androidx.lifecycle.viewModelScope
import com.galaxybruce.component.net.exception.AppLoginExpiresException
import com.galaxybruce.component.net.exception.AppNetException
import com.galaxybruce.component.net.exception.AppNetExceptionHandler
import com.galaxybruce.component.net.model.IAppBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @date 2020/9/16 11:48
 * @author bruce.zhang
 * @description 逻辑处理的入口类，在这里可以调用JPBaseRepository或者其他更深层次的逻辑处理类
 *
 *  注意：[JPBaseRequest] 与 [JPBaseRequestV2]的区别：
 *  JPBaseRequest：RxJava
 *  JPBaseRequestV2: 协程
 * add by bruce.zhang start
 *
 * TODO tip：
 * 链路：UI-ViewModel-Request-Repository/或者其他逻辑类
 *
 * UI: 负责UI展示和交互
 * ViewModel: ViewModel 的职责仅限于 状态托管，不建议在此处理 UI 逻辑，每个页面都要单独准备一个 state-ViewModel
 * Request: 逻辑处理的入口类，同时用MutableLiveData保存处理结果状态
 * Repository: 具体业务处理，比如网络请求，用callback的形式，把接口返回给request
 *
 *
 * request中处理的数据如何和通知给ViewModel呢？
 * 有两种方式：
 * 1. request和ViewModel解耦，让request可以复用。
 * request中定义和ViewModel中相同的字段，然后再UI上监听request中的数据变化，并设置给ViewModel中对应的字段
 * public class XXXRequest extends JPBaseRequest {
 *     public MutableLiveData<JPListDataModel> listData = new MutableLiveData<>();
 *
 *     public MutableLiveData<JPListDataModel> getListData() {
 *         if(listData == null) {
 *             listData = new MutableLiveData<>();
 *         }
 *         return listData;
 *     }
 *     ...
 * }
 *
 * public class XXXViewModel extends JPBaseViewModel {
 *     public MutableLiveData<JPListDataModel> listData = new MutableLiveData<>();
 *
 *     {
 *         listData.setValue(new JPListDataModel(null, false));
 *     }
 *     ....
 * }
 *
 *  // UI中监听
 *  mMainViewModel.request.getListData().observe(this, Observer {
 *     mMainViewModel.listData.value = it
 *  })
 *
 * 2. request和ViewModel强关联。
 * request中不保存字段，request持有ViewModel的引用，这样request中能直接使用ViewModel中的数据
 *  class JPOfficialAccountVideoDetailRequest(var viewModel: JPOfficialAccountVideoDetailViewModel) : JPBaseRequest() {
 *      ...
 *     fun reportShare(detail: LKVideoDetail, owner: LifecycleOwner) {
 *         mRepo?.reportShare(detail)?.observe(this, owner, false, {
 *             if (it.isSuccessful) {
 *                 viewModel.videoDetail.postValue(detail.apply { share_count += 1 })
 *             }
 *         }, {
 *             showToast(it.message)
 *         })
 *     }
 *  }
 *
 * request中也可以引用多个ViewModel:
 * class HomeCartRequest(private val viewModel: HomeCartViewModel) : JPBaseRequest() {
 *      lateinit var mMainViewModel: MainViewModel
 *      lateinit var mMemberViewModel: AppMemberViewModel
 *
 *      ...
 * }
 *
 * override fun initViewModel(): JPBaseViewModel {
 *     mMainViewModel = getActivityViewModel(MainViewModel::class.java)
 *     mMemberViewModel = getAppViewModel(AppMemberViewModel::class.java)
 *     mPageViewModel = getFragmentViewModel(HomeCartViewModel::class.java).apply {
 *         this.request.mMainViewModel = mMainViewModel
 *         this.request.mMemberViewModel = mMemberViewModel
 *    }
 *    return mPageViewModel
 * }
 *
 * add by bruce.zhang end
 *
 * modification history:
 */
abstract class JPBaseRequestV2(private val viewModel: JPBaseViewModel)
    : JPBaseRequest() {

    /**
     * 处理耗时任务，使场景：
     * 1. 处理单个的耗时任务，也可以是网络请求
     * 2. 处理多个网络请求
     * doTask(
     *     {
     *         val requestUrl: String = DemoUrl.URL_DEMO
     *         coroutineScope {
     *             val deferredOne = async { mApi.getDataSuspend(requestUrl) }
     *             val deferredTwo = async { mApi.getDataSuspend(requestUrl) }
     *             val response1 = deferredOne.await()
     *             val response2 = deferredTwo.await()
     *             JSON.toJSONString(response1.data) + "\n" + JSON.toJSONString(response2.data)
     *        }
     *    }, { response ->
     *        this.viewModel.name.value = response
     *    }, { exception ->
     *         showToast(exception.message)
     *     },
     *     true)
     */
    fun <T> doTask(
        block: suspend () -> T,
        successCallback: (T) -> Unit = {},
        errorCallback: (Throwable) -> Unit = {},
        showLoading: Boolean = false,
        loadingMessage: String? = null
    ) {
        viewModel.viewModelScope.launch {
            kotlin.runCatching {
                if (showLoading) {
                    showLoadingProgress(loadingMessage)
                }
                withContext(Dispatchers.IO) {
                    block()
                }
            }.onSuccess {
                if(showLoading) {
                    hideLoadingProgress()
                }
                successCallback(it)
            }.onFailure {
                if(showLoading) {
                    hideLoadingProgress()
                }
                errorCallback(it)
            }
        }
    }

    /**
     * 处理单个网络请求
     *
     * @param block 请求体方法，必须要用suspend关键字修饰
     * @param success 成功回调
     * @param error 失败回调
     * @param showLoading 是否显示加载框
     * @param loadingMessage 加载框提示内容
     */
    fun <T: IAppBean> request(
        block: suspend () -> T,
        successCallback: (T) -> Unit = {},
        errorCallback: (Throwable) -> Unit = {},
        showLoading: Boolean = true,
        loadingMessage: String? = null
    ): Job {
        return viewModel.viewModelScope.launch {
            kotlin.runCatching {
                if (showLoading) {
                    showLoadingProgress(loadingMessage)
                }
                // 请求体
                withContext(Dispatchers.IO) {
                    block()
                }
            }.onSuccess { response ->
                // 网络请求完成 关闭弹窗
                if(showLoading) {
                    hideLoadingProgress()
                }
                //统一处理登录态失效
                if (response.isExpireLogin) {
                    reLogin()
                }
                //是否处理业务异常
                if (response.isSuccessful) {
                    successCallback(response)
                } else {
                    errorCallback(AppNetException(response.code, response.message))
                }
            }.onFailure { throwable ->
                // 网络请求完成 关闭弹窗
                if(showLoading) {
                    hideLoadingProgress()
                }
                throwable.printStackTrace()
                // 统一处理登录态失效
                if(throwable is AppLoginExpiresException) {
                    reLogin()
                }
                //失败回调
                errorCallback(AppNetExceptionHandler.handleException(throwable))
            }
        }
    }
}