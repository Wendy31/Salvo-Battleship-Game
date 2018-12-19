package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toList;


@Entity
public class Game {

    // props
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers = new HashSet<>(); // gamePlayers has all GamePlayer in a list Set (no dublicates)

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER) // game has set of scores
    private Set<Score> scores = new HashSet<>();

    private Date date;


    // constructor x1
    public Game() {
        this.date = new Date();
    }


    // methods from props
    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Long getId() {
        return this.id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }


    // other methods
    public void addGamePlayer(GamePlayer gamePlayer) { // adds a game player to a Game List
        gamePlayers.add(gamePlayer);
    }

    public void addScore(Score score) {
        scores.add(score);
    }

    @JsonIgnore
    public List<Player> getPlayers() { // from gamePlayers loop to get Player and get list
        return gamePlayers.stream().map(gamePlayer -> gamePlayer.getPlayer()).collect(toList());
    }


    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", gamePlayers=" + gamePlayers +
                ", scoreSet=" + scores +
                ", date=" + date +
                '}';
    }
}


