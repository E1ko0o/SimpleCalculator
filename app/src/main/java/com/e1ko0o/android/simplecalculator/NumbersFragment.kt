package com.e1ko0o.android.simplecalculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.e1ko0o.android.simplecalculator.databinding.FragmentNumbersLinearBinding

class NumbersFragment : Fragment(R.layout.fragment_numbers_constraint) {
    private lateinit var binding: FragmentNumbersLinearBinding
    private lateinit var mainViewModel: MainViewModel

    private inner class Controller : View.OnClickListener {
        override fun onClick(v: View?) {
            val btn = v?.findViewById<Button>(v.id)
            when (val btnId = btn?.text.toString()) {
                in "0".."9" -> mainViewModel.onNumberClicked(btnId.toDouble())
                "=" -> mainViewModel.onResultClicked()
                "C" -> mainViewModel.onClearClicked()
                "." -> mainViewModel.onDotClicked(btnId)
                "⌫" -> mainViewModel.onDeleteLastSymbolClicked()
                "E" -> mainViewModel.onEClicked()
                "PI" -> mainViewModel.onPIClicked()
                else -> mainViewModel.onOperationClicked(btnId)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNumbersLinearBinding.bind(view)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
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
            binding.btnE,
            binding.btnPI,
            binding.btnDeleteLastSymbol,
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
        mainViewModel.liveDataForResult.observe(viewLifecycleOwner) {
            binding.tvResult.text = it
        }
        mainViewModel.liveDataForNumber.observe(viewLifecycleOwner) {
            binding.tvNumber.text = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_numbers_linear, container, false)
//        return inflater.inflate(R.layout.fragment_numbers_constraint, container, false)
    }

    companion object {
        fun newInstance(): NumbersFragment {
            return NumbersFragment()
        }
    }
}
