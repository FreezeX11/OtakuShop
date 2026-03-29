package com.Backend.Util;

import com.Backend.Entity.User;
import com.Backend.Enumeration.UserProfile;
import com.Backend.Service.CartService;
import com.Backend.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserUtil {
    private final CartService cartService;

    public void createCartIfEligible(User user) {
        if ("USER".equals(user.getUserProfile().getUserProfile().name()))
            cartService.createCart(user);
    }
}
