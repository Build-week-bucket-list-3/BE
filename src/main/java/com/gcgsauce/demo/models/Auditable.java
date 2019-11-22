package com.gcgsauce.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
abstract class Auditable
{
    @CreatedBy
    @JsonIgnore
    protected String createdby;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    protected Date createddate;

    @LastModifiedBy
    @JsonIgnore
    protected String lastmodifiedby;

    @LastModifiedDate
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastmodififeddate;

    public String getCreatedby() {
        return createdby;
    }

    public Date getCreateddate() {
        return createddate;
    }

    public String getLastmodifiedby() {
        return lastmodifiedby;
    }

    public Date getLastmodififeddate() {
        return lastmodififeddate;
    }
}
