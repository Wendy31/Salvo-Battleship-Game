package com.codeoftheweb.salvo;

// sends data to front end, JS displays data
// controls what users want to see. Controls flow of data.
// controls what user can see, methods to be executed

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;

@RestController // gets data on request and returns a data (JSON) from GameRepo
@RequestMapping("/api") // avoids URL name conflicts
public class SalvoController {


    //..................................Game Info....................................//
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
        Map<String, Object> getGameInfo = new LinkedHashMap<>(); // map called getGameInfo
        getGameInfo.put("id", game.getId()); // put Key ID
        getGameInfo.put("created", game.getDate()); // put Key Created
        getGameInfo.put("gamePlayers", game.getGamePlayers() // gamePlayers has all game players
                .stream() // loop thru all games
                .map(gamePlayer -> getGamePlayerInfo(gamePlayer)) // gamePlayers has gamePlayer. For each gamePlayer do
                // function and mutate gamePlayer.
                .collect(toList()));
        getGameInfo.put("scores", game.getScores()
                .stream()
                .map(score -> getScoreInfo(score))
                .collect(toList()));
        return getGameInfo; // return Map object
    }

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GameRepository gameRepo; // variable that stores data from GameRepo

    private Map<String, Object> getGamePlayerInfo(GamePlayer gamePlayer) {
        Map<String, Object> getGamePlayerInfo = new LinkedHashMap<>();
        getGamePlayerInfo.put("id", gamePlayer.getId()); // gets gamePlayer Id
        getGamePlayerInfo.put("player", getPlayerInfo(gamePlayer.getPlayer()));
        return getGamePlayerInfo;
    }

    private Map<String, Object> getPlayerInfo(Player player) {
        Map<String, Object> getPlayerInfo = new LinkedHashMap<>();
        getPlayerInfo.put("id", player.getId());
        getPlayerInfo.put("email", player.getUserName());
        return getPlayerInfo;
    }

    private Map<String, Object> getScoreInfo(Score score) {
        Map<String, Object> getScoreInfo = new LinkedHashMap<>();
        getScoreInfo.put("name", score.getPlayer().getUserName());
        getScoreInfo.put("score", score.getScore());
        return getScoreInfo;
    }


    //............................Game View Info..................................//
    @Autowired
    private GamePlayerRepository gamePlayerRepo; // data is accessed from GamePlayerRepo

    @RequestMapping("/game_view/{gamePlayerId}")
    private Map<String, Object> findGamePlayersInOneGame(@PathVariable Long gamePlayerId) { //Annotation extracts desired game player ID from URL
        GamePlayer gamePlayer = gamePlayerRepo.getOne(gamePlayerId);
        Map<String, Object> getGameView = new LinkedHashMap<>(); // creates gameView object with given data
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
        getGameView.put("salvoes", getSalvoInfo(gamePlayer.getGame().getGamePlayers() //
                .stream()
                .map(gp -> gp.getSalvoes())
                .flatMap(salvoes -> salvoes.stream())
                .collect(Collectors.toSet())));// GP has set of ships
        return getGameView;
    }

    private Map<String, Object> getCurrentGPShip(Ship ship) {
        Map<String, Object> getCurrentGPShip = new HashMap<>();
        getCurrentGPShip.put("type", ship.getShipType());
        getCurrentGPShip.put("locations", ship.getLocation());
        return getCurrentGPShip;
    }

    private Map<Long, Map> getSalvoInfo(Set<Salvo> salvos) { // Map with Long (id) as key, with another Map inside
        Map<Long, Map> getSalvoInfo = new HashMap<>();
        Map<String, List<String>> salvoTurnAndLocation; // second Map is String (turn) with list of string values (locations)
        for (Salvo salvo : salvos) { // loop thru all salvo in salvoes
            if (!getSalvoInfo.containsKey(salvo.getGamePlayer().getId())) { // if main map does not contain key ID
                salvoTurnAndLocation = new HashMap<>(); // make new map
                salvoTurnAndLocation.put(salvo.getTurn(), salvo.getLocation()); // put Turn as key, Location as value
                getSalvoInfo.put(salvo.getGamePlayer().getId(), salvoTurnAndLocation); // and put main map with key ID, and turn/location as value
            } else { // else if map has id of first player, salvoTurnAndLocation-Map becomes main Map with opponent ID
                salvoTurnAndLocation = getSalvoInfo.get(salvo.getGamePlayer().getId());
                salvoTurnAndLocation.put(salvo.getTurn(), salvo.getLocation()); // with Opponent turn/ location info
            }
        }
        return getSalvoInfo;
    }




//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @RequestMapping(path = "/players", method = RequestMethod.POST)
//    public ResponseEntity<Object> register(
//            @RequestParam String userName, @RequestParam String password) {
//
//        if (userName.isEmpty() || password.isEmpty()) {
//            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
//        }
//
//        if (playerRepository.findByUserName(userName) !=  null) {
//            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
//        }
//
//        playerRepository.save(new Player(userName, passwordEncoder.encode(password)));
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }

}
