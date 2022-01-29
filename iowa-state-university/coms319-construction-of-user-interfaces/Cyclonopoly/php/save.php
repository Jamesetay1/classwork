<?php

require_once("config.php");
require_once("rb.php");

$postdata = json_decode(file_get_contents('php://input'), true);

R::setup($config['db']['A5']['RBConnection'], $config['db']['A5']['username'], $config['db']['A5']['password'], $config['db']['A5']['RBFreeze']);

$status = array(
            "success" => true,
            "errorlist" => array(),
            "details" => array()
        );

  $game = R::dispense( 'game' );

  $game->turn = $postdata["turn"];

  foreach($postdata["players"] as $player){
    $gameplayer = R::dispense('player');
    $gameplayer->name = $player["name"];
    $gameplayer->cash = $player["cash"];
    $gameplayer->location = $player["location"];
    $gameplayer->jail = $player["jail"];
    $gameplayer->numjailturns = $player["numJailTurns"];

    $game->ownPlayerList[] = $gameplayer;
  }

  foreach($postdata["props"] as $location){
    $gamelocation = R::dispense('location');
    $gamelocation->name = $location["name"];
    $gamelocation->owner = $location["owner"];
    $gamelocation->upgrades = $location["upgrades"];
    $gamelocation->location = $location["location"];

    $game->ownLocationList[] = $gamelocation;
  }

  $newgame = R::store($game);

  R::close();

  $status["details"] = $newgame;
  $status["success"] = true;


header('Content-type: application/json');
echo json_encode($status);
