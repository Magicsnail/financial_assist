package com.magic.snail.assist.dao

import android.arch.persistence.room.*
import com.magic.snail.assist.entity.BudgetThing

@Dao
interface BudgetThingDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(leafs: List<BudgetThing>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(leaf : BudgetThing)


    @Delete
    fun delete(leaf : BudgetThing)
    @Query("delete from budget_thing where project=:project")
    fun deleteProject(project: String)


    @Update
    fun update(leaf: BudgetThing)
    @Query("update budget_thing set tag=:tag where project=:project and name=:name")
    fun updateTag(project: String, name:String, tag:String)


    @Query("select * from budget_thing where project = :project ORDER BY max(realCost, budget) DESC")
    fun queryBudgets(project:String) : List<BudgetThing>

}