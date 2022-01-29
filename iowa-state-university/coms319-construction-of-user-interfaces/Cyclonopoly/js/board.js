var num_players;
var outputstr = "";
var players = ""; //player string that is displayed on the screen
var turn;
var sum; //sum of dice roll
var beginning = 0;
var ACADEMIC_PROBATION = 31;
var HOME = 11;
var freeParkingMoney = 100; //set to 100 to initially have amount there
var diceOne;
var diceTwo;
var doubleCount = 0;
var getOutFree = false;



function rollDiceV2() {
	var dice1, dice2;
	var dices = ['&#9856;', '&#9857;', '&#9858;', '&#9859;', '&#9860;', '&#9861;'];
	var stopped = true;
	var t;

	//TODO -- Add Padding to box so die are centered (because I don't know how).
	var dialog = bootbox.dialog({
		className: "card-popup",
		message: '<div class ="row">' +
			'<div class="col-md-2"/>' +
			'<div class="col-md-4">' +
			'<div id="newdice1" style="font-size:200px"></div>' +
			'</div>' +
			'<div class="col-md-4">' +
			'<div id="newdice2" style="font-size:200px"></div>' +
			'</div>' +
			'<div class="col-md-2"/>' +
			'</div><p id="p_roll_result"><p>',
		closeButton: false
	});
	dice1 = document.getElementById("newdice1");
	dice2 = document.getElementById("newdice2");
	stopstart();
	setTimeout(function() {
		stopstart();
		document.getElementById("p_roll_result").innerHTML = player[turn].name +
			" rolled a " + sum;
		setTimeout(function() {
			dialog.modal('hide');
			var todelete = document.getElementById("img_player_" + turn);
			todelete.parentNode.removeChild(todelete);
			checkPassGo();
			checkJail();
			player[turn].location = (player[turn].location + sum) < 41 ? player[turn]
				.location + sum : player[turn].location + sum - 40;
			sum ///JUST LIKE THIS!!! LOOK IT GOES DOWN!!!
			document.getElementById("div_foreground_" + player[turn].location).innerHTML +=
				'<img id="img_player_' + turn + '" src="img/icon' + turn +
				'.jpg" style="height:4.5vmin;width:4.5vmin;float:left;">';
			checkOtherSpaces(sum);
		}, 2000);
	}, 2000);
	//dialog.modal('hide');

	function change() {
		diceOne = Math.floor(Math.random() * 6);
		diceTwo = Math.floor(Math.random() * 6);
		dice1.innerHTML = dices[diceOne];
		dice2.innerHTML = dices[diceTwo];
		sum = diceOne + diceTwo + 2;
	}

	function stopstart() {
		if (stopped) {
			stopped = false;
			t = setInterval(change, 100);
		} else {
			clearInterval(t);
			stopped = true;
		}

	}
}

function rollDiceForCampusMailCard() {
	var dice1, dice2;
	var dices = ['&#9856;', '&#9857;', '&#9858;', '&#9859;', '&#9860;', '&#9861;'];
	var stopped = true;
	var t;


	var dialog = bootbox.dialog({
		message: '<div class ="row">' +
			'<div class="col-md-4">' +
			'<div id="newdice1" style="font-size:200px"></div>' +
			'</div>' +
			'<div class="col-md-4">' +
			'<div id="newdice2" style="font-size:200px"></div>' +
			'<div>' +
			'</div><p id="p_roll_result"><p>',
		closeButton: false
	});
	dice1 = document.getElementById("newdice1");
	dice2 = document.getElementById("newdice2");
	stopstart();
	setTimeout(function() {
		stopstart();
		document.getElementById("p_roll_result").innerHTML = player[turn].name +
			" rolled a " + sum;
		setTimeout(function() {
			dialog.modal('hide');
			var todelete = document.getElementById("img_player_" + turn);
			todelete.parentNode.removeChild(todelete);
			checkPassGo();
			checkJail();
			player[turn].location = (player[turn].location - sum) > 0 ? player[turn]
				.location - sum : player[turn].location - sum + 40;
			document.getElementById("div_foreground_" + player[turn].location).innerHTML +=
				'<img id="img_player_' + turn + '" src="img/icon' + turn +
				'.jpg" style="height:4.5vmin;width:4.5vmin;float:left">';
			checkOtherSpaces(sum);
		}, 2000);
	}, 2000);
	//dialog.modal('hide');

	function change() {
		diceOne = Math.floor(Math.random() * 6);
		diceTwo = Math.floor(Math.random() * 6);
		dice1.innerHTML = dices[diceOne];
		dice2.innerHTML = dices[diceTwo];
		sum = diceOne + diceTwo + 2;
	}

	function stopstart() {
		if (stopped) {
			stopped = false;
			t = setInterval(change, 100);
		} else {
			clearInterval(t);
			stopped = true;
		}

	}
}

