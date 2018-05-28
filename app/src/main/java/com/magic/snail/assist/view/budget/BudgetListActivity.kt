package com.magic.snail.assist.view.budget

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.magic.snail.assist.Constants
import com.magic.snail.assist.R
import com.magic.snail.assist.DataRepository
import com.magic.snail.assist.entity.BudgetThing
import com.magic.snail.assist.entity.Project
import com.magic.snail.assist.global.AppExecutors
import com.magic.snail.assist.view.base.BaseActivity
import com.magic.snail.assist.view.tag.TagListActivity
import com.magic.snail.widget.alert.SnailContextMenu
import kotlinx.android.synthetic.main.activity_project_detail.*

class BudgetListActivity  : BaseActivity() {

    var project: Project? = null

    var listAdapter : BudgetListAdapter? = null

    var createLeafDialog : CreateBudgetDialog? = null

    var budgetToSetTag : BudgetThing? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_detail)

        Log.i("Time", "11")
        val prjName = intent.getStringExtra(Constants.PARAM_PROJECT_NAME)
        project = DataRepository.instance.getProject(prjName)
        if (project == null) {
            finish()
            return
        }
        setTitle("项目: $prjName")

        setRightAction("标签", View.OnClickListener { _ ->
            val intent = Intent(this, TagListActivity::class.java)
            intent.putExtra(Constants.PARAM_PROJECT_NAME, project!!.name)
            startActivity(intent)
        })

        createLeafDialog= CreateBudgetDialog()
        createLeafDialog!!.project = project

        listAdapter = BudgetListAdapter()
        listView.adapter = listAdapter

        listAdapter?.setBudgets(project!!.budgetThings)
        listAdapter?.notifyDataSetChanged()

        addThingBtn.setOnClickListener { view ->
            createLeafDialog?.show(this, null)
        }
        filterBtn.setOnClickListener {
            val intent = Intent(this, TagListActivity::class.java)
            intent.putExtra(Constants.PARAM_PROJECT_NAME, project!!.name)
            intent.putExtra(Constants.PARAM_SELECTION_MODE, true)
            startActivityForResult(intent, Constants.REQUEST_CODE_FILTER)
        }

        listView.setOnItemLongClickListener { adapterView, view, pos, id ->
            val theLeaf = listAdapter!!.getItem(pos)
            if (theLeaf != null) {
                SnailContextMenu.builder()
                        .items(arrayOf("修改", "删除", "设置标签"))
                        .listener { id ->
                            if (id == 0) {
                                createLeafDialog?.show(this, theLeaf)

                            } else if (id == 1){
                                DataRepository.instance.deleteBudgetThing(theLeaf)

                            } else if (id == 2) {
                                budgetToSetTag = theLeaf
                                val intent = Intent(this, TagListActivity::class.java)
                                intent.putExtra(Constants.PARAM_PROJECT_NAME, project!!.name)
                                intent.putExtra(Constants.PARAM_SELECTION_MODE, true)
                                intent.putExtra(Constants.PARAM_SELECTION_TAGS, theLeaf.tag)
                                startActivityForResult(intent, Constants.REQUEST_CODE_SET_TAG)
                            }
                        }
                        .build(this)
                        .show()
            }
            return@setOnItemLongClickListener true
        }

        listView.setOnItemClickListener { adapterView, view, pos, l ->
            val theLeaf = listAdapter!!.getItem(pos)
            if (theLeaf != null) {
                BudgetInfoDialog().show(this, theLeaf)
            }
        }

        refreshBriefInfo()

        Log.i("Time", "22")
    }


    override fun onDataChange(project: String?) {
        AppExecutors.instance.mainThread().execute({
            this.project = DataRepository.instance.getProject(this.project!!.name)
            listAdapter?.setBudgets(this.project?.budgetThings)
            listAdapter?.notifyDataSetChanged()

            refreshBriefInfo()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_CODE_SET_TAG && data != null) {
            if (budgetToSetTag != null) {
                var tag = data!!.getStringExtra(Constants.PARAM_SELECTION_TAGS)
                budgetToSetTag!!.tag = tag
                DataRepository.instance.updateBudgetTag(budgetToSetTag!!, tag)
            }
            budgetToSetTag = null

        } else if (requestCode == Constants.REQUEST_CODE_FILTER && data != null) {
            var tag = data!!.getStringExtra(Constants.PARAM_SELECTION_TAGS)
            if (tag.isNullOrEmpty()) {
                filterBtn.text = "筛选"
            } else {
                filterBtn.text = "筛选: " + tag
            }
            listAdapter?.filterTag = tag
            listAdapter?.resetShowNodes()
            listAdapter?.notifyDataSetChanged()

            refreshBriefInfo()

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun refreshBriefInfo() {
        var build = StringBuilder()
        build.append("总预算[").append(project?.budget.toString()).append("]元, ")
                .append("已花费[").append(project?.getCost()).append("]元, ")
                .append("预期花费[").append(project?.getPredictCost()).append("]元")
        if (!listAdapter?.filterTag.isNullOrEmpty()) {
            build.append("\n")
                    .append("筛选后花费总计[").append(listAdapter?.getCost()).append("]元, ")
                    .append("预期花费[").append(listAdapter?.getPredictCost()).append("]元")
        }
        briefInfoTextView.text = build.toString()
    }
}
