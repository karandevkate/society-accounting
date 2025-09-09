package com.fqts.mysociety.service.impl;

import com.fqts.mysociety.dto.request.StaffRequestDto;
import com.fqts.mysociety.dto.request.StaffSalaryRequestDto;
import com.fqts.mysociety.dto.response.StaffResponseDto;
import com.fqts.mysociety.dto.response.StaffSalaryResponseDto;
import com.fqts.mysociety.exception.FileHandlingException;
import com.fqts.mysociety.exception.SocietyNotFoundException;
import com.fqts.mysociety.exception.StaffMismatchSocietyException;
import com.fqts.mysociety.exception.StaffNotFoundException;
import com.fqts.mysociety.model.Society;
import com.fqts.mysociety.model.Staff;
import com.fqts.mysociety.model.StaffSalary;
import com.fqts.mysociety.repository.SocietyRepository;
import com.fqts.mysociety.repository.StaffRepository;
import com.fqts.mysociety.repository.StaffSalaryRepository;
import com.fqts.mysociety.service.FileService;
import com.fqts.mysociety.service.StaffService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

  private static final Logger log = LoggerFactory.getLogger(
          StaffServiceImpl.class);  // Logger definition
  final SocietyRepository societyRepository;
  final StaffRepository staffRepository;
  final FileService fileService;
  final StaffSalaryRepository staffSalaryRepository;

  @Override
  public UUID createStaff(StaffRequestDto staffRequestDto) {

    byte[] govIdBytes;
    try {
      govIdBytes = staffRequestDto.getGovIdPhoto().getBytes();
    } catch (IOException e) {
      throw new FileHandlingException("Error reading government ID photo: " + e.getMessage());
    }

    byte[] staffPhotoBytes;
    try {
      staffPhotoBytes = staffRequestDto.getStaffPhoto().getBytes();
    } catch (IOException e) {
      throw new FileHandlingException("Error reading staff photo: " + e.getMessage());
    }

    Staff staff = new Staff();
    staff.setName(staffRequestDto.getName());
    staff.setGovIdNumber(staffRequestDto.getGovIdNumber());
    staff.setGovIdPhoto(govIdBytes);
    staff.setStaffPhoto(staffPhotoBytes);
    staff.setContact(staffRequestDto.getContact());
    staff.setEmail(staffRequestDto.getEmail());
    staff.setPermanentAddress(staffRequestDto.getPermanentAddress());
    staff.setCurrentAddress(staffRequestDto.getCurrentAddress());
    staff.setRole(staffRequestDto.getRole());
    staff.setSalary(staffRequestDto.getSalary());
    staff.setJoiningDate(staffRequestDto.getJoiningDate());
    staff.setStaffStatus(Boolean.TRUE);

    Society society = societyRepository.findById(staffRequestDto.getSocietyId())
            .orElseThrow(() -> new SocietyNotFoundException(
                    "Society not found with id: " + staffRequestDto.getSocietyId()));

    staff.setSociety(society);
    staffRepository.save(staff);
    log.info("Staff created with ID: {}", staff.getStaffId());
    return staff.getStaffId();
  }


  @Override
  public UUID updateStaffDetails(UUID staffId, StaffRequestDto staffRequestDto) {
    return null;
  }

  @Override
  public Boolean updateStaffStatus(UUID staffId) throws StaffNotFoundException {
    log.info("Updating status for staff with ID: {}", staffId);
    Staff staff = staffRepository.findById(staffId)
            .orElseThrow(() -> new StaffNotFoundException("Staff with id " + staffId + " not found"));

    staff.setStaffStatus(!staff.getStaffStatus());
    staffRepository.save(staff);
    log.info("Staff status updated for staffId: {} to status: {}", staffId, staff.getStaffStatus());
    return staff.getStaffStatus();
  }


  @Override
  @Transactional(readOnly = true)
  public List<StaffResponseDto> getStaffBySocietyId(UUID societyId) {
    log.info("Fetching staff for societyId: {}", societyId);
    List<Staff> staffList = staffRepository.getStaffBySocietyId(societyId);

    if (staffList.isEmpty()) {
      log.warn("No staff found for society with id: {}", societyId);
      throw new StaffNotFoundException("No staff found for society with id: " + societyId);
    }

    Society society = societyRepository.findById(societyId)
            .orElseThrow(() -> new SocietyNotFoundException("Society not found with id: " + societyId));

    log.info("Staff list fetched for societyId: {} with {} staff members", societyId, staffList.size());

    return staffList.stream()
            .map(staff -> {
              try {
                return new StaffResponseDto(
                        staff.getStaffId(),
                        staff.getName(),
                        staff.getGovIdNumber(),
                        encodeToBase64(staff.getGovIdPhoto()),
                        encodeToBase64(staff.getStaffPhoto()),
                        staff.getContact(),
                        staff.getEmail(),
                        staff.getPermanentAddress(),
                        staff.getCurrentAddress(),
                        staff.getRole(),
                        staff.getSalary(),
                        staff.getJoiningDate(),
                        staff.getStaffStatus(),
                        society.getSocietyName()
                );
              } catch (Exception e) {
                log.error("Error encoding LOB data for staffId: {}", staff.getStaffId(), e);
                throw new RuntimeException("Failed to process staff data", e);
              }
            })
            .collect(Collectors.toList());
  }

  private String encodeToBase64(byte[] data) {
    if (data == null) {
      log.warn("Null data encountered during Base64 encoding");
      return "";
    }
    return Base64.getEncoder().encodeToString(data);
  }

  @Override
  public StaffResponseDto getStaffByIdAndSocietyId(UUID staffId, UUID societyId) {
    log.info("Fetching staff with ID: {} for societyId: {}", staffId, societyId);
    Society society = societyRepository.findById(societyId)
            .orElseThrow(() -> new SocietyNotFoundException("Society not found with id: " + societyId));

    Staff staff = staffRepository.findById(staffId)
            .orElseThrow(() -> new StaffNotFoundException("Staff not found with id: " + staffId));

    if (!staff.getSociety().getSocietyId().equals(societyId)) {
      log.warn("Staff with ID: {} does not belong to society with ID: {}", staffId, societyId);
      throw new StaffMismatchSocietyException(
              "Staff with ID " + staffId + " does not belong to society with ID " + societyId);
    }
    log.info("Staff with ID: {} found for societyId: {}", staffId, societyId);
    return new StaffResponseDto(
            staff.getStaffId(),
            staff.getName(),
            staff.getGovIdNumber(),
            encodeToBase64(staff.getGovIdPhoto()),
            encodeToBase64(staff.getStaffPhoto()),
            staff.getContact(),
            staff.getEmail(),
            staff.getPermanentAddress(),
            staff.getCurrentAddress(),
            staff.getRole(),
            staff.getSalary(),
            staff.getJoiningDate(),
            staff.getStaffStatus(),
            society.getSocietyName()
    );
  }

  @Override
  public StaffResponseDto getStaffById(UUID staffId) {
    log.info("Fetching staff with ID: {}", staffId);
    Staff staff = staffRepository.findById(staffId)
            .orElseThrow(() -> new StaffNotFoundException("Staff not found with id: " + staffId));

    log.info("Staff with ID: {} found", staffId);
    return new StaffResponseDto(
            staff.getStaffId(),
            staff.getName(),
            staff.getGovIdNumber(),
            encodeToBase64(staff.getGovIdPhoto()),
            encodeToBase64(staff.getStaffPhoto()),
            staff.getContact(),
            staff.getEmail(),
            staff.getPermanentAddress(),
            staff.getCurrentAddress(),
            staff.getRole(),
            staff.getSalary(),
            staff.getJoiningDate(),
            staff.getStaffStatus(),
            staff.getSociety().getSocietyName()
    );
  }


  @Override
  public UUID paySalary(StaffSalaryRequestDto staffSalaryRequestDto) {
    log.info("Processing salary payment for staff with ID: {}", staffSalaryRequestDto.getStaffId());

    Staff staff = staffRepository.findById(staffSalaryRequestDto.getStaffId())
            .orElseThrow(() -> {
              log.error("Staff not found with ID: {}", staffSalaryRequestDto.getStaffId());
              return new StaffNotFoundException(
                      "Staff not found with id: " + staffSalaryRequestDto.getStaffId());
            });
    Society society = societyRepository.findById(staffSalaryRequestDto.getSocietyId())
            .orElseThrow(() -> {
              log.error("Society not found with ID: {}", staffSalaryRequestDto.getSocietyId());
              return new SocietyNotFoundException(
                      "Society not found with id: " + staffSalaryRequestDto.getSocietyId());
            });

    StaffSalary staffSalary = new StaffSalary();
    staffSalary.setStaff(staff);
    staffSalary.setAmount(staffSalaryRequestDto.getAmount());
    staffSalary.setTransactionId(staffSalaryRequestDto.getTransactionId());
    staffSalary.setPaymentDate(staffSalaryRequestDto.getPaymentDate());
    staffSalary.setDescription(staffSalaryRequestDto.getDescription());
    staffSalary.setSociety(society);
    staffSalaryRepository.save(staffSalary);
    log.info("Salary paid successfully to staff with ID: {}. Transaction ID: {}",
            staffSalary.getStaff().getStaffId(), staffSalary.getTransactionId());

    return staffSalary.getSalaryId();
  }

  @Override
  @Transactional(readOnly = true)
  public List<StaffSalaryResponseDto> getStaffSalaryBySociety(UUID societyId) {
    log.info("Fetching staff salaries for society with ID: {}", societyId);
    List<StaffSalary> staffSalaryList = staffSalaryRepository.findAllBySocietyID(societyId);
    if (staffSalaryList.isEmpty()) {
      log.warn("No staff salary records found for society ID: {}", societyId);
    }

    List<StaffSalaryResponseDto> staffSalaryResponseDtoList = new ArrayList<>();
    for (StaffSalary staffSalary : staffSalaryList) {
      StaffSalaryResponseDto staffSalaryResponseDto = new StaffSalaryResponseDto();
      staffSalaryResponseDto.setSalaryId(staffSalary.getSalaryId());
      staffSalaryResponseDto.setName(staffSalary.getStaff().getName());
      staffSalaryResponseDto.setContact(staffSalary.getStaff().getContact());
      staffSalaryResponseDto.setEmail(staffSalary.getStaff().getEmail());
      staffSalaryResponseDto.setTransactionId(staffSalary.getTransactionId());
      staffSalaryResponseDto.setAmount(staffSalary.getAmount());
      staffSalaryResponseDto.setPaymentDate(staffSalary.getPaymentDate());
      staffSalaryResponseDto.setDescription(staffSalary.getDescription());

      staffSalaryResponseDtoList.add(staffSalaryResponseDto);
    }

    log.info("Fetched {} staff salary records for society ID: {}", staffSalaryList.size(),
            societyId);
    return staffSalaryResponseDtoList;
  }

  @Override
  @Transactional(readOnly = true)
  public List<StaffSalaryResponseDto> getStaffSalaryByMonth(LocalDate paidDate) {
    LocalDate date = paidDate;
    int currentMonth = date.getMonthValue();
    int currentYear = date.getYear();
    List<StaffSalary> staffSalaryList = staffSalaryRepository.findStaffSalaryByMonthAndYear(
            currentMonth, currentYear);

    List<StaffSalaryResponseDto> staffSalaryResponseDtoList = new ArrayList<>();
    for (StaffSalary staffSalary : staffSalaryList) {
      StaffSalaryResponseDto staffSalaryResponseDto = new StaffSalaryResponseDto();
      staffSalaryResponseDto.setSalaryId(staffSalary.getSalaryId());
      staffSalaryResponseDto.setName(staffSalary.getStaff().getName());
      staffSalaryResponseDto.setContact(staffSalary.getStaff().getContact());
      staffSalaryResponseDto.setEmail(staffSalary.getStaff().getEmail());
      staffSalaryResponseDto.setTransactionId(staffSalary.getTransactionId());
      staffSalaryResponseDto.setAmount(staffSalary.getAmount());
      staffSalaryResponseDto.setPaymentDate(staffSalary.getPaymentDate());
      staffSalaryResponseDto.setDescription(staffSalary.getDescription());
      staffSalaryResponseDtoList.add(staffSalaryResponseDto);
    }

    return staffSalaryResponseDtoList;
  }


}
