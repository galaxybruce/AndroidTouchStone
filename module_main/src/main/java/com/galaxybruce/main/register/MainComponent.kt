package com.galaxybruce.main.register

import com.billy.cc.core.component.CC
import com.billy.cc.core.component.CCResult
import com.billy.cc.core.component.CCUtil
import com.billy.cc.core.component.IComponent
import com.billy.cc.core.component.IMainThread
import com.galaxybruce.component.proguard.IProguardKeeper
import com.galaxybruce.login.ui.activity.LoginActivity
import com.galaxybruce.main.ui.activity.MainActivity
import com.galaxybruce.main.ui.activity.NetTestActivity


class MainComponent : IComponent, IMainThread, IProguardKeeper {

    override fun getName(): String {
        return "MainComponent"
    }

    override fun shouldActionRunOnMainThread(actionName: String?, cc: CC?): Boolean {
        return true
    }

    override fun onCall(cc: CC): Boolean {
        when(cc.actionName) {
            "openMainActivity" -> {
                CCUtil.navigateTo(cc, MainActivity::class.java)
                CC.sendCCResult(cc.callId, CCResult.success())
            }
            "openTestActivity" -> {
                CCUtil.navigateTo(cc, NetTestActivity::class.java)
                CC.sendCCResult(cc.callId, CCResult.success())
            }
            "openLoginActivity" -> {
                CCUtil.navigateTo(cc, LoginActivity::class.java)
                CC.sendCCResult(cc.callId, CCResult.success())
            }
        }
        return false
    }
}