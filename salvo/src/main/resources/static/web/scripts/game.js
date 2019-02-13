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
    wrongPlace: false,
    existingShip: false,
    horizontal: true,
    vertical: false,
    highlight: false,
    isPlaceable: false,
    shipSelected: "",
    clickaircraft: true,
    clickbattleship: true,
    clicksubmarine: true,
    clickdestroyer: true,
    clickpatrol: true,
    salvoIsPlaceable: true,
    salvoArray: [],
    shipObj: [
      {
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
    ],
  },

  methods: {
    fetchData() {
      fetch(this.apiGameView, { // fectch data from controller
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
            app.gamesViewData = myData; // then stores it in gamesViewData
            console.log(app.gamesViewData);
            app.ships = myData.ships;
            console.log(app.ships); // log data object
            app.getShipLocation(); // function needs data first in order to run. Get data, then it can show ship location.
            app.showGameAndPlayerInfoGV();
            app.salvoes = myData.salvoes;
            console.log(app.salvoes);
            app.getSalvoLocation();
            app.highlightSalvoPreLoc();
            app.showOppHits();
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
          window.location.href = "games.html";
          console.log("Request success: ", data);
        })
        .catch(function (error) {
          console.log("Request failure: ", error);
        });
    },

    postShips() {
      var has5 = true;
      this.shipObj.forEach(function (ship) {
        console.log(ship.location.length)
        if (ship.location.length == 0) {
          has5 = false;
        }
      });
      if (has5) {
        fetch("/api/games/players/" + app.playerIdNumber + "/ships", {
          credentials: "include",
          headers: {
            Accept: "application/json",
            "Content-Type": "application/json"
          },
          method: "POST",
          body: JSON.stringify(app.shipObj)
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
      } else {
        alert("You need 5 ships to play");
      }
    },

    postSalvoes() {
      if (app.salvoArray.length == 5) {
        fetch("/api/games/players/" + app.playerIdNumber + "/salvoes", {
          credentials: "include",
          headers: {
            Accept: "application/json",
            "Content-Type": "application/json"
          },
          method: "POST",
          body: JSON.stringify
            ({
              turn: null,
              location: app.salvoArray
            })
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
      } else {
        alert("You have 5 shots. USE THEM!")
      }
    },

    //.......................................OTHER FUNCTIONS.......................................//
    getGamePlayerID() {
      var url = new URL(window.location.href); // new = new object contrsuctor called URL, which is the current URL. Window.location.href returns the href (URL) of the current page (e.g http://localhost:8080/web/game.html?gp=)
      console.log(url);
      this.playerIdNumber = url.searchParams.get("gp");
      this.apiGameView += this.playerIdNumber; // in url get everything after gp. concatenate with api
      //apiGameView = apiGameView + url.searchParams.get("gp");
      // e.g. http://localhost:8080/api/game_view/2 so data on gamePlayer 2 can be fetched, for example

      console.log(this.apiGameView);
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


    //...................................SHIPS......................................//
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
      this.shipSelected = shipType;
      this.highlight = true;
      switch (this.shipSelected) {
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
      var cells = document.querySelectorAll(".hostGridTD"); // select all cells
      cells.forEach(function (cell) {
        // for each cell allow mouseover function
        cell.onmouseover = function (event) {
          if (app.highlight) {
            var cellID = event.target.id; // gets cell ID and slice
            var letterID = cellID.slice(0, 1);
            var numberID = cellID.slice(1, 3);
            var shipLocations = [];
            var shipOverlap = [];
            var letterPos = app.rowName.indexOf(letterID);

            // this loop creates extra cells
            for (var i = 0; i < shipLength; i++) {
              var cellBox;
              if (app.horizontal) {
                cellBox = letterID + (parseInt(numberID) + parseInt(i));

                // if off-grid
                if (parseInt(numberID) + parseInt(i) > 10) {
                  app.wrongPlace = true;
                  app.isPlaceable = false;
                } else {
                  app.wrongPlace = false;
                  app.isPlaceable = true;
                }

                // if in-grid and contains ship
                if (
                  document.getElementById(
                    letterID + (parseInt(numberID) + parseInt(i))
                  ) &&
                  parseInt(numberID) + parseInt(i) < 10
                ) {
                  if (document.getElementById(letterID + (parseInt(numberID) + parseInt(i)))
                    .classList.contains("shipLocation")
                  ) {
                    app.existingShip = true;
                    app.isPlaceable = false;
                  } else {
                    app.existingShip = false;
                    app.isPlaceable = true;
                  }
                }
              }

              // vertical conditions
              if (app.vertical) {
                cellBox =
                  app.rowName[parseInt(letterPos) + parseInt(i)] + numberID;

                // if off-grid
                if (!app.rowName[parseInt(letterPos) + parseInt(i)]) {
                  app.wrongPlace = true;
                  app.isPlaceable = false;
                } else {
                  app.wrongPlace = false;
                  app.isPlaceable = true;
                }

                // if in-grid and contains ship
                if (document.getElementById(app.rowName[parseInt(letterPos) + parseInt(i)] + numberID) && app.rowName[parseInt(letterPos) + parseInt(i)] + numberID) {
                  if (
                    document.getElementById(app.rowName[parseInt(letterPos) + parseInt(i)] + numberID)
                      .classList.contains("shipLocation")
                  ) {
                    app.existingShip = true;
                    app.isPlaceable = false;
                  } else {
                    app.existingShip = false;
                    app.isPlaceable = true;
                  }
                }
              }
              shipOverlap.push(app.existingShip) // pushed boolean values in array
              shipLocations.push(cellBox); // put locations in array to check if more than 10 (ie off-grid)
            }


            // this loop changes the color of all cells
            for (let j = 0; j < shipLocations.length; j++) {
              if (app.horizontal) {
                var shipHorizontal = document.getElementById(
                  letterID + (parseInt(numberID) + parseInt(j)) // loop thru all locations to get index and add to ID
                );
                if (!app.wrongPlace) {
                  shipHorizontal.style.background = "pink"; // if < 10 = pink
                  app.isPlaceable = true;
                }
                if (app.wrongPlace && shipHorizontal) {
                  shipHorizontal.style.background = "red"; // if > 10 && has ship IDs = red
                  app.isPlaceable = false;
                }
                if (shipOverlap.includes(true) && shipHorizontal) { // if array has true AND has ID
                  shipHorizontal.style.background = "red"; // if there's ship = red
                  app.isPlaceable = false;
                }
              }

              if (app.vertical) {
                var shipVertical = document.getElementById(
                  app.rowName[parseInt(letterPos) + parseInt(j)] + numberID // loop thru all locations to get index and add to ID
                );
                if (!app.wrongPlace) {
                  shipVertical.style.background = "pink"; // if < 10 = pink
                  app.isPlaceable = true;
                }
                if (app.wrongPlace && shipVertical) {
                  shipVertical.style.background = "red"; // if > 10 && has ship IDs = red
                  app.isPlaceable = false;
                }
                if (shipOverlap.includes(true) && shipVertical) { // if array has true AND has ID
                  shipVertical.style.background = "red"; // if there's ship = red
                  app.isPlaceable = false;
                }
              }
            }
            console.log(shipLocations);
            console.log(shipOverlap);
            cell.onclick = () => { // arrow function for onClick to invoke another function
              app.placeShip(shipLocations);
              app.shipObj.find(ship => ship.shipType == app.shipSelected).location = shipLocations; // finds value in array
              app.highlight = false;

              if (app.isPlaceable) {
                app["click" + app.shipSelected] = false; // app["click" + app.shipSelected] = !app["click" + app.shipSelected]; // app.clickaircraft = opposite state
                console.log(app["click" + app.shipSelected]); // app.aircraft == false; therefore shows undo button
              }
            };
          }
        };

        // when click, calls the function
        cell.onmouseout = function (event) {
          // mouseout of the same cells as above
          var cellID = event.target.id; // gets cell ID and slice
          var letterID = cellID.slice(0, 1);
          var numberID = cellID.slice(1, 3);
          var letterPos = app.rowName.indexOf(letterID);

          for (var i = 0; i < shipLength; i++) {
            var shipHorizontal = document.getElementById(
              letterID + (parseInt(numberID) + parseInt(i))
            );
            var shipVertical = document.getElementById(
              app.rowName[parseInt(letterPos) + parseInt(i)] + numberID // loop thru all locations to get index and add to ID
            );
            if (shipHorizontal) {
              shipHorizontal.style.background = ""; // if same ship cells, mouseout
            }
            if (shipVertical) {
              shipVertical.style.background = "";
            }
          }
        };
      });
    },

    orientation(direction) {
      // if these buttons are clicked, changes the state of variable
      if (direction == "vertical") {
        this.vertical = true;
        this.horizontal = false;
      } else if (direction == "horizontal") {
        this.vertical = false;
        this.horizontal = true;
      }
    },

    placeShip(arrayLocation) {
      if (this.isPlaceable) {
        console.log("ship placed");
        for (var i = 0; i < arrayLocation.length; i++) {
          var shipPlaced = arrayLocation[i];
          var gridLocation = document.getElementById(shipPlaced);
          gridLocation.classList.add("shipLocation");
        }
      } else {
        alert("Invalid position!");
      }
    },

    undoShip(shipName) {
      this.shipSelected = shipName;
      for (var i = 0; i < this.shipObj.length; i++) {
        if (this.shipObj[i].shipType == this.shipSelected) {
          this.shipObj[i].location.forEach(loc => { // for each loc "H1"
            console.log(loc);
            var gridLocation = document.getElementById(loc); // find el by id "H1"
            gridLocation.classList.remove("shipLocation");
          })
          this["click" + this.shipSelected] = true; // contains true/ false value. FALSE = TRUE;
          console.log(this["click" + this.shipSelected]); // if this.clickaircraft == true; show 'aircraft' button
        }
      }
    },


    //...................................SALVOES......................................//
    highlightSalvoPreLoc() {
      var cells = document.querySelectorAll(".gridTD"); // select all grid cells
      cells.forEach(cell => {
        cell.onmouseover = (event) => {
          var cellID = event.target.id;
          if (document.getElementById(cellID).classList.contains("hostSalvoes")) {
            this.salvoIsPlaceable = false;
          } else {
            this.salvoIsPlaceable = true;
          }
          if (this.salvoIsPlaceable) {
            var salvoBox = document.getElementById(cellID);
            salvoBox.style.background = "orange";
          }
        }

        cell.onclick = (event) => {
          var cell = event.target.id;
          if (this.salvoArray.indexOf(cell) == -1 && this.salvoArray.length < 5) { // if index of this cell ID doesnt exist and less than 5 put salvoes in
            if (!document.getElementById(cell).classList.contains("hostSalvoes")) {
              var hostSalvoes = document.getElementById(cell);
              hostSalvoes.classList.add("hostSalvoes");
              this.salvoArray.push(cell);
              console.log(this.salvoArray);
            }
          } else if (this.salvoArray.indexOf(cell) != -1) { // else if index of thid cell ID DOES exist, remove from array and class name
            this.salvoArray.splice(this.salvoArray.indexOf(cell), 1);
            console.log(this.salvoArray);
            var hostSalvoes = document.getElementById(cell);
            hostSalvoes.classList.remove("hostSalvoes");
          } else {
            alert("You can only place 5 shots")
          }
        }

        cell.onmouseout = (event) => {
          var cellID = event.target.id;
          var salvoBox = document.getElementById(cellID);
          salvoBox.style.background = "";
        }
      })
    },

    getSalvoLocation() {
      for (var keyID in this.salvoes) {
        // displays players ID
        for (var keyTurn in this.salvoes[keyID]) {
          // Displays turn
          var turn = this.salvoes[keyID][keyTurn]; // display turn number
          console.log(turn);
          for (var salvo in turn) {
            var salvoLocation = turn[salvo]; // locations of salvoes
            console.log(salvoLocation);
            if (keyID == this.playerIdNumber) {
              var hostSalvoes = document.getElementById(salvoLocation);
              hostSalvoes.classList.add("hostSalvoes");
              console.log(keyTurn);
              hostSalvoes.textContent = keyTurn;
            } else {
              for (var i = 0; i < this.ships.length; i++) {
                // loop thru each ship
                if (this.ships[i].locations.includes(salvoLocation)) {
                  // if ship location has salvoLocation
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

    //...................................HITS ON MY GRID......................................//
    showOppHits() {

      // var oppHits;
      for (var i = 0; i < this.ships.length; i++) {
        for (var j = 0; j < this.ships[i].length; j++) {
          var hostShips = this.ships[i].locations[j];
          console.log(hostShips);

          var cell = document.getElementById(hostShips);
          console.log(cell);
          if (cell.classList.contains("opponentSalvoes") && cell.classList.contains("shipLocation")) {
            cell.classList.add("oppHits");
          }
        }
      }
    }
  },

  created() {
    this.getGamePlayerID(); // need to create api first to be able to do fetch
    this.fetchData(); // once api created, then fetch data
  },

  updated() { }
});
