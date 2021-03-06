package com.example.play_android.mvp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.jess.arms.di.component.AppComponent
import com.jess.arms.utils.ArmsUtils

import com.example.play_android.di.component.DaggerProjectComponent
import com.example.play_android.di.module.ProjectModule
import com.example.play_android.mvp.contract.ProjectContract
import com.example.play_android.mvp.presenter.ProjectPresenter

import com.example.play_android.R
import com.example.play_android.app.api.entity.ClassifyResponse
import com.example.play_android.app.base.MySupportFragment
import com.example.play_android.app.event.OpenDrawer
import com.example.play_android.mvp.ui.activity.SearchActivity
import com.example.play_android.mvp.ui.adapter.ProjectTabAdapter
import com.example.play_android.mvp.ui.adapter.PublicTabAdapter
import com.flyco.tablayout.listener.OnTabSelectListener
import kotlinx.android.synthetic.main.fragment_project.*
import kotlinx.android.synthetic.main.include_title.*
import kotlinx.android.synthetic.main.include_title_tab.*
import org.simple.eventbus.EventBus


class ProjectFragment : MySupportFragment<ProjectPresenter>(), ProjectContract.View {
    companion object {
        fun newInstance(): ProjectFragment {
            val fragment = ProjectFragment()
            return fragment
        }
    }

    private var adapter: ProjectTabAdapter? = null

    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerProjectComponent //如找不到该类,请编译一下项目
            .builder()
            .appComponent(appComponent)
            .projectModule(ProjectModule(this))
            .build()
            .inject(this)
    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_project, container, false);
    }

    override fun initData(savedInstanceState: Bundle?) {
        mPresenter?.initTabTitle()
        toolbar_project_tab.run {
            title = "项目"
            inflateMenu(R.menu.menu_activity_home)
            setNavigationOnClickListener {
                EventBus.getDefault().post(OpenDrawer(), "OpenDrawer")
            }
            setOnMenuItemClickListener {
                launchActivity(Intent(_mActivity, SearchActivity::class.java))
                false
            }
        }
    }

    override fun setData(data: Any?) {

    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showMessage(message: String) {
        ArmsUtils.snackbarText(message)
    }

    override fun launchActivity(intent: Intent) {
        ArmsUtils.startActivity(intent)
    }

    override fun killMyself() {

    }

    override fun setTabTitle(classIfy: MutableList<ClassifyResponse>) {
        adapter = ProjectTabAdapter(childFragmentManager, classIfy)
        vp_content.adapter = adapter
        tab_project.run {
            setOnClickListener {
                vp_content.currentItem = tab_project.currentTab
            }
            setViewPager(vp_content)
        }
    }
}