function checkJail() {
	if (player[turn].jail == 1) {
		//if have card to get out of jail

		if (player[turn].getOutFree == true) {
			//ask player if they would like to purchase the spot
			var CardName = 'img/' + (player[turn].location) + 'Card.jpg';
			bootbox.confirm({
				className: "getoutfree-popup",
				message: "<h2>Would you like to use your Leave Home Free card?</h2>",
				buttons: {
					cancel: {
						label: '<i class="fa fa-times"></i> No'
					},
					confirm: {
						label: '<i class="fa fa-check"></i> Yes'
					}
				},
				callback: function(result) {
					if (result == true) {
						//player gets out and looses getOutFree card
						getOutFree = false;
						player[turn].jail = 0;

					} else {
						//do nothing
					}
				}
			});

		}
		// Jailed player did not roll doubles
		if (diceOne != diceTwo) {
			player[turn].numJailTurns = player[turn].numJailTurns - 1;
			sum = 0;

			if (player[turn].numJailTurns == 0) {
				player_pay_cash(turn, 50);
				player_remove_jail(turn);
			}
		} else
			player_remove_jail(turn);
	}
}

function finishTurn() {
	//mortgage properties if player has no money
	var cashadded = 0;
	if (player[turn].cash <= 0) {
		for (i = 0; i < properties.length; i++) {
			if (properties[i].owner != undefined) {
				if (properties[i].owner == turn && properties[i].isMortgaged == 0) {
					properties[i].isMortgaged = 1;
					cashadded += properties[i].mortgage;
				}
			}
		}
		player_add_cash(turn, cashadded);
		if (player[turn].cash <= 0) {
			//remove player from game somehow.
			var todelete = document.getElementById("img_player_" + turn);
			todelete.parentNode.removeChild(todelete);
			player[turn].location = -1;

		} else {
			alert("all properties mortgaged. " + cashadded + " added to your account.");
		}
	}

	//Handles losing turn or updating turn
	if (checkGameStatus()) {
		for (var i = 1; i < num_players; i++) {
			if (diceOne != diceTwo) {
				doubleCount = 0;
				if (player[(turn + i) % num_players].loseTurn == 0) {
					turn = (turn + i) % num_players;
					break;
				} else {
					player[(turn + i) % num_players].loseTurn = Math.max(0, player[(turn + i) %
						num_players].loseTurn - 1);
				}
			}
			//Handles doubles
			else {
				if (player[turn].loseTurn != 0) {
					player[(turn + i) % num_players].loseTurn = Math.max(0, player[(turn + i) %
						num_players].loseTurn - 1);
					turn = (turn + i) % num_players;
					doubleCount = 0;
					break;
				}
				doubleCount++;
				//Go to jail from 3 doubles
				if (doubleCount == 3) {
					movePlayerLocation(HOME);
					player_put_jail(turn);
					doubleCount = 0;
					turn = (turn + 1) % num_players;
				}
				break;
			}
		}
		while (player[turn].location == -1) {
			turn = (turn + 1) % num_players;
		}
		document.getElementById("btn_roll").innerHTML = player[turn].name +
			"\'s turn: roll dice"
	} else {
		var winner;
		for (i = 0; i < player.length; i++) {
			if (player[i].cash > 0) {
				winner = player[i];
				break;
			}
		}
		if (winner != null) {
			bootbox.alert(winner.name + " won the game!", function() {
				clearGame();
				gameSetup();
			});
		} else {
			bootbox.alert("error in end of game");
		}
	}
}

