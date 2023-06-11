package com.hospital.backend.Services;

import com.hospital.backend.Exceptions.InviteNotFoundException;
import com.hospital.backend.Models.Invite;
import com.hospital.backend.Repositories.InvitesRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvitesService implements IInvitesService{
    private final InvitesRepository invitesRepository;

    private static final Logger logger = LoggerFactory.getLogger(InvitesService.class);

    @Override
    public List<Invite> findAllByDepartment(Long id) {
        return this.invitesRepository.findAllByDepartment_Id(id);
    }

    @Override
    public Invite findByEmail(String email) {
        return this.invitesRepository.findFirstByEmail(email).orElseThrow(InviteNotFoundException::new);
    }

    @Override
    public Invite save(Invite invite) {
        logger.info(String.format("Saving invite with id %d", invite.getId()));
        return invitesRepository.save(invite);
    }

    @Override
    public void deleteById(Long id) {
        invitesRepository.deleteById(id);
    }

    
}
