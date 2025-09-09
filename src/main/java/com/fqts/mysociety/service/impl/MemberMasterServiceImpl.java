package com.fqts.mysociety.service.impl;

import com.fqts.mysociety.dto.response.SocietyMemberListDTO;
import com.fqts.mysociety.exception.FlatNotFoundException;
import com.fqts.mysociety.exception.MemberNotFoundException;
import com.fqts.mysociety.model.*;
import com.fqts.mysociety.repository.FlatRepository;
import com.fqts.mysociety.repository.MemberRepository;
import com.fqts.mysociety.repository.SocietyMemberRepository;
import com.fqts.mysociety.repository.SocietyRepository;
import com.fqts.mysociety.service.MemberMasterService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberMasterServiceImpl implements MemberMasterService {
    private static final Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);
    private final SocietyMemberRepository societyMemberRepository;
    private final MemberRepository memberRepository;
    private final SocietyRepository societyRepository;
    private  final FlatRepository flatRepository;
    public List<SocietyMemberListDTO> getMembersBySocietyId(UUID societyId) {
        List<SocietyMember> members = societyMemberRepository.findBySocietyId(societyId);

        return members.stream().map(sm -> {
            var member = sm.getMember();
            var flat = sm.getFlats().isEmpty() ? null : sm.getFlats().get(0); // pick the first flat

            return new SocietyMemberListDTO(
                    flat != null ? flat.getId().getFlatNumber() : null,
                    flat != null ? flat.getFlat() : null,
                    flat != null ? flat.getWing() : null,
                    flat != null ? flat.getFlatType() : null,
                    flat != null && flat.isSelfOccupied(),
                    sm.getSociety().getSocietyId(),
                    sm.getSociety().getSocietyName(),
                    sm.getSociety().getRegistrationNumber(),
                    sm.getSocietyMemberId(),
                    sm.getStatus() == Status.APPROVED,
                    sm.getRole(),
                    member.getMemberId(),
                    member.getFullName(),
                    member.getMobile(),
                    member.getAlternateMobile(),
                    member.getEmail(),
                    member.getAlternateEmail(),
                    member.getNativeAddress(),
                    member.getNativeMobile(),
                    member.getGovId(),
                    member.getCreatedAt()
            );
        }).collect(Collectors.toList());
    }

    public List<SocietyMemberListDTO> getApprovedOrAutoApprovedMembers(UUID societyId) {
        List<SocietyMember> members = societyMemberRepository.findBySocietyId(societyId);

        return members.stream()
                .filter(sm -> sm.getStatus() == Status.APPROVED || sm.getStatus() == Status.AUTOAPPROVED)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<SocietyMemberListDTO> getPendingMembers(UUID societyId) {
        List<SocietyMember> members = societyMemberRepository.findBySocietyId(societyId);

        return members.stream()
                .filter(sm -> sm.getStatus() == Status.PENDING)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private SocietyMemberListDTO mapToDTO(SocietyMember sm) {
        var member = sm.getMember();
        var flat = sm.getFlats().isEmpty() ? null : sm.getFlats().get(0);

        return new SocietyMemberListDTO(
                flat != null ? flat.getId().getFlatNumber() : null,
                flat != null ? flat.getFlat() : null,
                flat != null ? flat.getWing() : null,
                flat != null ? flat.getFlatType() : null,
                flat != null && flat.isSelfOccupied(),
                sm.getSociety().getSocietyId(),
                sm.getSociety().getSocietyName(),
                sm.getSociety().getRegistrationNumber(),
                sm.getSocietyMemberId(),
                sm.getStatus() == Status.APPROVED || sm.getStatus() == Status.AUTOAPPROVED,
                sm.getRole(),
                member.getMemberId(),
                member.getFullName(),
                member.getMobile(),
                member.getAlternateMobile(),
                member.getEmail(),
                member.getAlternateEmail(),
                member.getNativeAddress(),
                member.getNativeMobile(),
                member.getGovId(),
                member.getCreatedAt()
        );
    }



    @Override
    public void updateLastVisited(UUID memberId, UUID societyId, FlatId flatId) {
        log.info("Updating last visited for memberId: {}, societyId: {}, flatId: {}", memberId, societyId, flatId);

        // Fetch member
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.error("Member not found with memberId: {}", memberId);
                    return new RuntimeException("Member not found");
                });


        // Update centralized last visited fields
        member.setLastVisitedSocietyId(societyId);

        SocietyMember societyMember = societyMemberRepository
                .findByMember_MemberIdAndSociety_SocietyId(memberId, societyId)
                .orElseThrow(() -> {
                    log.error("Society Member not found with memberId : {} and societyId: {}", memberId, societyId);
                    return new MemberNotFoundException("Society Member not found associated with society");
                });

        if (flatId != null) {
            societyMember.setLastVisitedFlatNumber(flatId.getFlatNumber()); // or getFlatNumber() depending on your FlatId
        }


        memberRepository.save(member);
        log.info("Updated last visited info for memberId: {}. Society: {}, Flat: {}",
                memberId, societyId, flatId != null ? flatId.getFlatNumber() : null);
    }

}

