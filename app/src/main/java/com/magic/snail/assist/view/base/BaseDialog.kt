package com.magic.snail.assist.view.base

abstract class BaseDialog {

    var type : Int = _CREATE

    companion object {
        val _CREATE : Int = 1
        val _MODIFY : Int = 2
    }

}