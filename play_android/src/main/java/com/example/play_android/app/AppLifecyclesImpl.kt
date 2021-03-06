package com.example.play_android.app

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex

import com.jess.arms.base.delegate.AppLifecycles
import com.jess.arms.integration.cache.IntelligentCache
import com.jess.arms.utils.ArmsUtils
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher

import butterknife.ButterKnife

import com.example.play_android.BuildConfig
import com.tencent.mmkv.MMKV
import me.yokeyword.fragmentation.Fragmentation

import timber.log.Timber

/**
 * ================================================
 * 展示 [AppLifecycles] 的用法
 *
 *
 * Created by MVPArmsTemplate on 02/11/2020 13:52
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
class AppLifecyclesImpl : AppLifecycles {

    override fun attachBaseContext(base: Context) {
        MultiDex.install(base) //这里比 onCreate 先执行,常用于 MultiDex 初始化,插件化框架的初始化
    }

    override fun onCreate(application: Application) {
        if (LeakCanary.isInAnalyzerProcess(application)) {
            return
        }
        if (BuildConfig.LOG_DEBUG) {//Timber初始化
            Timber.plant(Timber.DebugTree())
            ButterKnife.setDebug(true)
        }
        //LeakCanary 内存泄露检查
        //使用 IntelligentCache.KEY_KEEP 作为 key 的前缀, 可以使储存的数据永久存储在内存中
        //否则存储在 LRU 算法的存储空间中, 前提是 extras 使用的是 IntelligentCache (框架默认使用)
        ArmsUtils.obtainAppComponentFromContext(application).extras()
            .put(
                IntelligentCache.getKeyOfKeep(RefWatcher::class.java.name),
                if (BuildConfig.USE_CANARY) LeakCanary.install(application) else RefWatcher.DISABLED
            )
        // 初始化Fragment管理
        Fragmentation.builder()
            // 显示悬浮球 ; 其他Mode:SHAKE: 摇一摇唤出   NONE：隐藏
            .stackViewMode(Fragmentation.NONE)
            .debug(BuildConfig.DEBUG)
            .install()
        //初始化MMKV
        MMKV.initialize(application.filesDir.absolutePath + "/mmkv")

    }

    override fun onTerminate(application: Application) {

    }
}
