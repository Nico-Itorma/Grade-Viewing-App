package com.nicanoritorma.gradeview.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nicanoritorma.gradeview.R;
import com.nicanoritorma.gradeview.dataModels.Course_DataModel;

import java.util.ArrayList;

/**
 * adapter for recycler view used by fragment_courselist to display courses enrolled or added
 */
public class Adapter_CourseList extends RecyclerView.Adapter<Adapter_CourseList.CourseViewHolder> {

    private ArrayList<Course_DataModel> courseList;
    private OnSubjectClickListener subjectClickListener;

    public Adapter_CourseList (ArrayList<Course_DataModel> courseList, OnSubjectClickListener subjectClickListener) {
        this.courseList = courseList;
        this.subjectClickListener = subjectClickListener;
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tv_course_name;
        private final TextView tv_prof_name;
        private final TextView tv_YearAndBlock;
        private OnSubjectClickListener subjectClickListener;

        public CourseViewHolder(View itemView, OnSubjectClickListener subjectClickListener) {
            super(itemView);
            tv_course_name = itemView.findViewById(R.id.tv_course_name);
            tv_prof_name = itemView.findViewById(R.id.tv_prof_name);
            tv_YearAndBlock = itemView.findViewById(R.id.tv_yearAndBlock);
            this.subjectClickListener = subjectClickListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            subjectClickListener.onSubjectClick(getAdapterPosition());
        }

    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        return new CourseViewHolder(v, subjectClickListener);
    }

    @Override
    public void onBindViewHolder(Adapter_CourseList.CourseViewHolder holder, int position) {
        Course_DataModel currentItem = courseList.get(position);
        holder.tv_course_name.setText(currentItem.getCourse_name());
        holder.tv_prof_name.setText(currentItem.getProf_name());
        holder.tv_YearAndBlock.setText(currentItem.getYearAndBlock());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public interface OnSubjectClickListener {
        void onSubjectClick(int position);
    }
}

