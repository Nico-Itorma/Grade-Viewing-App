<?php
require "Database.php";
$db = new Database();
if (isset($_POST['name']) && isset($_POST['username']) && isset($_POST['password']) && isset($_POST['userType'])) 
{
    if ($db->dbConnect()) 
    {
        if ($db->checkUser($_POST['username']) == true)
        {
            echo "Student ID Number already exist";
        } 
        else 
        {
            if ($db->signUp("users", $_POST['name'], $_POST['username'], $_POST['password'], $_POST['userType'])) 
            {
                echo "Sign up success";
            } 
            else echo "Sign up Failed";
        }        
    } 
    else echo "Error: Database connection";
} 
else echo "All fields are required";
?>