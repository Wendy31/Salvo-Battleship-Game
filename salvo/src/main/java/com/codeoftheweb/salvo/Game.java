package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Date;
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

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<GamePlayer> gamePlayers; // gamePlayers has all GamePlayer in a list Set (no dublicates)

    private Date date;

    // constructor x1
    public Game() {
        this.date = new Date();
    }

    // methods
    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Long getId(){
        return this.id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void addGamePlayer(GamePlayer gamePlayer) { // adds a game player to a Game List
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }

    @JsonIgnore
    public List<Game> getGames() { // returns a list of games being played
        return gamePlayers.stream().map(sub -> sub.getGame()).collect(toList());
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", gamePlayers=" + gamePlayers +
                ", date=" + date +
                '}';
    }
}


