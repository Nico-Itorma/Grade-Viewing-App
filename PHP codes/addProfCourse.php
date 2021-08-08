<?php

//function to add a subject or course when professor click the + button in app
    require "Database.php";
    $db = new Database();
    if (isset($_POST['subject_name']) && isset($_POST['ProfName']) && isset($_POST['YearAndBlock']) && isset($_POST['Department'])) {
        if ($db->dbConnect()) 
        {
            $course_name = $_POST['subject_name'];
            $prof_name = $_POST['ProfName'];
            $yearAndBlock = $_POST['YearAndBlock'];
            $college = $_POST['Department'];

            $course_name = $db->prepareData($course_name);
            $prof_name = $db->prepareData($prof_name);
            $yearAndBlock = $db->prepareData($yearAndBlock);
            $college = $db->prepareData($college);
        
            $sqlCheckProf = "SELECT * FROM users WHERE name='$prof_name'";
            $result = mysqli_query($db->dbConnect(), $sqlCheckProf);

            if (mysqli_num_rows($result) > 0)
            {
                $row = mysqli_fetch_assoc($result);
                if ($prof_name == $row['name']) 
                {
                    $prof_id = $row['uid'];
                    $add_course_sql = "INSERT INTO subjects (subject_name, ProfName, YearAndBlock, Department) VALUES ('" .$course_name. "','" .$prof_id. "','" . $yearAndBlock . "','" .$college. "')";
                    
                    if (mysqli_query($db->dbConnect(), $add_course_sql)) {
                        echo "Course added";
                    } else echo "Course not added";
                }   
            }      
        } else echo "Error: Database connection";
    } else echo "All fields are required";
?>