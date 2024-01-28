package com.galaxybruce.main.register

import com.billy.cc.core.component.IComponent
import com.billy.cc.core.component.register.ICCRegister
import com.galaxybruce.component.proguard.IProguardKeeper

/**
 * @date 2023/7/19 13:41
 * @author bruce.zhang
 * @description 收银通对外暴露的接口
 * <p>
 * modification history:
 */
class MainRegister : ICCRegister, IProguardKeeper {

    override fun getComponents(): MutableList<IComponent> {
        return mutableListOf(MainComponent())
    }

}