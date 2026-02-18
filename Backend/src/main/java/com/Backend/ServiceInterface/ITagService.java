package com.Backend.ServiceInterface;

import com.Backend.Payload.Request.TagRequest;
import com.Backend.Payload.Response.TagResponse;

import java.util.List;

public interface ITagService {
    void addTag(TagRequest tagRequest);
    void updateTag(Long id, TagRequest tagRequest);
    void deleteTag(Long id);
    TagResponse getTag(Long id);
    List<TagResponse> getTags();
}
