<?php 

//function to display added subjects by the professor
    require "Database.php";
    $db = new Database();

    $name = $_GET['name'];
    if (isset($name)) {
        if ($db->dbConnect()) {
            $db->getUserUID($name);
        } else echo "Error: Database connection";
    }
?>