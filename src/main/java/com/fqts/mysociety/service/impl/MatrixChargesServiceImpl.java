package com.fqts.mysociety.service.impl;

import com.fqts.mysociety.dto.request.MatrixChargesDTO;
import com.fqts.mysociety.dto.response.MatrixChargesResponseDTO;
import com.fqts.mysociety.exception.FlatNotFoundException;
import com.fqts.mysociety.exception.MatrixChargeNotFoundException;
import com.fqts.mysociety.exception.SocietyNotFoundException;
import com.fqts.mysociety.model.Flat;
import com.fqts.mysociety.model.MatrixCharges;
import com.fqts.mysociety.model.Society;
import com.fqts.mysociety.repository.FlatRepository;
import com.fqts.mysociety.repository.MatrixChargesRepository;
import com.fqts.mysociety.repository.SocietyRepository;
import com.fqts.mysociety.service.MatrixChargesService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatrixChargesServiceImpl implements MatrixChargesService {

  private final MatrixChargesRepository matrixChargesRepository;
  private final SocietyRepository societyRepository;
  private final FlatRepository flatsRepository;

  @Override
  public List<MatrixCharges> getChargesBySocietyAndFlatNumber(UUID societyId, String flatNumber) {
    return matrixChargesRepository.findBySociety_SocietyIdAndFlat_Id_FlatNumber(societyId,
        flatNumber);
  }


  @Override
  public MatrixChargesResponseDTO createCharge(MatrixChargesDTO dto)
      throws SocietyNotFoundException {
    Society society = societyRepository.findById(dto.getSociety())
        .orElseThrow(
            () -> new SocietyNotFoundException("Society not found with id: " + dto.getSociety()));

    Flat flat = flatsRepository.findById_FlatNumber(dto.getFlats())
        .orElseThrow(
            () -> new FlatNotFoundException("Flat not found with number: " + dto.getFlats()));

    MatrixCharges charge = new MatrixCharges();
    charge.setSociety(society);
    charge.setFlat(flat);
    charge.setChargesType(dto.getChargesType());
    charge.setDescription(dto.getDescription());
    charge.setFlatType(dto.getFlatType());
    charge.setAmount(dto.getAmount());
    charge.setInterest(dto.getInterest());
    charge.setPaymentFrequency(dto.getPaymentFrequency());

    MatrixCharges saved = matrixChargesRepository.save(charge);

    MatrixChargesResponseDTO response = new MatrixChargesResponseDTO();
    response.setMatrixChargesId(saved.getMatrixChargesId());
    response.setSocietyId(saved.getSociety().getSocietyId());
    response.setFlatNumber(saved.getFlat().getId().getFlatNumber());
    response.setChargesType(saved.getChargesType());
    response.setDescription(saved.getDescription());
    response.setFlatType(saved.getFlatType());
    response.setAmount(saved.getAmount());
    response.setInterest(saved.getInterest());
    response.setPaymentFrequency(saved.getPaymentFrequency());

    return response;
  }


  @Override
  public List<MatrixCharges> getChargesBySociety(UUID societyId) {
    return matrixChargesRepository.findBySociety_SocietyIdAndFlat_Id_FlatNumber(societyId,
        null); // If flatNumber is not provided, return all for the society
  }

  @Override
  public MatrixCharges updateCharge(UUID id, MatrixCharges updatedCharge) {
    Optional<MatrixCharges> optional = matrixChargesRepository.findById(id);

    if (optional.isPresent()) {
      MatrixCharges existing = optional.get();

      existing.setChargesType(updatedCharge.getChargesType());
      existing.setDescription(updatedCharge.getDescription());
      existing.setFlatType(updatedCharge.getFlatType());
      existing.setAmount(updatedCharge.getAmount());
      existing.setInterest(updatedCharge.getInterest());
      existing.setPaymentFrequency(updatedCharge.getPaymentFrequency());

      // Set relationships properly
      existing.setFlat(updatedCharge.getFlat());         // <-- updated
      existing.setSociety(updatedCharge.getSociety());     // <-- updated

      return matrixChargesRepository.save(existing);
    } else {
      throw new MatrixChargeNotFoundException("Charge not found with id: " + id);
    }
  }

  @Override
  public void deleteCharge(UUID id) {
    if (matrixChargesRepository.existsById(id)) {
      matrixChargesRepository.deleteById(id);
    } else {
      throw new MatrixChargeNotFoundException("Charge not found with id: " + id);
    }
  }


}
