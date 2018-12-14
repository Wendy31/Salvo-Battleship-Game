var app = new Vue({
  el: "#app",
  data: {
    urlApiGames: "/api/games", // calls a method created in Java controller that accesses database. 
    // fetch bridges between front end (JS) and back end (Java).
    gamesData: null,
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
        });
    }
  },
  created() {
    // life cycle management
    this.fetchData();
  },

  updated() {}
});