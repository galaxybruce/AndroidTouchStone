package com.galaxybruce.sample.register

import com.billy.cc.core.component.CC
import com.billy.cc.core.component.CCResult
import com.billy.cc.core.component.IComponent
import com.galaxybruce.component.util.ToastUtils

/**
 * @date 2023/2/23 15:41
 * @author bruce.zhang
 * @description
 *
 * 调用方式
 * CC.obtainBuilder("SampleComponent")
 * .setActionName("showToast")
 * .build()
 * .call()
 *
 * modification history:
 */
class SampleComponent : IComponent {

    override fun getName(): String {
        return "SampleComponent"
    }

    override fun onCall(cc: CC): Boolean {
        when(cc.actionName) {
            "showToast" -> {
                ToastUtils.showToast(cc.context, "调用成功")
                CC.sendCCResult(cc.callId, CCResult.success())
            }
        }
        return false
    }
}