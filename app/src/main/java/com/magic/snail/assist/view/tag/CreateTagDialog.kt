package com.magic.snail.assist.view.tag

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.magic.snail.assist.R
import com.magic.snail.assist.DataRepository
import com.magic.snail.assist.entity.Project
import com.magic.snail.assist.entity.ThingTag
import com.magic.snail.assist.view.base.BaseDialog
import com.magic.snail.widget.alert.SnailAlertDialog
import kotlinx.android.synthetic.main.dialog_create_project.view.*

class CreateTagDialog constructor(prj: Project) : BaseDialog() {
    var dialog : SnailAlertDialog? = null

    val project = prj


    fun show(context : Context, argNode: ThingTag?) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_create_node, null)
        var title = "创建标签"
        var posBtn = "创建"
        if (argNode != null) {
            type = _MODIFY
            title = "修改标签"
            posBtn = "修改"
            view.titleEditView.setText(argNode.name)
            view.titleEditView.isEnabled = false
        }
        SnailAlertDialog.Builder(context)
                .setTitle(title)
                .setView(view)
                .setCancelable(false)
                .setAutoDismissOnClickButton(false)
                .setPositiveButton(posBtn, { dialog, i ->
                    val title = view.titleEditView.text.toString()
                    val budget = view.budgetEditView.text.toString()
                    if (title == null || title.isEmpty()) {
                        Toast.makeText(context, "请输入有效的项目名称", Toast.LENGTH_SHORT).show()
                    } else if (type == _CREATE){
                        val node = ThingTag.createBean(project.name, title)
                        DataRepository.instance.addThingTag(node)
                        dialog.dismiss()
                    }
                })
                .setNegativeButton("取消", {dialogInterface, i -> dialogInterface.dismiss() })
                .create()
                .show()
    }
}