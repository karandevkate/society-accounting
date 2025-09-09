package com.fqts.mysociety.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlatResponseDTO {

    private String flatNumber;
    private String wing;
    private String flat;
    private UUID societyId;
    private String societyName;
    private String flatType;
    private boolean isSelfOccupied;

}
