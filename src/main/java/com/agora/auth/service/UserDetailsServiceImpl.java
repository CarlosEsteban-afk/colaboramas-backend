package com.agora.auth.service;

import com.agora.user.model.User;
import com.agora.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

        @Autowired
        private UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

                User user = userRepository.findUserByUsername(username)
                                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
                List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

                user.getRoles()
                                .forEach(role -> authorityList.add(
                                                new SimpleGrantedAuthority("ROLE_".concat(role.getRoleName().name()))));

                user.getRoles().stream()
                                .flatMap(role -> role.getPermissionEntities().stream())
                                .forEach(permission -> authorityList.add(
                                                new SimpleGrantedAuthority(permission.getPermissionName().name())));

                return new org.springframework.security.core.userdetails.User(user.getUsername(),
                                user.getPassword(),
                                user.getIsEnabled(),
                                user.getAccountNoExpired(),
                                user.getCredentialNoExpired(),
                                user.getAccountNoLocked(),
                                authorityList);
        }

}
