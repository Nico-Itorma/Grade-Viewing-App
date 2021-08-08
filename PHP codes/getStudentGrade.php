<?php 
    require "Database.php";
    $db = new Database();

    $student_username = $_GET['student_username'];
    $subject_name = $_GET['subject_name'];
    $subject_YandB = $_GET['subject_YandB'];

    if (isset($student_username) && isset($subject_name) && isset($subject_YandB))
    {
        $get_student_grade = "SELECT student_courses.student_grade FROM student_courses INNER JOIN subjects ON student_courses.subject_enrolled = subjects.subject_id
        WHERE student_courses.student_idNum = '$student_username' AND subjects.subject_name = '$subject_name' AND subjects.YearAndBlock = '$subject_YandB'";

        $get_student_grade_result = mysqli_query($db->dbConnect(), $get_student_grade);

        if (mysqli_num_rows($get_student_grade_result) > 0)
        {
            $row = mysqli_fetch_assoc($get_student_grade_result);
            echo $row['student_grade'];
        }
        else {
            echo "Student grade not found";
        }
    }
?>