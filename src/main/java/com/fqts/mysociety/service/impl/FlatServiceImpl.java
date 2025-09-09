package com.fqts.mysociety.service.impl;

import com.fqts.mysociety.dto.request.FlatRequestDTO;
import com.fqts.mysociety.dto.response.FlatResponseDTO;
import com.fqts.mysociety.exception.FlatNotFoundException;
import com.fqts.mysociety.exception.SocietyNotFoundException;
import com.fqts.mysociety.model.Flat;
import com.fqts.mysociety.model.FlatId;
import com.fqts.mysociety.model.Society;
import com.fqts.mysociety.model.SocietyMember;
import com.fqts.mysociety.repository.FlatRepository;
import com.fqts.mysociety.repository.SocietyMemberRepository;
import com.fqts.mysociety.repository.SocietyRepository;
import com.fqts.mysociety.service.FlatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlatServiceImpl implements FlatService {

    private final FlatRepository flatRepository;
    private final SocietyRepository societyRepository;
    private final SocietyMemberRepository societyMemberRepository;


    @Override
    public FlatResponseDTO createFlat(FlatRequestDTO dto) throws SocietyNotFoundException {
        log.info("Creating flat with number: {}-{}", dto.getWing(), dto.getFlat());
        Society society = societyRepository.findById(dto.getSocietyId())
                .orElseThrow(() -> {
                    log.error("Society not found with ID: {}", dto.getSocietyId());
                    return new SocietyNotFoundException(dto.getSocietyId());
                });

        Flat flat = new Flat();
        FlatId flatId = new FlatId();
        flatId.setFlatNumber(dto.getWing() + "-" + dto.getFlat()); // Combine wing and flat for flatNumber
        flatId.setSocietyId(dto.getSocietyId());
        flat.setId(flatId);

        flat.setWing(dto.getWing());
        flat.setFlat(dto.getFlat());
        flat.setFlatType(dto.getFlatType());
        flat.setSociety(society);
        flat.setSocietyMember(null);
        flat.setSelfOccupied(dto.isSelfOccupied());
        Flat saved = flatRepository.save(flat);
        log.info("Flat created successfully: {}", saved.getId().getFlatNumber());
        return mapToResponseDTO(saved);
    }

    @Override
    public FlatResponseDTO getFlatById(String flatNumber, UUID societyId) throws FlatNotFoundException {
        log.info("Fetching flat by number: {} and societyId: {}", flatNumber, societyId);

        FlatId flatId = new FlatId(flatNumber, societyId);

        Flat flat = flatRepository.findById(flatId)
                .orElseThrow(() -> {
                    log.error("Flat not found with number: {} and societyId: {}", flatNumber, societyId);
                    return new FlatNotFoundException(flatNumber);
                });

        return mapToResponseDTO(flat);
    }

    @Override
    public List<FlatResponseDTO> getFlatsBySocietyId(UUID societyId) throws SocietyNotFoundException {
        log.info("Fetching flats by societyId: {}", societyId);
        Society society = societyRepository.findById(societyId)
                .orElseThrow(() -> {
                    log.error("Society not found with ID: {}", societyId);
                    return new SocietyNotFoundException(societyId);
                });

        List<Flat> flats = flatRepository.findBySociety(society);
        log.info("Found {} flats for societyId {}", flats.size(), societyId);
        return flats.stream().map(this::mapToResponseDTO).toList();

    }

    private FlatResponseDTO mapToResponseDTO(Flat flat) {
        String societyName = null;
        Society society = societyRepository.findById(flat.getId().getSocietyId()).orElse(null);
        if (society != null) {
            societyName = society.getSocietyName();
        }

        return new FlatResponseDTO(
                flat.getId().getFlatNumber(),
                flat.getWing(),
                flat.getFlat(),
                flat.getId().getSocietyId(),
                societyName,
                flat.getFlatType(),
                flat.isSelfOccupied()
        );
    }

    @Override
    public List<FlatResponseDTO> getFlatsBySocietyMemberAndSociety(UUID societyMemberId, UUID societyId) {
        log.info("Fetching flats for societyMemberId: {} in societyId: {}", societyMemberId, societyId);

        List<Flat> flats = flatRepository.findBySocietyMemberIdAndSocietyId(societyMemberId, societyId);
        log.debug("Found {} flats", flats.size());

        List<FlatResponseDTO> responseList = flats.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        log.info("Returning {} flats registered by member in society", responseList.size());
        return responseList;
    }

    @Override
    public void registerMemberToFlat(String flatNumber, UUID societyId, UUID societyMemberId) {
        log.info("Attempting to register societyMemberId [{}] to flat [{}] in society [{}]",
                societyMemberId, flatNumber, societyId);

        Flat flat = flatRepository.findById_FlatNumberAndId_SocietyId(flatNumber, societyId)
                .orElseThrow(() -> {
                    log.error("Flat [{}] in society [{}] not found", flatNumber, societyId);
                    return new FlatNotFoundException(flatNumber);
                });

        SocietyMember societyMember = societyMemberRepository.findById(societyMemberId)
                .orElseThrow(() -> {
                    log.error("Society Member [{}] not found", societyMemberId);
                    return new RuntimeException("Society Member Not Found");
                });

        flat.setSocietyMember(societyMember);
        flatRepository.save(flat);

        log.info("Successfully registered societyMemberId [{}] to flat [{}]", societyMemberId, flatNumber);
    }


    public void saveFlatsFromExcel(MultipartFile file, UUID societyId) throws IOException, SocietyNotFoundException {
        log.info("Uploading flats for society ID: {}", societyId);
        Society society = societyRepository.findById(societyId)
                .orElseThrow(() -> {
                    log.error("Society not found with ID: {}", societyId);
                    return new SocietyNotFoundException(societyId);
                });

        List<Flat> flats = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                String wing = getCellValue(row.getCell(0)); // Wing
                String flat = getCellValue(row.getCell(1)); // Flat
                String flatType = getCellValue(row.getCell(2)); // Flat Type
                String ownerStatus = getCellValue(row.getCell(3)); // Owner Status

                if (wing.isEmpty() || flat.isEmpty() || flatType.isEmpty()) {
                    log.warn("Skipping row {} due to missing required fields", row.getRowNum());
                    continue;
                }

                Flat flatEntity = new Flat();
                FlatId flatId = new FlatId();
                flatId.setFlatNumber(wing + "-" + flat); // Combine wing and flat for flatNumber
                flatId.setSocietyId(societyId);
                flatEntity.setId(flatId);
                flatEntity.setWing(wing);
                flatEntity.setFlat(flat);
                flatEntity.setFlatType(flatType);
                flatEntity.setSociety(society);
                flatEntity.setSocietyMember(null);
                flatEntity.setSelfOccupied("Yes".equalsIgnoreCase(ownerStatus));

                flats.add(flatEntity);
            }

            flatRepository.saveAll(flats);
            log.info("Successfully uploaded {} flats for society ID: {}", flats.size(), societyId);
        } catch (IOException e) {
            log.error("Error processing Excel file: {}", e.getMessage());
            throw e;
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

}
