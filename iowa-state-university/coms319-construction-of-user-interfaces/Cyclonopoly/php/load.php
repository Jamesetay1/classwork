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

  $sql = 'select * from player  where game_id= :id';
  $params[":id"] = $postdata["id"];

  $players = R::getAll($sql, $params);

  $sql = 'select * from location  where game_id= :id';
  $params[":id"] = $postdata["id"];

  $props = R::getAll($sql, $params);

  $sql = 'select turn from game  where id= :id';
  $params[":id"] = $postdata["id"];

  $turn = R::getAll($sql, $params);

  $status["details"]["game"] = $turn;
  $status["details"]["props"] = $props;
  $status["details"]["players"] = $players;
  $status["success"] = true;


header('Content-type: application/json');
echo json_encode($status);
