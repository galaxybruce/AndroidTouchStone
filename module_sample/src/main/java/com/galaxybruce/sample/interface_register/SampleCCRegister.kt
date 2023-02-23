package com.galaxybruce.sample.interface_register

import com.billy.cc.core.component.IComponent
import com.billy.cc.core.component.IGlobalCCInterceptor
import com.billy.cc.core.component.register.ICCRegister

/**
 * @author bruce.zhang
 * @date 2023/2/22 16:07
 *
 * modification history:
 */
class SampleCCRegister : ICCRegister {

    override fun getComponents(): MutableList<IComponent> {
        return mutableListOf(SampleComponent())
    }

    override fun getGlobalCCInterceptors(): MutableList<IGlobalCCInterceptor>? {
        return null
    }

}