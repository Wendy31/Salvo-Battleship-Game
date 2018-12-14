package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    private Set<GamePlayer> gamePlayerSet;

    private String userName;


    // Constructors x2
    public Player() { }

    public Player(String userName) {
        this.userName = userName;
    }


    // method
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

    public void addGamePlayer(GamePlayer gamePlayer) { // adds gamePlayer to gamePlayer list
        gamePlayer.setPlayer(this);
        gamePlayerSet.add(gamePlayer);
    }

    @JsonIgnore
    public List<Player> getPlayers() { // returns a list of players playing in a game
        return gamePlayerSet.stream().map(sub -> sub.getPlayer()).collect(toList());
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", gamePlayerSet=" + gamePlayerSet +
                ", userName='" + userName + '\'' +
                '}';
    }
}