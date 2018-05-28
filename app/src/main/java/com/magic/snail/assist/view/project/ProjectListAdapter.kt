package com.magic.snail.assist.view.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.magic.snail.assist.R
import com.magic.snail.assist.entity.Project
import kotlinx.android.synthetic.main.item_project.view.*

class ProjectListAdapter : BaseAdapter() {
    private var projectList: ArrayList<Project> = ArrayList()

    fun setProjects(list: List<Project>?) {
        projectList.clear()
        if (list != null) {
            projectList.addAll(list!!)
        }

        if (projectList.size == 0) {
            val mock = Project.createBean("开启自己的小算盘项目吧！")
            mock.tag = Project.MOCK_NAME
            projectList.add(mock)
        }
    }

    override fun getItem(pos: Int): Project? {
        return projectList?.get(pos)
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun getCount(): Int {

        return if (projectList == null) {
            0
        } else {
            projectList!!.size
        }
    }

    override fun getView(pos: Int, convertView: View?, parent: ViewGroup): View {
        val project = getItem(pos)

        return if (convertView == null) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_project, parent, false)
            view.titleTextView.text = project?.name
            view.budgetTextView.text = getProjectInfoString(project)
            view
        } else {
            convertView.titleTextView.text = project?.name
            convertView.budgetTextView.text = getProjectInfoString(project)
            convertView
        }
    }

    private fun getProjectInfoString(project: Project?): String {
        return if (project == null) {
            "无效项目"
        } else if (project.tag == Project.MOCK_NAME) {
            "总预算：" + project.budget
        } else {
            "总预算：" + project.budget.toString()
        }
    }

}