package com.meguru.chatproject.user.domain.vo.response.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginSuccess {

    private Long uid;

    private String name;

    private String avatar;

    private String token;
}
