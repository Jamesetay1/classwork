var properties;

function lookupTile(tile_num) {
  //find a better way than alerts to display this later
  //for now alerts are fine I guess
  if (tile_num == 1) {
    bootbox.alert('When you pass, or land on GO - you collect $200!');
  } else if (tile_num == 37 || tile_num == 8 || tile_num == 23) {
    bootbox.alert('Draw a Contingency Card. <br>  Good or Bad? Nobody Knows!');
  } else if (tile_num == 21) {
    bootbox.alert('Free parking: Collect all money in the middle!');
  } else if (tile_num == 3 || tile_num == 18 || tile_num == 34) {
    bootbox.alert('Campus Mail - Draw a Card!')
  } else if (tile_num == 31) {
    bootbox.alert('Get put on academic probation');
  } else if (tile_num == 11) {
    bootbox.alert('Academic Probation. Go Home!')
  } else {
    lookupProperty(tile_num);
  }
}

//if you land on a property card.
function propertyturn(amountRolled) {
  //Should be 3 but I guess it works with 2...
  if (doubleCount == 2) {
    finishTurn();
    return;
  }
  var changeamount = -1 + amountRolled;

  if (changeamount + player[turn].location > 40) {
    changeamount = changeamount - 40 + player[turn].location;
  }
  //if not a spot that can be bought
  if (properties[player[turn].location - 1].owner !== undefined) {
    //check if spot has not been bought yet
    if (properties[player[turn].location - 1].owner === null) {
      //if has money to buy, buy it
      if (player[turn].cash >= properties[player[turn].location - 1].cost) {
        //ask player if they would like to purchase the spot
        var CardName = 'img/' + (player[turn].location) + 'Card.jpg';
        setTimeout(function() {
          bootbox.confirm({
            className: "card-popup",
            message: "<h2>Would you like to buy this property for $" +
              properties[player[turn].location - 1].cost +
              "?</h2><br><img src='" +
              CardName + "'>",
            buttons: {
              cancel: {
                label: '<i class="fa fa-times"></i> Nope'
              },
              confirm: {
                label: '<i class="fa fa-check"></i> Yup'
              }
            },
            callback: function(result) {
              if (result == true) {
                //take money from player
                player[turn].cash = player[turn].cash - properties[
                  player[turn].location - 1].cost;

                //assign player as owner to spot
                properties[player[turn].location - 1].owner = turn;
                finishTurn();
                outputPlayersList();
              } else {
                finishTurn();
              }
            }
          });
        }, 700);
      } else {
        finishTurn();
      }

      //if does not have enough money to buy, do nothing

    } else { //space has an owner. you must pay him/her
      player[turn].cash = player[turn].cash - properties[player[turn].location -
        1].rent[properties[player[turn].location - 1].upgrades];
      player[properties[player[turn].location - 1].owner].cash = player[
        properties[player[turn].location - 1].owner].cash + properties[player[
        turn].location - 1].rent[properties[player[turn].location - 1].upgrades];
      finishTurn();
    }
  } else {
    finishTurn();
  }
}

/**
 * Will ask how many upgrades the player wishes to buy and put the upgrades on that card.
 */