// Clears the data from the game.
function clearGame() {
	// Removes the icons from the board.
	for (var i = 0; i < num_players; i++) {
		var todelete = document.getElementById("img_player_" + i);
		todelete.parentNode.removeChild(todelete);
	}

	num_players = 0;
	outputstr = "";
	players = "";
	beginning = 0;
	freeParkingMoney = 100;
	player = [];
}

function saveGame() {
	var webMethod, postObj, props;

	webMethod = 'php/save.php';
	postObj = {};
	props = [];
	j = 0;
	for (i = 0; i < properties.length; i++) {
		if (properties[i].owner !== undefined) {
			props.push(properties[i]);
			props[j].location = i;
			if (properties[i].upgrades === undefined) {
				props[j].upgrades = null;
			}
			j++;
		}
	}
	postObj.turn = turn;
	postObj.props = props;
	postObj.players = player;

	postData(webMethod, postObj, handleSave);
}

postData = function(webMethod, dataObj, handlerCallBackFunc) {
	"use strict";
	$.ajax({
		url: webMethod,
		data: JSON.stringify(dataObj),
		dataType: 'json',
		type: 'POST',
		async: true,
		contentType: 'application/json; charset=utf-8',
		beforeSend: function() {
			//alert('sending');
		},
		complete: function() {
			//alert('complete');
		},
		success: function(jsondata, textStatus, jqXHR) {
			//alert ('success');
			handlerCallBackFunc(jsondata);
		},
		error: function(xhr, status, error) {
			//alert ('error');
			if (xhr.status === 0 && status === 'error' && error === '') {
				alert('NoXX response from server at: ' + this.url);
			} else {
				alert('Error: ' + error + '\nserver says: ' + xhr.responseText);
			}
		}
	})
};

handleSave = function(jsondata) {
	console.log(jsondata);
	bootbox.alert("Game saved. Your game ID is " + jsondata.details);
}

function loadGame() {
	bootbox.prompt("Please enter the ID of your saved game", function(result) {
		if (result === null || parseInt(result) === NaN || result < 0) {
			alert('please enter an integer ID');
		} else {
			var webMethod, postObj, props;

			webMethod = 'php/load.php';
			postObj = {};
			postObj.id = result;

			postData(webMethod, postObj, handleLoad);
		}
	});
}

handleLoad = function(jsondata) {
	console.log(jsondata);
	if (jsondata.success && jsondata.details.props !== undefined && jsondata.details
		.players !== undefined) {
		//reset properties
		initProps();
		//restore property values
		for (var i = 0; i < jsondata.details.props.length; i++) {
			properties[jsondata.details.props[i].location].owner = jsondata.details.props[
				i].owner;
			properties[jsondata.details.props[i].location].upgrades = jsondata.details.props[
				i].upgrades;
		}
		//reset player locations
		for (i = 0; i < 8; i++) {
			var todelete = document.getElementById("img_player_" + i);
			if (todelete !== null) {
				todelete.parentNode.removeChild(todelete);
			}
		}
		//restore player ownLocationList
		player = [];
		num_players = jsondata.details.players.length;
		for (i = 0; i < jsondata.details.players.length; i++) {
			player.push({
				"name": jsondata.details.players[i].name,
				"cash": parseInt(jsondata.details.players[i].cash),
				"location": parseInt(jsondata.details.players[i].location),
				"jail": parseInt(jsondata.details.players[i].jail),
				"numJailTurns": parseInt(jsondata.details.players[i].numjailturns)
			})
			document.getElementById("div_foreground_" + jsondata.details.players[i].location)
				.innerHTML += '<img id="img_player_' + i + '" src="img/icon' + i +
				'.jpg" style="height:4.5vmin;width:4.5vmin;float:left">';
		}
		outputPlayersList();

		turn = jsondata.details.game[0].turn;
		document.getElementById("btn_roll").innerHTML = player[turn].name +
			"\'s turn: roll dice"

	}
}

