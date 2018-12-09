package cn.nexttec.rterminal.injection.module

import cn.nexttec.rterminal.presenter.WelcomePresenter
import dagger.Module
import dagger.Provides

@Module
class WelcomeModule{

    @Provides
    fun providesPresenter():WelcomePresenter{
        return WelcomePresenter()
    }
}