package com.management.model.Settings;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FileUploadResponse<T> {

    @JsonIgnore
    private List<T> result;

    private String batchId;

    private int successCount;

    private int failedCount;

    private BigDecimal totalAmount;

    private Boolean errorFound = Boolean.FALSE;

    private List<RowError> rowErrors = new ArrayList<>();

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public Boolean getErrorFound() {
        return errorFound;
    }

    public void setErrorFound(Boolean errorFound) {
        this.errorFound = errorFound;
    }

    public List<RowError> getRowErrors() {
        return rowErrors;
    }

    public void setRowErrors(RowError row) {
        rowErrors.add(row);
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }

}