package com.gcgsauce.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bucketlistitems")
public class BucketlistItem extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("itemId")
    private long bucketlistitemid;

    @JsonProperty("name")
    private String itemname;

    @JsonProperty("description")
    private String itemdescription;

    @OneToMany(mappedBy = "bucketlistitem", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("bucketlistitem")
    private List<Photo> photos = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "bucketlistid")
    @JsonIgnoreProperties("bucketlistitems")
    @JsonIgnore
    private Bucketlist bucketlist;

    public BucketlistItem() {
    }

    public BucketlistItem(String itemname, String itemdescription, Bucketlist bucketlist) {
        this.itemname = itemname;
        this.itemdescription = itemdescription;
        this.bucketlist = bucketlist;
    }

    public long getBucketlistitemid() {
        return bucketlistitemid;
    }

    public void setBucketlistitemid(long bucketlistitemid) {
        this.bucketlistitemid = bucketlistitemid;
    }
    
    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemdescription() {
        return itemdescription;
    }

    public void setItemdescription(String itemdescription) {
        this.itemdescription = itemdescription;
    }

    public Bucketlist getBucketlist() {
        return bucketlist;
    }

    public void setBucketlist(Bucketlist bucketlist) {
        this.bucketlist = bucketlist;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}
