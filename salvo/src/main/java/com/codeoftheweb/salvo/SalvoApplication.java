package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class, args);
    }

    @Bean // returns an object?
    public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository,
            GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository) {
        return (args) -> {
            // ........................create new players.......................//
            Player p1 = new Player("j.bauer@ctu.gov");
            Player p2 = new Player("c.obrian@ctu.gov");
            Player p3 = new Player("kim_bauer@gmail.com");
            Player p4 = new Player("t.almeida@ctu.gov");

            // add players to player repo
            playerRepository.save(p1);
            playerRepository.save(p2);
            playerRepository.save(p3);
            playerRepository.save(p4);

            // ........................creates new games........................//
            Game g1 = new Game();
            Game g2 = new Game();
            Game g3 = new Game();
            Game g4 = new Game();
            Game g5 = new Game();
            Game g6 = new Game();
            Game g7 = new Game();
            Game g8 = new Game();

            g2.setDate(Date.from(g2.getDate().toInstant().plusSeconds(3600)));
            g3.setDate(Date.from(g3.getDate().toInstant().plusSeconds(7200)));

            // adds new games to repo
            gameRepository.save(g1);
            gameRepository.save(g2);
            gameRepository.save(g3);
            gameRepository.save(g4);
            gameRepository.save(g5);
            gameRepository.save(g6);
            gameRepository.save(g7);
            gameRepository.save(g8);

            // ......................create gamePlayers.......................//
            //.... game one ....//
            GamePlayer gp1 = new GamePlayer(p1, g1);
            GamePlayer gp2 = new GamePlayer(p2, g1);

            //.... game two ....//
            GamePlayer gp3 = new GamePlayer(p1, g2);
            GamePlayer gp4 = new GamePlayer(p2, g2);

            //.... game three ....//
            GamePlayer gp5 = new GamePlayer(p2, g3);
            GamePlayer gp6 = new GamePlayer(p4, g3);

            //.... game four ....//
            GamePlayer gp7 = new GamePlayer(p2, g4);
            GamePlayer gp8 = new GamePlayer(p1, g4);

            //.... game five ....//
            GamePlayer gp9 = new GamePlayer(p4, g5);
            GamePlayer gp10 = new GamePlayer(p1, g5);

            //.... game six ....//
            GamePlayer gp11 = new GamePlayer(p3, g6);
            GamePlayer gp12 = new GamePlayer();

            //.... game eight ....//
            GamePlayer gp15 = new GamePlayer(p3, g8);
            GamePlayer gp16 = new GamePlayer(p4, g8);

            // add to repo
            gamePlayerRepository.save(gp1);
            gamePlayerRepository.save(gp2);
            gamePlayerRepository.save(gp3);
            gamePlayerRepository.save(gp4);
            gamePlayerRepository.save(gp5);
            gamePlayerRepository.save(gp6);
            gamePlayerRepository.save(gp7);
            gamePlayerRepository.save(gp8);
            gamePlayerRepository.save(gp9);
            gamePlayerRepository.save(gp10);
            gamePlayerRepository.save(gp11);
            gamePlayerRepository.save(gp12);
            gamePlayerRepository.save(gp15);
            gamePlayerRepository.save(gp16);


            // .......................create ships, ship locations and add ships to gamePlayer........................//
            //.... game one ....//
            Ship ship1 = new Ship("Destroyer", new ArrayList<>(Arrays.asList("H2", "H3", "H4")), gp1);
            Ship ship2 = new Ship("Submarine", new ArrayList<>(Arrays.asList("E1", "F1", "G1")), gp1);
            Ship ship3 = new Ship("Patrol Boat", new ArrayList<>(Arrays.asList("B4", "B5")), gp1);
            Ship ship4 = new Ship("Destroyer", new ArrayList<>(Arrays.asList("B5", "C5", "C5")), gp2);
            Ship ship5 = new Ship("Patrol Boat", new ArrayList<>(Arrays.asList("F1", "F2")), gp2);

            //.... game two ....//
            Ship ship6 = new Ship("Destroyer", new ArrayList<>(Arrays.asList("B5", "C5", "D5")), gp3);
            Ship ship7 = new Ship("Patrol Boat", new ArrayList<>(Arrays.asList("C6", "C7")), gp3);
            Ship ship8 = new Ship("Submarine", new ArrayList<>(Arrays.asList("A2", "A3", "A4")), gp4);
            Ship ship9 = new Ship("Patrol Boat", new ArrayList<>(Arrays.asList("G6", "H6")), gp4);

            //.... game three ....//
            Ship ship10 = new Ship("Destroyer", new ArrayList<>(Arrays.asList("B5", "C5", "D5")), gp5);
            Ship ship11 = new Ship("Patrol Boat", new ArrayList<>(Arrays.asList("C6", "C7")), gp5);
            Ship ship12 = new Ship("Submarine", new ArrayList<>(Arrays.asList("A2", "A3", "A4")), gp6);
            Ship ship13 = new Ship("Patrol Boat", new ArrayList<>(Arrays.asList("G6", "H6")), gp6);

            //.... game four ....//
            Ship ship14 = new Ship("Destroyer", new ArrayList<>(Arrays.asList("B5", "C5", "D5")), gp7);
            Ship ship15 = new Ship("Patrol Boat", new ArrayList<>(Arrays.asList("C6", "C7")), gp7);
            Ship ship16 = new Ship("Submarine", new ArrayList<>(Arrays.asList("A2", "A3", "A4")), gp8);
            Ship ship17 = new Ship("Patrol Boat", new ArrayList<>(Arrays.asList("G6", "H6")), gp8);

            //.... game five ....//
            Ship ship18 = new Ship("Destroyer", new ArrayList<>(Arrays.asList("B5", "C5", "D5")), gp9);
            Ship ship19 = new Ship("Patrol Boat", new ArrayList<>(Arrays.asList("C6", "C7")), gp9);
            Ship ship20 = new Ship("Submarine", new ArrayList<>(Arrays.asList("A2", "A3", "A4")), gp10);
            Ship ship21 = new Ship("Patrol Boat", new ArrayList<>(Arrays.asList("G6", "H6")), gp10);

            //.... game six ....//
            Ship ship22 = new Ship("Destroyer", new ArrayList<>(Arrays.asList("B5", "C5", "D5")), gp11);
            Ship ship23 = new Ship("Patrol Boat", new ArrayList<>(Arrays.asList("C6", "C7")), gp11);

            //.... game eight ....//
            Ship ship30 = new Ship("Destroyer", new ArrayList<>(Arrays.asList("B5", "C5", "D5")), gp15);
            Ship ship31 = new Ship("Patrol Boat", new ArrayList<>(Arrays.asList("C6", "C7")), gp15);
            Ship ship32 = new Ship("Submarine", new ArrayList<>(Arrays.asList("A2", "A3", "A4")), gp16);
            Ship ship33 = new Ship("Patrol Boat", new ArrayList<>(Arrays.asList("G6", "H6")), gp16);

            shipRepository.save(ship1);
            shipRepository.save(ship2);
            shipRepository.save(ship3);
            shipRepository.save(ship4);
            shipRepository.save(ship5);
            shipRepository.save(ship6);
            shipRepository.save(ship7);
            shipRepository.save(ship8);
            shipRepository.save(ship9);
            shipRepository.save(ship10);
            shipRepository.save(ship11);
            shipRepository.save(ship12);
            shipRepository.save(ship13);
            shipRepository.save(ship14);
            shipRepository.save(ship15);
            shipRepository.save(ship16);
            shipRepository.save(ship17);
            shipRepository.save(ship18);
            shipRepository.save(ship19);
            shipRepository.save(ship20);
            shipRepository.save(ship21);
            shipRepository.save(ship22);
            shipRepository.save(ship23);
            shipRepository.save(ship30);
            shipRepository.save(ship31);
            shipRepository.save(ship32);
            shipRepository.save(ship33);


            // ......................create salvoes.......................//
            //.... game one ....//
            Salvo salvo1 = new Salvo("1", new ArrayList<>(Arrays.asList("B5", "C5", "F1")), gp1);
            Salvo salvo2 = new Salvo("1", new ArrayList<>(Arrays.asList("B4", "B5", "B6")), gp2);
            Salvo salvo3 = new Salvo("2", new ArrayList<>(Arrays.asList("F2", "D5")), gp1);
            Salvo salvo4 = new Salvo("2", new ArrayList<>(Arrays.asList("E1", "H3", "A2")), gp2);

            //.... game two ....//
            Salvo salvo5 = new Salvo("1", new ArrayList<>(Arrays.asList("A2", "A4", "G6")), gp3);
            Salvo salvo6 = new Salvo("1", new ArrayList<>(Arrays.asList("B5", "D5", "C7")), gp4);
            Salvo salvo7 = new Salvo("2", new ArrayList<>(Arrays.asList("A3", "H6")), gp3);
            Salvo salvo8 = new Salvo("2", new ArrayList<>(Arrays.asList("C5", "C6")), gp4);

            //.... game three ....//
            Salvo salvo9 = new Salvo("1", new ArrayList<>(Arrays.asList("G6", "H6", "A4")), gp5);
            Salvo salvo10 = new Salvo("1", new ArrayList<>(Arrays.asList("H1", "H2", "H3")), gp6);
            Salvo salvo11 = new Salvo("2", new ArrayList<>(Arrays.asList("A2", "A3", "D8")), gp5);
            Salvo salvo12 = new Salvo("2", new ArrayList<>(Arrays.asList("E1", "F2", "G3")), gp6);

            //.... game four ....//
            Salvo salvo13 = new Salvo("1", new ArrayList<>(Arrays.asList("A3", "A4", "F7")), gp7);
            Salvo salvo14 = new Salvo("1", new ArrayList<>(Arrays.asList("B5", "C6", "H1")), gp8);
            Salvo salvo15 = new Salvo("2", new ArrayList<>(Arrays.asList("A2", "G6", "H6")), gp7);
            Salvo salvo16 = new Salvo("2", new ArrayList<>(Arrays.asList("C5", "C7", "D5")), gp8);

            //.... game five ....//
            Salvo salvo17 = new Salvo("1", new ArrayList<>(Arrays.asList("A1", "A2", "A3")), gp9);
            Salvo salvo18 = new Salvo("1", new ArrayList<>(Arrays.asList("B5", "B6", "C7")), gp10);
            Salvo salvo19 = new Salvo("2", new ArrayList<>(Arrays.asList("G6", "G7", "G8")), gp9);
            Salvo salvo20 = new Salvo("2", new ArrayList<>(Arrays.asList("C6", "D6", "E6")), gp10);
            Salvo salvo21 = new Salvo("3", new ArrayList<>(Arrays.asList("H1", "H8")), gp10);

            salvoRepository.save(salvo1);
            salvoRepository.save(salvo2);
            salvoRepository.save(salvo3);
            salvoRepository.save(salvo4);
            salvoRepository.save(salvo5);
            salvoRepository.save(salvo6);
            salvoRepository.save(salvo7);
            salvoRepository.save(salvo8);
            salvoRepository.save(salvo9);
            salvoRepository.save(salvo10);
            salvoRepository.save(salvo11);
            salvoRepository.save(salvo12);
            salvoRepository.save(salvo13);
            salvoRepository.save(salvo14);
            salvoRepository.save(salvo15);
            salvoRepository.save(salvo16);
            salvoRepository.save(salvo17);
            salvoRepository.save(salvo18);
            salvoRepository.save(salvo19);
            salvoRepository.save(salvo20);
            salvoRepository.save(salvo21);
        };
    }
}
