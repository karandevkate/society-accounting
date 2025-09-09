package com.fqts.mysociety.service.impl;

import com.fqts.mysociety.dto.request.SocietyExpenseRequestDto;
import com.fqts.mysociety.dto.response.SocietyExpenseResponseDto;
import com.fqts.mysociety.exception.ExpenseNotFoundException;
import com.fqts.mysociety.exception.SocietyNotFoundException;
import com.fqts.mysociety.model.Society;
import com.fqts.mysociety.model.SocietyExpense;
import com.fqts.mysociety.repository.SocietyExpenseRepository;
import com.fqts.mysociety.repository.SocietyRepository;
import com.fqts.mysociety.repository.StaffSalaryRepository;
import com.fqts.mysociety.service.SocietyExpenseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SocietyExpenseImpl implements SocietyExpenseService {

    private static final Logger log = LoggerFactory.getLogger(SocietyExpenseImpl.class);
    private final SocietyRepository societyRepository;
    private final SocietyExpenseRepository societyExpenseRepository;
    private final StaffSalaryRepository salaryRepository;

    @Override
    public UUID createExpense(SocietyExpenseRequestDto societyExpenseRequestDto) {
        log.info("Creating new expense: {}", societyExpenseRequestDto);
        SocietyExpense societyExpense = new SocietyExpense();
        societyExpense.setExpenseType(societyExpenseRequestDto.getExpenseType());
        societyExpense.setExpenseDate(societyExpenseRequestDto.getExpenseDate());
        societyExpense.setTotalAmount(societyExpenseRequestDto.getTotalAmount());
        societyExpense.setDescription(societyExpenseRequestDto.getDescription());
        societyExpense.setCreatedAt(new Date());
        Society society = societyRepository.findById(societyExpenseRequestDto.getSocietyId()).orElseThrow(() -> new SocietyNotFoundException(societyExpenseRequestDto.getSocietyId()));

        societyExpense.setSociety(society);
        societyExpenseRepository.save(societyExpense);
        log.info("Expense created successfully with ID: {}", societyExpense.getExpenseId());
        return societyExpense.getExpenseId();
    }


    @Override
    public List<SocietyExpenseResponseDto> fetchSocietyExpense(UUID societyId) {
        log.info("Fetching expenses for society ID: {}", societyId);
        if (!societyRepository.existsById(societyId)) {
            log.warn("Society with ID {} not found", societyId);
            throw new SocietyNotFoundException("Society with ID " + societyId + " not found");
        }
        List<SocietyExpense> societyExpenseList = societyExpenseRepository.findSocietyExpenseBySocietyID(societyId);

        if (societyExpenseList == null || societyExpenseList.isEmpty()) {
            log.warn("No expenses found for society ID: {}", societyId);
            throw new ExpenseNotFoundException("No expenses found for society ID: " + societyId);
        }

        List<SocietyExpenseResponseDto> societyExpenseResponseDtoList = new ArrayList<>();
        for (SocietyExpense societyExpense : societyExpenseList) {
            SocietyExpenseResponseDto societyExpenseResponseDto = new SocietyExpenseResponseDto();
            societyExpenseResponseDto.setExpenseId(societyExpense.getExpenseId());
            societyExpenseResponseDto.setExpenseType(societyExpense.getExpenseType());
            societyExpenseResponseDto.setExpenseDate(societyExpense.getExpenseDate());
            societyExpenseResponseDto.setTotalAmount(societyExpense.getTotalAmount());
            societyExpenseResponseDto.setDescription(societyExpense.getDescription());
            societyExpenseResponseDtoList.add(societyExpenseResponseDto);
        }
        log.info("Fetched {} expenses for society ID: {}", societyExpenseList.size(), societyId);
        return societyExpenseResponseDtoList;
    }

}
