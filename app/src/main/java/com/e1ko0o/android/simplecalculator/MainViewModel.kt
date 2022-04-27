package com.e1ko0o.android.simplecalculator

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.pow
import kotlin.math.sqrt

private const val TAG = "LOL!"
class MainViewModel : ViewModel() {
    val liveDataForResult = MutableLiveData<String>()
    val liveDataForNumber = MutableLiveData<String>()

    private var result: Double = 0.0
    private var operation: String = ""
    private var number: Double = 0.0

    private fun doMath() {
        when (operation.lowercase().trim()) {
            "%" -> result *= number / 100
            "^" -> result = result.pow(number.toInt())
            "√" -> result = if (number.toString().contains("-")) 0.0 else sqrt(number)
            "+" -> result += number
            "-" -> result -= number
            "*" -> result *= number
            "/" -> result /= number
            else -> result = number
        }
        if (result.toString().endsWith(".0"))
            liveDataForNumber.value = result.toInt().toString()
        else
            liveDataForNumber.value = result.toString()
    }

    fun onDotClicked(dot: String) {
        liveDataForResult.value += dot
        liveDataForNumber.value += dot
    }

    fun onNumberClicked(number: Double) {
        val liveDataString = liveDataForResult.value.toString().lowercase()
        val ptr = "[+\\-/%*^√(?!null)]".toRegex()
        val ptrToRemove = liveDataString.dropLastWhile { !ptr.matches(it.toString()) }
        val lastNum = liveDataString.replace(ptrToRemove, "") + number.toInt().toString()
        if (lastNum == number.toInt().toString() && liveDataString != "null") {
            if (lastNum.contains(".")) {
                liveDataForResult.value += number.toString()
                liveDataForNumber.value = number.toString()
            } else {
                liveDataForResult.value += number.toInt().toString()
                liveDataForNumber.value = number.toInt().toString()
            }
        } else if (lastNum == number.toInt().toString() && liveDataString == "null") {
            if (lastNum.contains(".")) {
                liveDataForResult.value = number.toString()
                liveDataForNumber.value = number.toString()
            } else {
                liveDataForResult.value = number.toInt().toString()
                liveDataForNumber.value = number.toInt().toString()
            }
        } else {
            if (lastNum.contains(".")) {
                liveDataForResult.value += number.toString()
                liveDataForNumber.value += number.toString()
            } else {
                liveDataForResult.value += number.toInt().toString()
                liveDataForNumber.value += number.toInt().toString()
            }
        }
        //@todo основная логика работает, но выводятся лишние точки
        this.number = lastNum.toDouble()
//        doMath()
    }

    fun onOperationClicked(operation: String) {
        if (operation == "√") {
            liveDataForResult.value = operation + result.toString()
            result = if (result.toString().dropLast(1) != "") result.toString().dropLast(1)
                .toDouble() else 0.0
        } else {
            liveDataForResult.value += operation
            result = if (result.toString().dropLast(1) != "") result.toString().dropLast(1)
                .toDouble() else 0.0
        }
        liveDataForNumber.value = ""
        this.operation = operation
        result = number
    }

    fun onResultClicked() { //@todo test
        doMath()
        number = result
        if (number.toString().endsWith(".0")) {
            liveDataForResult.value = result.toInt().toString()
            liveDataForNumber.value = ""
        } else {
            liveDataForResult.value = result.toString()
            liveDataForNumber.value = ""
        }
    }

    fun onClearClicked() {
        result = 0.0
        number = 0.0
        operation = ""
        liveDataForResult.value = ""
        liveDataForNumber.value = ""
    }
}