function buyUpgrades(prop_num) {
  bootbox.prompt({
    title: "How many upgrades would you like to purchase?",
    inputType: 'select',
    inputOptions: [{
      text: 'Choose one...',
      value: '',
    }, {
      text: 'Buy One Year of Credit',
      value: '1',
    }, {
      text: 'Buy Two Years of Credit',
      value: '2',
    }, {
      text: 'Buy Three Years of Credit',
      value: '3',
    }, {
      text: 'Buy Four Years of Credit',
      value: '4',
    }, {
      text: 'Buy a Diploma',
      value: '5',
    }],
    callback: function(result) {
      if (result === '1') {
        if (properties[prop_num].upgrades === 4) {
          alert(
            "You already have 4 YOC on this card. If you would like to upgrade again purchase a diploma"
          );
          buyUpgrades(prop_num);
        } else {
          properties[prop_num].upgrades = properties[prop_num].upgrades +
            1;
          player[turn].cash = player[turn].cash - properties[prop_num].upgrade_cost;
          player[turn].numYOC = player[turn].numYOC + 1;
          outputPlayersList();
          bootbox.hideAll();
        }
      } else if (result === '2') {
        if (properties[prop_num].upgrades === 3) {
          alert(
            "You already have 3 YOC on this card. If you would like to upgrade again, buy only 1 YOC"
          );
          buyUpgrades(prop_num);
        } else if (properties[prop_num].upgrades === 4) {
          alert(
            "You already have 4 YOC on this card. If you would like to upgrade again purchase a diploma"
          );
          buyUpgrades(prop_num);
        } else {
          properties[prop_num].upgrades = properties[prop_num].upgrades +
            (2);
          player[turn].cash = player[turn].cash - properties[prop_num].upgrade_cost *
            2;
          player[turn].numYOC = player[turn].numYOC + 2;
          outputPlayersList();
          bootbox.hideAll();
        }
      } else if (result === '3') {
        if (properties[prop_num].upgrades === 2) {
          alert(
            "You already have 2 YOC on this card. If you would like to upgrade again, buy only 2 YOC"
          );
          buyUpgrades(prop_num);
        } else if (properties[prop_num].upgrades === 3) {
          alert(
            "You already have 3 YOC on this card. If you would like to upgrade again, buy only 1 YOC"
          );
          buyUpgrades(prop_num);
        } else if (properties[prop_num].upgrades === 4) {
          alert(
            "You already have 4 YOC on this card. If you would like to upgrade again purchase a diploma"
          );
          buyUpgrades(prop_num);
        } else {
          properties[prop_num].upgrades = properties[prop_num].upgrades +
            (3);
          player[turn].cash = player[turn].cash - properties[prop_num].upgrade_cost *
            3;
          player[turn].numYOC = player[turn].numYOC + 3;
          outputPlayersList();
          bootbox.hideAll();
        }
      } else if (result === '4') {
        if (properties[prop_num].upgrades === 1) {
          alert(
            "You already have 1 YOC on this card. If you would like to upgrade again, buy only 3 YOC"
          );
          buyUpgrades(prop_num);
        } else if (properties[prop_num].upgrades === 2) {
          alert(
            "You already have 2 YOC on this card. If you would like to upgrade again, buy only 2 YOC"
          );
          buyUpgrades(prop_num);
        } else if (properties[prop_num].upgrades === 3) {
          alert(
            "You already have 3 YOC on this card. If you would like to upgrade again, buy only 1 YOC"
          );
          buyUpgrades(prop_num);
        } else if (properties[prop_num].upgrades === 4) {
          alert(
            "You already have 4 YOC on this card. If you would like to upgrade again purchase a diploma"
          );
          buyUpgrades(prop_num);
        } else {
          properties[prop_num].upgrades = properties[prop_num].upgrades +
            (4);
          player[turn].cash = player[turn].cash - properties[prop_num].upgrade_cost *
            4;
          player[turn].numYOC = player[turn].numYOC + 4;
          outputPlayersList();
          bootbox.hideAll();
        }
      } else if (result === '5') {
        if (properties[prop_num].upgrades < 4) {
          alert(
            "You must have 4 YOC on this card before purchasing a diploma. Please purchase that first"
          );
          buyUpgrades(prop_num);
        } else if (properties[prop_num].upgrades === 5) {
          alert("You have already earned your diploma!");
        } else {
          properties[prop_num].upgrades = properties[prop_num].upgrades +
            1;
          player[turn].cash = player[turn].cash - properties[prop_num].upgrade_cost;
          player[turn].numYOC = player[turn].numYOC - 4;
          player[turn].numD = player[turn].numD + 1;
          outputPlayersList();
          bootbox.hideAll();
        }
      }


    }
  });



}

/**
 * Checks to see if all properties are owned by the current player and has enough for at least one upgrade.
 * @param prop_num
 */
