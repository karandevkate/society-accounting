package com.fqts.mysociety.service;

import com.fqts.mysociety.dto.request.BillCollectionRequestDTO;
import com.fqts.mysociety.dto.response.BillCollectionResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface BillCollectionService {
    BillCollectionResponseDTO saveBill(BillCollectionRequestDTO requestDTO, MultipartFile image);

    BillCollectionResponseDTO getBillById(UUID id);

    List<BillCollectionResponseDTO> getAllBillBySocietyId(UUID societyId);

    List<BillCollectionResponseDTO> getBillsByFlatNumber(String flatNumber);

    BillCollectionResponseDTO manuallyVerifyBill(UUID billId, String verifiedBy);

    void deleteBillById(UUID id);

}