function checkOtherSpaces(amountRolled) {
	//Contingency Card
	if (player[turn].location == 8 || player[turn].location == 23 || player[turn].location ==
		37) {
		contingencyTurn();
		finishTurn();
	}
	//Campus Mail Card
	else if (player[turn].location == 3 || player[turn].location == 18 || player[
			turn].location == 34) {
		campusMailTurn();
		finishTurn();
	}
	//tuition increase  location 5
	else if (player[turn].location == 5) {
		freeParkingMoney += 200;
		player_pay_cash(turn, 200);
		finishTurn();
	}
	//parking ticket
	else if (player[turn].location == 39) {
		freeParkingMoney += 75;
		player_pay_cash(turn, 75);
		finishTurn();
	}

	//free parking
	else if (player[turn].location == 21) {
		player_add_cash(turn, freeParkingMoney);
		freeParkingMoney = 100;
		finishTurn();
	}
	//parking ticket
	else if (player[turn].location == 39) {
		player_pay_cash(turn, 75);
		finishTurn();
	}
	//cy
	else if (player[turn].location == 13) {
		var changeamount = -1 + amountRolled;

		if (changeamount + player[turn].location > 40) {
			changeamount = changeamount - 40 + player[turn].location;
		}

		//if not a spot that can be bought
		if (properties[player[turn].location - 1].owner != undefined) {
			//check if spot has not been bought yet
			if (properties[player[turn].location - 1].owner == null) {
				//if has money to buy, buy it
				if (player[turn].cash >= properties[player[turn].location - 1].cost) {
					//ask player if they would like to purchase the spot
					var CardName = 'img/' + (player[turn].location) + 'Card.jpg';
					bootbox.confirm("<h2>Would you like to buy Cy?</h2><br><img src='" +
						CardName + "'>",
						function(result) {
							if (result == true) {
								//take money from player
								player_pay_cash(turn, properties[player[turn].location - 1].cost);

								//asign player as owner to spot
								properties[player[turn].location - 1].owner = turn;
								finishTurn();
								outputPlayersList();
							} else {
								finishTurn();
							}
						});



				} else {
					finishTurn();
				}

				//if does not have enough money to buy, do nothing

			} else { //space has an owner. you must pay him/her
				//check if lancelot and cy have same owner
				var amountOwe = 0;
				if (player[properties[player[turn].location - 1].owner] == player[
						properties[29].owner]) {
					amountOwe = amountRolled * 10;
				} else {
					amountOwe = amountRolled * 4;
				}
				player_pay_cash(turn, amountOwe);
				player_add_cash(properties[player[turn].location - 1].owner, amountOwe);
				finishTurn();
			}
		} else {
			finishTurn();
		}
	}
	//lancelot
	else if (player[turn].location == 29) {

		var changeamount = -1 + amountRolled;

		if (changeamount + player[turn].location > 40) {
			changeamount = changeamount - 40 + player[turn].location;
		}
		//if not a spot that can be bought
		if (properties[player[turn].location - 1].owner != undefined) {
			//check if spot has not been bought yet
			if (properties[player[turn].location - 1].owner == null) {
				//if has money to buy, buy it
				if (player[turn].cash >= properties[player[turn].location - 1].cost) {
					//ask player if they would like to purchase the spot
					var CardName = 'img/' + (player[turn].location) + 'Card.jpg';
					setTimeout(function() {
						bootbox.confirm(
							"<h2>Would you like to buy Lancelot and Elaine?</h2><br><img src='" +
							CardName + "'>",
							function(result) {
								if (result == true) {
									//take money from player
									player_pay_cash(turn, properties[player[turn].location - 1].cost);

									//asign player as owner to spot
									properties[player[turn].location - 1].owner = turn;
									finishTurn();
									outputPlayersList();
								} else {
									finishTurn();
								}
							});
					}, 700);
				} else {
					finishTurn();
				}

				//if does not have enough money to buy, do nothing

			} else { //space has an owner. you must pay him/her
				//check if lancelot and cy have same owner
				var amountOwe = 0;
				if (player[properties[player[turn].location - 1].owner] == player[
						properties[13].owner]) {
					amountOwe = amountRolled * 10;
				} else {
					amountOwe = amountRolled * 4;
				}
				player_pay_cash(turn, amountOwe);
				player_add_cash(properties[player[turn].location - 1].owner, amountOwe);
				finishTurn();
			}
		} else {
			finishTurn();
		}
	}
	//roads
	else if (player[turn].location == 6 || player[turn].location == 36 || player[
			turn].location == 16 || player[turn].location == 26) {
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
						bootbox.confirm(
							"<h2>Would you like to buy this property?</h2><br><img src='" +
							CardName + "'>",
							function(result) {
								if (result == true) {
									//take money from player
									player_pay_cash(turn, properties[player[turn].location - 1].cost);

									//asign player as owner to spot
									properties[player[turn].location - 1].owner = turn;
									finishTurn();
									outputPlayersList();
								} else {
									finishTurn();
								}
							});
					}, 700);
				} else {
					finishTurn();
				}

				//if does not have enough money to buy, do nothing

			} else { //space has an owner. you must pay him/her
				//check how many roads are owned by same person
				var amountOwe = 0;
				var howmanyowned = 0;
				//welch
				if (player[properties[player[turn].location - 1].owner] == player[
						properties[6].owner]) {
					//amountOwe = amountRolled *10;
					howmanyowned++;
				} else if (player[properties[player[turn].location - 1].owner] == player[
						properties[26].owner]) {
					howmanyowned++;
				} else if (player[properties[player[turn].location - 1].owner] == player[
						properties[36].owner]) {
					howmanyowned++;
				} else if (player[properties[player[turn].location - 1].owner] == player[
						properties[16].owner]) {
					howmanyowned++;
				}
				if (howmanyowned == 1) {
					amountOwe = 25;
				} else if (howmanyowned == 2) {
					amountOwe = 50;
				} else if (howmanyowned == 3) {
					amountOwe = 150;
				} else if (howmanyowned == 4) {
					amountOwe = 200;
				}
				player_pay_cash(turn, amountOwe);
				player_add_cash(properties[player[turn].location - 1].owner, amountOwe);
				finishTurn();
			}
		} else {
			finishTurn();
		}

	} else if (player[turn].location == ACADEMIC_PROBATION) {
		movePlayerLocation(HOME);
		player_put_jail(turn);
	} else {
		//property space
		propertyturn(amountRolled);
	}

	outputPlayersList();
}

