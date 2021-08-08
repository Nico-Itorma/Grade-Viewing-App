<?php
    require "Database.php";
    $db = new Database();

    $student_username = $_GET['student_username'];
    $subject_name = $_GET['subject_name'];
    $subject_YandB = $_GET['subject_YandB'];
    $grade = $_GET['student_grade'];
    $haveGrade = $_GET['haveGrade'];

    if (isset($student_username) && isset($subject_name) && isset($subject_YandB) && isset($grade) )
    {
        if ($db->dbConnect())
        {             
            //check if grade for student is null
            $update_grade = "SELECT student_courses.student_grade 
            FROM student_courses
            INNER JOIN subjects ON student_courses.subject_enrolled = subjects.subject_id 
            WHERE subjects.subject_name = '$subject_name' AND subjects.YearAndBlock = '$subject_YandB' AND student_courses.student_idNum = '$student_username'";
            $update_grade_result = mysqli_query($db->dbConnect(), $update_grade);

            if (mysqli_num_rows($update_grade_result) > 0)
            {  
                $row = mysqli_fetch_assoc($update_grade_result);      
                //add student_uid, subject_id and grade to the grades table
                $student_grade = $row['student_grade'];
            
                //grade is already assigned and prof want to override
                if ($student_grade != NULL)
                {
                    //show initially when passed param haveGrade is false
                    if ($haveGrade == 'false')
                    {
                        echo "Grade exist";
                    }
                    
                    if ($haveGrade == 'true')
                    {
                        $update_grade_for_student = "UPDATE student_courses INNER JOIN subjects ON student_courses.subject_enrolled = subjects.subject_id
                        SET student_courses.student_grade = '$grade'
                        WHERE student_courses.student_idNum = '$student_username'
                        AND subjects.subject_name = '$subject_name'
                        AND subjects.YearAndBlock = '$subject_YandB' ";
                        if (mysqli_query($db->dbConnect(), $update_grade_for_student)) {
                            echo "Grade added";
                        } else echo "Grade not added";
                    }
                }
                //no grade assigned
                else if ($student_grade == NULL && $haveGrade == 'false')
                {
                    $update_grade_for_student = "UPDATE student_courses INNER JOIN subjects ON student_courses.subject_enrolled = subjects.subject_id
                    SET student_courses.student_grade = '$grade'
                    WHERE student_courses.student_idNum = '$student_username'
                    AND subjects.subject_name = '$subject_name'
                    AND subjects.YearAndBlock = '$subject_YandB'";
                    if (mysqli_query($db->dbConnect(), $update_grade_for_student)) {
                        echo "Grade added";
                    } else echo "Grade not added";
                }
            }
        }                        
    }
    else echo "Error: " .mysqli_connect_error($db->dbConnect());
?>