package com.Backend.Mapper;

import com.Backend.Entity.Tag;
import com.Backend.Payload.Request.TagRequest;
import com.Backend.Payload.Response.TagResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Component
public class TagMapper {

    public Tag toTag(TagRequest tagRequest) {
        Tag tag = new Tag();

        tag.setName(tagRequest.getName());
        tag.setCreatedDate(LocalDateTime.now());

        return tag;
    }

    public TagResponse toTagResponse(Tag tag) {
        TagResponse tagResponse = new TagResponse();

        tagResponse.setId(tag.getId());
        tagResponse.setName(tag.getName());

        return tagResponse;
    }
}
