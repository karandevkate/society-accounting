package com.fqts.mysociety.repository;

import com.fqts.mysociety.model.MatrixCharges;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatrixChargesRepository extends JpaRepository<MatrixCharges, UUID> {
    //    List<MatrixCharges> findBySociety_SocietyIdAndFlats_FlatNumber(UUID societyId, String flatNumber);
    List<MatrixCharges> findBySociety_SocietyIdAndFlat_Id_FlatNumber(UUID societyId, String flatNumber);

}