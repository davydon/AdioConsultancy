package com.management.model.Settings;

import java.util.ArrayList;
import java.util.List;


public class RowError {
    private Integer row;
    private List<String> cellErrMsg = new ArrayList<>();


    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public List<String> getCellErrMsg() {
        return cellErrMsg;
    }

    public void setCellErrMsg(String cellErrMsg) {
        this.cellErrMsg.add(cellErrMsg);
    }

    @Override
    public String toString() {
        return
                "" + cellErrMsg;
    }
}
