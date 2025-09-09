package com.fqts.mysociety.scheduler;

import com.fqts.mysociety.model.Society;
import com.fqts.mysociety.model.Status;
import com.fqts.mysociety.service.SocietyService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SocietyStatusScheduler {

  private static final Logger logger = LoggerFactory.getLogger(SocietyStatusScheduler.class);
  private final SocietyService societyService;

  public SocietyStatusScheduler(SocietyService societyService) {
    this.societyService = societyService;
  }

  @Scheduled(cron = "0 0 0 * * *")
  public void updateAutoApprovedSocieties() {
    logger.info("Running scheduled task to update auto-approved societies.");
    LocalDate fifteenDaysAgo = LocalDate.now().minusDays(15);
    Date cutoffDate = Date.from(fifteenDaysAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());

    List<Society> autoApprovedSocieties = societyService.findByStatusAndRegisteredBefore(
        Status.AUTOAPPROVED, cutoffDate);

    for (Society society : autoApprovedSocieties) {
      logger.info(
          "Society {} with status AUTOAPPROVED registered on {} is now being set to PENDING.",
          society.getSocietyId(), society.getRegisteredAt());
      society.setStatus(Status.PENDING);
      societyService.save(society);
    }
    logger.info("Scheduled task to update auto-approved societies completed. {} societies updated.",
        autoApprovedSocieties.size());
  }
}