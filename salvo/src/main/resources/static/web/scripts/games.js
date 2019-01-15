var app = new Vue({
  el: "#app",
  data: {
    urlApiGames: "/api/games", // calls a method created in Java controller that accesses database.
    // fetch bridges between front end (JS) and back end (Java).
    gamesData: null,
    apiGamesObj: null,
    columnName: ["Position", "Name", "Total score", "Wins", "Losses", "Ties"],
    leaderboard: [],
    scores: {},
    gamesThead: ["Game", "Game Player One", "Game Player Two", "Action"],
    password: "",
    username: "",
    currentUser: false,
    currentUserName: false,
    createGameBtn: true,
  },

  methods: {
    fetchData() {
      fetch(this.urlApiGames, {
          method: "GET"
        })
        .then(function (data) {
          return data.json();
        })
        .then(function (myData) {
          if (myData.player !== "Guest") {
            // if theres player, user is logged in
            app.currentUser = true;
            app.currentUserName = true;
            app.username = myData.player.email;
          }

          app.apiGamesObj = myData;
          app.gamesData = myData.games;
          console.log(app.gamesData);
          app.createLeaderboard();
          app.getScoreArray();
          app.getPlayerScores();
        });
    },

    login() {
      fetch("/api/login", {
          // fetch data from this path
          credentials: "include",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          method: "POST", // POST method = create (posting data to the back)
          body: "userName=" + this.username + "&password=" + this.password
        })
        .then(function (data) {
          // only 1 then. no need json object
          if (data.status === 200) {
            // User is signed in.
            app.currentUserName = true;
            location.reload();

            alert("Welcome " + app.username);
          } else {
            alert("Incorrect login. Please try again.");
          }
          console.log("Request success: ", data);
        })
        .catch(function (error) {
          console.log("Request failure: ", error);
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
          console.log("Request success: ", data);
          location.reload();
          app.clearInputField();
        })
        .catch(function (error) {
          console.log("Request failure: ", error);
        });
    },

    register() {
      if (app.username === "" || app.password === "") {
        alert("Invalid input. Please try again.");
        return;
      }
      fetch("/api/players", {
          credentials: "include",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          method: "POST",
          body: "userName=" + this.username + "&password=" + this.password
        })
        .then(function (data) {
          // return json data
          return data.json();
        })
        .then(function (data) {
          console.log(data);
          if (data.error) {
            alert(data.error);
          } else {
            alert("You have successfully signed up to Salvo!");
            app.clearInputField();
            console.log("Request success: ", data);
          }
        })
        .catch(function (error) {
          console.log("Request failure: ", error);
        });
    },

    createGame() {
      fetch("/api/games", {
          credentials: "include",
          headers: {
            Accept: "application/json",
            "Content-Type": "application/x-www-form-urlencoded"
          },
          method: "POST"
        })
        .then(function (response) {
          return response.json();
        })
        .then(function (json) {
          if (json.error) {
            alert(json.error);
          } else {
            alert("You have successfully added a new game!");
            console.log("Request success: ", json);
            window.location = "game.html?gp=" + json.gpid;
          }
        })
        .catch(function (error) {
          console.log("Request failure: ", error);
        });
    },

    joinGame(gameID) {
      fetch("/api/game_view/" + gameID + "/players", {
          credentials: "include",
          headers: {
            Accept: "application/json",
            "Content-Type": "application/x-www-form-urlencoded"
          },
          method: "POST"
        })
        .then(function (response) {
          return response.json();
        })
        .then(function (json) {
          if (json.error) {
            alert(json.error);
          } else {
            alert("You have successfully joined a game!");
            console.log("Request success: ", json);
            window.location = "game.html?gp=" + json.gpid;
          }
        })
        .catch(function (error) {
          console.log("Request failure: ", error);
        });
    },

    clearInputField() {
      this.username = "";
      this.password = "";
    },

    showReenter(game) {
      var gp = game.gamePlayers;
      console.log(gp);
      for (var j = 0; j < gp.length; j++) {
        if (gp[j].player.id == this.apiGamesObj.player.id) {
          // if gp and user are same = re-enter game
          return true;
        }
      }
      return false;
    },

    getGpID(game) {
      var gp = game.gamePlayers;
      for (var i = 0; i < gp.length; i++) {
        if (gp[i].player.id == this.apiGamesObj.player.id) {
          return gp[i].id;
        }
      }
    },

    showJoinGame(game) {
      var gp = game.gamePlayers;
      for (var i = 0; i < gp.length; i++) {
        if (gp.length == 1 && this.currentUser == true) {
          return true;
        }
      }
      return false;
    },

    createLeaderboard() {
      var allNames = [];
      for (var i = 0; i < this.gamesData.length; i++) {
        var gamePlayers = this.gamesData[i].gamePlayers;
        for (var j = 0; j < gamePlayers.length; j++) {
          if (
            allNames.findIndex(
              player => player.name == gamePlayers[j].player.email
            ) == -1
          ) {
            var userEmail = gamePlayers[j].player.email;
            allNames.push({
              name: userEmail,
              total_scores: 0,
              wins: 0,
              losses: 0,
              ties: 0
            });
          }
        }
      }
      this.leaderboard = Array.from(new Set(allNames)); // array of obj stored in global variable
      console.log(this.leaderboard);
    },

    getScoreArray() {
      var scoresArray = [];
      for (var i = 0; i < this.gamesData.length; i++) {
        var scores = this.gamesData[i].scores;
        for (var n = 0; n < scores.length; n++) {
          scoresArray.push(scores[n]);
        }
      }
      console.log(scoresArray);
      return scoresArray;
    },

    getPlayerScores() {
      var scoresArray = this.getScoreArray();
      for (var i = 0; i < this.leaderboard.length; i++) {
        for (var j = 0; j < scoresArray.length; j++) {
          if (this.leaderboard[i].name == scoresArray[j].name) {
            this.leaderboard[i].total_scores += scoresArray[j].score;
            switch (
              scoresArray[j].score // executes a block of code depending on different cases
            ) {
              case 1.0:
                this.leaderboard[i].wins++;
                break;
              case 0.5:
                this.leaderboard[i].ties++;
                break;
              case 0.0:
                this.leaderboard[i].losses++;
              default:
            }
          }
        }
      }
      this.leaderboard.sort(function (playerA, playerB) {
        if (playerA.total_scores < playerB.total_scores) {
          return 1; // if 1, B goes first
        } else if (playerA.total_scores > playerB.total_scores) {
          return -1; // if -1, A goes first
        } else {
          return (
            playerA.wins +
            playerA.losses +
            playerA.ties -
            (playerB.wins + playerB.losses + playerB.ties)
          ); // finds negative or positive value
        }
      });
    }
  },

  created() {
    this.fetchData();
  },

  updated() {}
});