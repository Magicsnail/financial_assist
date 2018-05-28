package com.magic.snail.assist.view.budget

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.magic.snail.assist.R
import com.magic.snail.assist.DataRepository
import com.magic.snail.assist.entity.BudgetThing
import com.magic.snail.assist.entity.Project
import com.magic.snail.assist.view.base.BaseDialog
import com.magic.snail.widget.alert.SnailAlertDialog
import kotlinx.android.synthetic.main.dialog_create_leaf.view.*

class CreateBudgetDialog : BaseDialog() {

    var dialog : SnailAlertDialog? = null

    var project : Project? = null


    fun show(context : Context, argLeaf: BudgetThing?) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_create_leaf, null)
        var posBtn = "记录"
        if (argLeaf != null) {
            type = _MODIFY
            posBtn = "修改"
            view.titleEditView.setText(argLeaf.name)
            view.titleEditView.isEnabled = false
            view.budgetEditView.setText(argLeaf.budget.toString())
            view.costEditView.setText(argLeaf.realCost.toString())
            view.remarkEditView.setText(argLeaf.description)
        }

        SnailAlertDialog.Builder(context)
                .setTitle("记一笔")
                .setView(view)
                .setCancelable(false)
                .setAutoDismissOnClickButton(false)
                .setPositiveButton(posBtn, { dialog, i ->
                    val title = view.titleEditView.text.toString()
                    val budget = view.budgetEditView.text.toString()
                    val cost = view.costEditView.text.toString()
                    val remark = view.remarkEditView.text.toString()

                    if (title == null || title.isEmpty()) {
                        Toast.makeText(context, "请输入有效的项目名称", Toast.LENGTH_SHORT).show()

                    } else if (type == _CREATE) {
                        val leaf = BudgetThing.createBean(project!!.name, title)
                        if (budget != null && budget.isNotEmpty()) {
                            leaf.budget = budget.toDouble()
                        }
                        if (cost != null && cost.isNotEmpty()) {
                            leaf.realCost = cost.toDouble()
                        }
                        leaf.description = remark
                        DataRepository.instance.addBudgetThing(leaf)

                        dialog.dismiss()

                    } else {
                        if (budget != null && budget.isNotEmpty()) {
                            argLeaf?.budget = budget.toDouble()
                        } else{
                            argLeaf?.budget = 0.0
                        }
                        if (cost != null && cost.isNotEmpty()) {
                            argLeaf?.realCost = cost.toDouble()
                        } else {
                            argLeaf?.realCost = 0.0
                        }
                        argLeaf?.description = remark
                        DataRepository.instance.modifyBudgetThing(argLeaf!!)
                        dialog.dismiss()
                    }
                })
                .setNegativeButton("取消", {dialogInterface, i -> dialogInterface.dismiss() })
                .create()
                .show()
    }
}