package com.gcgsauce.demo.services;

import com.gcgsauce.demo.models.Photo;

public interface PhotoService {

    Photo findPhotoById(long id);

    Photo updatePhoto(Photo updatedPhoto, long id);

    void deletePhoto(long id);
}
