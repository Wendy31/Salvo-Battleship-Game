var app = new Vue({
  el: "#app",
  data: {
    urlApiGames: "/api/games", // calls a method created in Java controller that accesses database. 
    // fetch bridges between front end (JS) and back end (Java).
    gamesData: null,
    columnName: ["Position", "Name", "Total score", "Wins", "Losses", "Ties"],
    leaderboard: [],
    scores: {},
    gamesThead: ["Game Creation", "Game Player One", "Game Player Two"]

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
          app.gamesData = myData;
          console.log(app.gamesData);
          app.createLeaderboard();
          app.getScoreArray();
          app.getPlayerScores();


        });
    },
    createLeaderboard() {
      var allNames = [];
      for (var i = 0; i < this.gamesData.length; i++) {
        var gamePlayers = this.gamesData[i].gamePlayers;
        for (var j = 0; j < gamePlayers.length; j++) {
          if (allNames.findIndex(player => player.name == gamePlayers[j].player.email) == -1) {
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
            switch (scoresArray[j].score) { // executes a block of code depending on different cases
              case 1.0:
                this.leaderboard[i].wins++;
                break;
              case 0.5:
                this.leaderboard[i].ties++;
                break;
              case 0.0:
                this.leaderboard[i].losses++
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
          return ((playerA.wins + playerA.losses + playerA.ties) - (playerB.wins + playerB.losses + playerB.ties)) // finds negative or positive value
        }
      })
    }
  },

  created() {
    this.fetchData();
  },

  updated() {}
});