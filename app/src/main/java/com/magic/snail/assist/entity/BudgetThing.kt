package com.magic.snail.assist.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity

@Entity(tableName = "budget_thing",
        primaryKeys = ["project", "name"])
open class BudgetThing {

    @ColumnInfo(name = "project")
    var project:String = ""

    @ColumnInfo(name = "name")
    var name:String = ""

    @ColumnInfo(name="tag")
    var tag:String = ""

    @ColumnInfo(name = "budget")
    var budget:Double = 0.0        // 预算金额

    @ColumnInfo(name = "realCost")
    var realCost:Double = 0.0      // 实际开销

    @ColumnInfo(name = "description")
    var description:String? = null



    companion object {
        fun createBean(projectId:String, name: String): BudgetThing {
            val node = BudgetThing()
            node.project = projectId
            node.name = name
            return node
        }
    }

    fun copy(leaf: BudgetThing) {
        this.name = leaf.name
        this.project = leaf.project
        this.tag = leaf.tag
        this.budget = leaf.budget
        this.realCost = leaf.realCost
        this.description = leaf.description
    }
}