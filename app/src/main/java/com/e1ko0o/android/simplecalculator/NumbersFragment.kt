package com.e1ko0o.android.simplecalculator

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import com.e1ko0o.android.simplecalculator.databinding.FragmentNumbersBinding

private const val TAG = "LOL!"

class NumbersFragment : Fragment(R.layout.fragment_numbers) {
    private lateinit var binding: FragmentNumbersBinding

    private inner class Controller : View.OnClickListener {
        override fun onClick(v: View?) {
            val btn = v?.findViewById<Button>(v.id)
            when (val btnId = btn?.text.toString()) {
                in "0".."9" -> onNumberClicked(btnId.toInt())
                "More" -> onShowMoreOperationsClicked()
                "=" -> onResultClicked()
                "C" -> onClearClicked()
                else -> onOperationClicked(btnId)
            }
        }
    }

    private fun doMath(operation: String, number: Int) {
        Log.d(TAG, "$result $operation $number !!!!!!")
        when (operation.trim()) {
            "+" -> result += number
            "-" -> result -= number
            "*" -> result *= number
            "/" -> if (number != 0) {
                result /= number
            } else {
                Toast.makeText(
                    activity?.applicationContext,
                    "Can't divide by zero",
                    Toast.LENGTH_SHORT
                ).show()
                result = 0.0
            }
        }
        Log.d(TAG, "$result $operation $number")
    }

    fun onNumberClicked(number: Int) {
        if (binding.tvNumber.text.isDigitsOnly()) {
            binding.tvNumber.append(number.toString())
            NumbersFragment.number = binding.tvNumber.text.toString().toInt()
        } else {
            binding.tvNumber.text = number.toString()
            NumbersFragment.number = number
        }
        binding.tvResult.append(number.toString())
    }

    fun onOperationClicked(operation: String) {
        result = if (binding.tvResult.text.toString().isEmpty())
            0.0
        else
            binding.tvResult.text.toString().toDouble()
        binding.tvResult.append(operation)
        binding.tvNumber.text = operation
//            if (operation.trim() == ".") {
//                binding.tvResult.text
//            }
        NumbersFragment.operation = operation
    }

    fun onShowMoreOperationsClicked() {
//            вызывать новый фрагмент
        Toast.makeText(
            activity?.applicationContext,
            "Show more operations",
            Toast.LENGTH_SHORT
        )
            .show()
    }

    fun onClearClicked() {
        result = 0.0
        number = 0
        operation = ""
        binding.tvResult.text = ""
        binding.tvNumber.text = ""
    }

    fun onResultClicked() {
        doMath(operation, number)
        binding.tvResult.text = result.toString()
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
            binding.btnMinus,
            binding.btnMore,
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
        Log.d(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_numbers, container, false)
    }

    companion object {
        fun newInstance(): NumbersFragment {
            return NumbersFragment()
        }

        private var result: Double = 0.0
        private var operation: String = ""
        private var number: Int = 0
    }
}
