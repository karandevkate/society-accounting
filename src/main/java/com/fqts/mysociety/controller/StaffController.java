package com.fqts.mysociety.controller;

import com.fqts.mysociety.dto.request.StaffRequestDto;
import com.fqts.mysociety.dto.request.StaffSalaryRequestDto;
import com.fqts.mysociety.dto.response.StaffResponseDto;
import com.fqts.mysociety.dto.response.StaffSalaryResponseDto;
import com.fqts.mysociety.service.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
public class StaffController {

  private static final Logger log = LoggerFactory.getLogger(StaffController.class);
  private final StaffService staffService;

  @PostMapping()
  @Operation(summary = "Create a new staff member with file uploads")
  public ResponseEntity<String> createStaff(
      @RequestParam("name") String name,
      @RequestParam("govIdNumber") String govIdNumber,
      @RequestParam("govIdPhoto") MultipartFile govIdPhoto,
      @RequestParam("staffPhoto") MultipartFile staffPhoto,
      @RequestParam("contact") String contact,
      @RequestParam("email") String email,
      @RequestParam("permanentAddress") String permanentAddress,
      @RequestParam("currentAddress") String currentAddress,
      @RequestParam("role") String role,
      @RequestParam("salary") Double salary,
      @RequestParam("joiningDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date joiningDate,
      @RequestParam("societyId") UUID societyId) {

    log.info("Received staff creation request with data:");
    log.info("Name: {}", name);
    log.info("Gov ID Number: {}", govIdNumber);
    log.info("Contact: {}", contact);
    log.info("Email: {}", email);
    log.info("Permanent Address: {}", permanentAddress);
    log.info("Current Address: {}", currentAddress);
    log.info("Role: {}", role);
    log.info("Salary: {}", salary);
    log.info("Joining Date: {}", joiningDate);
    log.info("Society ID: {}", societyId);
    log.info("Gov ID Photo: name={}, size={} bytes, type={}",
        govIdPhoto.getOriginalFilename(), govIdPhoto.getSize(), govIdPhoto.getContentType());
    log.info("Staff Photo: name={}, size={} bytes, type={}",
        staffPhoto.getOriginalFilename(), staffPhoto.getSize(), staffPhoto.getContentType());

    try {
      StaffRequestDto staffRequestDto = new StaffRequestDto();
      staffRequestDto.setName(name);
      staffRequestDto.setGovIdNumber(govIdNumber);
      staffRequestDto.setGovIdPhoto(govIdPhoto);
      staffRequestDto.setStaffPhoto(staffPhoto);
      staffRequestDto.setContact(contact);
      staffRequestDto.setEmail(email);
      staffRequestDto.setPermanentAddress(permanentAddress);
      staffRequestDto.setCurrentAddress(currentAddress);
      staffRequestDto.setRole(role);
      staffRequestDto.setSalary(salary);
      staffRequestDto.setJoiningDate(joiningDate);
      staffRequestDto.setSocietyId(societyId);

      UUID staffId = staffService.createStaff(staffRequestDto); // Create staff logic
      log.info("Staff created successfully with ID: {}", staffId);
      return ResponseEntity.ok("Staff created successfully with ID: " + staffId);

    } catch (Exception e) {
      log.error("Error while creating staff: {}", e.getMessage(), e);
      return ResponseEntity.status(500).body("Error while creating staff: " + e.getMessage());
    }
  }

  @GetMapping("/{societyId}")
  public ResponseEntity<List<StaffResponseDto>> getStaffBySocietyId(@PathVariable UUID societyId) {
    log.info("Fetching staff list for societyId={}", societyId);
    List<StaffResponseDto> staffList = staffService.getStaffBySocietyId(societyId);
    log.info("Found {} staff members for societyId={}", staffList.size(), societyId);
    return ResponseEntity.ok(staffList);
  }

  @PutMapping("/status/{staffId}")
  public ResponseEntity<Boolean> updateStaffStatus(@PathVariable UUID staffId) {
    Boolean updatedStatus = staffService.updateStaffStatus(staffId);
    log.info("Updated status for staffId={} to {}", staffId, updatedStatus);
    return ResponseEntity.ok(updatedStatus);
  }

  @GetMapping("/{societyId}/staffdetails/{staffId}")
  public StaffResponseDto getStaffByIdAndSocietyId(@PathVariable UUID staffId,
      @PathVariable UUID societyId) {
    log.info("Fetching staff details for staffId={} and societyId={}", staffId, societyId);
    return staffService.getStaffByIdAndSocietyId(staffId, societyId);
  }

  @GetMapping("/staffofsociety/{staffId}")
  public ResponseEntity<StaffResponseDto> getStaffById(@PathVariable UUID staffId) {
    log.info("Fetching staff by staffId={}", staffId);
    StaffResponseDto staffResponse = staffService.getStaffById(staffId);
    return ResponseEntity.ok(staffResponse);
  }

  @PostMapping("/staffSalary")
  public ResponseEntity<UUID> payStaffSalary(
      @RequestBody StaffSalaryRequestDto staffSalaryRequestDto) {
    return ResponseEntity.ok(staffService.paySalary(staffSalaryRequestDto));
  }

  @GetMapping("{societyId}/staffSalary")
  public ResponseEntity<List<StaffSalaryResponseDto>> getStaffSalaryBySocietyID(
      @PathVariable("societyId") UUID societyId) {
    return ResponseEntity.ok(staffService.getStaffSalaryBySociety(societyId));
  }

  @GetMapping("{paidDate}/staffSalarybyDate")
  public ResponseEntity<List<StaffSalaryResponseDto>> getStaffSalaryByMonth(
      @PathVariable LocalDate paidDate) {
    return ResponseEntity.ok(staffService.getStaffSalaryByMonth(paidDate));
  }
}


