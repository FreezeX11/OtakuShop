package com.Backend.Mapper;

import com.Backend.Entity.Image;
import com.Backend.Payload.Response.ImageResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class ImageMapper {

    public ImageResponse toImageResponse(Image image) {
        ImageResponse imageResponse = new ImageResponse();

        imageResponse.setId(image.getId());
        imageResponse.setUrl(image.getUrl());

        return imageResponse;
    }
}
