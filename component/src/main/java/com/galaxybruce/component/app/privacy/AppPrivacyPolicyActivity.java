package com.galaxybruce.component.app.privacy;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.blankj.utilcode.util.AppUtils;
import com.galaxybruce.component.ui.dialog.AppConfirmDialog;
import com.galaxybruce.component.util.AppBuildConfigUtils;
import com.galaxybruce.component.util.crosssp.AppProcessSPHelper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author bruce.zhang
 * @date 2021/8/10 18:25
 * @description 隐私政策和服务协议
 *
 * 变量
 * privacy_policy_agreed_type: 0: 默认状态 1-同意; 2-拒绝
 *
 * <p>
 * modification history:
 */
public class AppPrivacyPolicyActivity extends AppCompatActivity {

    private String mAlertMsg;

    @Override
    public Resources getResources() {
        return AdaptScreenUtils.adaptWidth(super.getResources(), 750);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mAlertMsg = intent.getStringExtra("alertMsg");
        showPrivateDialog(mAlertMsg);
    }

    /**
     * 第一次提示
     */
    private void showPrivateDialog(String message) {
        if(TextUtils.isEmpty(message)) {
            String appName = AppUtils.getAppName();
            String appProtocolServer = AppBuildConfigUtils.getAppMetaDataString("appProtocolServer");
            String appProtocolPrivacy = AppBuildConfigUtils.getAppMetaDataString("appProtocolPrivacy");
            message = "请您了解，您需要注册成为" + appName
                    + "用户后方可使用本软件相关功能，关于注册，您可以参考"
                    + "<font color=\"#CC0000\"><a href=\"" + appProtocolServer + "?hideFeedback=1" + "\">《用户服务协议》</a></font>。"
                    + "请您充分了解在使用本软件过程中我们可能收集、使用或者共享您个人信息的情形，"
                    + "你可阅读" +
                    "<font color=\"#CC0000\"><a href=\"" + appProtocolPrivacy + "?hideFeedback=1" + "\">《隐私政策》</a></font>" +
                    "了解详细信息。";
        }

        new AppConfirmDialog.Builder()
                .setCancelText("不同意")
                .setConfirmText("同意")
                .setTitle("温馨提示")
                .setCancelable(false)
                .setMessageGravity(Gravity.START)
                .setMessage(message, true,true)
                .setCallback(new AppConfirmDialog.AppConfirmDialogCallback() {
                    @Override
                    public void onCancel() {
                        showSecondPrivateDialog();
                    }

                    @Override
                    public void onConfirm() {
                        AppProcessSPHelper.save(AppPrivacyPolicyActivity.this,
                                AppPrivacyUtil.PRIVACY_STATUS_KEY, 1);
                        finish();
                    }
                })
                .show(getSupportFragmentManager(), "showPrivateDialog");
    }

    /**
     * 第二次提示
     */
    private void showSecondPrivateDialog() {
        new AppConfirmDialog.Builder()
                .setCancelText("仍不同意")
                .setConfirmText("查看协议")
                .setTitle("您需要同意本隐私政策，才能继续使用" + AppUtils.getAppName())
                .setCancelable(false)
                .setMessageGravity(Gravity.START)
                .setMessage("若您不同意本隐私政策，很遗憾我们将无法为您提供服务。")
                .setCallback(new AppConfirmDialog.AppConfirmDialogCallback() {
                    @Override
                    public void onCancel() {
                        showThirdPrivateDialog();
                    }

                    @Override
                    public void onConfirm() {
                        showPrivateDialog(mAlertMsg);
                    }
                })
                .show(getSupportFragmentManager(), "showSecondPrivateDialog");
    }

    /**
     * 第三次提示
     */
    private void showThirdPrivateDialog() {
        new AppConfirmDialog.Builder()
                .setCancelText("退出应用")
                .setConfirmText("再次查看")
                .setTitle("我们将充分尊重并保护您的隐私，请放心")
                .setCancelable(false)
                .setMessageGravity(Gravity.LEFT)
                .setMessage("")
                .setCallback(new AppConfirmDialog.AppConfirmDialogCallback() {
                    @Override
                    public void onCancel() {
                        AppProcessSPHelper.save(AppPrivacyPolicyActivity.this,
                                AppPrivacyUtil.PRIVACY_STATUS_KEY, 2);
                        finishAndRemoveTask();
                    }

                    @Override
                    public void onConfirm() {
                        showPrivateDialog(mAlertMsg);
                    }
                })
                .show(getSupportFragmentManager(), "showThirdPrivateDialog");
    }
}
