package com.magic.snail.assist.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity

@Entity(tableName = "tag",
        primaryKeys = ["project", "name"])
open class ThingTag {

    @ColumnInfo(name = "project")
    var project:String = ""

    @ColumnInfo(name = "name")
    var name:String = ""


    companion object {
        fun createBean(project: String, name: String): ThingTag {
            val tag = ThingTag()
            tag.project = project
            tag.name = name
            return tag
        }
    }
}