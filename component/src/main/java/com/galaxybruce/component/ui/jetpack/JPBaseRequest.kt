package com.galaxybruce.component.ui.jetpack

import androidx.lifecycle.DefaultLifecycleObserver
import com.galaxybruce.component.net.exception.AppLoginExpiresException
import com.galaxybruce.component.net.exception.AppNetException
import com.galaxybruce.component.net.model.IAppBean
import com.galaxybruce.component.net.transformer.AppNetResponseTransformer
import com.galaxybruce.component.util.AppConstants.EMPTY_STR
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @date 2020/9/16 11:48
 * @author bruce.zhang
 * @description 逻辑处理的入口类，在这里可以调用JPBaseRepository或者其他更深层次的逻辑处理类
 *
 *  注意：[JPBaseRequest] 与 [JPBaseRequestV2]的区别：
 *  JPBaseRequestV2 在 JPBaseRequest基础上支持协程
 *
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
abstract class JPBaseRequest : IJPViewModelAction by JPRequestAction(), DefaultLifecycleObserver {

    fun <T : IAppBean> handleEverythingResult(showLoading: Boolean): ObservableTransformer<T, T> {
        return handleEverythingResult(showLoading, EMPTY_STR)
    }

    fun <T : IAppBean> handleOnlyNetworkResult(showLoading: Boolean): ObservableTransformer<T, T> {
        return handleEverythingResult(showLoading, EMPTY_STR)
    }

    fun <T : IAppBean> handleOnlyNetworkResult(showLoading: Boolean,
                                                    loadingMessage: String): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream -> handleCommon(
            upstream, showLoading, loadingMessage, true) }
    }

    private fun <T : IAppBean> handleEverythingResult(showLoading: Boolean,
                                                      loadingMessage: String): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream -> handleCommon(
            upstream, showLoading, loadingMessage, false) }
    }

    open fun <T : IAppBean> handleCommon(upstream: Observable<T>, showLoading: Boolean,
                                         loadingMessage: String, handleOnlyNetworkResult: Boolean): Observable<T> {
        return upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(AppNetResponseTransformer.ErrorResumeFunction())
                .doOnSubscribe {
                    if (showLoading) {
                        showLoadingProgress(loadingMessage)
                    }
                }
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    if (showLoading) {
                        hideLoadingProgress()
                    }
                }
                .doOnNext {
                    //统一处理登录态失效
                    if (it.isExpireLogin) {
                        reLogin()
                    }
                }
                .doOnNext {
                    //是否处理业务异常
                    if (!it.isSuccessful && !handleOnlyNetworkResult) {
                        throw AppNetException(it.code, it.message)
                    }
                }
                .doOnError {
                    it.printStackTrace()
                    if (showLoading) {
                        hideLoadingProgress()
                    }
                    if(it is AppLoginExpiresException) {
                        reLogin()
                    }
                }
    }

}