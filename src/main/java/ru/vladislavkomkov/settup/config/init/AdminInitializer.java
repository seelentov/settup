package ru.vladislavkomkov.settup.config.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.vladislavkomkov.settup.model.Admin;
import ru.vladislavkomkov.settup.model.Role;
import ru.vladislavkomkov.settup.repository.RoleRepository;
import ru.vladislavkomkov.settup.repository.AdminRepository;

import java.util.Collections;
import java.util.Optional;

@Component
public class AdminInitializer implements CommandLineRunner {
    private final AdminRepository adminRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(AdminRepository adminRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Optional<Role> ownerOpt = roleRepository.findByName(Role.OWNER);
        if (ownerOpt.isEmpty()) {
            roleRepository.save(new Role(Role.OWNER));
            ownerOpt = roleRepository.findByName(Role.OWNER);
        }

        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setActive(true);
        admin.setRoles(Collections.singleton(ownerOpt.get()));

        adminRepository.save(admin);
    }
}
