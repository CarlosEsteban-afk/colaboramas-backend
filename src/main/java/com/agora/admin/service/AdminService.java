package com.agora.admin.service;

import com.agora.admin.model.AdminStats;
import com.agora.admin.model.CountryCount;
import com.agora.admin.repository.AdminStatsRepository;
import com.agora.admin.repository.CountryCountRepository;
import com.agora.auth.model.RoleEnum;
import com.agora.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminStatsRepository adminStatsRepository;

    @Autowired
    private CountryCountRepository countryCountRepository;

    @Transactional
    public AdminStats refreshStats() {
        long total = userRepository.count();
        long investigators = userRepository.countByRolesRoleName(RoleEnum.ACADEMICO);
        long communicators = userRepository.countByRolesRoleName(RoleEnum.COMUNICADOR);

        AdminStats stats = new AdminStats(total, investigators, communicators);
        adminStatsRepository.save(stats);

        // refresh country counts (top 8)
        List<Object[]> byCountry = userRepository.countUsersByCountry();
        List<CountryCount> countries = new ArrayList<>();
        int limit = Math.min(8, byCountry.size());
        for (int i = 0; i < limit; i++) {
            Object[] row = byCountry.get(i);
            String country = (String) row[0];
            Number cnt = (Number) row[1];
            countries.add(new CountryCount(country, cnt.longValue()));
        }

        // replace existing country counts
        countryCountRepository.deleteAll();
        countryCountRepository.saveAll(countries);

        return stats;
    }

    public AdminStats getLatestStats() {
        return adminStatsRepository.findTopByOrderByCreatedAtDesc().orElse(null);
    }

    public List<CountryCount> getTopCountries() {
        return countryCountRepository.findAll();
    }
}
