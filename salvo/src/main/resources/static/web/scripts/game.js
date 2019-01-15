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
    logoutBtn: true,
    shipObj: [{
        shipType: "aircraft",
        location: [],
        length: "5"
      },
      {
        shipType: "battleship",
        location: [],
        length: "4"
      },
      {
        shipType: "submarine",
        location: [],
        length: "3"
      },
      {
        shipType: "destroyer",
        location: [],
        length: "3"
      },
      {
        shipType: "patrol",
        location: [],
        length: "2"
      }
    ]
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
          if (myData.error) {
            alert(myData.error);
            history.back();
          } else {
            // if (myData.player.email) {
            //   app.userExists = true;
            // };
            app.gamesViewData = myData; // then stores it in gamesViewData
            console.log(app.gamesViewData);
            app.ships = myData.ships;
            console.log(app.ships); // log data object
            app.getShipLocation(); // function needs data first in order to run. Get data, then it can show ship location.
            app.showGameAndPlayerInfoGV();
            app.salvoes = myData.salvoes;
            console.log(app.salvoes);
            app.getSalvoLocation();
            // app.highlightPreLocation();
          }
        });
    },

    logout() {
      fetch("/api/logout", {
          credentials: "include",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          method: "POST"
        })
        .then(function (data) {
          window.location.href = 'games.html';
          console.log("Request success: ", data);
        })
        .catch(function (error) {
          console.log("Request failure: ", error);
        });
    },

    postShips() {
      fetch("/api/games/players/" + gpid + "/ships", {
          credentials: "include",
          headers: {
            Accept: "application/json",
            "Content-Type": "application/json"
          },
          method: "POST",
          body: JSON.stringify([{
              shipType: "patrol boat",
              location: ["A1", "B1", "C1"]
            },
            {
              shipType: "destroyer",
              location: ["H5", "H6"]
            }
          ])
        })
        .then(function (response) {
          return response.json();
        })
        .then(function (json) {
          if (json.error) {
            alert(json.error);
          } else {
            alert(json.success);
          }
        })
        .catch(function (error) {
          console.log("Request failure: ", error);
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

    getLength(shipType) {
      switch (shipType) {
        case "aircraft": // if its aircraft run the function below and pass 5 thru
          this.highlightPreLocation(5);
          break;

        case "battleship":
          this.highlightPreLocation(4);
          break;

        case "submarine":
          this.highlightPreLocation(3);
          break;

        case "destroyer":
          this.highlightPreLocation(3);
          break;

        case "patrol":
          this.highlightPreLocation(2);
          break;

        default:
          break;
      }
    },

    highlightPreLocation(shipLength) {
      console.log(shipLength);

      var cells = document.querySelectorAll(".hostGridTD");
      cells.forEach(function (cell) {
        var cellID = cell.id;
        cellID.slice(1);
        console.log(cellID);

      });

      // highlights cells under mouse
      tableHost.onmouseover = function (event) {
        let target = event.target.closest('td');
        target.style.background = 'pink';
        console.log()
      };
      tableHost.onmouseout = function (event) {
        let target = event.target.closest('td');
        target.style.background = '';
      };

      tableHost.addEventListener("click", function (event) {
        if (event.target && event.target.nodeName == "TD") {
          var cell = document.getElementById("id");
          console.log(cell);
        }
      });
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