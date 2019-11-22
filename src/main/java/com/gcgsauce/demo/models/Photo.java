package com.gcgsauce.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;

@Entity
@Table(name = "tables")
public class Photo extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("photoId")
    private long photoid;

    @Column
    @JsonProperty("photoLink")
    @URL
    private String photolink;

    @ManyToOne
    @JoinColumn(name = "bucketlistitemid")
    @JsonIgnoreProperties("photos")
    private BucketlistItem bucketlistitem;

    public Photo() {
    }

    public Photo(String photolink, BucketlistItem bucketlistitem) {
        this.photolink = photolink;
        this.bucketlistitem = bucketlistitem;
    }

    public long getPhotoid() {
        return photoid;
    }

    public void setPhotoid(long photoid) {
        this.photoid = photoid;
    }

    public String getPhotolink() {
        return photolink;
    }

    public void setPhotolink(String photolink) {
        this.photolink = photolink;
    }

    public BucketlistItem getBucketlistitem() {
        return bucketlistitem;
    }

    public void setBucketlistitem(BucketlistItem bucketlistitem) {
        this.bucketlistitem = bucketlistitem;
    }
}
