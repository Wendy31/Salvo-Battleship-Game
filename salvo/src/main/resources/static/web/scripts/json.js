{
    "id": 2,
    "created": "2018-12-14T13:12:04.855+0000",
    "gamePlayers": [{
            "id": 4,
            "player": {
                "id": 2,
                "email": "c.obrian@ctu.gov"
            }
        },
        {
            "id": 3,
            "player": {
                "id": 1,
                "email": "j.bauer@ctu.gov"
            }
        }
    ],
    "ships": [{
            "locations": [
                "A2",
                "A3",
                "A4"
            ],
            "type": "Submarine"
        },
        {
            "locations": [
                "G6",
                "H6"
            ],
            "type": "Patrol Boat"
        }
    ]
    "salvoes": {
        "23": {
            "1": ["H1", "A2"],
            "2": ["B4", "D8"]
        },
        "54": {
            "1": ["C5", "E6"],
            "2": ["A7", "F1"]
        }
    }
}



//.......................................//

[{
        "turn": "1",
        "player": "23",
        "locations": ["H1", "A2"]
    },
    {
        "turn": "1",
        "player": "54",
        "locations": ["C5", "E6"]
    },
    {
        "turn": "2",
        "player": "23",
        "locations": ["B4", "D8"]
    },
    {
        "turn": "2",
        "player": "54",
        "locations": ["A7", "F1"]
    }
]


{
    "1": {
        "23": ["H1", "A2"],
        "54": ["C5", "E6"]
    },
    "2": {
        "23": ["B4", "D8"],
        "54": ["A7", "F1"]
    }
}


{
    "23": {
        "1": ["H1", "A2"],
        "2": ["B4", "D8"]
    },
    "54": {
        "1": ["C5", "E6"],
        "2": ["A7", "F1"]
    }
}