<?php 
    require "Database.php";
    $db = new Database();

    $student_idNum = $_GET['student_idNum'];
    $subject_name = $_GET['subject_name'];
    $YandB = $_GET['YandB'];

    if (isset($student_idNum) && isset($subject_name) && isset($YandB)) {
        if ($db->dbConnect()) 
        {

            $check_if_idNum_exist_in_users = "SELECT * FROM users u WHERE u.username='$student_idNum'";
            $check_if_idNum_exist_in_users_result = mysqli_query($db->dbConnect(), $check_if_idNum_exist_in_users);
        
            if (mysqli_num_rows($check_if_idNum_exist_in_users_result) > 0)
            {
                $row = mysqli_fetch_assoc($check_if_idNum_exist_in_users_result);
                if ($student_idNum == $row['username'] && $row['userType'] == 2)
                {
                    $check_if_student_already_exist = "SELECT sc.student_idNum, s.subject_name FROM (student_courses sc INNER JOIN subjects s ON sc.subject_enrolled = s.subject_id) WHERE student_idNum='" .$student_idNum. "' AND s.subject_name ='" .$subject_name."'";
                    $check_if_student_already_exist_result = mysqli_query($db->dbConnect(), $check_if_student_already_exist);

                    if (mysqli_num_rows($check_if_student_already_exist_result) > 0)
                    {
                        echo "Student already exist";
                    }
                    else {
                        $get_the_subject_id = "SELECT * FROM subjects s WHERE s.subject_name='$subject_name' AND s.YearAndBlock='$YandB'";
                        $get_the_subject_id_result = mysqli_query($db->dbConnect(), $get_the_subject_id);
                        if (mysqli_num_rows($get_the_subject_id_result) > 0)
                        {
                            $row = mysqli_fetch_assoc($get_the_subject_id_result);
                            if (($subject_name == $row['subject_name']) && ($YandB == $row['YearAndBlock']))
                            {
                                if ($db->addStudent_to_subject($student_idNum, $subject_name, $YandB))
                                {
                                    echo "Student added";
                                }else "ERROR: Student not inserted in subject: '$subject_name'";
                            }
                        }
                    }
                } 
                else if ($row['userType'] == 1)
                {
                    echo "ID number does not belong to a student";
                }
            }
 
        } else echo "Error: Database connection";
    }

    

?>
