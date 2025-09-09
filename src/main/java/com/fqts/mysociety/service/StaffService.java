package com.fqts.mysociety.service;

import com.fqts.mysociety.dto.request.StaffRequestDto;
import com.fqts.mysociety.dto.request.StaffSalaryRequestDto;
import com.fqts.mysociety.dto.response.StaffResponseDto;
import com.fqts.mysociety.dto.response.StaffSalaryResponseDto;
import com.fqts.mysociety.exception.StaffNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface StaffService {

  UUID createStaff(StaffRequestDto staffRequestDto);

  UUID updateStaffDetails(UUID staffId, StaffRequestDto staffRequestDto);

  Boolean updateStaffStatus(UUID staffId) throws StaffNotFoundException;

  List<StaffResponseDto> getStaffBySocietyId(UUID SocietyId);

  StaffResponseDto getStaffByIdAndSocietyId(UUID staffId, UUID SocietyId);

  StaffResponseDto getStaffById(UUID staffId);

  UUID paySalary(StaffSalaryRequestDto staffSalaryRequestDto);

  List<StaffSalaryResponseDto> getStaffSalaryBySociety(UUID SocietyId);

  List<StaffSalaryResponseDto> getStaffSalaryByMonth(LocalDate paidDate);
}
