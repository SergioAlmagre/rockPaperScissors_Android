package com.example.rockpaperscissors_android

import Api.ServiceBuilder
import Api.UserAPI
import InterConnection.InterConnections
import Model.Data
import Model.User
import Model.Result
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.rockpaperscissors_android.databinding.ActivityGameBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import Model.Game
import Model.RecoveryGame
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    lateinit var binding: ActivityGameBinding
    var allFigures = arrayListOf<String>("paper", "stone", "scissors")
    var figure = ""
    var randomFigures = ""
    var player1Value = 0
    var player1Points = 0
    var player2Value = 0
    var player2Points = 0
    var context = this
    var maxGameNumberInt = 0
    var idHand = 1
    var job = GlobalScope
    var player1Win = 0
    var player2Win = 0
    var gameOver = 0
    var playerWinner = 0
    var recoveryGame:RecoveryGame? = null

    //Declare the variables for users from Intent before on check with hasExtra to not problems with scope.
    var player1: User? = null
    var player2 = InterConnections.machine
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_game)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ask with hasExtra if "actualUser" or "guestUser" is in the intent.
        if (intent.hasExtra("actualUser")) {
            player1 = intent.getSerializableExtra("actualUser") as User
        } else if (intent.hasExtra("guestUser")) {
            player1 = intent.getSerializableExtra("guestUser") as User
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
        newResult()

        maxGameNumber()
        if(maxGameNumberInt == 0){
            maxGameNumberInt = 1
        }


        binding.toolbarGame.title = "    "
        binding.toolbarGame.subtitle = "    "
        //        binding.toolbarUserDetails.setLogo(R.drawable.ic_logo)

        setSupportActionBar(binding.toolbarGame)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarGame.setNavigationOnClickListener {
            finish()
        }

        binding.btnStart.setOnClickListener {

            gameStarted(player1!!.userId!!)

            if (player1Points < 5 || player2Points < 5) {
                CoroutineScope(Dispatchers.Main).launch {
                    figure = throwAutomaticHand(context)
                    calculatePoins()
                    addMovementDb()
                    checkWinner()
                }
            }
        }
    }

    fun personalizedActivity() {
//        var actualGame = LLAMADA A PARTIDA
        binding.lblNamePlayer.text = player1!!.userName
        binding.lblMachine.text = player2.userName
        binding.lblPointsPlayer.text = player1Points.toString()
        binding.lblPointsMachine.text = player2Points.toString()
    }

    fun maxGameNumber() {
            val request = ServiceBuilder.buildService(UserAPI::class.java)
            val call = request.getMaxGameId()

            call.enqueue(object : Callback<Data> {
                override fun onResponse(call: Call<Data>, response: Response<Data>) {
                    val post = response.body()
                    if (response.isSuccessful && post != null) {
                        maxGameNumberInt = post.maxGameId
                        if(maxGameNumberInt == 0){
                            maxGameNumberInt = 1
                        }

                    } else {
//                        Toast.makeText(context, "No idGameMax found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Data>, t: Throwable) {

                }
            })
    }

    fun addMovementDb() {
        val request = Api.ServiceBuilder.buildService(Api.UserAPI::class.java)
        var movements = ArrayList<Game>()

        var newGame1 = Game(-1,
            maxGameNumberInt,
            player1!!.userId, idHand, player1Win
        )
        Log.e("MaxGameNumber", newGame1.toString())
        Log.e("MaxGameNumber",maxGameNumberInt.toString())
        var newGame2 = Game(-1,
            maxGameNumberInt,
            player2!!.userId, idHand, player2Win
        )

        movements.add(newGame1)
        movements.add(newGame2)

        for (i in movements) {
            val call = request.newGame(i)

            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                )
                {
                    if (response.isSuccessful) {
//                        Toast.makeText(context, "Movement added", android.widget.Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error to add movement", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Error de conexión
                    android.widget.Toast.makeText(
                        context,
                        "Connection error",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    /**
     * @param idHand is controller from checkWinner() function.
     * increment idHand for each hand until that one player get 5 points
     * a new game is created and the idHand is reset to 1.
     */
    fun checkWinner() {
        if (player1Points == 5) {
            Toast.makeText(this, "You win this game", Toast.LENGTH_SHORT).show()
            gameOver = 1
            playerWinner = player1!!.userId!!
            newResult()
            newGame()
        } else if (player2Points == 5) {
            Toast.makeText(this, "You lose this game", Toast.LENGTH_SHORT).show()
            gameOver = 1
            playerWinner = player2.userId!!
            newResult()
            newGame()
        }
        idHand++
        gameOver = 0
    }

    fun newResult() {
        var newResult = Result(-1, playerWinner, gameOver)
        val request = Api.ServiceBuilder.buildService(Api.UserAPI::class.java)
        val call = request.newResult(newResult)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            )
            {
                if (response.isSuccessful) {
                        Toast.makeText(context, "Result added", android.widget.Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Error to add result", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Error de conexión
                android.widget.Toast.makeText(
                    context,
                    "Connection error",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    /**
     * @param maxId is controller from newGame() function.
     * this allows to create a new game with the id of the last game + 1.
     */
    fun newGame() {
        player1Points = 0
        player2Points = 0
        idHand = 1
        binding.lblPointsPlayer.text = player1Points.toString()
        binding.lblPointsMachine.text = player2Points.toString()
        maxGameNumberInt++
    }

    fun calculatePoins() {
        when (figure) {
            "stone" -> {
                player2Value = 1
            }

            "paper" -> {
                player2Value = 2
            }

            "scissors" -> {
                player2Value = 3
            }
        }

        when (binding.rboGroup.checkedRadioButtonId) {
            R.id.rboStone -> {
                player1Value = 1
            }

            R.id.rboPaper -> {
                player1Value = 2
            }

            R.id.rboScissors -> {
                player1Value = 3
            }
        }

        player1Win = 0
        player2Win = 0

        if (player1Value == player2Value) {
            Toast.makeText(this, "Tie", Toast.LENGTH_SHORT).show()
        } else if (player1Value == 1 && player2Value == 2) {
            Toast.makeText(this, "Loser", Toast.LENGTH_SHORT).show()
            player2Win = 1
            player2Points++
            binding.lblPointsMachine.text = player2Points.toString()
        } else if (player1Value == 1 && player2Value == 3) {
            player1Win = 1
            player1Points++
            binding.lblPointsPlayer.text = player1Points.toString()
            Toast.makeText(this, "Winner", Toast.LENGTH_SHORT).show()
        } else if (player1Value == 2 && player2Value == 1) {
            player1Win = 1
            player1Points++
            binding.lblPointsPlayer.text = player1Points.toString()
            Toast.makeText(this, "Winner", Toast.LENGTH_SHORT).show()
        } else if (player1Value == 2 && player2Value == 3) {
            player2Win = 1
            player2Points++
            binding.lblPointsMachine.text = player2Points.toString()
            Toast.makeText(this, "Loser", Toast.LENGTH_SHORT).show()
        } else if (player1Value == 3 && player2Value == 1) {
            player2Win = 1
            player2Points++
            binding.lblPointsMachine.text = player2Points.toString()
            Toast.makeText(this, "Loser", Toast.LENGTH_SHORT).show()
        } else if (player1Value == 3 && player2Value == 2) {
            player1Win = 1
            player1Points++
            binding.lblPointsPlayer.text = player1Points.toString()
            Toast.makeText(this, "Winner", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun throwAutomaticHand(context: Context): String {
        for (i in 0..20) {
            randomFigures = allFigures[Random.nextInt(0, allFigures.size)]
            val resourceId = context.resources.getIdentifier(randomFigures, "drawable", packageName)
            binding.imageFigures.setImageResource(resourceId)
            // Pausar durante 50 milisegundos
            delay(100)
        }
        return randomFigures
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menugame, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btnMenuNewGame -> {
                newGame()
            }
            R.id.btnMenuGiveUp -> {
                Toast.makeText(this, "Loser", Toast.LENGTH_LONG).show()
            }
            R.id.btnMenuScore -> {
//                var intentV1 = Intent(this, ScoreActivity::class.java)
//                startActivity(intentV1)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun gameStarted(idUser:Int):Boolean{
        var isStarted = false

        val request = Api.ServiceBuilder.buildService(Api.UserAPI::class.java)
        val call = request.recoveryGame(idUser)

        call.enqueue(object : Callback<RecoveryGame> {
            override fun onResponse(call: Call<RecoveryGame>, response: Response<RecoveryGame>) {
                if (response.isSuccessful) {
                    recoveryGame = response.body()
                    isStarted = true
                    Log.e("gameStarted", recoveryGame.toString())
                }
            }
            override fun onFailure(call: Call<RecoveryGame>, t: Throwable) {
                android.widget.Toast.makeText(
                    context,
                    "Connection error",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        })
        return isStarted
    }


}