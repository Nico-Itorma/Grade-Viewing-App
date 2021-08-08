<?php 
    require "Database.php";
    $db = new Database();

    $username = $_GET['username'];
    $password = $_GET['password'];
    if (isset($username) && isset($password))
    {
        if ($db->dbConnect())
        {
            //check if username exist in the database users
            $check_uName = "SELECT * FROM users WHERE username='$username'";
            $check_uname_result = mysqli_query($db->dbConnect(), $check_uName);
            if (mysqli_num_rows($check_uname_result) > 0)
            {
                $row = mysqli_fetch_assoc($check_uname_result);
                if ($username == $row['username'])
                {
                    $username = $db->prepareData($username);
                    $newPassword = password_hash($password, PASSWORD_DEFAULT);
                    $sql = "UPDATE users SET users.password = '$newPassword' WHERE users.username = '$username'";
        
                    if (mysqli_query($db->dbConnect(), $sql))
                    {
                        echo "Password changed";
                    }
                    else echo "Error: " .mysqli_error($db->dbConnect());
                }
            } else echo "Error: username not found";
        } 
        else 
        {
            echo "Error: " .mysqli_connect_error($db->dbConnect());
        }
    }

?>