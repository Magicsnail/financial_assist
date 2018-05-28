package com.magic.snail.assist.view.tag

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.magic.snail.assist.Constants
import com.magic.snail.assist.DataRepository
import com.magic.snail.assist.R
import com.magic.snail.assist.entity.Project
import com.magic.snail.assist.global.AppExecutors
import com.magic.snail.assist.view.base.BaseActivity
import com.magic.snail.widget.alert.SnailContextMenu
import kotlinx.android.synthetic.main.activity_project_detail.*

class TagListActivity : BaseActivity(), DataRepository.Observer {

    var project:Project? = null

    var listAdapter : TagListAdapter? = null

    var createNodeDialog : CreateTagDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_tag_list)

        val prjName = intent.getStringExtra(Constants.PARAM_PROJECT_NAME)
        project = DataRepository.instance.getProject(prjName)
        if (project == null) {
            finish()
            return
        }

        // 是否是selection模式
        var selection = intent.getBooleanExtra(Constants.PARAM_SELECTION_MODE, false)

        setTitle("标签页: $prjName")

        createNodeDialog = CreateTagDialog(project!!)

        listAdapter = TagListAdapter()
        listAdapter!!.selectionMode = selection
        if (selection) {
            var selTags = intent.getStringExtra(Constants.PARAM_SELECTION_TAGS)
            if (selTags != null) {
                listAdapter!!.selectionTags.addAll(selTags.split(","))
            }
        }

        listView.adapter = listAdapter

        listAdapter?.setTags(project?.tags)
        listAdapter?.notifyDataSetChanged()

        if (selection) {
            setRightAction("确定", View.OnClickListener { _ ->
                if (listAdapter!!.selectionMode) {
                    var intent = Intent()
                    intent.putExtra(Constants.PARAM_SELECTION_TAGS, listAdapter!!.getSelectTags())
                    setResult(RESULT_OK, intent)
                }
                finish()
            })
        } else {
            setRightAction("新增", View.OnClickListener { _ ->
                createNodeDialog?.show(this, null)
            })
        }

        listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, pos, id ->
            val node = listAdapter?.getItem(pos)
            if (node != null && node.project == Project.MOCK_NAME) {
                createNodeDialog?.show(this, null)
            }
        }

        listView.onItemLongClickListener = AdapterView.OnItemLongClickListener { adapterView, view, pos, id ->
            val theNode = listAdapter!!.getItem(pos)
            if (theNode != null) {
                SnailContextMenu.builder()
                        .items(arrayOf("修改", "删除"))
                        .listener { id ->
                            if (id == 0) {
                                createNodeDialog?.show(this, theNode)
                            } else {
                                DataRepository.instance.deleteThingTag(theNode)
                            }
                        }
                        .build(this)
                        .show()
            }
            return@OnItemLongClickListener true
        }

    }



    override fun onDataChange(project: String?) {
        AppExecutors.instance.mainThread().execute({
            this.project = DataRepository.instance.getProject(this.project!!.name)
            listAdapter?.setTags(this.project?.tags)
            listAdapter?.notifyDataSetChanged()
        })
    }

}