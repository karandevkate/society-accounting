package com.fqts.mysociety.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocietyMemberResponseDto {
  private UUID societyMemberId;

  private UUID societyId;
  private String societyName;
  private String societyUniqueCode;
  private String role;
  private String lastVisitedFlatNumber;

}
