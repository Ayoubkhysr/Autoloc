# Atelier 5 : Spring MVC et SpringDoc — Documentation de l'API REST
## Note de synthèse (`docs/api-notes.md`)

---

### A. Endpoints

Tableau récapitulatif des endpoints exposés par l'API REST AutoLoc (préfixe global `/api` défini via `server.servlet.context-path=/api`) :

| Contrôleur | Verbe HTTP | Route | Méthode Service appelée | Code Succès | Codes d'Erreur | Description |
| :--- | :--- | :--- | :--- | :---: | :---: | :--- |
| `ContratController` | `GET` | `/api/contrats` | `contratService.findAll()` | 200 | — | Liste tous les contrats |
| `ContratController` | `GET` | `/api/contrats/{id}` | `contratService.findById(id)` | 200 | 404 | Consulte un contrat par identifiant |
| `ContratController` | `POST` | `/api/contrats` | `contratService.create(contrat)` | 201 | 400 | Crée un nouveau contrat (avec paiements en cascade) |
| `ContratController` | `PUT` | `/api/contrats/{id}` | `contratService.update(id, contrat)` | 200 | 400, 404 | Modifie un contrat existant |
| `ContratController` | `DELETE` | `/api/contrats/{id}` | `contratService.deleteById(id)` | 204 | 404 | Supprime un contrat (cascade remove sur paiements) |
| `PaiementController` | `GET` | `/api/paiements` | `paiementService.findAll()` | 200 | — | Liste tous les paiements (lecture seule) |
| `PaiementController` | `GET` | `/api/paiements/{id}` | `paiementService.findById(id)` | 200 | 404 | Consulte un paiement par identifiant |
| `PaiementController` | `POST` | `/api/paiements` | *(non exposé)* | — | 405 | Refusé : les paiements se créent via le Contrat |
| `AgenceController` | `GET` | `/api/agences` | `agenceService.findAll()` | 200 | — | Liste toutes les agences |
| `AgenceController` | `GET` | `/api/agences/{id}` | `agenceService.findById(id)` | 200 | 404 | Consulte une agence |
| `AgenceController` | `POST` | `/api/agences` | `agenceService.create(agence)` | 201 | 400 | Crée une agence (nom obligatoire) |
| `AgenceController` | `PUT` | `/api/agences/{id}` | `agenceService.update(id, agence)` | 200 | 400, 404 | Modifie une agence |
| `AgenceController` | `DELETE` | `/api/agences/{id}` | `agenceService.deleteById(id)` | 204 | 404 | Supprime une agence |
| `EmployeController` | `GET` | `/api/employes` | `employeService.findAll()` | 200 | — | Liste tous les employés |
| `EmployeController` | `GET` | `/api/employes/{id}` | `employeService.findById(id)` | 200 | 404 | Consulte un employé |
| `EmployeController` | `POST` | `/api/employes` | `employeService.create(employe)` | 201 | 400 | Crée un employé (nom & rôle obligatoires) |
| `EmployeController` | `PUT` | `/api/employes/{id}` | `employeService.update(id, employe)` | 200 | 400, 404 | Modifie un employé |
| `EmployeController` | `DELETE` | `/api/employes/{id}` | `employeService.deleteById(id)` | 204 | 404 | Supprime un employé |
| `VehiculeController` | `GET` | `/api/vehicules` | `vehiculeService.findAll()` | 200 | — | Liste tous les véhicules |
| `VehiculeController` | `GET` | `/api/vehicules/{id}` | `vehiculeService.findById(id)` | 200 | 404 | Consulte un véhicule |
| `VehiculeController` | `POST` | `/api/vehicules` | `vehiculeService.create(vehicule)` | 201 | 400 | Ajoute un véhicule (tarif journalier non négatif) |
| `VehiculeController` | `PUT` | `/api/vehicules/{id}` | `vehiculeService.update(id, vehicule)` | 200 | 400, 404 | Modifie un véhicule |
| `VehiculeController` | `DELETE` | `/api/vehicules/{id}` | `vehiculeService.deleteById(id)` | 204 | 404 | Supprime un véhicule |
| `EquipementController` | `GET` | `/api/equipements` | `equipementService.findAll()` | 200 | — | Liste tous les équipements |
| `EquipementController` | `GET` | `/api/equipements/{id}` | `equipementService.findById(id)` | 200 | 404 | Consulte un équipement |
| `EquipementController` | `POST` | `/api/equipements` | `equipementService.create(equipement)` | 201 | 400 | Crée un équipement (libellé non vide) |
| `EquipementController` | `PUT` | `/api/equipements/{id}` | `equipementService.update(id, equipement)` | 200 | 400, 404 | Modifie un équipement |
| `EquipementController` | `DELETE` | `/api/equipements/{id}` | `equipementService.deleteById(id)` | 204 | 404 | Supprime un équipement |
| `ClientController` | `GET` | `/api/clients` | `clientService.findAll()` | 200 | — | Liste tous les clients |
| `ClientController` | `GET` | `/api/clients/{id}` | `clientService.findById(id)` | 200 | 404 | Consulte un client |
| `ClientController` | `POST` | `/api/clients` | `clientService.create(client)` | 201 | 400 | Crée un client (nom et e-mail obligatoires) |
| `ClientController` | `PUT` | `/api/clients/{id}` | `clientService.update(id, client)` | 200 | 400, 404 | Modifie un client |
| `ClientController` | `DELETE` | `/api/clients/{id}` | `clientService.deleteById(id)` | 204 | 404 | Supprime un client |
| `ReservationController` | `GET` | `/api/reservations` | `reservationService.findAll()` | 200 | — | Liste toutes les réservations |
| `ReservationController` | `GET` | `/api/reservations/{id}` | `reservationService.findById(id)` | 200 | 404 | Consulte une réservation |
| `ReservationController` | `POST` | `/api/reservations` | `reservationService.create(reservation)` | 201 | 400 | Crée une réservation (date fin >= date début) |
| `ReservationController` | `PUT` | `/api/reservations/{id}` | `reservationService.update(id, reservation)` | 200 | 400, 404 | Modifie une réservation |
| `ReservationController` | `DELETE` | `/api/reservations/{id}` | `reservationService.deleteById(id)` | 204 | 404 | Supprime une réservation |
| `MaintenanceController` | `GET` | `/api/maintenances` | `maintenanceService.findAll()` | 200 | — | Liste toutes les maintenances |
| `MaintenanceController` | `GET` | `/api/maintenances/{id}` | `maintenanceService.findById(id)` | 200 | 404 | Consulte une maintenance |
| `MaintenanceController` | `POST` | `/api/maintenances` | `maintenanceService.create(maintenance)` | 201 | 400 | Enregistre une maintenance |
| `MaintenanceController` | `PUT` | `/api/maintenances/{id}` | `maintenanceService.update(id, maintenance)` | 200 | 400, 404 | Modifie une maintenance |
| `MaintenanceController` | `DELETE` | `/api/maintenances/{id}` | `maintenanceService.deleteById(id)` | 204 | 404 | Supprime une maintenance |

