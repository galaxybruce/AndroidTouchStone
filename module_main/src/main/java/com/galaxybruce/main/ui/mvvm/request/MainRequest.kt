package com.galaxybruce.main.ui.mvvm.request

import android.annotation.SuppressLint
import android.content.Context
import com.galaxybruce.component.ui.dialog.AppConfirmDialog
import com.galaxybruce.component.ui.jetpack.JPBaseRequest
import com.galaxybruce.component.ui.jetpack.doSuspendTask
import com.galaxybruce.component.ui.jetpack.doTask
import com.galaxybruce.main.ui.mvvm.viewmodel.MainViewModel
import kotlinx.coroutines.*


class MainRequest(viewModel: MainViewModel) : JPBaseRequest(viewModel) {

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
        return doSuspendTask(doTaskCallback = { successCallback, errorCallback ->
            AppConfirmDialog.create("提示",
                "协程中弹窗输入内容111？",
                false,
                object : AppConfirmDialog.AppConfirmDialogCallback {
                    override fun onCancel() {
                        errorCallback("用户点击取消")
                    }

                    override fun onConfirm() {
                        successCallback("用户点击确定")
                    }
                })
                .show(context, "AppConfirmDialog_2")
        }, canceledCallback = {

        })
    }
}