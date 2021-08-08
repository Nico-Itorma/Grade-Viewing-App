<?php
require "DataBaseConfig.php";

class Database
{
    public $connect;
    public $data;
    private $sql;
    protected $servername;
    protected $username;
    protected $password;
    protected $databasename;

    public function __construct()
    {
        $this->connect = null;
        $this->data = null;
        $this->sql = null;
        $dbc = new DatabaseConfig();
        $this->servername = $dbc->servername;
        $this->username = $dbc->username;
        $this->password = $dbc->password;
        $this->databasename = $dbc->databasename;
    }

    function dbConnect()
    {
        $this->connect = mysqli_connect($this->servername, $this->username, $this->password, $this->databasename);
        return $this->connect;
    }

    function prepareData($data)
    {
        return mysqli_real_escape_string($this->connect, stripslashes(htmlspecialchars($data)));
    }

    function signUp($table, $fullname, $username, $password, $user_type)
    {
        $fullname = $this->prepareData($fullname);
        $username = $this->prepareData($username);
        $password = $this->prepareData($password);
        $password = password_hash($password, PASSWORD_DEFAULT);

        $check_userType_sql = "SELECT * FROM user_type WHERE user_type='$user_type'";
        $check_userType_result = mysqli_query($this->connect, $check_userType_sql);
        if (mysqli_num_rows($check_userType_result) > 0) {
            $row = mysqli_fetch_assoc($check_userType_result);
            if ($user_type==$row['user_type'])
            {
                $user_type = $row['id'];
                $this->sql = "INSERT INTO " .$table. " (name, username, password, userType) VALUES ('" .$fullname. "','" .$username. "','" .$password. "','" .$user_type. "')";
        
                if (mysqli_query($this->connect, $this->sql)) {
                   return true;
               } else return false;
            }
        }
    }

    //check if username already exist in table
    function checkUser ($username) 
    {
        $username = $this->prepareData($username);

        $sql = "SELECT * FROM users WHERE username='$username'";
        $result = mysqli_query($this->connect, $sql);
        if (mysqli_num_rows($result) > 0) {
            $row = mysqli_fetch_assoc($result);
            if ($username==$row['username'])
            {
                return true;
                echo "Username already exist";
            }
        }
        else return false;
    }


    //get the user id according to the app passed parameter
    function getUserUID($name) 
    {
        $name = $this->prepareData($name);
        $username_query = "SELECT users.uid, users.name, users.userType FROM users users WHERE users.name = '".$name."'";
        $username_result = mysqli_query($this->connect, $username_query);
        if (mysqli_num_rows($username_result) > 0) 
        {
            $row = mysqli_fetch_assoc($username_result);
            if ($name == $row['name'])
            {
                $real_username = $row['uid'];
                if ($row['userType'] == 1)
                {
                    $this->getProfCourse($real_username);
                }
                else {
                    $this->getStudentCourse($real_username);
                }
            }
        }
    }


    //gets the classes created by professor user
    function getProfCourse ($userUId) {
       
        $sql = "SELECT sub.subject_name, sub.YearAndBlock, sub.Department FROM subjects sub WHERE sub.ProfName = '" .$userUId. "' ORDER BY sub.subject_id";
        $result = mysqli_query($this->connect, $sql);
        $json_array = array();
    
        if (mysqli_num_rows($result) > 0)
        {
            while ($row = mysqli_fetch_assoc($result)) {
                $json_array[] = $row;
            }
             echo json_encode(['subject_list' => $json_array]);
        } else {
            echo "NO_SUBJECT_FOUND_ON:'$userUId'";
        }
    }     

    //function to add student to a subject added by the professor
    function addStudent_to_subject($student_idNum, $subject_name, $YandB) {
        $student_idNum = $this->prepareData($student_idNum);
        $subject_name = $this->prepareData($subject_name);
        $YandB = $this->prepareData($YandB);


        $get_the_subject_id = "SELECT * FROM subjects s WHERE s.subject_name='$subject_name' AND s.YearAndBlock='$YandB'";
        $get_the_subject_id_result = mysqli_query($this->connect, $get_the_subject_id);
        if (mysqli_num_rows($get_the_subject_id_result) > 0)
        {
            $row2 = mysqli_fetch_assoc($get_the_subject_id_result);
            if (($subject_name == $row2['subject_name']) && ($YandB == $row2['YearAndBlock']))
            {
                $subject_id = $row2['subject_id'];
                $add_student_to_db = "INSERT INTO student_courses (student_idNum, subject_enrolled, student_grade) VALUES ('" .$student_idNum. "','" .$subject_id. "', NULL)";
                if (mysqli_query($this->connect, $add_student_to_db)) 
                {
                    return true;
                } else return false;
            }
        }
    }
}
?>