function isEligible(prop_num) {

  if (prop_num === 1 || prop_num === 3) {
    if (properties[1].owner === turn && properties[3].owner === turn && player[
        turn].cash >= 50) {
      return true;
    }
  } else if (prop_num === 6 || prop_num === 8 || prop_num === 9) {
    if (properties[6].owner === turn && properties[8].owner === turn &&
      properties[9].owner === turn && player[turn].cash >= 50) {
      return true;
    }
  } else if (prop_num === 11 || prop_num === 13 || prop_num === 14) {
    if (properties[11].owner === turn && properties[13].owner === turn &&
      properties[14].owner === turn && player[turn].cash >= 100) {
      return true;
    }
  } else if (prop_num === 16 || prop_num === 18 || prop_num === 19) {
    if (properties[16].owner === turn && properties[18].owner === turn &&
      properties[19].owner === turn && player[turn].cash >= 100) {
      return true;
    }
  } else if (prop_num === 21 || prop_num === 23 || prop_num === 24) {
    if (properties[21].owner === turn && properties[23].owner === turn &&
      properties[24].owner === turn && player[turn].cash >= 150) {
      return true;
    }
  } else if (prop_num === 26 || prop_num === 27 || prop_num === 29) {
    if (properties[26].owner === turn && properties[27].owner === turn &&
      properties[29].owner === turn && player[turn].cash >= 150) {
      return true;
    }
  } else if (prop_num === 31 || prop_num === 32 || prop_num === 34) {
    if (properties[31].owner === turn && properties[32].owner === turn &&
      properties[34].owner === turn && player[turn].cash >= 200) {
      return true;
    }
  } else if (prop_num === 37 || prop_num === 39) {
    if (properties[37].owner === turn && properties[39].owner === turn &&
      player[turn].cash >= 200) {
      return true;
    }
  } else {
    return false;
  }


}

function unmortgage(propNumber) {
  // The payment needed to lift the mortgage is the value of the mortgage plus 10%.
  var mortgagePayment = properties[propNumber - 1].mortgage * 1.1;

  if (mortgagePayment <= player[turn].cash) {
    player[turn].cash -= mortgagePayment;
    properties[propNumber - 1].isMortgaged = 0;
    outputPlayersList();
  } else {
    alert("You do not have enough cash to lift the mortgage on this property.");
  }
}

function lookupProperty(prop_num) {
  var owner = "";

  if (JSON.stringify(properties[prop_num - 1].owner) == 'null') {
    owner = "For Sale!"
  } else {
    owner = player[JSON.stringify(properties[prop_num - 1].owner)].name
  };
  //if it is a road/Cy/Lancelot

  var CardNum = JSON.stringify(prop_num);
  var CardName = 'img/' + CardNum + 'Card.jpg';


  if (prop_num == 6 || prop_num == 13 || prop_num == 16 || prop_num == 26 ||
    prop_num == 29 || prop_num == 36) {
    let msg = '<b><h2>Property Name:  ' + JSON.stringify(properties[prop_num -
        1].name) +
      '<br>Owner:  ' + owner +
      '<br>Cost to Buy:  $' + JSON.stringify(properties[prop_num - 1].cost) +
      '<br>Mortgaged?:  ' + (properties[prop_num - 1].isMortgaged === 1 ? "Yes" :
        "No") + "<br><br/><img src='" + CardName + "'><b></h2>";
    if (properties[prop_num - 1].isMortgaged) {
      var btns = {
        cancel: {
          label: 'Close',
          className: "btn-default",
          callback: function() {
            bootbox.hideAll();
          }
        },
        unmortgage: {
          label: "Unmortage Property",
          className: "btn-primary",
          callback: function() {
            unmortgage(prop_num);
          }
        }
      };
      bootbox.dialog({
        size: "medium",
        className: "card-popup",
        message: msg,
        buttons: btns
      });
    } else {
      bootbox.alert({
        size: "medium",
        className: "card-popup",
        message: msg
      });
    }
    //if it is a real property '<br>Upgrades:' + JSON.stringify(properties[prop_num - 1].upgrades
  } else {
    //TO TEST WITH PROPERTIES DO SOMETHING LIKE THIS.
    // properties[1].owner=0;//CAMPUSTOWN TO Player 1
    //properties[3].owner=0;//CENTRAL CAMPUS to Player 1

    // properties[16].owner=1;
    //properties[18].owner=1;
    // properties[19].owner=1;
    var msg = '<b><h2>Property Name:  ' + JSON.stringify(properties[prop_num -
        1].name) +
      '<br>Owner:  ' + owner +
      '<br>Cost to Buy:  $' + JSON.stringify(properties[prop_num - 1].cost) +
      '<br>Upgrades:' + JSON.stringify(properties[prop_num - 1].upgrades) +
      '<br>Mortgaged?:  ' + (properties[prop_num - 1].isMortgaged === 1 ? "Yes" :
        "No") + "<br><br/><img src='" + CardName + "'><b></h2>";
    if (properties[prop_num - 1].isMortgaged) {
      var btns = {
        cancel: {
          label: 'Close',
          className: "btn-default",
          callback: function() {
            bootbox.hideAll();
          }
        },
        unmortgage: {
          label: "Unmortage Property",
          className: "btn-primary",
          callback: function() {
            unmortgage(prop_num);
          }
        }
      }
    } else if (properties[prop_num - 1].owner != null) {
      btns = {
        cancel: {
          label: 'Close',
          callback: function() {
            bootbox.hideAll();
          }
        },
        ok: {
          label: 'Upgrade Property',
          callback: function() {
            if (isEligible(prop_num - 1) === true) {
              buyUpgrades(prop_num - 1);
            } else {
              alert(
                "You either do not own all properties of this color, or do not have enough money for even one upgrade"
              );
            }
            //bootbox.hideAll();
          }
        },
      };

    } else {
      btns = {
        cancel: {
          label: 'Close',
          callback: function() {
            bootbox.hideAll();
          }
        }
      };
    }
    bootbox.dialog({
      size: "medium",
      className: "card-popup",
      message: msg,
      buttons: btns
    });

  }
}

