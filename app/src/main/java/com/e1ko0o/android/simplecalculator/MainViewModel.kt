package com.e1ko0o.android.simplecalculator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.pow
import kotlin.math.sqrt

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
        result = String.format("%.9f", result).toDouble()
        if (result.toString().endsWith(".0")) {
            liveDataForNumber.value = result.toInt().toString()
            result = result.toInt().toDouble()
        }
        else if (result.toString().contains('E')) {
            liveDataForNumber.value = result.toBigDecimal().toString()
        } else
            liveDataForNumber.value = result.toString()
    }

    fun onDotClicked(dot: String) {
        liveDataForResult.value += dot
        liveDataForNumber.value += dot
    }

    fun onNumberClicked(number: Double) {
        val liveDataString = liveDataForResult.value.toString().lowercase()
        val ptr = "[+\\-/%*^√(?!nul)]".toRegex()
        val ptrToRemove = liveDataString.dropLastWhile { !ptr.matches(it.toString()) }
        var lastFullNumber = liveDataString.replace(ptrToRemove, "")
        if (number != kotlin.math.E && number != kotlin.math.PI) {
             lastFullNumber += number.toInt().toString()
            if (lastFullNumber == number.toInt().toString() && liveDataString != "null") {
                liveDataForResult.value += number.toInt().toString()
                liveDataForNumber.value = number.toInt().toString()
            } else if (lastFullNumber == number.toInt().toString() && liveDataString == "null") {
                liveDataForResult.value = number.toInt().toString()
                liveDataForNumber.value = number.toInt().toString()
            } else {
                liveDataForResult.value += number.toInt().toString()
                liveDataForNumber.value += number.toInt().toString()
            }
            this.number = lastFullNumber.toDouble()
        } else {
            lastFullNumber += number.toString()
            if (lastFullNumber == number.toString() && liveDataString != "null") {
                liveDataForResult.value += number.toString()
                liveDataForNumber.value = number.toString()
            } else if (lastFullNumber == number.toString() && liveDataString == "null") {
                liveDataForResult.value = number.toString()
                liveDataForNumber.value = number.toString()
            } else {
                liveDataForResult.value += number.toString()
                liveDataForNumber.value += number.toString()
            }
            this.number = lastFullNumber.toDouble()
        }
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

    fun onResultClicked() {
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

    fun onDeleteLastSymbolClicked() {
        if (liveDataForResult.value == null) {
            onClearClicked()
            return
        }
        if (liveDataForResult.value.toString().isNotEmpty()
            && liveDataForResult.value != null
        ) {
            val resultWithoutLastSymbol = liveDataForResult.value.toString().dropLast(1)
            liveDataForResult.value = resultWithoutLastSymbol
        }
        if (liveDataForNumber.value.toString().isNotEmpty()
            && liveDataForNumber.value != null
        ) {
            val numberWithoutLastSymbol = liveDataForNumber.value.toString().dropLast(1)
            liveDataForNumber.value = numberWithoutLastSymbol
        }
    }

    fun onEClicked() {
        onNumberClicked(kotlin.math.E)
    }

    fun onPIClicked() {
        onNumberClicked(kotlin.math.PI)
    }
}
