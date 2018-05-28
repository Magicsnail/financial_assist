package com.magic.snail.assist.view.budget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.magic.snail.assist.R
import com.magic.snail.assist.entity.BudgetThing
import kotlinx.android.synthetic.main.item_budget.view.*

class BudgetListAdapter : BaseAdapter() {

    private var toShowNodes = ArrayList<BudgetThing>()

    private var backupNodes = ArrayList<BudgetThing>()

    var filterTag:String? = null

    fun setBudgets(list: List<BudgetThing>?) {
        backupNodes.clear()
        if (list != null) {
            backupNodes.addAll(list)
        }

        resetShowNodes()
    }

    fun resetShowNodes() {
        if (filterTag.isNullOrEmpty()) {
            toShowNodes.clear()
            toShowNodes.addAll(backupNodes)
        } else {
            toShowNodes.clear()
            for (budget in backupNodes) {
                if (budget.tag != null && budget.tag.contains(filterTag!!)) {
                    toShowNodes.add(budget)
                }
            }
        }
    }

    override fun getView(pos: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView == null) {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_budget, parent, false)
            fillLeafView(pos, view)
            return view
        } else {
            fillLeafView(pos, convertView)
            return convertView
        }
    }


    private fun fillLeafView(pos: Int, view: View) {
        val leaf: BudgetThing = getItem(pos)
        view.leafNameTextView.text = leaf.name
        view.descriptionTextView.text = leaf.description
        if (!leaf.tag.isNullOrEmpty()) {
            view.leafTagTextView.text = "[" + leaf.tag + "]"
        } else {
            view.leafTagTextView.text = ""
        }

        if (leaf.realCost > 0.001) {
            view.costTextView.text = "已花费: " + leaf.realCost.toString()
        } else if (leaf.budget > 0.001) {
            view.costTextView.text = "预算: " + leaf.budget.toString()
        } else {
            view.costTextView.text = "待预算"
        }

    }

    override fun getItem(pos: Int): BudgetThing {
        return toShowNodes[pos]
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun getCount(): Int {
        return toShowNodes.size
    }

    fun getCost(): Double {
        var cost = 0.0
        for (node in toShowNodes!!) {
            cost += node.realCost
        }
        return cost
    }

    fun getPredictCost(): Double {
        var totalCost = 0.0
        for (node in toShowNodes!!) {
            // 如果已经有具体的花费了，该项以实际值计算
            totalCost += if (node.realCost > 0.0001) {
                node.realCost
            } else {
                node.budget
            }
        }
        return totalCost
    }
}