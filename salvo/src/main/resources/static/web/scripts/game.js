var app = new Vue({
  el: "#app",
  data: {
    apiGameView: "/api/game_view/", // end point that need id number. has data from back-end of controller, providing specific data. No need to add localhost 8080, coz its automatic. This url is where my data (Gameview object) is stored.
    gamesViewData: null, // beginning is always null, coz no data is fetched yet
    gridCells: 10,
    columnName: ["", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"],
    rowName: ["", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
    ships: [],
    playerIdNumber: null,
    currentPlayer: null,
    opponentPlayer: null,
    salvoes: {},
  },

  methods: {
    fetchData() {
      fetch(this.apiGameView, {
          // fectch data from controller
          method: "GET"
        })
        .then(function (data) {
          return data.json(); // makes it into a JSON object
        })
        .then(function (myData) {
          app.gamesViewData = myData; // then stores it in gamesViewData
          console.log(app.gamesViewData);
          app.ships = myData.ships;
          console.log(app.ships); // log data object
          app.getShipLocation(); // function needs data first in order to run. Get data, then it can show ship location.
          app.showGameAndPlayerInfoGV();
          app.salvoes = myData.salvoes;
          console.log(app.salvoes);
          app.getSalvoLocation();
        });
    },

    getGamePlayerID() {
      var url = new URL(window.location.href); // new = new object contrsuctor called URL, which is the current URL. Window.location.href returns the href (URL) of the current page (e.g http://localhost:8080/web/game.html?gp=)
      console.log(url);
      this.playerIdNumber = url.searchParams.get("gp");
      this.apiGameView += this.playerIdNumber; // in url get everything after gp. concatenate with api
      //apiGameView = apiGameView + url.searchParams.get("gp");
      // e.g. http://localhost:8080/api/game_view/2 so data on gamePlayer 2 can be fetched, for example

      console.log(this.apiGameView);
    },

    getShipLocation() {
      for (var i = 0; i < this.ships.length; i++) {
        for (var j = 0; j < this.ships[i].locations.length; j++) {
          var hostShips = this.ships[i].locations[j]; // shipLocation is "H4"
          console.log(hostShips);
          var gridLocation = document.getElementById(hostShips); // gets Element and sets id name to shipLocation i.e "H4"
          gridLocation.classList.add("shipLocation"); // "H4" element is added a class call "gridLocation", for CSS styling
        }
      }
    },

    showGameAndPlayerInfoGV() {
      for (var i = 0; i < this.gamesViewData.gamePlayers.length; i++) {
        var gameViewPlayers = this.gamesViewData.gamePlayers[i].player.email;
        console.log(gameViewPlayers);
        console.log(this.gamesViewData.gamePlayers[i].id);
        if (this.playerIdNumber == this.gamesViewData.gamePlayers[i].id) {
          this.currentPlayer = gameViewPlayers;
        } else {
          this.opponentPlayer = gameViewPlayers;
        }
      }
    },

    getSalvoLocation() {
      for (var keyID in this.salvoes) { // displays players ID
        for (var keyTurn in this.salvoes[keyID]) { // Displays turn
          var turn = this.salvoes[keyID][keyTurn]; // display turn number
          console.log(turn);
          for (var salvo in turn) {
            var salvoLocation = turn[salvo]; // locations of salvoes
            if (keyID == this.playerIdNumber) {
              var hostSalvoes = document.getElementById(salvoLocation + "opp");
              hostSalvoes.classList.add("hostSalvoes");
              hostSalvoes.textContent = keyTurn;
            } else {
              for (var i = 0; i < this.ships.length; i++) { // loop thru each ship
                if (this.ships[i].locations.includes(salvoLocation)) { // if ship location has salvoLocation
                  var opponentSalvoes = document.getElementById(salvoLocation); // add salvoLocation to grid element
                  opponentSalvoes.classList.add("opponentSalvoes"); // add class for CSS
                  opponentSalvoes.textContent = keyTurn;
                }
              }
            }
          }
        }
      }
    },
  },

  created() {
    this.getGamePlayerID(); // need to create api first to be able to do fetch
    this.fetchData(); // once api created, then fetch data

  },

  updated() {}
});