//if you land on a contingency card
function contingencyTurn() {

	var cardNum = Math.floor(Math.random() * 14) + 1;
	var CardName = 'img/CC' + JSON.stringify(cardNum) + '.jpg';

	//Should be 3 but I guess it works with 2...
	if (doubleCount == 2) {
		finishTurn();
		return;
	}

	setTimeout(function() {
		bootbox.alert({
			size: "medium",
			className: "card-popup",
			message: '<b><h2>Contingency Card:  ' +
				'<br><br/>Player ' + JSON.stringify(turn + 1) + ': ' + player[turn].name +
				'<br><br/><img style="width: 300px" src=' + CardName + '><b></h2>'
		});
	}, 700);

	var i;
	var howMuchMoney;
	var sum;
	switch (cardNum) {
		//Pay $200
		case 1:
			freeParkingMoney += 200;
			player_pay_cash(turn, 200);
			break;
			//Pay each player $200
		case 2:
			howMuchMoney = 0;
			if (player[turn].cash >= 200 * (num_players - 1))
				howMuchMoney = 200;
			else
				howMuchMoney = Math.floor(player[turn].cash / (num_players - 1));

			player_pay_cash(turn, 200 * (num_players - 1));

			for (i = 0; i < num_players; i++) {
				if (i != turn)
					player_add_cash(i, howMuchMoney);
			}
			break;
			//Move to Parks Library
		case 3:
			movePlayerLocation(32);
			break;
			//Pay $100 into freeParking
		case 4:
			freeParkingMoney += 100;
			player_pay_cash(turn, 100);
			break;
			//Move to Jack Trice Stadium
		case 5:
			movePlayerLocation(28);
			break;
			//Collect $50 from bank
		case 6:
			player_add_cash(turn, 50);
			break;
			//Collect $5 from each player
		case 7:
			sum = 0;
			howMuchMoney = 0;
			for (i = 0; i < num_players; i++) {
				if (i != turn) {
					if (player[i].cash >= 5) {
						player_pay_cash(i, 5);
						howMuchMoney = 5;
					} else {
						howMuchMoney = player[i].cash;
						player[i].cash = 0;
					}
					sum += howMuchMoney;
				}
			}
			player_add_cash(turn, sum);
			break;
			//Move to Academic Probation
		case 8:
			movePlayerLocation(HOME);
			player_put_jail(turn);
			turn = (turn + 1) % num_players;
			break;
			//Collect $200 from scholarship
		case 9:
			player_add_cash(turn, 200);
			break;
			//Collect $25 from each player
		case 10:
			sum = 0;
			for (i = 0; i < num_players; i++) {
				if (i != turn) {
					if (player[i].cash >= 25) {
						player_pay_cash(i, 25);
						howMuchMoney = 25;
					} else {
						howMuchMoney = player[i].cash;
						player[i].cash = 0;
					}
					sum += howMuchMoney;
				}
			}
			player_add_cash(turn, sum);
			break;
			//Move to Campanile with another player
		case 11:
			bootbox.prompt(
				"Enter the player number that you would like to take with you to the Campanile!",
				function(result) {
					result -= 1;
					moveOtherPlayerLocation(20, result);
					moveMultiplePlayersLocation(20);
				});
			break;
			//Lose 2 turns
		case 12:
			player[turn].loseTurn = 2;
			break;
			//Move to free parking and collect it
		case 13:
			movePlayerLocation(21);
			player_add_cash(turn, freeParkingMoney);
			freeParkingMoney = 100;
			break;
			//Move to Reimain Gardens
		case 14:
			movePlayerLocation(12);
			break;
	}
}

