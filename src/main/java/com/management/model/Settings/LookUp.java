package com.management.model.Settings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.management.model.AbstractModel;
import com.management.utility.CustomDateSerializer;


import java.sql.Timestamp;

/**
 * Created by Lukman.Arogundade on 11/17/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LookUp extends AbstractModel {

    //  @JsonIgnore
    private Long id;

    private String lookupType;
    private String lookupValue;
    private String lookupDescription;
    private String lookupSort;

    //@JsonIgnore
    @JsonSerialize(using = CustomDateSerializer.class)
    private Timestamp createdOn;
    @JsonIgnore
    private String createdBy;
    //@JsonIgnore
    @JsonSerialize(using = CustomDateSerializer.class)
    private Timestamp lastUpdatedOn;

    @JsonIgnore

    private String lastUpdatedBy;

    private Integer status;



    private Long maker;
    private Long checker;
    private int approved;


    public Timestamp getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Timestamp lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Long getMaker() {
        return maker;
    }

    public void setMaker(Long maker) {
        this.maker = maker;
    }

    public Long getChecker() {
        return checker;
    }

    public void setChecker(Long checker) {
        this.checker = checker;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLookupType() {
        return lookupType;
    }

    public void setLookupType(String lookupType) {
        this.lookupType = lookupType;
    }

    public String getLookupValue() {
        return lookupValue;
    }

    public void setLookupValue(String lookupValue) {
        this.lookupValue = lookupValue;
    }

    public String getLookupDescription() {
        return lookupDescription;
    }

    public void setLookupDescription(String lookupDescription) {
        this.lookupDescription = lookupDescription;
    }

    public String getLookupSort() {
        return lookupSort;
    }

    public void setLookupSort(String lookupSort) {
        this.lookupSort = lookupSort;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }


}
