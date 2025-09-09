package com.fqts.mysociety.controller;

import com.fqts.mysociety.dto.request.MatrixChargesDTO;
import com.fqts.mysociety.dto.response.MatrixChargesResponseDTO;
import com.fqts.mysociety.exception.SocietyNotFoundException;
import com.fqts.mysociety.model.MatrixCharges;
import com.fqts.mysociety.service.MatrixChargesService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/charges")
public class MatrixChargesController {

  @Autowired
  private MatrixChargesService service;

  private Role getLoggedInUserRole() {
    // Placeholder for role validation logic
    return Role.ADMIN; // Assume user is an admin for now
  }

  @GetMapping("/society/{societyId}/flat/{flatNumber}")
  public ResponseEntity<List<MatrixCharges>> getChargesBySocietyAndFlatNumber(
      @PathVariable UUID societyId,
      @PathVariable String flatNumber) {

    List<MatrixCharges> charges = service.getChargesBySocietyAndFlatNumber(societyId, flatNumber);

    if (charges.isEmpty()) {
      return ResponseEntity.status(404).body(null); // No charges found
    }
    return ResponseEntity.ok(charges); // Return found charges
  }

  @PostMapping
  public ResponseEntity<?> createCharge(@RequestBody MatrixChargesDTO charge)
      throws SocietyNotFoundException {
    Role userRole = getLoggedInUserRole();

    if (userRole != Role.ADMIN) {
      return ResponseEntity.status(403).body("Only Admin can create charges.");
    }

    MatrixChargesResponseDTO savedCharge = service.createCharge(charge);
    return ResponseEntity.ok(savedCharge);
  }

  @PutMapping("/{id}")
  public ResponseEntity<MatrixCharges> updateCharge(@PathVariable UUID id,
      @RequestBody MatrixCharges updatedCharge) {
    Role userRole = getLoggedInUserRole();

    if (userRole != Role.ADMIN) {
      // If the user is not an admin, return a 403 Forbidden response with a string message.
      return ResponseEntity.status(403).body(null);  // Empty body for error response
    }

    // Update the charge and return the updated charge object
    MatrixCharges updated = service.updateCharge(id, updatedCharge);
    return ResponseEntity.ok(updated);  // Return updated charge with 200 OK status
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteCharge(@PathVariable UUID id) {
    Role userRole = getLoggedInUserRole();

    if (userRole != Role.ADMIN) {
      return ResponseEntity.status(403).body("Only Admin can delete charges.");
    }

    service.deleteCharge(id);
    return ResponseEntity.ok("Charge deleted successfully.");
  }


  private enum Role {ADMIN, USER}
}
