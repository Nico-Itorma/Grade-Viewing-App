<?php
    require "Database.php";
    $db = new Database();

    $student_name = $_GET['student_name'];
    $subject_code = $_GET['subject_code'];

    if (isset($student_name) && isset($subject_code)) {
        if ($db->dbConnect()) {
            if ($db->addStudentCourse($student_name, $subject_code)) {
                echo "Course added";
            }else echo "Error: Course not added";
        } else echo "Error: Database connection";
    } else echo "All fields are required";
?>