package com.nicanoritorma.gradeview.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nicanoritorma.gradeview.R;
import com.nicanoritorma.gradeview.dataModels.Student_DataModel;

import java.util.ArrayList;
/**
    used in activity studentList in course
    adapter for student list in recycler view for student that are listed in a subject
 **/
public class Adapter_StudentList extends RecyclerView.Adapter<Adapter_StudentList.StudentViewHolder> {

    private ArrayList<Student_DataModel> studentList;
    private OnSubjectInCourseClickListener courseClickListener;

    public Adapter_StudentList(ArrayList<Student_DataModel> studentList, OnSubjectInCourseClickListener clickListener) {
        this.studentList = studentList;
        this.courseClickListener = clickListener;
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView student_name;
        private final TextView student_idNum;
        private final TextView student_grade;
        OnSubjectInCourseClickListener clickListener;

        public StudentViewHolder(View itemView, OnSubjectInCourseClickListener clickListener ) {
            super(itemView);
            student_name = itemView.findViewById(R.id.tv_student_name);
            student_idNum = itemView.findViewById(R.id.tv_student_idNum);
            student_grade = itemView.findViewById(R.id.tv_student_grade);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            clickListener.onSubjectClick(getAdapterPosition());
        }
    }

    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
        return new StudentViewHolder(v, courseClickListener);
    }

    @Override
    public void onBindViewHolder(Adapter_StudentList.StudentViewHolder holder, int position) {
        Student_DataModel currentItem = studentList.get(position);
        holder.student_name.setText(currentItem.getStudent_name());
        holder.student_idNum.setText(currentItem.getStudent_idNum());
        String STUDENT_GRADE = "Student Grade: ";

        //check if there is a grade for a student
        if (currentItem.getStudent_grade().equals("null"))
        {
            holder.student_grade.setText(STUDENT_GRADE);
        }
        else {
            holder.student_grade.setText(STUDENT_GRADE + currentItem.getStudent_grade());
        }
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public interface OnSubjectInCourseClickListener {
        void onSubjectClick(int position);
    }

}
