package com.example.play_android.mvp.model

import android.app.Application
import com.example.play_android.app.api.entity.ApiPagerResponse
import com.example.play_android.app.api.entity.ArticleResponse
import com.example.play_android.app.api.service.ApiService
import com.google.gson.Gson
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel

import com.jess.arms.di.scope.ActivityScope
import javax.inject.Inject

import com.example.play_android.mvp.contract.SearchResultContract
import io.reactivex.Observable


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
class SearchResultModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager),
    SearchResultContract.Model {
    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application

    override fun getResult(
        pageNo: Int,
        searchKey: String
    ): Observable<ApiPagerResponse<MutableList<ArticleResponse>>> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getSearchDataByKey(pageNo, searchKey)
            .map { it.data }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
