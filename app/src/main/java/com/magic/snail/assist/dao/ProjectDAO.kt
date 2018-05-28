package com.magic.snail.assist.dao

import android.arch.persistence.room.*
import com.magic.snail.assist.entity.Project

@Dao
interface ProjectDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(nodes: List<Project>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(project: Project)


    @Query("delete from project where name= :project")
    fun delete(project: String) : Int
    @Delete
    fun delete(project : Project)


    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(project: Project)



    @Query("select * from project where name = :project")
    fun query(project:String) : Project
    @Query("select * from project")
    fun queryAll() : List<Project>

}