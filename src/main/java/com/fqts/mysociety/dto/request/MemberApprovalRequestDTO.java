package com.fqts.mysociety.dto.request;


import com.fqts.mysociety.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberApprovalRequestDTO {
    private Role role;
}

