package com.fqts.mysociety.dto.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocietyDetailsWithCountsResponse {
    private UUID id;
    private String name;
    private String address;
    private String state;
    private String city;

    private long pendingMembers;
    private long approvedMembers;
    private long totalUsers;
}