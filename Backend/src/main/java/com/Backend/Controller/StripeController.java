package com.Backend.Controller;

import com.Backend.Service.StripeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/stripe/webhooks")
@AllArgsConstructor
public class StripeController {
    private final StripeService stripeService;

    @PostMapping
    ResponseEntity<Void> handleStripeEvent(HttpServletRequest request) throws Exception {
        log.info("stripe controller");
        stripeService.handleStripeEvent(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
