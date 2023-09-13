package com.example.myapplication

import android.content.res.Resources
import android.health.connect.datatypes.units.Length
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var roll_button:Button;
    private lateinit var keep_button:Button;
    private lateinit var dice_image:ImageView;
    private lateinit var new_button:Button;
    private lateinit var active_player_text:TextView;
    private lateinit var current_round_score_text:TextView;
    private lateinit var score_player1_text:TextView;
    private lateinit var score_player2_text:TextView;
    private var current_round_score = 0;
    private lateinit var active_player:Player;
    private val diceImages = arrayOf(
        R.drawable.dice_1,
        R.drawable.dice_2,
        R.drawable.dice_3,
        R.drawable.dice_4,
        R.drawable.dice_5,
        R.drawable.dice_6
    )
    val dice = Dice(diceImages.size)
    val Players= setOf(Player("Player 1"), Player("Player 2"))
    var iterator = Players.iterator()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        change_active_player()
        find_all_views()
        desactivateButton(keep_button, new_button)
        refreshUI()
        createScoreView()
        setListeners()
    }

    private fun setListeners(){
        roll_button.setOnClickListener{
            current_round_score +=rollTheDice()
        }
        keep_button.setOnClickListener{
            keepTheDice()
        }
        new_button.setOnClickListener{
            roll_button.isEnabled = true
            keep_button.isEnabled = false
            new_button.isEnabled = false
        }
    }

    private fun find_all_views(){
        roll_button = findViewById(R.id.roll_button)
        keep_button = findViewById(R.id.keep_button)
        new_button = findViewById(R.id.new_button)
        active_player_text = findViewById(R.id.active_player_text)
        active_player_text = findViewById(R.id.active_player_text)
        dice_image = findViewById(R.id.dice_image)
    }

    private fun desactivateButton(vararg buttons: Button){
        for (button in buttons){
            button.isEnabled = false
        }
    }
    private fun activateButton(vararg buttons: Button){
        for (button in buttons){
            button.isEnabled = true
        }
    }

    private fun  refreshUI(){
        active_player_text.text = active_player.name
    }

    private fun createScoreView(){
        val container = findViewById<LinearLayout>(R.id.container)
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        val textViewHeight = screenHeight / Players.size

        for (player in Players) {
            val textView = TextView(this)
            textView.text = player.name
            textView.gravity = Gravity.CENTER
            textView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                textViewHeight
            )
            container.addView(textView)
        }
    }

    private fun showToast(text:String, length: Int =0){
        val toast = Toast.makeText(this, text, length)
        toast.show()
    }
    private fun keepTheDice() {
        showToast("dice kept!")
        roll_button.isEnabled = true
        keep_button.isEnabled = false
        new_button.isEnabled = false
        active_player.score += current_round_score
        current_round_score = 0
        change_active_player()
        println("ta mere")
        refreshUI()
    }

    private fun rollTheDice(): Int {
        showToast("dice rolled!")
        val result = dice.rollTheDice()
        dice_image.setImageResource(diceImages[result - 1])
        desactivateButton(roll_button)
        activateButton(keep_button, new_button)
        if (result == 1){
            current_round_score = 0
            keepTheDice()
        }
        else{
            refreshUI()
        }
        return result;
    }
    fun change_active_player(){
        active_player = if (iterator.hasNext()) iterator.next() else Players.first();
    }


}




class Player(name:String){
    companion object{
        private var dice = Dice(6)
    }
    var score = 0;
    var name = name;

}
class Dice (nb_de_faces: Int){
    private val nb_de_faces = nb_de_faces
    fun rollTheDice(): Int {
        return Random.nextInt(0, nb_de_faces);
    }

}