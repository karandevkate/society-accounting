package com.fqts.mysociety.service;

import com.fqts.mysociety.dto.request.SocietyExpenseRequestDto;
import com.fqts.mysociety.dto.response.SocietyExpenseResponseDto;

import java.util.*;

public interface SocietyExpenseService {

    UUID createExpense(SocietyExpenseRequestDto societyExpenseRequestDto);
    List<SocietyExpenseResponseDto> fetchSocietyExpense(UUID societyId);
}
