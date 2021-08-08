<?php 

//this will get the subjects for student in which the professor added
    require "Database.php";
    $db = new Database();

    $student_idNum = $_GET['student_idNum'];
    if (isset($student_idNum)) {
        if ($db->dbConnect()) 
        {
            $sql = "SELECT s.subject_name, s.YearAndBlock, users.name
            FROM (subjects s INNER JOIN users ON s.ProfName = users.uid)
            INNER JOIN student_courses ON s.subject_id = student_courses.subject_enrolled
            WHERE student_courses.student_idNum = '$student_idNum'
            ORDER BY s.subject_id";
            $result = mysqli_query($db->dbConnect(), $sql);
            $json_array = array();
    
            if (mysqli_num_rows($result) > 0) {
                while ($row = mysqli_fetch_assoc($result)) {
                    $json_array[] = $row;
                }
                echo json_encode(['subject_list' => $json_array]);
            } else echo "NO_SUBJECT_FOUND";
        } else echo "Error: Database connection";
    }
?>