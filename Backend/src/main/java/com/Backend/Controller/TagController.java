package com.Backend.Controller;

import com.Backend.Payload.Request.TagRequest;
import com.Backend.Payload.Response.ApiResponse;
import com.Backend.Service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
public class TagController {
    private final TagService tagService;

    @PostMapping
    public ResponseEntity<Void> addTag(@Valid @RequestBody TagRequest tagRequest) {
        tagService.addTag(tagRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTag(
            @PathVariable Long id,
            @Valid @RequestBody TagRequest tagRequest
    ) {
        tagService.updateTag(id, tagRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getTag(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.OK.value(),
                tagService.getTag(id)
        );

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getTags() {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.OK.value(),
                tagService.getTags()
        );

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.OK
        );
    }
}
