package tn.esprit.tpautoloc.service;

import java.util.List;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.web.dto.ClientDTO;

public interface IClientService {
    Client create(Client client);
    Client findById(Long id);
    List<Client> findAll();
    Client update(Long id, Client client);
    void deleteById(Long id);
    long count();
    boolean existsById(Long id);

    // Atelier 7 : DTO
    ClientDTO creer(ClientDTO dto);
    ClientDTO findByIdDto(Long id);
    List<ClientDTO> findAllDto();
}
