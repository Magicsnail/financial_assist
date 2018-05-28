package com.magic.snail.assist.view.budget

import android.content.Context
import android.view.LayoutInflater
import com.magic.snail.assist.R
import com.magic.snail.assist.entity.BudgetThing
import com.magic.snail.widget.alert.SnailAlertDialog
import kotlinx.android.synthetic.main.dialog_leaf_info.view.*

class BudgetInfoDialog {

    fun show(context: Context, theLeaf: BudgetThing) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_leaf_info, null)

        var build = StringBuilder()
        if (theLeaf.budget > 0.001) {
            build.append("预算: "+ theLeaf.budget.toString()).append("\n")
        }
        build.append("已花费: "+ theLeaf.realCost)

        view.infoTextView.text = build.toString()
        if (!theLeaf.description.isNullOrEmpty()) {
            view.remarkTextView.text = "说明: " + theLeaf.description
        }


        SnailAlertDialog.Builder(context)
                .setTitle(theLeaf.name)
                .setView(view)
                .create()
                .show()
    }
}