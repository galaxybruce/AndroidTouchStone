package com.galaxybruce.main.ui.mvvm.request

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.galaxybruce.component.ui.dialog.AppConfirmDialog
import com.galaxybruce.component.ui.jetpack.JPBaseRequest
import com.galaxybruce.main.ui.mvvm.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext


class MainRequest(private val viewModel: MainViewModel) : JPBaseRequest() {

//    private val mApi: LiveApi = KRetrofitFactory.createService(LiveApi::class.java)

    @SuppressLint("CheckResult")
    fun performRequest(context: Context) {
//        val requestUrl: String = LiveServer.URL_CREATE_LIVE
//
//        mApi.createLive(requestUrl, liveCreateModel)
//            .compose(this.handleEverythingResult<LiveEntity<Any>>(true))
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//               this.viewModel.title.set("返回数据成功")
//            }, {
//                showToast(it.message)
//                this.viewModel.title.set("返回数据失败")
//            })

        viewModel.viewModelScope.launch {
            val r1 = request()
            val input = showInputDialog(context)
            request2(input.getOrElse { "输入出现异常" })
        }
    }

    private suspend fun request(): String = withContext(Dispatchers.IO) {
        // 在这里执行接口请求操作，返回结果
        "Result from request"
    }

    private suspend fun request2(text: String) {
        viewModel.viewModelScope.launch {
            showToast(text)
        }
    }

    /**
     * [利用协程同步代码形式获取弹窗返回结果](https://juejin.cn/post/7131396910438416392)
     */
    private suspend fun showInputDialog(context: Context): Result<String> {
        return suspendCancellableCoroutine { continuation ->
            continuation.invokeOnCancellation {
                // todo 协程被取消的处理
            }

            AppConfirmDialog.create("提示",
                "协程中弹窗输入内容？",
                false,
                object : AppConfirmDialog.AppConfirmDialogCallback {
                    override fun onCancel() {
                        if (!continuation.isCompleted) {
                            //为true则是点击了确定,已经返回结果
                            continuation.resume(Result.failure(RuntimeException("用户点击取消")), null)
                        }
                    }

                    override fun onConfirm() {
                        continuation.resume(Result.success("用户点击确定"), null)
                    }
                })
                .show(context, "AppConfirmDialog_2")
        }
    }
}