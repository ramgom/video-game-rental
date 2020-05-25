package com.ramgom.videogamerental.users.responses;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDetailsResponse {
    Long id;
    String name;

    @Builder.Default Integer loyaltyPoints = 0;
}
