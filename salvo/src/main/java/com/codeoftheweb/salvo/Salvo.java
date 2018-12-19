package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.HashSet;
import java.util.List;

@Entity // maps a class to a table
public class Salvo {

    @Id
    @GenericGenerator(name = "native", strategy = "native")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) // connects to GamePlayer object using JPA
    @JoinColumn(name="gamePlayerID")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name = "salvoLocation")
    private List<String> location;

    private String turn;


    // constructor
    public Salvo() {
    }

    public Salvo(String turn, List<String> location, GamePlayer gamePlayer) {
        this.turn = turn;
        this.location = location;
        this.gamePlayer = gamePlayer;
        gamePlayer.addSalvo(this); // constructor has 1 gamePlayer that will pass this salvo object thru
    }


    // method
    public Long getId() {
        return id;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public List<String> getLocation() {
        return location;
    }

    public void setLocation(List<String> location) {
        this.location = location;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }


    @Override
    public String toString() {
        return "Salvo{" +
                "id=" + id +
                ", gamePlayer=" + gamePlayer +
                ", location=" + location +
                ", turn='" + turn + '\'' +
                '}';
    }
}
