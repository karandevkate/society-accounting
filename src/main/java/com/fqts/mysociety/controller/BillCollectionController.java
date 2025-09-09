package com.fqts.mysociety.controller;

import com.fqts.mysociety.dto.request.BillCollectionRequestDTO;
import com.fqts.mysociety.dto.response.BillCollectionResponseDTO;
import com.fqts.mysociety.service.BillCollectionService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/bills")
@AllArgsConstructor
public class BillCollectionController {

  private static final Logger logger = LoggerFactory.getLogger(BillCollectionController.class);

  private final BillCollectionService billCollectionService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BillCollectionResponseDTO createBill(
            @RequestPart("dto") BillCollectionRequestDTO dto,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        logger.info(dto.toString());
        logger.info("API called: Create Bill for flat: {}, amount: {}", dto.getFlatNumber(), dto.getPaidAmount());
        return billCollectionService.saveBill(dto, image);
    }

  @PostMapping("/{id}/verify")
  public BillCollectionResponseDTO verifyBill(
      @PathVariable UUID id,
      @RequestParam String verifiedBy) {
    logger.info("API called: Verify Bill with ID: {} by {}", id, verifiedBy);
    return billCollectionService.manuallyVerifyBill(id, verifiedBy);
  }

  @GetMapping("/{id}")
  public BillCollectionResponseDTO getBill(@PathVariable UUID id) {
    logger.info("API called: Get Bill with ID: {}", id);
    return billCollectionService.getBillById(id);
  }

  @GetMapping("/society/{societyId}")
  public List<BillCollectionResponseDTO> getAllBills(@PathVariable UUID societyId) {
    logger.info("API called: Get All Bills");
    return billCollectionService.getAllBillBySocietyId(societyId);
  }

  @GetMapping("/flat/{flatNumber}")
  public List<BillCollectionResponseDTO> getBillsByFlat(@PathVariable String flatNumber) {
    return billCollectionService.getBillsByFlatNumber(flatNumber);
  }

  @DeleteMapping("/{id}")
  public void deleteBill(@PathVariable UUID id) {
    logger.warn("API called: Delete Bill with ID: {}", id);
    billCollectionService.deleteBillById(id);
  }
}