//If you land on a campus mail card
function campusMailTurn() {

	var cardNum = /*Math.floor(Math.random() * 14)+1*/ 2;
	var CardName = 'img/CM' + JSON.stringify(cardNum) + '.jpg';

	//Should be 3 but I guess it works with 2...
	if (doubleCount == 2) {
		finishTurn();
		return;
	}

	setTimeout(function() {
		bootbox.alert({
			className: "card-popup",
			message: '<b><h2>Campus Mail Card:  ' +
				'<br><br/>Player ' + JSON.stringify(turn + 1) + ':  ' + player[turn].name +
				'<br><br/><img style="width: 300px" src=' + CardName + '><b></h2>'
		});
	}, 700);

	var i;
	var howMuchMoney;
	var sum;
	switch (cardNum) {
		//Pay $25 for each year of credit, $100 for each diploma
		case 1:
			freeParkingMoney += Math.min(player[turn].numD * 100 + player[turn].numYOC *
				25, player[turn].cash);
			player_pay_cash(turn, player[turn].numD * 100 + player[turn].numYOC * 25)
			break;
			//Lose a turn
		case 2:
			player[turn].loseTurn = 1;
			break;
			//Collect $100
		case 3:
			player_add_cash(turn, 100);
			break;
			//Pay $100
		case 4:
			freeParkingMoney += 100;
			player_pay_cash(turn, 100);
			break;
			//Pay $50
		case 5:
			freeParkingMoney += 50;
			player_pay_cash(turn, 50);
			break;
			//Leave home free
		case 6: //TODO implement ability to hold card
			getOutFree = true;
			break;
			//Collect $100
		case 7:
			player_add_cash(turn, 100);
			break;
			//Pay $80
		case 8:
			freeParkingMoney += 80;
			player_pay_cash(turn, 80);
			break;
			//Pay $50
		case 9:
			freeParkingMoney += 50;
			player_pay_cash(turn, 50);
			break;
			//Roll again, but backwards
		case 10:
			rollDiceForCampusMailCard();
			break;
			//Pay $20
		case 11:
			freeParkingMoney += 20;
			player_pay_cash(turn, 20);
			break;
			//Collect $100
		case 12:
			player_add_cash(turn, 100);
			break;
			//Lose a turn
		case 13:
			player[turn].loseTurn += 1;
			break;
			//Move to Academic Dishonesty
		case 14:
			movePlayerLocation(HOME);
			player_put_jail(turn);
			turn = (turn + 1) % num_players;
			break;
	}

}

