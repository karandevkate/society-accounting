package com.fqts.mysociety.controller;

import com.fqts.mysociety.dto.request.SocietyExpenseRequestDto;
import com.fqts.mysociety.dto.request.SocietyRegistrationDto;
import com.fqts.mysociety.dto.request.SocietyRequest;
import com.fqts.mysociety.dto.response.SocietyDetailsWithCountsResponse;
import com.fqts.mysociety.dto.response.SocietyExpenseResponseDto;
import com.fqts.mysociety.dto.response.SocietyResponse;
import com.fqts.mysociety.dto.response.SocietyWithAdminResponseDto;
import com.fqts.mysociety.exception.InvalidSocietyRequestException;
import com.fqts.mysociety.exception.SocietyNotFoundException;
import com.fqts.mysociety.model.Society;
import com.fqts.mysociety.service.SocietyExpenseService;
import com.fqts.mysociety.service.SocietyService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/society")
@AllArgsConstructor
public class SocietyController {

  private static final Logger log = LoggerFactory.getLogger(SocietyController.class);

  private final SocietyService societyService;
  private final SocietyExpenseService societyExpenseService;

  @PostMapping("/registersociety")
  public ResponseEntity<String> registration(
      @RequestBody SocietyRegistrationDto societyRegistrationDto) {

      UUID societyId = societyService.registerSocietyWithAdmin(societyRegistrationDto);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body("Society registered successfully with ID: " + societyId);

  }

  @GetMapping("/get/{id}")
  public ResponseEntity<SocietyResponse> getSocietyById(@PathVariable UUID id) {
      log.info("Fetching society with ID: {}", id);
      SocietyResponse society = societyService.getSocietyById(id);
      return ResponseEntity.ok(society);
  }

  @GetMapping("/getAllSocieties")
  public ResponseEntity<List<SocietyResponse>> getAllSocieties() {
    List<SocietyResponse> societies = societyService.getAllSocieties();

    if (societies.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body(societies);
    }
    return ResponseEntity.ok(societies);
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<Society> updateSociety(@PathVariable UUID id,
      @RequestBody SocietyRequest request) {
      log.info("Updating society with ID: {}", id);
      Society updatedSociety = societyService.updateSociety(id, request);
      return ResponseEntity.ok(updatedSociety);
  }

  @PutMapping("/{id}/approve")
  public ResponseEntity<String> approveSociety(@PathVariable UUID id) {

      log.info("Approving society with ID: {}", id);
      return ResponseEntity.ok(societyService.approveSociety(id));

  }

  @PutMapping("/{id}/reject")
  public ResponseEntity<String> rejectSociety(@PathVariable UUID id) {
      log.info("Rejecting society with ID: {}", id);
      return ResponseEntity.ok(societyService.rejectSociety(id));
  }

  @PostMapping("/societyExpense")
  public ResponseEntity<?> addSocietyExpense(@RequestBody SocietyExpenseRequestDto societyExpenseRequestDto) {
    log.info("Adding new society expense");
    UUID expenseId = societyExpenseService.createExpense(societyExpenseRequestDto);
    log.info("Society expense created with ID: {}", expenseId);
    return ResponseEntity.ok(expenseId);
  }

  @GetMapping("/{societyId}/societyExpense")
  public ResponseEntity<List<SocietyExpenseResponseDto>> getSocietyExpense(@PathVariable UUID societyId) {
    log.info("Fetching expenses for society ID: {}", societyId);
    List<SocietyExpenseResponseDto> expenses = societyExpenseService.fetchSocietyExpense(societyId);
    log.info("Fetched {} expenses for society ID: {}", expenses.size(), societyId);
    return ResponseEntity.ok(expenses);
  }

  @GetMapping("/{societyId}/with-admins")
  public ResponseEntity<SocietyWithAdminResponseDto> getSocietyWithAdmins(@PathVariable UUID societyId) {
    log.info("Fetching society with admins for ID: {}", societyId);
    SocietyWithAdminResponseDto response = societyService.getSocietyWithAdmins(societyId);
    log.info("Fetched society with admins successfully for ID: {}", societyId);
    return ResponseEntity.ok(response);
  }



    @GetMapping("/get-with-counts/{id}")
    public ResponseEntity<SocietyDetailsWithCountsResponse> getSocietyWithCounts(@PathVariable UUID id) {
        SocietyDetailsWithCountsResponse response = societyService.getSocietyWithCounts(id);
        return ResponseEntity.ok(response);
    }

}
