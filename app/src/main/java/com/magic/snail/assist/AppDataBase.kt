package com.magic.snail.assist

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.magic.snail.assist.dao.BudgetThingDAO
import com.magic.snail.assist.dao.TagDAO
import com.magic.snail.assist.entity.BudgetThing
import com.magic.snail.assist.entity.Project
import com.magic.snail.assist.entity.ThingTag

/**
 * Created by cheney on 2018/4/16.
 */
@Database(entities = arrayOf(Project::class, ThingTag::class, BudgetThing::class),
        version = 1,
        exportSchema = false)
abstract class AppDataBase : RoomDatabase() {


    abstract fun tagDAO(): TagDAO

    abstract fun budgetThingDAO() : BudgetThingDAO

    abstract fun projectDAO() : com.magic.snail.assist.dao.ProjectDAO

    companion object {
        private var sInstance: AppDataBase?=null

        fun getInstance(context: Context) : AppDataBase {
            if (sInstance == null) {
                sInstance = Room
                        .databaseBuilder(context, AppDataBase::class.java, "assit.db")
                        .addCallback(object: RoomDatabase.Callback(){})
                        .build()
            }
            return sInstance as AppDataBase
        }
    }
}