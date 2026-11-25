package tn.esprit.tpautoloc.service.impl;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IClientRepository;
import tn.esprit.tpautoloc.service.IClientService;
import tn.esprit.tpautoloc.web.dto.ClientDTO;
import tn.esprit.tpautoloc.web.mapper.ClientMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public Client create(Client client) {
        if (client.getIdClient() != null) {
            throw new IllegalArgumentException("Un nouveau client ne doit pas avoir d'identifiant");
        }
        if (client.getNom() == null || client.getNom().trim().isEmpty() ||
            client.getEmail() == null || client.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom et l'email du client sont obligatoires");
        }
        return clientRepository.save(client);
    }

    @Override
    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client update(Long id, Client client) {
        if (client.getNom() == null || client.getNom().trim().isEmpty() ||
            client.getEmail() == null || client.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom et l'email du client sont obligatoires");
        }
        Client existant = findById(id);
        existant.setNom(client.getNom());
        existant.setPrenom(client.getPrenom());
        existant.setEmail(client.getEmail());
        existant.setTelephone(client.getTelephone());
        existant.setNumPermis(client.getNumPermis());
        existant.setDateInscription(client.getDateInscription());
        return clientRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client introuvable", id);
        }
        clientRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return clientRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return clientRepository.existsById(id);
    }

    // Atelier 7 DTO
    @Override
    public ClientDTO creer(ClientDTO dto) {
        Client client = clientMapper.toEntity(dto);
        if (client.getDateInscription() == null) {
            client.setDateInscription(LocalDate.now());
        }
        Client saved = clientRepository.save(client);
        return clientMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDTO findByIdDto(Long id) {
        return clientMapper.toDto(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> findAllDto() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toDto)
                .toList();
    }
}
