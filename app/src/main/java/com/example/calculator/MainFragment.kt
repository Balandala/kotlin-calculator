package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.calculator.databinding.FragmentMainBinding
import dev.androidbroadcast.vbpd.viewBinding

class MainFragment : View.OnClickListener, Fragment(R.layout.fragment_main) {

    private val binding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)

    private val actions = listOf("X", "+", "/", "—", "%")

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
        when {
            buttonValue.first().isDigit() -> {
                val displayText = binding.displayActive.text.toString()
                binding.displayActive.text = displayText + buttonValue
            }
            equals(binding.btnClear) -> {
                binding.displayActive.text = null
                binding.displayBackground.text = null
                binding.displayOperation.text = null
            }
            actions.contains(buttonValue) -> { /* It's action button, perform this */ }
            equals(binding.btnEquals) -> { /* It's '=' button, solve the equation */ }
            buttonValue == "," -> {

            }
            else -> { /* And more more actions ;) */ }
        }
    }
}