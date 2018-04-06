package com.example.bwise.tictactoe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button gameGrid[][] = new Button[3][3];
    private Button newGameButton;
    private TextView messageTextView;

    //set up preferences
    private SharedPreferences prefs;

    private int turn;
    private String message;
    private boolean gameOver;
    private String gameString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get references to widgets (buttons/game grid)
        //first row
        gameGrid[0][0] = (Button) findViewById(R.id.square1);
        gameGrid[0][1] = (Button) findViewById(R.id.square2);
        gameGrid[0][2] = (Button) findViewById(R.id.square3);
        //second row
        gameGrid[1][0] = (Button) findViewById(R.id.square4);
        gameGrid[1][1] = (Button) findViewById(R.id.square5);
        gameGrid[1][2] = (Button) findViewById(R.id.square6);
        //third row
        gameGrid[2][0] = (Button) findViewById(R.id.square7);
        gameGrid[2][1] = (Button) findViewById(R.id.square8);
        gameGrid[2][2] = (Button) findViewById(R.id.square9);
        //references to non-game board widgets
        newGameButton = (Button) findViewById(R.id.newGameButton);
        messageTextView = (TextView) findViewById(R.id.messageTextView);


        //get shared preference default

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //set event handlers
        for(int x= 0; x < gameGrid.length; x++){
            for(int y = 0; y < gameGrid[x].length; y++){
                gameGrid[x][y].setOnClickListener(this);
            }
        }

        newGameButton.setOnClickListener(this);

        setStartingValues();

    }//end onCreate

    @Override
    public void onPause() {

        //create game string
        gameString = "";
        String square = "";
        for (int x = 0; x < gameGrid.length; x++) {
            for (int y = 0; y < gameGrid[x].length; y++) {
                square = gameGrid[x][y].getText().toString();
                //use a space for an unused square
                //saves a 9 char string representing the board(X, O, " ")
                if (square.equals("")) {
                    square = " ";
                }
                gameString += square;
            }
        }
        //save the instance variables
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("turn", turn);
        editor.putString("message", message);
        editor.putBoolean("gameOver", gameOver);
        editor.putString("gameString", gameString);
        editor.commit();

        super.onPause();
    }//end onPause

    @Override
    public void onResume() {
        super.onResume();

        //restore the instance variables from the onPause prefs
        turn = prefs.getInt("turn", 1);
        gameOver = prefs.getBoolean("gameOver", false);
        message = prefs.getString("message", "Player X's turn");
        gameString = prefs.getString("gameString", "         ");

        //set the message to restore the squares
        messageTextView.setText(message);

        //use the gameString to restore the squares
        int i = 0;
        for (int x = 0; x < gameGrid.length; x++) {
            for (int y = 0; y < gameGrid[x].length; y++) {
                String square = gameString.substring(i, i + 1);
                gameGrid[x][y].setText(square);
                i++;
            }
        }


    }//end onResume

    public void setStartingValues() {
        turn = 1;
        gameOver = false;
        message = "Player X's turn";
        messageTextView.setText(message);
        //empty 9 char string for onPause
        gameString = "         ";
    }//end setStartingValue

    private void clearGrid() {
        for(int x = 0; x < gameGrid.length; x++){
            for(int y = 0; y < gameGrid[x].length; y++){
                //sets each square to blank
                gameGrid[x][y].setText(" ");
            }
        }
    }//end clearGrid

    private void startNewGame() {
        clearGrid();
        setStartingValues();
    }//end startNewGame


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.newGameButton:
                startNewGame();
                break;
            default:
                if (!gameOver) {
                    Button b = (Button) view;

                    if (b.getText().equals(" ")) {
                        if (turn % 2 !=0) {
                            b.setText("X");
                            message = "Player O's turn";
                        } else {
                            b.setText("O");
                            message = "Player X's turn";
                        }

                        turn++;
                        checkForGameOver();
                    } else {
                        message = "That square is taken. Try again.";
                    }
                }
                messageTextView.setText(message);
        }

    }//end onClick

    private void checkForGameOver() {
        //Check for 3 in a row
        for (int x = 0; x < 3; x++) {
            if (!gameGrid[x][0].getText().equals(" ") &&
                    gameGrid[x][0].getText().equals(gameGrid[x][1].getText()) &&
                    gameGrid[x][1].getText().equals(gameGrid[x][2].getText())
                    ) {
                message = gameGrid[x][0].getText() + " wins!";
                gameOver = true;
                return;
            }
        }

        // Columns
        for (int y = 0; y < 3; y++) {
            if (!gameGrid[0][y].getText().equals(" ") &&
                    gameGrid[0][y].getText().equals(gameGrid[1][y].getText()) &&
                    gameGrid[1][y].getText().equals(gameGrid[2][y].getText())
                    ) {
                message = gameGrid[0][y].getText() + " wins!";
                gameOver = true;
                return;
            }
        }

        // Diagonal 1
        if (!gameGrid[0][0].getText().equals(" ") &&
                gameGrid[0][0].getText().equals(gameGrid[1][1].getText()) &&
                gameGrid[1][1].getText().equals(gameGrid[2][2])
                ) {
            message = gameGrid[0][0].getText() + " wins!";
            gameOver = true;
            return;
        }

        // Diagonal 2
        if (!gameGrid[2][0].getText().equals(" ") &&
                gameGrid[2][0].getText().equals(gameGrid[1][1].getText()) &&
                gameGrid[0][2].getText().equals(gameGrid[1][1].getText())
                ) {
            message = gameGrid[2][0].getText() + " wins!";
            gameOver = true;
            return;
        }

        if (turn > 9) {
            message = "It's a tie!";
            gameOver = true;
            return;
        }

        gameOver = false;
    }

}
