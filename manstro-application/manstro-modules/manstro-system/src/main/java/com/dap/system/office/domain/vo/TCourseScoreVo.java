package com.dap.system.office.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.dap.system.office.domain.TScore;

public class TCourseScoreVo {
    // 课程id

    private Integer courseId;

    // 课程名字
    @Excel(name="课程名字")
    private String courseName;

    // input的rule名称
    private String note;

    @Excel(name="分数")
    private Double score;


    public TCourseScoreVo() {
    }

    public TCourseScoreVo(TScore tScore) {
        this.courseId = tScore.getCourseId();
        this.courseName = tScore.getCourseName();
        this.note = tScore.getNote();
        this.score = tScore.getScore();
    }

    public TCourseScoreVo(TCourseScoreVo tScore) {
        this.courseId = tScore.getCourseId();
        this.courseName = tScore.getCourseName();
        this.note = tScore.getNote();
        if(tScore.getScore() != null){
            this.score = tScore.getScore();
        }
    }
    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public TCourseScoreVo(TCourseSelectorVo tcourseSelectorVo){
        this.courseId = tcourseSelectorVo.getId();
        this.courseName = tcourseSelectorVo.getCouName();
        this.note = tcourseSelectorVo.getNote();
    }


}
