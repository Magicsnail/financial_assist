package com.magic.snail.assist.view.project

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.magic.snail.assist.R
import com.magic.snail.assist.DataRepository
import com.magic.snail.assist.entity.Project
import com.magic.snail.assist.view.base.BaseDialog
import com.magic.snail.widget.alert.SnailAlertDialog
import kotlinx.android.synthetic.main.dialog_create_project.view.*

class CreateProjectDialog : BaseDialog() {

    var dialog : SnailAlertDialog? = null


    fun show(context: Context, argProject: Project?) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_create_project, null)
        if (argProject != null) {
            view.titleEditView.setText(argProject.name)
            view.titleEditView.isEnabled = false
            view.budgetEditView.setText(argProject.budget.toString())
        }
        var tit = "创建项目"
        var pos = "创建"
        if (argProject != null) {
            tit = "修改项目"
            pos = "修改"
        }

        SnailAlertDialog.Builder(context)
                .setTitle(tit)
                .setView(view)
                .setCancelable(false)
                .setAutoDismissOnClickButton(false)
                .setPositiveButton(pos, { dialog, i ->
                    val title = view.titleEditView.text.toString()
                    val budget = view.budgetEditView.text.toString()

                    if (title == null || title.isEmpty()) {
                        Toast.makeText(context, "请输入有效的项目名称", Toast.LENGTH_SHORT).show()
                    } else if (argProject == null) {
                        val project = Project.createBean(title)
                        if (budget != null && budget.isNotEmpty()) {
                            project.budget = budget.toDouble()
                        }
                        DataRepository.instance.addProject(project)

                        dialog.dismiss()
                    } else {
                        if (budget != null && budget.isNotEmpty()) {
                            argProject?.budget = budget.toDouble()
                            DataRepository.instance.modifyProject(argProject!!)
                        }
                        dialog.dismiss()
                    }
                })
                .setNegativeButton("取消", {dialogInterface, i -> dialogInterface.dismiss() })
                .create()
                .show()
    }
}