---

### B. Annotations Jackson

#### 1. Tableau des associations traitées

| Association | Annotation retenue | Justification |
| :--- | :--- | :--- |
| `Contrat.paiements` | `@JsonManagedReference("contrat-paiements")` | Côté parent visible d'une véritable composition : les paiements font partie intégrante du contrat et doivent être sérialisés avec lui. |
| `Paiement.contrat` | `@JsonBackReference("contrat-paiements")` | Côté enfant masqué : rompt la boucle infinie de sérialisation (`StackOverflowError`) lors de la génération du JSON. |
| `Agence.employes` | `@JsonIgnore` | L'agence ne doit pas sérialiser la liste complète de ses employés pour éviter un payload lourd et des dépendances circulaires. |
| `Agence.vehicules` | `@JsonIgnore` | Évite de charger et sérialiser tout le parc de véhicules lors de la simple consultation d'une agence. |
| `Vehicule.maintenances` | `@JsonIgnore` | Rompt la boucle bidirectionnelle entre le véhicule et son historique de maintenance. |
| `Vehicule.reservations` | `@JsonIgnore` | Rompt la boucle bidirectionnelle entre le véhicule et ses réservations. |
| `Equipement.vehicules` | `@JsonIgnore` | Empêche la boucle `ManyToMany` lors de la consultation des équipements. |
| `Client.reservations` | `@JsonIgnore` | Rompt la boucle bidirectionnelle entre le client et ses réservations. |
| `Reservation.contrat` | `@JsonIgnore` | Rompt la boucle `OneToOne` bidirectionnelle entre la réservation et son contrat associé. |

