var app = new Vue({
  el: "#app",
  data: {
    apiGameView: "/api/game_view/", // end point that need id number. has data from back-end of controller, providing specific data. No need to add localhost 8080, coz its automatic. This url is where my data (Gameview object) is stored.
    gamesViewData: null, // beginning is always null, coz no data is fetched yet
    gridCells: 10,
    columnName: ["", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"],
    rowName: ["", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
    ships: []
  },

  methods: {
    fetchData() {
      fetch(this.apiGameView, {
        // fectch data from controller
        method: "GET"
      })
        .then(function(data) {
          return data.json(); // makes it into a JSON object
        })
        .then(function(myData) {
          app.gamesViewData = myData; // then stores it in gamesViewData
          console.log(app.gamesViewData);
          app.ships = myData.ships;
          console.log(app.ships); // log data object
        });
    },

    getGamePlayerID() {
      var url = new URL(window.location.href); // new = new object contrsuctor called URL, which is the current URL. Window.location.href returns the href (URL) of the current page (e.g http://localhost:8080/web/game.html?gp=)
      this.apiGameView += url.searchParams.get("gp"); // in url get everything after gp. concatenate with api
      //apiGameView = apiGameView + url.searchParams.get("gp");
      // e.g. http://localhost:8080/api/game_view/2 so data on gamePlayer 2 can be fetched, for example

      console.log(this.apiGameView);
    },

    getShipLocation() {
      for (var i = 0; i < this.ships.length; i++) {
        for (var j = 0; j < this.ships[i]["location"]; j++) {
          var shipLocation = this.ships[i]["location"][j];
          var gridLocation = document.getElementById("shipLocation");
          console.log(gridLocation);
        }
      }
    }
  },

  created() {
    this.getGamePlayerID(); // need to create api first to be able to do fetch
    this.fetchData(); // once api created, then fetch data
    this.getShipLocation();
  },

  updated() {}
});
