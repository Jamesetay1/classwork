var player = [];

function moveMultiplePlayersLocation(moveTo) {
  turn -= 1;
  var toDelete = document.getElementById("img_player_" + turn);
  toDelete.parentNode.removeChild(toDelete);
  player[turn].location = moveTo;
  document.getElementById("div_foreground_" + player[turn].location).innerHTML +=
    '<img id="img_player_' + turn + '" src="img/icon' + turn +
    '.jpg" style="height:4.5vmin;width:4.5vmin;float:left">';
  turn += 1;
}

//Moves Player to some location passed in from moveTo
function movePlayerLocation(moveTo) {
  var toDelete = document.getElementById("img_player_" + turn);
  toDelete.parentNode.removeChild(toDelete);
  player[turn].location = moveTo;
  document.getElementById("div_foreground_" + player[turn].location).innerHTML +=
    '<img id="img_player_' + turn + '" src="img/icon' + turn +
    '.jpg" style="height:4.5vmin;width:4.5vmin;float:left">';
}

function moveOtherPlayerLocation(moveTo, playerToMove) {
  var toDelete = document.getElementById("img_player_" + playerToMove);
  toDelete.parentNode.removeChild(toDelete);
  player[playerToMove].location = moveTo;
  document.getElementById("div_foreground_" + player[playerToMove].location).innerHTML +=
    '<img id="img_player_' + playerToMove + '" src="img/icon' + playerToMove +
    '.jpg" style="height:4.5vmin;width:4.5vmin;float:left">';
}

function player_pay_cash(player_num, amount) {
  //players can temporarily go negative
  player[player_num].cash = player[player_num].cash - parseInt(amount);
}

function player_add_cash(player_num, amount) {
  player[player_num].cash = player[player_num].cash + parseInt(amount);
}

function player_remove_jail(player_num) {
  player[player_num].jail = 0;
}

function player_put_jail(player_num) {
  player[player_num].jail = 1;
  player[player_num].numJailTurns = 3;
}
