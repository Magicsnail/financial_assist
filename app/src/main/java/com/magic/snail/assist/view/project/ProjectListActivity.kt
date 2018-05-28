package com.magic.snail.assist.view.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.magic.snail.assist.Constants
import com.magic.snail.assist.DataRepository
import com.magic.snail.assist.R
import com.magic.snail.assist.entity.Project
import com.magic.snail.assist.global.AppExecutors
import com.magic.snail.assist.view.base.BaseActivity
import com.magic.snail.assist.view.budget.BudgetListActivity
import com.magic.snail.widget.alert.SnailContextMenu
import kotlinx.android.synthetic.main.activity_main.*

class ProjectListActivity : BaseActivity(), DataRepository.Observer{

    var listAdapter : ProjectListAdapter? = null

    var createProjectDialog = CreateProjectDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideBackAction()
        hideRightAction()

        Log.i("Time", "11")
        addButton.setOnClickListener { view ->
            createProjectDialog.show(this, null)
        }

        listAdapter = ProjectListAdapter()
        projectListView.adapter = listAdapter

        projectListView.setOnItemClickListener { adapterView, view, pos, id ->
            val project = listAdapter!!.getItem(pos)
            if (project != null && project.tag != Project.MOCK_NAME) {
                val intent = Intent(this, BudgetListActivity::class.java)
                intent.putExtra(Constants.PARAM_PROJECT_NAME, project.name)
                startActivity(intent)
            } else {
                createProjectDialog.show(this, null)
            }
        }

        projectListView.setOnItemLongClickListener { adapterView, view, pos, id ->
            val project = listAdapter!!.getItem(pos)
            if (project != null && project.tag != Project.MOCK_NAME) {
                SnailContextMenu.builder()
                        .items(arrayOf("修改", "删除"))
                        .listener { id ->
                            if (id == 0) {
                                createProjectDialog.show(this, project)
                            } else {
                                DataRepository.instance.deleteProject(project!!.name)
                            }
                        }
                        .build(this)
                        .show()
            }
            return@setOnItemLongClickListener true
        }

        Log.i("Time", "22")

    }

    override fun onResume() {
        super.onResume()
        Log.i("Time", "11-1")
        listAdapter?.setProjects(DataRepository.Companion.instance.loadProjectList())
        listAdapter?.notifyDataSetChanged()
        Log.i("Time", "11-2")
    }


    override fun onDataChange(project: String?) {
        AppExecutors.instance.mainThread().execute({
            listAdapter?.setProjects(DataRepository.Companion.instance.loadProjectList())
            listAdapter?.notifyDataSetChanged()
        })
    }


}
