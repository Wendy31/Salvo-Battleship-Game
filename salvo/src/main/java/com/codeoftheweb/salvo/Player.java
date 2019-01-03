package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    private Set<GamePlayer> gamePlayerSet = new HashSet<>();

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER) // player has set of scores
    private Set<Score> scores = new HashSet<>();

    private String userName;

    private String password;


    // Constructors x2
    public Player() { }

    public Player(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }


    // method from props
    public Set<GamePlayer> getGamePlayerSet() {
        return gamePlayerSet;
    }

    public Long getId(){
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // other methods
    public void addGamePlayer(GamePlayer gamePlayer) { // adds gamePlayer to gamePlayers
        gamePlayerSet.add(gamePlayer);
    }

    public void addScore(Score score) {
        scores.add(score);
    }

    public Score getScore(Game game){ // loop thru scores and filter to get one score from game. if game is equal to (game) find score. if no score then = null.
        return this.scores.stream().filter(score -> score.getGame().equals(game)).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", gamePlayerSet=" + gamePlayerSet +
                ", scores=" + scores +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}