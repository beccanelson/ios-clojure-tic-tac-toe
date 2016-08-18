package application.game;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import tic_tac_toe.board$check_each_marker_for_win;
import tic_tac_toe.board$check_each_set_of_possible_moves;
import tic_tac_toe.board$tie_QMARK_;

public abstract class Game {
    public String[] board;
    private String marker;

    public Game(String[] board, String marker, String computerDifficulty) {
        this.board = board;
        this.marker = marker;
        IFn require = Clojure.var("clojure.core", "require");
        require.invoke(Clojure.read("tic-tac-toe.board"));
    }

    public Boolean hasWinner() {
        return (Boolean) board$check_each_marker_for_win.invokeStatic(board);
    }

    public Boolean isWinner(String marker) {
        return (Boolean) board$check_each_set_of_possible_moves.invokeStatic(board, marker);
    }

    public Boolean isTie() {
        return (Boolean) board$tie_QMARK_.invokeStatic(board);
    }

    public String getStatus() {
        if (this.isWinner("X")) {
            return "player1Wins";
        } else if (this.isWinner("O")) {
            return "player2Wins";
        } else if (this.isTie()) {
            return "tie";
        } else {
            return "in progress";
        }
    }

    public String[] getBoard() {
        return board;
    }
}