#### 2. Différence entre `@JsonBackReference` et `@JsonIgnore`
- **`@JsonBackReference`** fait partie d'une paire ordonnée avec `@JsonManagedReference`. Elle indique à Jackson que la relation est bidirectionnelle parent-enfant : le champ est omis lors de la sérialisation de l'enfant, mais lors de la désérialisation du parent, Jackson reconstitue automatiquement le lien bidirectionnel (l'enfant reçoit bien la référence vers son parent).
- **`@JsonIgnore`** est une exclusion pure et simple : le champ est ignoré à la fois en sérialisation (écriture JSON) et en désérialisation (lecture JSON). Il convient parfaitement aux relations où l'on ne souhaite jamais exposer la collection inverse (ex. liste des véhicules d'une agence).

---

### C. Postman et Swagger

| Outil | Ce qu'il apporte | Exemple concret dans l'Atelier 5 |
| :--- | :--- | :--- |
| **Postman** | Client HTTP complet dédié aux **tests**, à l'automatisation et aux scénarios de test séquentiels (variables d'environnement, assertions `pm.test()`, enchaînement de requêtes). | Test du scénario de cascade : création d'un contrat avec ses paiements en `POST /api/contrats`, capture dynamique de l'identifiant `contratId` dans les variables d'environnement, puis vérification immédiate dans `GET /api/paiements`. |
| **Swagger UI (SpringDoc)** | Documentation interactive et contrat d'interface vivant (OpenAPI v3), synchronisé automatiquement avec le code Java à l'exécution. | Découverte interactive des endpoints sur `http://localhost:8081/api/swagger-ui.html`, visualisation des schémas JSON attendus et exécution ponctuelle de requêtes via le bouton *"Try it out"*. |

---

### D. Anomalies SonarQube for IDE

| Règle SonarLint | Explication | Correction apportée |
| :--- | :--- | :--- |
| **S6813**<br>*(Field injection should not be used)* | L'injection par attribut (`@Autowired private IContratService contratService;`) masque les dépendances obligatoires, empêche l'usage de champs `final` et complique les tests unitaires isolés sans contexte Spring. | Utilisation stricte de l'injection par constructeur avec des champs `private final` et génération du constructeur via `@RequiredArgsConstructor` de Lombok sur tous les contrôleurs REST. |
| **S1186 / Conventions REST**<br>*(Méthodes non documentées ou codes de retour inappropriés)* | Ne pas spécifier les codes de retour HTTP (ex. 201 pour la création, 204 pour la suppression) enfreint les conventions RESTful et dégrade l'interopérabilité de l'API. | Utilisation des annotations `@ResponseStatus(HttpStatus.CREATED)` pour les `POST`, `@ResponseStatus(HttpStatus.NO_CONTENT)` pour les `DELETE`, et enrichissement complet via `@Operation` et `@ApiResponses` de SpringDoc. |

---

### E. Questions de compréhension

