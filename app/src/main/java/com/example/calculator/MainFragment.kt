package com.example.calculator

import android.view.View
import android.widget.Button
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.calculator.databinding.FragmentMainBinding
import dev.androidbroadcast.vbpd.viewBinding
import kotlin.text.contains

class MainFragment : View.OnClickListener, Fragment(R.layout.fragment_main) {

    private val binding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)

    private val actions = listOf("x", "+", "/", "-", "%")

    override fun onStart() {
        super.onStart()
        binding.btnLayout.children.filterIsInstance<Button>().
        forEach { button ->
            button.setOnClickListener(this)
        }
    }

    override fun onClick(view: View?) {
        val button = view as? Button
        button ?: return
        val buttonValue = button.text.toString()
        val displayText = binding.displayActive.text.toString()
        val backgroundText = binding.displayBackground.text.toString()
        val operation = binding.displayOperation.text.toString()
        when {
            buttonValue.first().isDigit() -> {
                if (displayText.contains(Regex("[a-zA-Z]")) || displayText.length >= 15)
                    return
                binding.displayActive.text = displayText + buttonValue
            }
            button.id == R.id.btn_clear -> {
                binding.displayActive.text = null
                binding.displayBackground.text = null
                binding.displayOperation.text = null
            }
            button.id == R.id.btn_delete -> {
                if (displayText.isEmpty()){
                    binding.displayActive.text = backgroundText
                    binding.displayBackground.text = null
                    binding.displayOperation.text = null
                }
                else {
                    binding.displayActive.text = displayText.substring(0, displayText.length-1)
                }
            }
            actions.contains(buttonValue) -> {
                if (buttonValue.equals("-") && displayText.isEmpty()) {
                    binding.displayActive.text = buttonValue
                    return
                }
                if (operation.isEmpty() && canBeParsed(displayText)) {
                    binding.displayOperation.text = buttonValue
                    binding.displayBackground.text = displayText
                    binding.displayActive.text = null
                }
            }
            button.id == R.id.btn_equals -> {
                if (backgroundText.isEmpty() || operation.isEmpty() || !canBeParsed(displayText))
                    return
                binding.displayActive.text = calculate(operation, backgroundText, displayText)
                                            .toString()
                                            .replace(".",",")
                binding.displayBackground.text = null
                binding.displayOperation.text = null
            }
            button.id == R.id.btn_period -> {
                if(displayText.contains(Regex("^-*[0-9]+(?!,)\$")))
                    binding.displayActive.text = displayText + buttonValue
            }
        }
    }
    fun canBeParsed(text: String) : Boolean{
        return text.contains(Regex("^-*[0-9]*,*[0-9E]+\$"))
    }

    fun calculate(operation: String, operand1: String, operand2: String) : Double{
        val num1 = operand1.replace(",",".").toDouble()
        val num2 = operand2.replace(",",".").toDouble()
        when {
            operation == "-" -> {
                return num1-num2
            }
            operation == "+" -> {
                return num1+num2
            }
            operation == "x" -> {
                return num1*num2
            }
            operation == "/" -> {
                if (num2 <= 1E-10)
                    return Double.NaN
                return num1/num2
            }
            operation == "%" -> {
                return num1/100 * num2
            }
        }
        throw IllegalArgumentException("Unsupported operation")
    }
}