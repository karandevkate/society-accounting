package com.fqts.mysociety.scheduler;

import com.fqts.mysociety.model.Society;
import com.fqts.mysociety.model.SocietyExpense;
import com.fqts.mysociety.repository.SocietyExpenseRepository;
import com.fqts.mysociety.repository.StaffSalaryRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class SocietyExpenseScheduler {

    private static final Logger log = LoggerFactory.getLogger(SocietyExpenseScheduler.class);
    private final SocietyExpenseRepository societyExpenseRepository;
    private final StaffSalaryRepository salaryRepository;

    //    @Scheduled(cron = "*/5 * * * * *")
    @Scheduled(initialDelay = 5000, fixedDelay = Long.MAX_VALUE)
    public void generateMonthlySalaryExpense() {
        log.info("Starting monthly salary expense generation for: June 2025");
            LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();

        List<Object[]> result = salaryRepository.findTotalPaidAndSocietyByMonthAndYear(currentMonth, currentYear);
        if (result.isEmpty()) {
            log.warn("No salaries found for current month: {}", today.getMonth());
            return;
        }
        Object[] row = result.get(0); // Assuming one society only
        Society society = (Society) row[0];
        Double total = (Double) row[1];
        if (total == null || total <= 0) {
            log.warn("Total salary is zero or null for current month: {}", today.getMonth());
            return;
        }
        LocalDate salaryForMonth = today.minusMonths(1);
        SocietyExpense expense = new SocietyExpense();
        expense.setExpenseId(UUID.randomUUID());
        expense.setExpenseType("Salaries");
        expense.setExpenseDate(today);
        expense.setTotalAmount(total.floatValue());
        expense.setDescription("Salary For " + salaryForMonth.getMonth() + " " + salaryForMonth.getYear());
        expense.setCreatedAt(new Date());
        expense.setSociety(society);
        societyExpenseRepository.save(expense);
        log.info("Saved salary expense for: {} {}", salaryForMonth.getMonth(), salaryForMonth.getYear());
    }

}
