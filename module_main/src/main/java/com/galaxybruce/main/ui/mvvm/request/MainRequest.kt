package com.galaxybruce.main.ui.mvvm.request

import android.annotation.SuppressLint
import android.content.Context
import com.galaxybruce.component.ui.dialog.AppConfirmDialog
import com.galaxybruce.component.ui.jetpack.JPBaseRequestV2
import com.galaxybruce.main.ui.mvvm.viewmodel.MainViewModel
import kotlinx.coroutines.*


class MainRequest(private val viewModel: MainViewModel) : JPBaseRequestV2(viewModel) {

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


        doTask(
            block = {
                // 多个连续请求中有弹窗
                val r1 = request()

                val input = showInputDialog(context)

                val r2 = request2(input.getOrThrow()) // input.getOrElse { "输入出现异常" }
                r2
            },
            successCallback = { result ->
                showToast(result)
            },
            errorCallback = {
                showToast(it.message)
            },
            showLoading = false
        )
    }

    private suspend fun request(): String {
        showLoadingProgress("请求1")
        val result = withContext(Dispatchers.IO) {
            delay(1000)
            // 在这里执行接口请求操作，返回结果
            "Result from request1"
        }
        hideLoadingProgress()
        return result
    }

    private suspend fun request2(text: String): String {
        showLoadingProgress("请求2")
        val result = withContext(Dispatchers.IO) {
            delay(1000)
            // 在这里执行接口请求操作，返回结果
            "Result from request2: ${text}"
        }
        hideLoadingProgress()
        return result
    }

    /**
     * [利用协程同步代码形式获取弹窗返回结果](https://juejin.cn/post/7131396910438416392)
     * [Kotlin--suspendCancellableCoroutine和suspendCoroutine的区别及使用](https://blog.csdn.net/qq_41751493/article/details/113698815)
     * [Kotlin协程-协程的暂停与恢复 & suspendCancellableCoroutine的使用](https://juejin.cn/post/7128555351725015054)
     */
    private suspend fun showInputDialog(context: Context): Result<String> {
        return suspendCancellableCoroutine { continuation ->
            continuation.invokeOnCancellation {
                // 协程被取消的处理
                continuation.resume(Result.failure(it ?: Exception("任务已取消")), null)
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