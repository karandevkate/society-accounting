package com.fqts.mysociety.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "incoming_file")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IncomingFile {

  @Id
  @Column(name = "incoming_file_id", updatable = false, nullable = false)
  private UUID incomingFileId = UUID.randomUUID();

  @Temporal(TemporalType.DATE)
  @Column(name = "uploaded_date")
  private Date uploadedDate;

  @Column(name = "uploaded_by")
  private String uploadedBy;

  @Column(name = "file_size")
  private Long fileSize;

  @Column(name = "bank_name")
  private String bankName;

  @Column(name = "original_file_name")
  private String originalFileName;

  @Column(name = "content_type")
  private String contentType;

  @Column(name = "number_of_records")
  private Integer numberOfRecords;

  @OneToMany(mappedBy = "incomingFile", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<BankStatement> bankStatements;

  @Column(name = "CHECKSUM")
  private String checksum;

}