package com.fqts.mysociety.repository;

import com.fqts.mysociety.model.IncomingFile;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomingFileRepository extends JpaRepository<IncomingFile, UUID> {

  Optional<IncomingFile> findByChecksum(String checksum);
}
