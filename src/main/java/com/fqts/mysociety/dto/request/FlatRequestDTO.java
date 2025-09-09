package com.fqts.mysociety.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlatRequestDTO {
    private UUID societyId;
    private String wing;
    private String flat;
    private String flatType;
    private boolean isSelfOccupied;
}