function checkPassGo() { //check which space the player is on and then do what the square wants you to do
	//pass go (done)
	if (beginning > 8) {
		for (i = 0; i < sum; i++) {
			if (player[turn].location + i + 1 == 41) {
				player_pay_cash(turn, 200);
			}
		}
	} else {
		beginning++;
	}
	outputPlayersList();
}

// Function to check if the game is over.
function checkGameStatus() {
	counter = 0;
	// Count the number of players with a positive cash balance.
	for (i = 0; i < num_players; i++) {
		if (player[i].cash > 0) {
			counter++;
		}
	}

	// If the number of players with a positive cash balance is greater than 1, the game continues. Else, the game is over.
	if (counter > 1)
		return true;
	else
		return false;
}

/*
DEPRECATED

function rollDice()
{
	const diceSide1= document.getElementById('diceside1');
	const diceSide2 = document.getElementById('diceside2');
	//const status = document.getElementById('


	const side1 = Math.floor(Math.random() *6)+1;
	const side2 = Math.floor(Math.random() *6)+1;


	diceSide1.innerHTML = side1;
	diceSide2.innerHTML = side2;

}*/

/**
 * This function initializes the properties, gets the players names, and assigns starting cash for each player
 */
function gameSetup() {
	turn = 0;
	properties = initProps();
	//console.log(properties);
	//num_players = prompt("Cyclonopoly: an Iowa State themed game sure to be lots of fun for you and your friends!\nEnter the number of players (2 to 8 players):", "");

	bootbox.prompt(
		"Cyclonopoly: an Iowa State themed game sure to be lots of fun for you and your friends!<br>Enter the names of players seperated by semi-colons (;) (2 to 8 players)",
		function(result) {
			if (result != null) {
				var input = result.split(';');
				num_players = Math.min(8, input.length);
			}

			if (input == null || input.length < 2) {
				player = [{
					"name": "Player 1",
					"cash": 1650,
					"location": 1,
					"loseTurn": 0,
					"jail": 0,
					"numJailTurns": 0,
					"numYOC": 0,
					"numD": 0
				}, {
					"name": "Player 2",
					"cash": 1650,
					"location": 1,
					"loseTurn": 0,
					"jail": 0,
					"numJailTurns": 0,
					"numYOC": 0,
					"numD": 0
				}]
				num_players = 2;
				for (i = 0; i < num_players && i < 8; i++) {
					outputstr += "Player " + (i + 1) + ": " + player[i].name + ", Cash: " +
						player[i].cash + "<br>";
					document.getElementById("div_foreground_1").innerHTML +=
						'<img id="img_player_' + i + '" src="img/icon' + i +
						'.jpg" style="height:4.5vmin;width:4.5vmin;float:left">';
				}
			} else {

				for (i = 0; i < num_players && i < 8; i++) {
					var tmp_name = input[i].trim();
					player[i] = {
						"name": tmp_name,
						"cash": 1650,
						"location": 1,
						"loseTurn": 0,
						"jail": 0,
						"numJailTurns": 0,
						"numYOC": 0,
						"numD": 0
					};
					outputstr += "Player " + (i + 1) + ": " + player[i].name + ", Cash: " +
						player[i].cash + "<br>"; //mess with till it works.
					document.getElementById("div_foreground_1").innerHTML +=
						'<img id="img_player_' + i + '" src="img/icon' + i +
						'.jpg" style="height:4.5vmin;width:4.5vmin;float:left">';
				}
			}
			document.getElementById("btn_roll").innerHTML = player[0].name +
				"\'s turn: roll dice"
			outputPlayersList();
		});
}

/**
 * function to display players names on screen along with the cash that they have so throughout game play, they can see how much money they have
 */
function outputPlayersList() {
	players = "Players: ";
	document.getElementById("playersList").innerHTML = players;
	for (j = 0; j < num_players; j++) {
		players = '<img src="img/icon' + j +
			'.jpg" style="height:4.5vmin;width:4.5vmin;float:left">' +
			"Player " + (j + 1) + ": " + player[j].name + ", Cash: " + player[j].cash;

		document.getElementById("player" + (j + 1)).innerHTML = players;
		jQuery('#player' + (j + 1)).height('4vmin');

	}

}
