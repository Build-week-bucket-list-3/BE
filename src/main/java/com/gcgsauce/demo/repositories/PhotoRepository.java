package com.gcgsauce.demo.repositories;

import com.gcgsauce.demo.models.Photo;
import org.springframework.data.repository.CrudRepository;

public interface PhotoRepository extends CrudRepository<Photo, Long> {

}
