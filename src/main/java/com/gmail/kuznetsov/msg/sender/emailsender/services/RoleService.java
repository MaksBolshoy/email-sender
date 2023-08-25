package com.gmail.kuznetsov.msg.sender.emailsender.services;

import com.gmail.kuznetsov.msg.sender.emailsender.repositories.RoleRepository;
import com.gmail.kuznetsov.msg.sender.emailsender.repositories.entities.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getByName(String name) {
        return roleRepository.findByRoleName(name);
    };
}
