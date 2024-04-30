package com.dap.system.office.domain.vo;

import java.util.Objects;

public class TCourseSelectorVo {

    private Integer id;

    private String couName;

    private String note;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCouName() {
        return couName;
    }

    public void setCouName(String couName) {
        this.couName = couName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TCourseSelectorVo that = (TCourseSelectorVo) o;

        if (!Objects.equals(id, that.id)) {
            return false;
        }
        return Objects.equals(couName, that.couName);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (couName != null ? couName.hashCode() : 0);
        return result;
    }


}
