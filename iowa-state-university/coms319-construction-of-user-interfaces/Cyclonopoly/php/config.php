<?php
/*
  The important thing to realize is that the config file should be included in every
  page of your project, or at least any page you want access to these settings.
  This allows you to confidently use these settings throughout a project because
  if something changes such as your database credentials, or a path to a specific resource,
  you'll only need to update it here.
 */
$config = array(
    "db" => array(
        "A5" => array(
          "dbname" => "db319a5",
          "username" => "dbu319a5",
          "password" => "ZWvF!ZWB",
          "host" => "mysql.cs.iastate.edu",
          "PDO_DSN" => "mysql:dbname=db319a5;host=mysql.cs.iastate.edu",
          "RBConnection" => "mysql:host=mysql.cs.iastate.edu;dbname=db319a5",
          "RBFreeze" => "TRUE",
        )
    ),



);
