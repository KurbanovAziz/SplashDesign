package com.example.splashdesign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.example.splashdesign.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var balance = 1000
    private var bet = 10
    private var win = 0

    private val slotIcons = listOf(
        R.drawable.ic_icon1,
        R.drawable.ic_icon2,
        R.drawable.ic_icon3,
        R.drawable.ic_icon4,
        R.drawable.ic_icon5,
        R.drawable.ic_icon6,
        R.drawable.ic_icon7,
        R.drawable.ic_icon8
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateBalanceText()

        binding.buttonSpin.setOnClickListener {
            spin()
        }
    }

    private fun spin() {
        if (balance < bet) {
            // not enough balance to make a bet
            return
        }

        balance -= bet
        updateBalanceText()

        val slot1 = Random.nextInt(slotIcons.size)
        val slot2 = Random.nextInt(slotIcons.size)
        val slot3 = Random.nextInt(slotIcons.size)

        val animation = AnimationUtils.loadAnimation(this, R.anim.slot_spin)

        binding.imageSlot1.startAnimation(animation)
        binding.imageSlot2.startAnimation(animation)
        binding.imageSlot3.startAnimation(animation)

        Handler(Looper.getMainLooper()).postDelayed({
            binding.imageSlot1.clearAnimation()
            binding.imageSlot2.clearAnimation()
            binding.imageSlot3.clearAnimation()
            binding.imageSlot1.setImageDrawable(ContextCompat.getDrawable(this, slotIcons[slot1]))
            binding.imageSlot2.setImageDrawable(ContextCompat.getDrawable(this, slotIcons[slot2]))
            binding.imageSlot3.setImageDrawable(ContextCompat.getDrawable(this, slotIcons[slot3]))

            checkWin(slot1, slot2, slot3)

        }, 1000)
    }

    private fun checkWin(slot1: Int, slot2: Int, slot3: Int) {
        val multipliers = mapOf(
            Pair(0, 2.5),
            Pair(1, 2.5),
            Pair(2, 2.5),
            Pair(3, 10.0),
            Pair(4, 10.0),
            Pair(5, 10.0),
            Pair(6, 2.5),
            Pair(7, 2.5)
        )

        var multiplier = 0.0

        if (slot1 == slot2 && slot2 == slot3) {
            // all slots are the same
            multiplier = multipliers[slot1]!!
        } else if (slot1 == slot2 || slot1 == slot3 || slot2 == slot3) {
            // two slots are the same
            multiplier = 1.0
        }

        win = (bet * multiplier).toInt()
        balance += win

        updateWinText()
        updateBalanceText()
    }

    private fun updateWinText() {
        binding.textWin.text = "Win: $win"
    }

    private fun updateBalanceText() {
        binding.textBalance.text = "Balance: $balance"
    }
}