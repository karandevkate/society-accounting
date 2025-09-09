package com.fqts.mysociety.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class FlatId implements Serializable {

  @Column(name = "flat_number")
  private String flatNumber;
  @Column(name = "society_id")
  private UUID societyId;

  @Override
  public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) {
          return false;
      }
    FlatId flatId = (FlatId) o;
    return Objects.equals(flatNumber, flatId.flatNumber) && Objects.equals(societyId,
        flatId.societyId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(flatNumber, societyId);
  }

  // Use Long or the correct type matching your Society ID


}
