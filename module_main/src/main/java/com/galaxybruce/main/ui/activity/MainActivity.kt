package com.galaxybruce.main.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import androidx.activity.result.ActivityResult
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.AdaptScreenUtils
import com.galaxybruce.component.file.AppFilePathManager
import com.galaxybruce.component.ui.activity.BaseTitleBarActivity
import com.galaxybruce.component.util.AppActivityResultUtil
import com.galaxybruce.component.util.AppFileProvider
import com.galaxybruce.component.util.log.AppLogUtils
import com.galaxybruce.main.R
import io.reactivex.functions.Function3
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

@Route(path = "/app/main")
class MainActivity : BaseTitleBarActivity(){

    override fun bindTitle(): CharSequence {
        return "MainActivity"
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
    }

    override fun bindLayoutId(): Int {
        return R.layout.activity_main
    }

    var n: Int = 0

    override fun initView(view: View?) {
        super.initView(view)
        image.setOnClickListener {
//            RouterUrlBuilder.instance("/test/TestActivity")
//                .addParam("a", "a11").go(this)

//            AppConfirmDialog.getInstance("提示", "哈哈哈哈", false,
//                object : AppConfirmDialog.AppConfirmDialogCallback {
//                    override fun onCancel() {
//                        showToast("cancel== ${++n}")
//                    }
//
//                    override fun onConfirm() {
//                        showToast("ok== ${++n}")                    }
//                })
//                .show(supportFragmentManager, "aaa")

//            AppActivityResultUtil.createDocument(mActivity, "test.pdf") { uri ->
//                uri?.let {
//                    AppLogUtils.i("uri: " + uri.toString())
//                    val cursor: Cursor? = mActivity.contentResolver.query(uri,
//                        null, null, null, null);
//                    if (cursor?.moveToFirst() == true) {
//                        val name: String = cursor?.getString(cursor.getColumnIndexOrThrow(
//                            OpenableColumns.DISPLAY_NAME))
//                    }
//                    cursor?.close();
//                }
//            }

//            AppActivityResultUtil.openDocument(mActivity, arrayOf("application/pdf")) { uri ->
//                uri?.let {
//                    AppLogUtils.i("uri: " + uri.toString())
//                }
//            }

//            AppActivityResultUtil.openActivity(mActivity,
//                object: AppActivityResultUtil.AppActivityResultCallbackInputIntent {
//                override fun onActivityResult(result: ActivityResult?) {
//                    result?.let {
//                        AppLogUtils.i("uri: " + result.toString())
//                    }
//                }
//
//                override fun createIntent(activity: Activity): Intent {
//                    return Intent(Intent.ACTION_OPEN_DOCUMENT)
//                        .putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/pdf"))
//                        .setType("*/*")
//                }
//            })

//            AppFilePathManager.getSaveFilePath(mActivity, "test.xml",
//                object: Function2<Uri?, Boolean, Unit> {
//                    override fun invoke(p1: Uri?, p2: Boolean) {
//                        AppLogUtils.i("uri: $p1 - $p2")
//                    }
//                })

            val filePath = AppFilePathManager.getAppPictureFilePath(mActivity, null, ".jpg")
            AppActivityResultUtil.takePicture(mActivity, filePath) { result ->
                AppLogUtils.i("uri: $result")
            }
        }
    }

    override fun bindData(savedInstanceState: Bundle?) {
    }

    override fun getTitleOverLabMode(): Boolean {
        return true
    }

    /**
     * 以pt为单位适配，这里的宽度已设计稿的标准像素尺寸为准
     */
    override fun getResources(): Resources {
        return AdaptScreenUtils.adaptWidth(super.getResources(), 750)
    }

}
