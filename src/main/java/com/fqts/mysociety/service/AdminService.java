package com.fqts.mysociety.service;

import com.fqts.mysociety.model.Role;
import java.util.UUID;

public interface AdminService {

  void approveMember(UUID memberId, Role role);
  void rejectMember(UUID societyMemberId, Role role);
}
