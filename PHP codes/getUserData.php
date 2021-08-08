<?php 

//function to get the user data of the user including the name, username, and rank (student or professor)
    require "Database.php";
    $db = new Database();

    $username = $_GET['username'];
    if (isset($username)) {
        if ($db->dbConnect()) 
        {
            $username = $db->prepareData($username);
        
            $query_to_select_rows_from_one_user = "SELECT user.name, user.username, user.userType FROM users user WHERE username = '" .$username. "'"; 
            $result = mysqli_query($db->dbConnect(), $query_to_select_rows_from_one_user);
            $json_array = array();
        
            if (mysqli_num_rows($result) != 0)
            {
                while ($row = mysqli_fetch_assoc($result)) {
                    $json_array[] = $row;
                }
                echo json_encode(['userData' => $json_array]);
            } else echo "No_user_data_available";
        } else echo "Error: Database connection";
    }
    else echo "Unknown user";
?>