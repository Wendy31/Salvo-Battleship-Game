package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Date;


@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) // can have many players to One game
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER) // can have many games One player has played
    @JoinColumn(name = "game_id")
    private Game game;

    private Date finishDate;

    private Double score;


    // conductor
    public Score() {
    }

    public Score(Player player, Game game, Double score) {
        this.finishDate = new Date();
        this.player = player;
        this.game = game;
        this.score = score;
        player.addScore(this); // call function in param. add score to player
        game.addScore(this); // add score to game
    }


    // method
    public Long getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Score{" +
                "id=" + id +
                ", player=" + player +
                ", game=" + game +
                ", finishDate=" + finishDate +
                ", score=" + score +
                '}';
    }
}

