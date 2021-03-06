package com.example.play_android.di.module

import com.jess.arms.di.scope.FragmentScope

import dagger.Module
import dagger.Provides

import com.example.play_android.mvp.contract.SystemContract
import com.example.play_android.mvp.model.SystemModel


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 02/11/2020 20:47
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
//构建SystemModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class SystemModule(private val view: SystemContract.View) {
    @FragmentScope
    @Provides
    fun provideSystemView(): SystemContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideSystemModel(model: SystemModel): SystemContract.Model {
        return model
    }
}
