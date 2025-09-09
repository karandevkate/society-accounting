package com.fqts.mysociety.service;

import com.fqts.mysociety.dto.request.MatrixChargesDTO;
import com.fqts.mysociety.dto.response.MatrixChargesResponseDTO;
import com.fqts.mysociety.exception.SocietyNotFoundException;
import com.fqts.mysociety.model.MatrixCharges;
import java.util.List;
import java.util.UUID;

public interface MatrixChargesService {

  List<MatrixCharges> getChargesBySocietyAndFlatNumber(UUID societyId, String flatNumber);

  MatrixChargesResponseDTO createCharge(MatrixChargesDTO chargesDTO)
      throws SocietyNotFoundException;

  List<MatrixCharges> getChargesBySociety(UUID societyId);

  MatrixCharges updateCharge(UUID id, MatrixCharges updatedCharge);

  void deleteCharge(UUID id);
}