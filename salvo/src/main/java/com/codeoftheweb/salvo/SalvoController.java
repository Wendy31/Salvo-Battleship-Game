package com.codeoftheweb.salvo;

// sends data to front end, JS displays data
// controls what users want to see. Controls flow of data.
// controls what user can see, methods to be executed

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RestController // gets data on request and returns a data (JSON) from GameRepo
@RequestMapping("/api") // avoids URL name conflicts
public class SalvoController {

    @Autowired // autowired creates and instance of a class, to be used in another class.
    private PlayerRepository playerRepository;
    @Autowired
    private GameRepository gameRepo;
    @Autowired
    private GamePlayerRepository gamePlayerRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ShipRepository shipRepository;
    @Autowired
    private SalvoRepository salvoRepository;


    //..................................Game Info....................................//
    @RequestMapping("/games")
    private Map<String, Object> getAllGames(Authentication authentication) {
        Map<String, Object> getAllGamesDetails = new LinkedHashMap<>();

        if (!isGuest(authentication)) { // if user is guest or player
            Player player = playerRepository.findByUserName(authentication.getName()); // gets name from auth object
            getAllGamesDetails.put("player", getPlayerInfo(player)); // adds info from getPlayerInfo method
        } else {
            getAllGamesDetails.put("player", "Guest");
        }
        getAllGamesDetails.put("games", gameRepo.findAll() // gets ALL games from gameRepo
                .stream() // loops thru each game
                .map(game -> getGameInfo(game)) // map mutates game. for each game do the function getGameInfo -> Data Transfer Object (DTO).
                .collect(toList())); // collect info and convert to list
        return getAllGamesDetails;
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
    @RequestMapping("/game_view/{gamePlayerId}")
    private ResponseEntity<Map<String, Object>> findGamePlayersInOneGame(@PathVariable Long gamePlayerId, Authentication authentication) { //Annotation extracts desired game player ID from URL
        Map<String, Object> getGameView = new LinkedHashMap<>(); // creates gameView object with given data

        if (!isGuest(authentication)) { // if user if logged in
            GamePlayer gamePlayer = gamePlayerRepo.getOne(gamePlayerId);
            Player player = playerRepository.findByUserName(authentication.getName());

            if (player.getId() == gamePlayer.getPlayer().getId()) { // AND user id match with gp ID
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
                getGameView.put("opponent_hits", getHostHits(gamePlayer));

                return new ResponseEntity<>(getGameView, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(makeMap("error", "You can not view your opponent's game information."), HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(makeMap("error", "You are not logged in."), HttpStatus.UNAUTHORIZED);
        }
    }

    private Map<String, Object> getHostHits(GamePlayer gamePlayer) {
        Map<String, Object> getHostHits = new HashMap<>();

//        Set<Ship> ships = gamePlayer.getOpponentInfo().getShips(); // has a set of ships (set does not allow dublicates like lists)
//        Stream<Ship> streamOfShips = ships.stream(); // uses class stream to perform functions like map, filter etc
//        Stream<List<String>> streamOflistOfShips = streamOfShips.map(ship -> ship.getLocation()); // a stream of a list with strings of locations
//        List<String> listOflistOfShips = streamOflistOfShips
//                .flatMap(pos-> pos.stream())
//                .collect(toList()); // list of lists, that is converted back to a list

        List<String> oppShipPositions = gamePlayer.getOpponentInfo().getShips() // list of opponent's ships
                .stream()
                .map(ship -> ship.getLocation())
                .flatMap(location -> location.stream())
                .collect(toList());

        List<String> hostSalvoes = gamePlayer.getSalvoes() // list of host salvoes
                .stream()
                .map(salvo1 -> salvo1.getLocation())
                .flatMap(loc -> loc.stream())
                .collect(toList());

        List<String> hits = new ArrayList<>(); // if opp ship locations have host salvoes, add to array
        for (String salvoo : hostSalvoes) {
            if (oppShipPositions.contains(salvoo)) {
                hits.add(salvoo);
            }
        }
        getHostHits.put("hits", hits); // put list of hits in a map and return
        return getHostHits;
    }

    private Map<String, Object> getCurrentGPShip(Ship ship) {
        Map<String, Object> getCurrentGPShip = new HashMap<>();
        getCurrentGPShip.put("type", ship.getShipType());
        getCurrentGPShip.put("locations", ship.getLocation());
        return getCurrentGPShip;
    }

    // gets salvoes of current and opponent's
    private Map<Long, Map> getSalvoInfo(Set<Salvo> salvos) { // Map with Long (id) as key, with another Map inside
        Map<Long, Map> getSalvoInfo = new HashMap<>(); // map with objects, inside object a list
        Map<String, List<String>> salvoTurnAndLocation; // second Map is String (turn) with list of string values (locations)
        for (Salvo salvo : salvos) { // loop thru all salvo in salvoes
            if (!getSalvoInfo.containsKey(salvo.getGamePlayer().getId())) { // if not current player
                salvoTurnAndLocation = new HashMap<>(); // make new map
                salvoTurnAndLocation.put(salvo.getTurn().toString(), salvo.getLocation()); // put Turn as key, Location as value
                getSalvoInfo.put(salvo.getGamePlayer().getId(), salvoTurnAndLocation); // and put main map with key ID, and turn/location as value
            } else { // else if map has id of first player, salvoTurnAndLocation-Map becomes main Map with opponent ID
                salvoTurnAndLocation = getSalvoInfo.get(salvo.getGamePlayer().getId()); // get current player
                salvoTurnAndLocation.put(salvo.getTurn().toString(), salvo.getLocation()); // with Opponent turn/ location info
            }
        }
        return getSalvoInfo;
    }


    //............................Getting current user info..................................//
    private Map<String, Object> getCurrentUser(Authentication authentication) {
        return (Map<String, Object>) playerRepository.findByUserName(authentication.getName());
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }


    //............................adds new player..................................//
    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createNewUser(@RequestParam String userName, @RequestParam String
            password) // takes 2 params
    {
        if (userName.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "Missing data"), HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByUserName(userName) != null) {
            return new ResponseEntity<>(makeMap("error", "Username already exists"), HttpStatus.FORBIDDEN);
        }

        Player newPlayer = playerRepository.save(new Player(userName, passwordEncoder.encode(password)));
        return new ResponseEntity<>(makeMap("player", newPlayer.getUserName()), HttpStatus.CREATED);
    }

    //...................................make map method....................................//
    private Map<String, Object> makeMap(String key, Object value) { // method to create new map
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    //............................adds new game..................................//
    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createNewGame(Authentication authentication) {

        if (isGuest(authentication)) { // if user not logged in
            return new ResponseEntity<>(makeMap("error", "You are not logged in."), HttpStatus.UNAUTHORIZED);

        } else {

            Player loggedPlayer = playerRepository.findByUserName(authentication.getName());
            Game newGame = gameRepo.save(new Game());

            GamePlayer newGP = gamePlayerRepo.save(new GamePlayer(loggedPlayer, newGame)); // save new GP with new user and game info

            return new ResponseEntity<>(makeMap("gpid", newGP.getId()), HttpStatus.CREATED);
        }
    }


    //............................join an existing game..................................//
    @RequestMapping(path = "/game_view/{gameId}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long gameId, Authentication authentication) {

        if (isGuest(authentication)) { // if user not logged in
            return new ResponseEntity<>(makeMap("error", "You are not logged in."), HttpStatus.UNAUTHORIZED);

        } else {

            Player loggedPlayer = playerRepository.findByUserName(authentication.getName()); // gets current user

            Game availableGame = gameRepo.getOne(gameId); // gets a game

            if (availableGame == null) {
                return new ResponseEntity<>(makeMap("error", "No such game."), HttpStatus.FORBIDDEN);
            }

            if (availableGame.getGamePlayers().size() == 1) { // a set of GPs length is 1
                GamePlayer newGP = gamePlayerRepo.save(new GamePlayer(loggedPlayer, availableGame)); // save new GP with new user and game info

                return new ResponseEntity<>(makeMap("gpid", newGP.getId()), HttpStatus.CREATED);

            } else {
                return new ResponseEntity<>(makeMap("error", "Game is full."), HttpStatus.FORBIDDEN);
            }
        }
    }

    //............................adds new ships and locations to GP and shipRepo..................................//
    @RequestMapping(value = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addShips(@PathVariable long gamePlayerId, @RequestBody Set<Ship> ships, Authentication authentication) {

        Player loggedPlayer = playerRepository.findByUserName(authentication.getName());
        GamePlayer gamePlayer = gamePlayerRepo.getOne(gamePlayerId);

        if (isGuest(authentication)) { // if user not logged in
            return new ResponseEntity<>(makeMap("error", "You are not logged in."), HttpStatus.UNAUTHORIZED);

        } else if (gamePlayer == null) { // if no gamePlayer with the given ID
            return new ResponseEntity<>(makeMap("error", "No such game player."), HttpStatus.UNAUTHORIZED);

        } else if (!loggedPlayer.getId().equals(gamePlayer.getPlayer().getId())) { // if logged in player does NOT equal gamePlayer's player ID
            return new ResponseEntity<>(makeMap("error", "No such game player."), HttpStatus.UNAUTHORIZED);

        } else if (!gamePlayer.getShips().isEmpty()) { // if user already has ships
            return new ResponseEntity<>(makeMap("error", "You have ships."), HttpStatus.FORBIDDEN);
        }

        for (Ship ship : ships) { // loop thru a set of ships. For each ship, add to gamePlayer and save to shipRepo with 3 params (as per the constructor)
            gamePlayer.addShip(ship);
            shipRepository.save(new Ship(ship.getShipType(), ship.getLocation(), gamePlayer));
        }
        return new ResponseEntity<>(makeMap("success", "ships placed"), HttpStatus.CREATED);
    }


    //............................salvoes and locations to GP and salvoRepo..................................//
    @RequestMapping(value = "/games/players/{gamePlayerId}/salvoes", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addSalvoes(@PathVariable long gamePlayerId, @RequestBody Salvo salvo, Authentication authentication) {

        Player loggedPlayer = playerRepository.findByUserName(authentication.getName());
        GamePlayer gamePlayer = gamePlayerRepo.getOne(gamePlayerId);

        if (isGuest(authentication)) { // if user not logged in
            return new ResponseEntity<>(makeMap("error", "You are not logged in."), HttpStatus.UNAUTHORIZED);

        } else if (gamePlayer == null) { // if no gamePlayer with the given ID
            return new ResponseEntity<>(makeMap("error", "No such game player."), HttpStatus.UNAUTHORIZED);

        } else if (!loggedPlayer.getId().equals(gamePlayer.getPlayer().getId())) { // if logged in player does NOT equal gamePlayer's player ID
            return new ResponseEntity<>(makeMap("error", "No such game player."), HttpStatus.UNAUTHORIZED);

        } else if (salvo.getLocation().size() != 5) { // if user already has salvoes
            return new ResponseEntity<>(makeMap("error", "You must have 5 salvoes to play."), HttpStatus.FORBIDDEN);

        } else {
            gamePlayer.addSalvo(salvo);
            salvoRepository.save(new Salvo(gamePlayer.getSalvoes().size(), salvo.getLocation(), gamePlayer)); // For each salvo, add to gamePlayer and save to salvoRepo with 3 params (as per the constructor)
            return new ResponseEntity<>(makeMap("success", "Salvoes placed"), HttpStatus.CREATED); // save new Salvo (the most recent turn, location, and gamePLayer)
        }
    }

}