package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.model.Authority;
import kg.attractor.job_search.model.Role;
import kg.attractor.job_search.model.RoleAuthority;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.model.UserRole;
import kg.attractor.job_search.repository.RoleAuthorityRepository;
import kg.attractor.job_search.repository.UserRepository;
import kg.attractor.job_search.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleAuthorityRepository roleAuthorityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        List<UserRole> userRoles = userRoleRepository.findById_User(user);
        List<Role> roles = userRoles.stream()
                .map(userRole -> userRole.getId().getRole())
                .toList();

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));

            List<RoleAuthority> roleAuthorities = roleAuthorityRepository.findById_Role(role);
            for (RoleAuthority roleAuthority : roleAuthorities) {
                Authority authority = roleAuthority.getId().getAuthority();
                authorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
            }
        }

        boolean enabled = user.getEnabled() != null && user.getEnabled();

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                enabled,
                true,
                true,
                true,
                authorities
        );
    }
}