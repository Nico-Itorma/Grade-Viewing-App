<?php 

    //function to get the list of student enrolled in a course
    require "Database.php";
    $db = new Database();

    $subject_name = $_GET['subject_name'];
    $subject_yearAndBlock = $_GET['YandB'];
    if (isset($subject_name)) {
        if ($db->dbConnect()) 
        {
            $subject_name = $db->prepareData($subject_name);
            $subject_yearAndBlock = $db->prepareData($subject_yearAndBlock);

            $select_student_from_subject = "SELECT users.name, users.username, student_courses.student_grade
            FROM ((users 
                  INNER JOIN student_courses ON student_courses.student_idNum = users.username)
                  INNER JOIN subjects ON student_courses.subject_enrolled = subjects.subject_id)
            WHERE subjects.subject_name = '$subject_name' AND subjects.YearAndBlock = '$subject_yearAndBlock'
            ORDER BY users.uid";      
            $select_student_from_subject_result = mysqli_query($db->dbConnect(), $select_student_from_subject);
                
            $json_array = array();
            if (mysqli_num_rows($select_student_from_subject_result) > 0)
            {
                while ($row = mysqli_fetch_assoc($select_student_from_subject_result)) {
                    $json_array[] = $row;                                            
                }
                echo json_encode(['Student_List' => $json_array]);
            }
            else echo "Subject ID not found";

        } else echo "Error: Database connection";
    }
?>