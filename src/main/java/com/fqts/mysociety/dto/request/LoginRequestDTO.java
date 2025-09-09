package com.fqts.mysociety.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    private String mobile;
    private String password;
}
