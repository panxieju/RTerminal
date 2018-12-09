package cn.nexttec.rterminal.injection.component

import cn.nexttec.rterminal.injection.module.WelcomeModule
import cn.nexttec.rterminal.ui.activity.WelcomeActivity
import dagger.Component
import dagger.Module

@Component (modules = [WelcomeModule::class])
interface WelcomeComponet{
    fun inject(activity:WelcomeActivity)
}