package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
public class GamePlayer {

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

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    // Ship object stored in another data table, JPA connects tables together
    private Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Salvo> salvoes = new HashSet<>();

    private Date date;


    // constructor x2
    public GamePlayer() {
    }

    public GamePlayer(Player player, Game game) { // constructor with player param, and game para.
        this.player = player;
        this.game = game;
        this.date = new Date();
        player.addGamePlayer(this);
        game.addGamePlayer(this);
    }


    // method
    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Player getPlayer() { // gets players from Class Player
        return player;
    }

    public void setPlayer(Player player) { // sets player to GamePlayer
        this.player = player;
    }

    public Game getGame() { // gets games from Class Game
        return game;
    }

    public void setGame(Game game) { // sets games to GamePlayer games
        this.game = game;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public Set<Salvo> getSalvoes() {
        return salvoes;
    }

    public void setSalvoes(Set<Salvo> salvoes) {
        this.salvoes = salvoes;
    }


    // other methods

    public void addShip(Ship ship) {
        ships.add(ship); // a ship is added into list of Ships
    }

    public void addSalvo(Salvo salvo) {
        salvoes.add(salvo); // add one salvo to list of salvos (variable) at a time
    }

//    public Integer getMostRecentTurn(){
//        if (!getSalvoes().isEmpty()){
//            return salvoes.stream()
//                    .map(salvo -> salvo.getTurn())
//                    .max((x, y) -> Integer.compare(x,y))
//                    .get();
//        } else {
//            return 0;
//        }
//    }

    @Override
    public String toString() {
        return "GamePlayer{" +
                "id=" + id +
                ", player=" + player +
                ", game=" + game +
                ", ships=" + ships +
                ", salvoes=" + salvoes +
                ", date=" + date +
                '}';
    }
}
