package com.example.play_android.mvp.presenter

import android.app.Application
import com.example.play_android.R
import com.example.play_android.app.api.entity.ApiPagerResponse
import com.example.play_android.app.api.entity.ArticleResponse

import com.jess.arms.integration.AppManager
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.mvp.BasePresenter
import com.jess.arms.http.imageloader.ImageLoader
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import javax.inject.Inject

import com.example.play_android.mvp.contract.SearchResultContract
import com.example.play_android.mvp.ui.activity.showToast
import com.example.play_android.mvp.ui.adapter.HomeAdapter
import com.jess.arms.utils.RxLifecycleUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 03/13/2020 16:22
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
class SearchResultPresenter
@Inject
constructor(model: SearchResultContract.Model, rootView: SearchResultContract.View) :
    BasePresenter<SearchResultContract.Model, SearchResultContract.View>(model, rootView) {
    @Inject
    lateinit var mErrorHandler: RxErrorHandler
    @Inject
    lateinit var mApplication: Application
    @Inject
    lateinit var mImageLoader: ImageLoader
    @Inject
    lateinit var mAppManager: AppManager

    var homeAdapter: HomeAdapter? = null

    fun getResult(pageNo: Int, searchKey: String) {
        mModel.getResult(pageNo, searchKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                if (pageNo == 0) mRootView.showLoading()
            }
            .doFinally {
                mRootView.hideLoading()
            }
            .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
            .subscribe(object :
                ErrorHandleSubscriber<ApiPagerResponse<MutableList<ArticleResponse>>>(mErrorHandler) {
                override fun onNext(data: ApiPagerResponse<MutableList<ArticleResponse>>) {
                    if (homeAdapter == null) {
                        homeAdapter = HomeAdapter(R.layout.item_article, data.datas)
                        homeAdapter!!.setEnableLoadMore(true)
                        mRootView.setContent(homeAdapter!!)
                    }
                    homeAdapter?.run {
                        if (data.over) {
                            loadMoreEnd()
                        } else {
                            if (pageNo == 0) {
                                // 刷新
                                setNewData(data.datas)
                            } else {
                                addData(data.datas)
                                loadMoreComplete()
                            }
                        }
                    }
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
