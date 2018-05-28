package com.magic.snail.assist.dao

import android.arch.persistence.room.*
import com.magic.snail.assist.entity.ThingTag

@Dao
interface TagDAO {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(nodes: List<ThingTag>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(node: ThingTag)


    @Delete
    fun delete(node: ThingTag)
    @Query("delete from tag where project=:project")
    fun deleteProject(project: String)


    @Update
    fun update(node: ThingTag)


    @Query("select * from tag where project = :project")
    fun query(project: String): List<ThingTag>

}
