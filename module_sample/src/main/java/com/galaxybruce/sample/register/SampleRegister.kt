package com.galaxybruce.sample.register

import com.billy.cc.core.component.IComponent
import com.billy.cc.core.component.register.ICCRegister

/**
 * @author bruce.zhang
 * @date 2023/2/22 16:07
 *
 * 该Register作为对外的暴露的所有功能的注册，可以实现多个接口，目前只是实现ICCRegister，也可以同时实现路由接口等
 * modification history:
 */
class SampleRegister : ICCRegister {

    override fun getComponents(): MutableList<IComponent> {
        return mutableListOf(SampleComponent())
    }

}