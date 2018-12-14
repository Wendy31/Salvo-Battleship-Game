package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.List;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    // GamePlayer object stored in another data table, you need to add JPA
    // annotations
    // to connect the tables together
    @ManyToOne(fetch = FetchType.EAGER) // connects to GamePlayer object using JPA
    @JoinColumn(name="gamePlayerID")
    private GamePlayer gamePlayer; // list of game players (no duplicates)

    private String shipType;

    @ElementCollection
    @Column(name = "shipLocation")
    private List<String> location;

    // constructor
    public Ship() {
    }

    public Ship(String shipType, List<String> location, GamePlayer gamePlayer) { // a ship will always have a type and locations
        this.shipType = shipType;
        this.location = location;
        this.gamePlayer = gamePlayer;
        gamePlayer.addShip(this); // ship is added to gamePlayer
    }

    // method
    public long getId() {
        return id;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public List<String> getLocation() {
        return location;
    }

    public void setLocation(List<String> location) {
        this.location = location;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    @Override
    public String toString() {
        return "Ship{" + "id=" + id + ", gamePlayers=" + gamePlayer + ", shipType='" + shipType + '\'' + ", location="
                + location + '}';
    }

}