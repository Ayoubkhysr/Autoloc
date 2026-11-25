package tn.esprit.tpautoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.web.dto.ClientDTO;

@Component
public class ClientMapper {

    public ClientDTO toDto(Client client) {
        if (client == null) return null;
        return ClientDTO.builder()
                .id(client.getIdClient())
                .nom(client.getNom())
                .prenom(client.getPrenom())
                .email(client.getEmail())
                .telephone(client.getTelephone())
                .numPermis(client.getNumPermis())
                .dateInscription(client.getDateInscription())
                .build();
    }

    public Client toEntity(ClientDTO dto) {
        if (dto == null) return null;
        Client client = new Client();
        client.setIdClient(dto.getId());
        client.setNom(dto.getNom());
        client.setPrenom(dto.getPrenom());
        client.setEmail(dto.getEmail());
        client.setTelephone(dto.getTelephone());
        client.setNumPermis(dto.getNumPermis());
        client.setDateInscription(dto.getDateInscription());
        return client;
    }
}
