package com.hit.comemyway.service;

import com.hit.comemyway.dto.request.ClinicAccountListRequest;
import com.hit.comemyway.dto.response.ClinicAccountListResponse;
import com.hit.comemyway.dto.response.ClinicStatisticsResponse;
import com.hit.comemyway.entity.AccountStatus;
import com.hit.comemyway.entity.Role;
import com.hit.comemyway.repository.ClinicStatisticsRepository;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
  private final ClinicStatisticsRepository clinicStatisticsRepository;

  public ClinicAccountListResponse listClinicAccounts(ClinicAccountListRequest request) {
    String keyword = request.getKeyword() == null || request.getKeyword().isBlank() ? null
        : "%" + request.getKeyword().trim().toLowerCase(Locale.ROOT) + "%";
    var page = PageRequest.of(0, request.getLimit() + 1);
    List<ClinicStatisticsResponse> rows = "active".equals(request.getActivation())
        ? clinicStatisticsRepository.activeClinicList(Role.CLINIC, AccountStatus.ACTIVE, keyword,
            request.getCursor(), page)
        : clinicStatisticsRepository.inactiveClinicList(Role.CLINIC, AccountStatus.ACTIVE, keyword,
            request.getCursor(), page);
    boolean hasNext = rows.size() > request.getLimit();
    List<ClinicStatisticsResponse> content = rows.stream().limit(request.getLimit()).toList();
    Long nextCursor = hasNext ? content.get(content.size() - 1).userId() : null;
    return new ClinicAccountListResponse(content, hasNext, nextCursor);
  }
}