#### 1. Quel composant reçoit toutes les requêtes, et comment retrouve-t-il la méthode du contrôleur à appeler ?
Toutes les requêtes HTTP entrantes sont réceptionnées par la **`DispatcherServlet`** (le *Front Controller* de Spring MVC). Pour identifier la méthode de contrôleur correspondante, elle interroge le composant **`HandlerMapping`** (notamment `RequestMappingHandlerMapping`), qui maintient une table de correspondance entre les routes/verbes HTTP (`@RequestMapping`, `@GetMapping`, etc.) et les méthodes Java associées.

#### 2. Pourquoi `ContratController` dépend-il de `IContratService` et non de `ContratRepository` ni de `ContratServiceImpl` ?
- **Non dépendance envers le Repository :** Le contrôleur appartient à la couche présentation/web et ne doit pas accéder directement aux données ; il doit déléguer les opérations à la couche métier (`Service`) qui valide les règles métier et gère la logique transactionnelle.
- **Dépendance envers l'interface `IContratService` plutôt que la classe concrète `ContratServiceImpl` :** Respect du principe d'inversion des dépendances (**DIP** de SOLID). Cela découple les couches, permet de substituer l'implémentation sans toucher au contrôleur et facilite grandement les tests unitaires avec des mocks (ex. Mockito).

#### 3. Pourquoi un `POST` renvoie-t-il 201 et un `DELETE` 204 ? Pourquoi une ressource absente donne-t-elle 404 et non 500 ?
- **POST → 201 Created :** Le verbe POST a pour finalité la création d'une nouvelle entité. Le code 201 confirme que la ressource a bien été créée et renvoie généralement son URI ou sa représentation avec son identifiant nouvellement attribué.
- **DELETE → 204 No Content :** La suppression s'est déroulée avec succès et la réponse n'a aucun contenu à retourner dans le corps du message.
- **404 Not Found vs 500 Internal Server Error :** Une ressource inexistante est une situation métier normale et prévisible côté client (erreur 4xx). Un code 500 signale un bogue interne ou une défaillance non maîtrisée du serveur, ce qui fausserait la supervision et masquerait la véritable cause de l'erreur.

#### 4. Pourquoi `PaiementController` n'expose-t-il que des `GET` ? Quelle réponse reçoit un `POST /api/paiements` ?
- **Lecture seule :** Dans les règles métier AutoLoc, un paiement n'a pas d'existence autonome : il est intrinsèquement lié à un contrat de location. Sa création et sa suppression sont portées par le contrat via la cascade JPA (`cascade = CascadeType.ALL`).
- **Réponse à un `POST /api/paiements` :** Comme aucun gestionnaire `@PostMapping` n'est déclaré sur le chemin `/paiements`, Spring MVC renvoie un code d'erreur **`405 Method Not Allowed`**.

#### 5. Quels risques présente l'exposition directe des entités JPA ? Pourquoi `Contrat.paiements` utilise-t-il `@JsonManagedReference` alors que `Agence.vehicules` utilise `@JsonIgnore` ?
- **Risques de l'exposition directe des entités JPA :**
  1. Couplage fort entre le schéma de base de données relationnelle et le contrat d'API publique (toute modification interne casse les clients).
  2. Risque de boucles de sérialisation infinies (`StackOverflowError`) sur les relations bidirectionnelles.
  3. Problèmes de proxies Hibernate LAZY non initialisés hors session (`LazyInitializationException` ou `ByteBuddyInterceptor` sérialisé).
  4. Risques de sécurité (sur-exposition de champs sensibles ou injection d'attributs non modifiables via *mass assignment*).
- **`Contrat.paiements` (@JsonManagedReference) vs `Agence.vehicules` (@JsonIgnore) :**
  - Pour un `Contrat`, les paiements sont essentiels pour connaître l'état financier du contrat lors de sa consultation : c'est une relation de composition forte, donc les paiements doivent figurer dans le JSON.
  - Pour une `Agence`, charger et sérialiser l'intégralité des véhicules du parc à chaque consultation d'agence produirait un volume de données inutilement volumineux et des requêtes N+1 lourdes, alors que l'on souhaite simplement les coordonnées de l'agence.
