package com.hit.comemyway.dto.response;

import com.hit.comemyway.entity.User;

public record RegisterResponse(String username,String email){public static RegisterResponse from(User user){return new RegisterResponse(user.getUsername(),user.getEmail());}}
