package com.magic.snail.assist.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "project")
open class Project {

    @ColumnInfo(name = "tag")
    var tag: String = ""

    @PrimaryKey
    @ColumnInfo(name = "name")
    var name:String = ""

    @ColumnInfo(name = "budget")
    var budget:Double = 0.0



    @Ignore
    var budgetThings : ArrayList<BudgetThing> = ArrayList()

    @Ignore
    var tags : ArrayList<ThingTag> = ArrayList()

    fun getCost() : Int {
        var cost = 0.0
        if (budgetThings != null) {
            for (node in budgetThings!!) {
                cost += node.realCost
            }
        }
        return cost.toInt()
    }

    fun getPredictCost() : Int {
        var totalCost = 0.0
        if (budgetThings != null) {
            for (node in budgetThings!!) {
                // 如果已经有具体的花费了，该项以实际值计算
                totalCost += if (node.realCost > 0.0001) {
                    node.realCost
                } else {
                    node.budget
                }
            }
        }
        return totalCost.toInt()
    }

    fun copy(leaf: Project) {
        this.name = leaf.name
        this.tag = leaf.tag
        this.budget = leaf.budget
    }

    companion object {

        val MOCK_NAME = "__mock"

        fun createBean(title : String) : Project {
            val project = Project()
            project.name = title
            return project
        }
    }
}