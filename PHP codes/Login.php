<?php
require "Database.php";
$db = new Database();
if (isset($_POST['username']) && isset($_POST['password'])) {
    if ($db->dbConnect()) 
    {
        $username = $_POST['username'];
        $password = $_POST['password'];
        $username = $db->prepareData($username);
        $password = $db->prepareData($password);
        $login_sql = "SELECT * FROM users WHERE username = '" .$username. "'";
        $result = mysqli_query($db->dbConnect(), $login_sql);
        $row = mysqli_fetch_assoc($result);

        if (mysqli_num_rows($result) != 0) {
            $dbusername = $row['username'];
            $dbpassword = $row['password'];

            if ($dbusername == $username && password_verify($password, $dbpassword)) 
            {
                echo "Login success";
            } 
            else echo "Username or Password wrong";
        } else echo "Username not found";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>