//this is a terrible way to do this, but can't think of a better way that works right now.
//we should probably do this by making a .json file and importing it later
function initProps() {
  return [{
      "name": "Go",
    }, {
      "name": "Campustown",
      "owner": null,
      "isMortgaged": 0, // boolean
      "upgrades": 0,
      "rent": {
        "0": 4,
        "1": 20,
        "2": 60,
        "3": 180,
        "4": 320,
        "5": 450
      },
      "mortgage": 30,
      "upgrade_cost": 50,
      "cost": 70
    }, {
      "name": "Letter From Home",
    }, {
      "name": "Central Campus",
      "owner": null,
      "isMortgaged": 0, // boolean
      "upgrades": 0,
      "rent": {
        "0": 4,
        "1": 20,
        "2": 60,
        "3": 180,
        "4": 320,
        "5": 450
      },
      "mortgage": 30,
      "upgrade_cost": 50,
      "cost": 80
    }, {
      "name": "Parking Ticket",
      "cost": 75
    }, {
      "name": "Welch Avenue",
      "owner": null,
      "isMortgaged": 0, // boolean
      "rent": {
        "0": 25,
        "1": 50,
        "2": 100,
        "3": 200,
      },
      "mortgage": 100,
      "cost": 250
    }, {
      "name": "Iowa State Daily",
      "owner": null,
      "isMortgaged": 0, // boolean
      "upgrades": 0,
      "rent": {
        "0": 6,
        "1": 30,
        "2": 90,
        "3": 270,
        "4": 400,
        "5": 550
      },
      "mortgage": 50,
      "upgrade_cost": 50,
      "cost": 90
    }, {
      "name": "Chance",
    }, {
      "name": "KURE 88.5",
      "owner": null,
      "isMortgaged": 0, // boolean
      "upgrades": 0,
      "rent": {
        "0": 6,
        "1": 30,
        "2": 90,
        "3": 270,
        "4": 400,
        "5": 550
      },
      "mortgage": 50,
      "upgrade_cost": 50,
      "cost": 90
    }, {
      "name": "Cyclone Fanatic",
      "owner": null,
      "isMortgaged": 0, // boolean
      "upgrades": 0,
      "rent": {
        "0": 8,
        "1": 40,
        "2": 100,
        "3": 300,
        "4": 450,
        "5": 600
      },
      "mortgage": 50,
      "upgrade_cost": 50,
      "cost": 110
    }, {
      "name": "Academic Probation"
    }, {
      "name": "Reiman Gardens",
      "upgrades": 0,
      "owner": null,
      "isMortgaged": 0, // boolean
      "rent": {
        "0": 10,
        "1": 50,
        "2": 150,
        "3": 450,
        "4": 625,
        "5": 750
      },
      "mortgage": 70,
      "upgrade_cost": 100,
      "cost": 130
    }, {
      "name": "Cy",
      "owner": null,
      "isMortgaged": 0, // boolean
      "rent": {
        "0": "2x number on dice",
        "1": "10x number on dice"
      },
      "mortgage": 75,
      "cost": 175
    }, {
      "name": "Morrill Hall",
      "upgrades": 0,
      "owner": null,
      "isMortgaged": 0, // boolean
      "rent": {
        "0": 10,
        "1": 50,
        "2": 150,
        "3": 450,
        "4": 625,
        "5": 750
      },
      "mortgage": 70,
      "upgrade_cost": 100,
      "cost": 130
    }, {
      "name": "CY Stephens Auditorium",
      "upgrades": 0,
      "owner": null,
      "isMortgaged": 0, // boolean
      "rent": {
        "0": 12,
        "1": 60,
        "2": 180,
        "3": 500,
        "4": 700,
        "5": 900
      },
      "mortgage": 80,
      "upgrade_cost": 100,
      "cost": 150
    }, {
      "name": "Lincoln way",
      "owner": null,
      "isMortgaged": 0, // boolean
      "rent": {
        "0": 25,
        "1": 50,
        "2": 100,
        "3": 200,
      },
      "mortgage": 100,
      "cost": 250
    }, {
      "name": "Marston Water Tower",
      "upgrades": 0,
      "owner": null,
      "isMortgaged": 0, // boolean
      "rent": {
        "0": 14,
        "1": 70,
        "2": 200,
        "3": 550,
        "4": 750,
        "5": 950
      },
      "mortgage": 90,
      "upgrade_cost": 100,
      "cost": 170
    }, {
      "name": "Letter From Home",
    }, {
      "name": "Lake Laverne",
      "upgrades": 0,
      "owner": null,
      "isMortgaged": 0, // boolean
      "rent": {
        "0": 14,
        "1": 70,
        "2": 200,
        "3": 550,
        "4": 750,
        "5": 950
      },
      "mortgage": 90,
      "upgrade_cost": 100,
      "cost": 170
    }, {
      "name": "Campanile",
      "upgrades": 0,
      "owner": null,
      "isMortgaged": 0, // boolean
      "rent": {
        "0": 16,
        "1": 80,
        "2": 220,
        "3": 600,
        "4": 800,
        "5": 1000
      },
      "mortgage": 90,
      "upgrade_cost": 100,
      "cost": 190
    }, {
      "name": "Free Parking",
      "pot": 0
    }, {
      "name": "The Hub",
      "upgrades": 0,
      "owner": null,
      "isMortgaged": 0, // boolean
      "rent": {
        "0": 18,
        "1": 90,
        "2": 250,
        "3": 700,
        "4": 875,
        "5": 1050
      },
      "mortgage": 110,
      "upgrade_cost": 150,
      "cost": 210
    }, {
      "name": "Chance",
    }, {
      "name": "The Maintenance Shop",
      "upgrades": 0,
      "owner": null,
      "isMortgaged": 0, // boolean
      "rent": {
        "0": 18,
        "1": 90,
        "2": 250,
        "3": 700,
        "4": 875,
        "5": 1050
      },
      "mortgage": 110,
      "upgrade_cost": 150,
      "cost": 210
    }, {
      "name": "Farm House",
      "upgrades": 0,
      "owner": null,
      "isMortgaged": 0, // boolean
      "rent": {
        "0": 20,
        "1": 100,
        "2": 300,
        "3": 750,
        "4": 925,
        "5": 1100
      },
      "mortgage": 120,
      "upgrade_cost": 150,
      "cost": 230
    }, {
      "name": "Union Drive",
      "owner": null,
      "isMortgaged": 0, // boolean
      "rent": {
        "0": 25,
        "1": 50,
        "2": 100,
        "3": 200,
      },
      "mortgage": 100,
      "cost": 250
    }, {
      "name": "Hilton Coliseum",
      "upgrades": 0,
      "owner": null,
      "isMortgaged": 0, // boolean
      "rent": {
        "0": 22,
        "1": 110,
        "2": 330,
        "3": 800,
        "4": 975,
        "5": 1150
      },
      "mortgage": 130,
      "upgrade_cost": 150,
      "cost": 250
    }, {
      "name": "Jack Trice Stadium",
      "upgrades": 0,
      "owner": null,
      "isMortgaged": 0, // boolean
      "rent": {
        "0": 22,
        "1": 110,
        "2": 330,
        "3": 800,
        "4": 975,
        "5": 1150
      },
      "mortgage": 130,
      "upgrade_cost": 150,
      "cost": 250
    }, {
      "name": "Lancelot and Elaine",
      "owner": null,
      "isMortgaged": 0, // boolean
      "rent": {
        "0": "2x number on dice",
        "1": "10x number on dice"
      },
      "mortgage": 75,
      "cost": 175
    }, {
      "name": "State Gym",
      "upgrades": 0,
      "owner": null,
      "isMortgaged": 0, // boolean
      "rent": {
        "0": 24,
        "1": 120,
        "2": 360,
        "3": 850,
        "4": 1025,
        "5": 1200
      },
      "mortgage": 140,
      "upgrade_cost": 150,
      "cost": 270
    }, {
      "name": "Put on Academic Probation",
    }, {
      "name": "Parks Library",
      "upgrades": 0,
      "owner": null,
      "isMortgaged": 0, // boolean
      "rent": {
        "0": 26,
        "1": 130,
        "2": 390,
        "3": 900,
        "4": 1100,
        "5": 1275
      },
      "mortgage": 150,
      "upgrade_cost": 200,
      "cost": 290
    }, {
      "name": "Memorial Union",
      "upgrades": 0,
      "owner": null,
      "isMortgaged": 0, // boolean
      "rent": {
        "0": 26,
        "1": 130,
        "2": 390,
        "3": 900,
        "4": 1100,
        "5": 1275
      },
      "mortgage": 150,
      "upgrade_cost": 200,
      "cost": 290
    }, {
      "name": "Letter From Home",
    }, {
      "name": "Armory",
      "upgrades": 0,
      "owner": null,
      "isMortgaged": 0, // boolean
      "rent": {
        "0": 28,
        "1": 150,
        "2": 450,
        "3": 1000,
        "4": 1200,
        "5": 1400
      },
      "mortgage": 160,
      "upgrade_cost": 200,
      "cost": 310
    }, {
      "name": "Duff Avenue",
      "owner": null,
      "isMortgaged": 0, // boolean
      "rent": {
        "0": 25,
        "1": 50,
        "2": 100,
        "3": 200,
      },
      "mortgage": 100,
      "cost": 250
    }, {
      "name": "Chance"
    }, {
      "name": "Curtiss Hall",
      "upgrades": 0,
      "owner": null,
      "isMortgaged": 0, // boolean
      "rent": {
        "0": 35,
        "1": 170,
        "2": 500,
        "3": 1000,
        "4": 1300,
        "5": 1500
      },
      "mortgage": 175,
      "upgrade_cost": 200,
      "cost": 375
    }, {
      "name": "Parking Ticket",
    }, {
      "name": "Beardshear Hall",
      "upgrades": 0,
      "owner": null,
      "isMortgaged": 0, // boolean
      "rent": {
        "0": 50,
        "1": 200,
        "2": 600,
        "3": 1400,
        "4": 1700,
        "5": 2000
      },
      "mortgage": 200,
      "upgrade_cost": 200,
      "cost": 475
    },


  ]

}
