package com.magic.snail.assist.view.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.magic.snail.assist.DataRepository
import com.magic.snail.assist.R
import kotlinx.android.synthetic.main.title_bar.*

open abstract class BaseActivity : AppCompatActivity(), DataRepository.Observer {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = resources.getColor(R.color.titlebar_background)

        DataRepository.instance.observers.add(this)
    }

    override fun onStart() {
        super.onStart()
        backButton.setOnClickListener{_ ->
            finish()
        }
    }

    override fun onDestroy() {
        DataRepository.instance.observers.remove(this)
        super.onDestroy()
    }

    fun setTitle(title:String) {
        titleTextView.text = title
    }

    fun hideBackAction() {
        backButton.visibility = View.GONE
        divider.visibility = View.GONE
    }

    fun hideRightAction() {
        rightAction.visibility = View.GONE
    }

    fun setRightAction(action:String, listener: View.OnClickListener) {
        rightAction.visibility = View.VISIBLE
        rightAction.text = action
        rightAction.setOnClickListener(listener)
    }
}