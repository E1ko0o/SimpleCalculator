package com.e1ko0o.android.simplecalculator

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.e1ko0o.android.simplecalculator.databinding.FragmentNumbersBinding
import kotlin.math.pow
import kotlin.math.sqrt

private const val TAG = "LOL!"
// @TODO Ты хорошо поработал, посмотри калькулятор гугла - вывод заранее снизу

class NumbersFragment : Fragment(R.layout.fragment_numbers) {
    private lateinit var binding: FragmentNumbersBinding

    private inner class Controller : View.OnClickListener {
        override fun onClick(v: View?) {
            val btn = v?.findViewById<Button>(v.id)
            when (val btnId = btn?.text.toString()) {
                in "0".."9" -> onNumberClicked(btnId.toDouble())
                "=" -> onResultClicked()
                "C" -> onClearClicked()
                "." -> onDotClicked(btnId)
                else -> onOperationClicked(btnId)
            }
        }
    }

    private fun doMath() {
        when (operation.lowercase().trim()) {
            "%" -> result *=  number / 100
            "^" -> result = result.pow(number.toInt())
            "√" -> {
                result = if (number.toString().contains("-")) {
                    Toast.makeText(
                        activity?.applicationContext,
                        "Can't take the square root of a negative number",
                        Toast.LENGTH_SHORT
                    ).show()
                    0.0
                } else sqrt(number)
            }
            "+" -> result += number
            "-" -> result -= number
            "*" -> result *= number
            "/" -> if (number.toString() != "0") result /= number
            else {
                Toast.makeText(
                    activity?.applicationContext,
                    "Can't divide by zero",
                    Toast.LENGTH_SHORT
                ).show()
                result = 0.0
            }
        }
    }

    fun onDotClicked(operation: String) {
        binding.tvResult.append(operation)
        binding.tvNumber.append(operation)
    }

    fun onNumberClicked(number: Double) {
        val buffer = binding.tvNumber.text.toString().replace(
            "[+\\-/%*^√]".toRegex(), "") + number.toInt().toString()
        if (buffer.isNotEmpty()) {
            if (number.toString().endsWith(".0") || number.toString().endsWith(".")) {
                binding.tvNumber.append(number.toInt().toString())
                binding.tvResult.append(number.toInt().toString())
            } else {
                binding.tvNumber.append(number.toString())
                binding.tvResult.append(number.toString())
            }
            NumbersFragment.number = buffer.toDouble()
        } else {
            if (number.toString().endsWith(".0") || number.toString().endsWith(".")) {
                binding.tvNumber.text = number.toInt().toString()
                binding.tvResult.append(number.toInt().toString())
            } else {
                binding.tvNumber.text = number.toString()
                binding.tvResult.append(number.toString())
            }
            NumbersFragment.number = number
        }
    }

    fun onOperationClicked(operation: String) {
        if (operation == "√") {
            binding.tvResult.text = operation + binding.tvResult.text
            result = if (binding.tvResult.text.toString().drop(1) != "")
                binding.tvResult.text.toString().drop(1).toDouble()
            else 0.0
        } else {
            binding.tvResult.append(operation)
            result = if (binding.tvResult.text.toString().dropLast(1) != "")
                binding.tvResult.text.toString().dropLast(1).toDouble()
            else 0.0
        }
        binding.tvNumber.text = operation
        NumbersFragment.operation = operation
    }

    fun onClearClicked() {
        result = 0.0
        number = 0.0
        operation = ""
        binding.tvResult.text = ""
        binding.tvNumber.text = ""
    }

    fun onResultClicked() {
        doMath()
        number = result
        if (number.toString().endsWith(".0") || number.toString().endsWith("."))
            binding.tvResult.text = result.toInt().toString()
        else
            binding.tvResult.text = result.toString()
        binding.tvNumber.text = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNumbersBinding.bind(view)
        val list: List<Button> = listOf(
            binding.btn0,
            binding.btn1,
            binding.btn2,
            binding.btn3,
            binding.btn4,
            binding.btn5,
            binding.btn6,
            binding.btn7,
            binding.btn8,
            binding.btn9,
            binding.btnDivide,
            binding.btnPlus,
            binding.btnPow,
            binding.btnPercent,
            binding.btnSqrt,
            binding.btnDot,
            binding.btnMinus,
            binding.btnMultiply,
            binding.btnClear,
            binding.btnResult
        )
        for (i in list) {
            i.setOnClickListener(Controller())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_numbers, container, false)
    }

    companion object {
        fun newInstance(): NumbersFragment {
            return NumbersFragment()
        }

        private var result: Double = 0.0
        private var operation: String = ""
        private var number: Double = 0.0
    }
}
