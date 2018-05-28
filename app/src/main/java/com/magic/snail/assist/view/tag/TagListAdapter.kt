package com.magic.snail.assist.view.tag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.magic.snail.assist.R
import com.magic.snail.assist.entity.Project
import com.magic.snail.assist.entity.ThingTag
import kotlinx.android.synthetic.main.item_tag.view.*

class TagListAdapter: BaseAdapter() {

    private var nodes = ArrayList<ThingTag>()

    var selectionMode = false

    var selectionTags = ArrayList<String>()

    fun setTags(list : List<ThingTag>?) {
        nodes.clear()
        if (list != null) {
            nodes.addAll(list)
        }

        if (nodes.size == 0) {
            val mock = ThingTag.createBean(Project.MOCK_NAME, "创建新标签！")
            nodes.add(mock)
        }
    }

    fun getSelectTags() : String{
        var build = StringBuilder()
        for (s in selectionTags) {
            if (build.isNotEmpty()) {
                build.append(",")
            }
            build.append(s)
        }
        return build.toString()
    }


    override fun getView(pos: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView == null) {
                var view = LayoutInflater.from(parent.context).inflate(R.layout.item_tag, parent, false)
                fillView(pos, view)
                return view

        } else {
                fillView(pos, convertView)
            return convertView
        }
    }

    fun isSelected(tag: ThingTag) : Boolean {
        return selectionTags.contains(tag.name)
    }

    private fun fillView(pos: Int, view: View) {
        val leaf = getItem(pos)
        view.titleTextView.text = leaf.name

        if (selectionMode) {
            view.checkbox.visibility = View.VISIBLE
            view.checkbox.isChecked = isSelected(leaf)
            view.checkbox.setOnCheckedChangeListener { compoundButton, checked ->
                if (checked) {
                    selectionTags.add(leaf.name)
                } else {
                    selectionTags.remove(leaf.name)
                }
            }

        } else {
            view.checkbox.visibility = View.GONE
        }
    }

    override fun getItem(pos: Int): ThingTag {
        return nodes[pos]
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun getCount(): Int {
        return nodes.size
    }

}