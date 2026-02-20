package com.Backend.Service;

import com.Backend.Entity.Tag;
import com.Backend.Exception.ResourceAlreadyExistException;
import com.Backend.Exception.ResourceInUseException;
import com.Backend.Exception.ResourceNotFoundException;
import com.Backend.Mapper.TagMapper;
import com.Backend.Payload.Request.TagRequest;
import com.Backend.Payload.Response.TagResponse;
import com.Backend.Repository.TagRepository;
import com.Backend.ServiceInterface.ITagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService implements ITagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public void addTag(TagRequest tagRequest) {
        if (tagRepository.findByNameIgnoreCase(tagRequest.getName().trim()).isPresent())
            throw new ResourceAlreadyExistException("This tag already exist");

        tagRepository.save(tagMapper.toTag(tagRequest));
    }

    @Override
    public void updateTag(Long id, TagRequest tagRequest) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This tag doesn't exist"));

        if (tagRepository.findByNameIgnoreCase(tagRequest.getName().trim()).isPresent())
            throw new ResourceAlreadyExistException("This tag already exist");

        existingTag.setName(tagRequest.getName());
        tagRepository.save(existingTag);
    }

    @Override
    public void deleteTag(Long id) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This tag doesn't exist"));

        if (!existingTag.getProducts().isEmpty())
            throw new ResourceInUseException("This tag is assigned to one or more products and cannot be deleted.");

        tagRepository.delete(existingTag);
    }

    @Override
    public TagResponse getTag(Long id) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This tag doesn't exist"));

        return tagMapper.toTagResponse(existingTag);
    }

    @Override
    public List<TagResponse> getTags() {
        return tagRepository.findAll().stream()
                .map(tagMapper::toTagResponse)
                .toList();
    }
}
