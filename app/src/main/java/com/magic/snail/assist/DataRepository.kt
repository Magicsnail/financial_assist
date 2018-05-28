package com.magic.snail.assist

import android.content.Context
import com.magic.snail.assist.entity.BudgetThing
import com.magic.snail.assist.entity.Project
import com.magic.snail.assist.entity.ThingTag
import com.magic.snail.assist.global.App
import com.magic.snail.assist.global.AppExecutors

class DataRepository {

    private var dataBase : AppDataBase? = null

    private var hasInit: Boolean = false;

    private val projectList = ArrayList<Project>()

    val observers = ArrayList<Observer>()

    private constructor(db : AppDataBase) {
        this.dataBase = db
    }

    interface Observer {
        // 如果project == null，说明是projectList变更，否则是某个project变更
        fun onDataChange(project : String?)
    }

    private fun noticeObserver(projectId : String?) {
        for (observer in observers) {
            observer.onDataChange(projectId)
        }
    }

    companion object {
        var instance : DataRepository = DataRepository(AppDataBase.getInstance(App.me as Context))
    }


    fun loadProjectList(): List<Project>? {
        if (hasInit) {
            return projectList
        } else {
            AppExecutors.instance.diskIO().execute({
                val projects: List<Project>? = dataBase?.projectDAO()?.queryAll()
                projectList.clear()
                if (projects != null) {
                    projectList.addAll(projects)
                }

                for (prj in projectList) {
                    val tags: List<ThingTag>? = dataBase?.tagDAO()?.query(prj.name)
                    if (tags != null) {
                        prj.tags.addAll(tags)
                    }

                    val bt: List<BudgetThing>? = dataBase?.budgetThingDAO()?.queryBudgets(prj.name)
                    if (bt != null) {
                        prj.budgetThings.addAll(bt)
                    }

                }
                hasInit = true

                noticeObserver(null)
            })
            return null
        }
    }


    fun getProject(name: String): Project? {
        for (prj in projectList) {
            if (prj.name.equals(name)) {
                return prj
            }
        }
        return null
    }

    /**
     * 删除Project，包括Node等
     */
    fun deleteProject(projectName: String) {
        for (project in projectList) {
            if (project.name.equals(projectName)) {
                projectList.remove(project)
                break
            }
        }

        AppExecutors.instance.diskIO().execute({
            dataBase?.projectDAO()?.delete(projectName)
            dataBase?.tagDAO()?.deleteProject(projectName)
            dataBase?.budgetThingDAO()?.deleteProject(projectName)

            noticeObserver(null)
        })
    }


    /**
     * 创建project，异步保存DB
     */
    fun addProject(project: Project) {
        if (!projectList.contains(project)) {
            projectList.add(project)

            // 保存DB
            AppExecutors.instance.diskIO().execute({
                dataBase?.projectDAO()?.insert(project)
                noticeObserver(null)
            })
        }
    }

    fun modifyProject(project: Project) {
        val prj = getProject(project.name)
        if (prj != null) {
            prj.copy(project)
            AppExecutors.instance.diskIO().execute({
                dataBase?.projectDAO()?.update(project)
                noticeObserver(project.name)
            })
        }
    }


    fun addBudgetThing(leaf: BudgetThing) {
        var project = getProject(leaf.project)
        if (project != null) {
            project.budgetThings.add(leaf)

            AppExecutors.instance.diskIO().execute({
                dataBase?.budgetThingDAO()?.insert(leaf)
                noticeObserver(leaf.project)
            })
        }
    }

    fun deleteBudgetThing(leaf: BudgetThing) {
        var project = getProject(leaf.project)
        if (project != null) {

            for (node in project.budgetThings) {
                if (node.name.equals(leaf.name)) {
                    project.budgetThings!!.remove(node)
                    break
                }
            }

            AppExecutors.instance.diskIO().execute({
                dataBase?.budgetThingDAO()?.delete(leaf)
                noticeObserver(leaf.project)
            })
        }
    }

    fun modifyBudgetThing(leaf: BudgetThing) {
        var project = getProject(leaf.project)
        if (project != null) {

            for (node in project.budgetThings) {
                if (node.name.equals(leaf.name)) {
                    node.copy(leaf)
                    break
                }
            }

            AppExecutors.instance.diskIO().execute({
                dataBase?.budgetThingDAO()?.update(leaf)
                noticeObserver(leaf.project)
            })
        }
    }


    fun addThingTag(tag: ThingTag) {
        var project = getProject(tag.project)
        if (project != null) {
            for (node in project.tags) {
                if (node.name.equals(tag.name)) {
                    return
                }
            }

            project.tags.add(tag)

            AppExecutors.instance.diskIO().execute({
                dataBase?.tagDAO()?.insert(tag)
                noticeObserver(tag.project)
            })
        }
    }

    fun deleteThingTag(leaf: ThingTag) {
        var project = getProject(leaf.project)
        if (project != null) {

            for (node in project.tags) {
                if (node.name.equals(leaf.name)) {
                    project.tags!!.remove(node)
                    break
                }
            }

            AppExecutors.instance.diskIO().execute({
                dataBase?.tagDAO()?.delete(leaf)
                noticeObserver(leaf.project)
            })
        }
    }

    fun updateBudgetTag(budgetThing: BudgetThing, tag:String) {
        budgetThing.tag = tag

        AppExecutors.instance.diskIO().execute({
            dataBase?.budgetThingDAO()?.updateTag(budgetThing.project, budgetThing.name, tag)

            noticeObserver(budgetThing.project)
        })
    }
}