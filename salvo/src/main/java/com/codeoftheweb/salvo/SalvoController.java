package com.codeoftheweb.salvo;

// sends data to front end, JS displays data
// controls what users want to see. Controls flow of data.
// controls what user can see, methods to be executed

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController // gets data on request and returns a data (JSON) from GameRepo
@RequestMapping("/api") // avoids URL name conflicts
public class SalvoController {


    //..................................Game Info....................................//

    @Autowired
    private GameRepository gameRepo; // variable that stores data from GameRepo

    @RequestMapping("/games")
    private List<Object> getAllGames() { // creates a list of object called "getAllGames"
        return gameRepo.findAll() // gets ALL games from gameRepo
                .stream() // loops thru each game
                .map(game -> getGameInfo(game)) // map mutates game. for each game do the function getGameInfo -> Data
                // Transfer Object (DTO).
                .collect(toList()); // collect info and convert to list
    }

    private Map<String, Object> getGameInfo(Game game) { // method to mutate (change form) game object. Need to pass
        // Game game thru as param.
        Map<String, Object> getGameInfo = new HashMap<>(); // map called getGameInfo
        getGameInfo.put("id", game.getId()); // put Key ID
        getGameInfo.put("created", game.getDate()); // put Key Created
        getGameInfo.put("gamePlayers", game.getGamePlayers() // gamePlayers has all game players
                .stream() // loop thru all games
                .map(gamePlayer -> getGamePlayerInfo(gamePlayer)) // gamePlayers has gamePlayer. For each gamePlayer do
                // function and mutate gamePlayer.
                .collect(toList()));
        return getGameInfo; // return Map object
    }

    private Map<String, Object> getGamePlayerInfo(GamePlayer gamePlayer) {
        Map<String, Object> getGamePlayerInfo = new HashMap<>();
        getGamePlayerInfo.put("id", gamePlayer.getId()); // gets gamePlayer Id
        getGamePlayerInfo.put("player", getPlayerInfo(gamePlayer.getPlayer())); // Key player + add function, insert a
        // player in param.
        return getGamePlayerInfo;
    }

    private Map<String, Object> getPlayerInfo(Player player) {
        Map<String, Object> getPlayerInfo = new HashMap<>();
        getPlayerInfo.put("id", player.getId());
        getPlayerInfo.put("email", player.getUserName());
        return getPlayerInfo;
    }


    //............................Game View Info..................................//

    @Autowired
    private GamePlayerRepository gamePlayerRepo; // data is accessed from GamePlayerRepo

    @RequestMapping("/game_view/{gamePlayerId}")
    private Map<String, Object> findGamePlayersInOneGame(@PathVariable Long gamePlayerId) { //Annotation extracts desired game player ID from URL
        GamePlayer gamePlayer = gamePlayerRepo.getOne(gamePlayerId);
        Map<String, Object> getGameView = new HashMap<>(); // creates gameView object with given data
        getGameView.put("id", gamePlayer.getGame().getId()); // start point from gamePlayer, to access Game, then gameID
        getGameView.put("created", gamePlayer.getGame().getDate());
        getGameView.put("gamePlayers", gamePlayer.getGame().getGamePlayers()
                .stream()
                .map(currentGamePlayers -> getGamePlayerInfo(currentGamePlayers)) // currentGP in GamePlayer method
                .collect(toList()));
        getGameView.put("ships", gamePlayer.getShips() // GP has set of ships
                .stream() // loop thru each ship
                .map(ship -> getCurrentGPShip(ship)) // map and make another object using method below
                .collect(toList()));
        getGameView.put("salvoes", gamePlayer.getSalvoes() // GP has set of ships
                .stream() // loop thru each salvo
                .map(salvo -> getSalvoInfo(salvo)) // map and make another object using method below
                .collect(toList()));
        return getGameView;
    }

    private Map<String, Object> getCurrentGPShip(Ship ship) {
        Map<String, Object> getCurrentGPShip = new HashMap<>();
        getCurrentGPShip.put("type", ship.getShipType());
        getCurrentGPShip.put("locations", ship.getLocation());
        return getCurrentGPShip;
    }


    private Map<String, Object> getSalvoInfo(Salvo salvo) {
        Map<String, Object> getSalvoInfo = new HashMap<>();
        getSalvoInfo.put("id", salvo.getGamePlayer().getId()
                .stream()
                .map(salvo -> getTurnAndLocation(salvo))
                .collect(toList()));
        return getSalvoInfo;
    }

    private Map<String, Object> getTurnAndLocation(Salvo salvo) {
        Map<String, Object> getTurnAndLocation = new HashMap<>();
        getTurnAndLocation.put("turn", salvo.getTurn());
        getTurnAndLocation.put("locations", salvo.getLocation());
        return getTurnAndLocation;
    }


}
