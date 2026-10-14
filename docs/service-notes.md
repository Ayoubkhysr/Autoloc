# Atelier 4 : Inversion de contrôle, injection de dépendances et couche Service
## Note de synthèse (`docs/service-notes.md`)

---

### A. Modes d'injection comparés

Tableau comparatif entre l'injection par constructeur et l'injection par attribut :

| Critère | Injection par Constructeur | Injection par Attribut (`@Autowired`) |
| :--- | :--- | :--- |
| **Champ `final` possible ?** | **Oui** (garantit l'immutabilité et l'assignation obligatoire à l'instanciation) | **Non** (les champs ne peuvent pas être `final` car Spring injecte par réflexion après instanciation) |
| **Dépendance visible dans la signature de la classe ?** | **Oui** (clairement explicitée dans les paramètres du constructeur) | **Non** (dépendances cachées à l'intérieur de la classe) |
| **Classe utilisable avec `new` hors conteneur sans dépendance `null` ?** | **Oui** (impossible d'instancier la classe sans fournir ses dépendances obligatoires) | **Non** (instancier avec `new` laisse les champs injectés à `null`, risquant des `NullPointerException`) |
| **Signalé par SonarQube for IDE ?** | **Non** (recommandé et conforme aux bonnes pratiques Clean Code) | **Oui** (règle **S6813** : *"Field dependency injection should not be used"*) |

#### Justification du choix AutoLoc
Dans le cadre de l'architecture AutoLoc, **l'injection par constructeur** (combinée avec l'annotation Lombok `@RequiredArgsConstructor` et des champs `final`) est systématiquement retenue car :
1. Elle garantit qu'aucun service ne peut exister dans un état incomplet ou invalide.
2. Elle facilite grandement l'écriture de tests unitaires isolés (instanciation directe avec des mocks Mockito sans nécessiter de conteneur Spring lourd).
3. Elle élimine les avertissements d'analyse statique de code (SonarLint S6813).

---

### B. Services

Tableau récapitulatif des neuf services de l'application AutoLoc :

| Service | Dépendances injectées | Mode d'injection | Justification |
| :--- | :--- | :--- | :--- |
| `ContratServiceImpl` | `IContratRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire et immuable vers le repository des contrats ; garantit la robustesse et la testabilité unitaire. |
| `AgenceServiceImpl` | `IAgenceRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance explicite sur l'abstraction pour l'accès aux données des agences ; sécurité à l'instanciation. |
| `EmployeServiceImpl` | `IEmployeRepository` | Constructeur (`@RequiredArgsConstructor`) | Accès aux employés sécurisé par champ final ; impossible d'instancier un service sans repository. |
| `VehiculeServiceImpl` | `IVehiculeRepository` | Constructeur (`@RequiredArgsConstructor`) | Centralise les règles métier sur les véhicules avec injection transparente du proxy Spring Data. |
| `EquipementServiceImpl` | `IEquipementRepository` | Constructeur (`@RequiredArgsConstructor`) | Couplage faible sur l'interface repository ; isolation parfaite pour les tests unitaires. |
| `ClientServiceImpl` | `IClientRepository` | Constructeur (`@RequiredArgsConstructor`) | Injection requise pour l'accès aux fiches clients sans dépendance cachée. |
| `ReservationServiceImpl`| `IReservationRepository`| Constructeur (`@RequiredArgsConstructor`) | Dépendance finale garantissant la cohérence du cycle de vie des réservations. |
| `PaiementServiceImpl` | `IPaiementRepository` | Constructeur (`@RequiredArgsConstructor`) | Service de consultation en lecture seule ; dépendance immuable injectée à la construction. |
| `MaintenanceServiceImpl`| `IMaintenanceRepository`| Constructeur (`@RequiredArgsConstructor`) | Respect de la convention d'architecture globale et absence d'état mutable. |

---

### C. Messages d'erreur du conteneur

#### Message 1
```text
APPLICATION FAILED TO START
Description:
Parameter 0 of constructor in tn.esprit.autoloc.web.controller.ContratController 
required a bean of type 'tn.esprit.autoloc.service.IContratService'
that could not be found.
Action:
Consider defining a bean of type 'tn.esprit.autoloc.service.IContratService' in your configuration.
```
* **Deux causes les plus probables côté `ContratServiceImpl` :**
  1. **Annotation manquante :** L'annotation `@Service` (ou `@Component`) a été oubliée sur la classe d'implémentation `ContratServiceImpl`. De ce fait, Spring ne l'enregistre pas comme bean lors du scan de composants.
  2. **Mauvais package :** La classe `ContratServiceImpl` est située dans un package non couvert par le scan de composants (en dehors de l'arborescence du package racine annoté `@SpringBootApplication`, par exemple `tn.esprit.autoloc` au lieu de `tn.esprit.tpautoloc`).
* **Correction :**
  - Ajouter l'annotation `@Service` sur `ContratServiceImpl`.
  - Vérifier que la classe se trouve bien sous le package scanné (ex. `tn.esprit.tpautoloc.service.impl`).

#### Message 2
```text
Description:
Parameter 0 of constructor in tn.esprit.autoloc.web.controller.NotificationController 
required a single bean, but 2 were found:
- emailNotificateur
- smsNotificateur
Action:
Consider marking one of the beans as @Primary, updating the consumer to accept multiple beans, or using @Qualifier to identify the bean that should be consumed
```
* **Deux propositions de correction :**
  1. **Option 1 (`@Primary`) :** Poser l'annotation `@Primary` sur la classe de l'implémentation par défaut souhaitée (ex. sur la classe `@Service @Primary public class EmailNotificateur implements Notificateur`). Le conteneur choisira ce bean en priorité s'il n'y a pas d'indication spécifique.
  2. **Option 2 (`@Qualifier`) :** Poser l'annotation `@Qualifier("emailNotificateur")` directement sur le paramètre du constructeur (ou au point d'injection) dans `NotificationController` pour lever explicitement l'ambiguïté vers le bean désiré.

#### Message 3
```text
The dependencies of some of the beans in the application context form a cycle:
clientServiceImpl
| ^
v |
reservationServiceImpl
```
* **Pourquoi l'injection par constructeur rend-elle ce cycle impossible à contourner ?**
  Lors d'une injection par constructeur, Spring doit instancier tous les arguments d'un constructeur *avant* de pouvoir appeler ce constructeur. Pour créer `ClientServiceImpl`, Spring a besoin d'une instance déjà existante de `ReservationServiceImpl`, et pour créer `ReservationServiceImpl`, il a besoin d'une instance déjà existante de `ClientServiceImpl`. Aucun des deux beans ne peut être créé en premier, ce qui bloque immédiatement le graphe d'instanciation (`BeanCurrentlyInCreationException`). (Contrairement à l'injection par champ ou par setter où Spring peut instancier des coquilles vides puis peupler les références mutuellement par réflexion après coup).
* **Piste de conception pour supprimer le cycle :**
  - **Refactoriser la responsabilité :** Extraire la logique ou les méthodes partagées dans un troisième service intermédiaire (ex. `ReservationClientFacade` ou `ReservationValidationService`) dont dépendront les deux services sans se référencer mutuellement.
  - **Événements applicatifs :** Remplacer l'appel direct synchrone par la publication d'événements Spring (`ApplicationEventPublisher` et `@EventListener`).

---

### D. Anomalies SonarQube for IDE

| Règle SonarLint | Explication | Correction apportée |
| :--- | :--- | :--- |
| **S6813**<br>*(Field dependency injection should not be used)* | L'injection de dépendances par attribut (`@Autowired private IRepository repo;`) masque les dépendances de la classe, empêche de déclarer les champs `final`, risque des `NullPointerException` hors du conteneur Spring et complique les tests unitaires. | Utilisation de l'injection par constructeur avec des champs déclarés `private final` et génération automatique du constructeur via `@RequiredArgsConstructor` de Lombok. |
| **S112**<br>*(Generic exceptions should never be thrown)* | Lever des exceptions génériques telles que `RuntimeException` ou `Exception` empêche les couches appelantes de distinguer précisément la nature de l'erreur et affaiblit la robustesse applicative. | Création et utilisation systématique d'une exception métier personnalisée explicite : `ResourceNotFoundException`. |
| **S2094 / Conventions JPA & Clean Code**<br>*(Pas de `@Data` sur les entités JPA)* | L'annotation `@Data` sur une entité JPA génère `equals()`, `hashCode()` et `toString()` sur tous les champs, risquant des boucles récursives (`StackOverflowError`) sur les relations bidirectionnelles et des ruptures de contrat de collections. | Utilisation ciblée des annotations Lombok (`@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`) sans `@Data`, et élimination des imports inutiles ou code mort. |

---

### E. Questions de compréhension

1. **Où est le `new` de `ContratServiceImpl` et qui l'exécute ?**
   Il n'y a aucun `new ContratServiceImpl(...)` écrit manuellement dans le code de l'application. C'est le conteneur Spring IoC (`ApplicationContext`) qui instancie la classe par réflexion au démarrage lors de la découverte du stéréotype `@Service`.

2. **Pourquoi un contrôleur dépendra-t-il de `IContratService` et non de `ContratServiceImpl` ?**
   Pour respecter le principe d'Inversion des Dépendances (DIP de SOLID) : la couche présentation dépend d'une abstraction (interface) et non d'une implémentation concrète. Cela permet de remplacer facilement l'implémentation (mocks de test, nouvelle logique métier, ajout d'aspects) sans modifier le code du contrôleur.

3. **Pourquoi un service singleton doit-il rester sans état ? Donner un exemple de bug.**
   Parce qu'une unique instance du bean singleton est partagée simultanément entre tous les threads des requêtes HTTP. Si le service conserve un état mutable (ex: un champ `private Contrat contratEnCours;`), la requête de l'utilisateur A modifiera cet attribut pendant que la requête de l'utilisateur B le lit, provoquant des corruptions de données croisées (*race condition* / faille critique de confidentialité).

4. **Pourquoi `update` charge-t-il l'entité existante avant d'appeler `save` au lieu de lui passer directement l'objet reçu ?**
   Passer directement l'objet reçu à `save` déclenche un `merge` qui écrase la totalité des colonnes en base par les valeurs de l'objet transmis. Si un champ n'a pas été renseigné dans la requête entrante, il serait écrasé par `null`. De plus, pour les collections gérées avec `orphanRemoval = true` (comme `paiements`), passer une liste vide ou nulle supprimerait automatiquement tous les paiements liés en base de données. Charger l'existant permet de ne recopier que les attributs modifiables autorisés tout en préservant l'intégrité des relations.

5. **Quelle différence entre `@Component` et `@Bean` ? Entre `@Primary` et `@Qualifier` ? Pourquoi `IPaiementService` n'expose-t-il ni création, ni modification, ni suppression ?**
   - **`@Component` vs `@Bean` :** `@Component` est une annotation de classe détectée automatiquement par le scan de composants du classpath. `@Bean` est une annotation de méthode (au sein d'une classe `@Configuration`) permettant d'instancier et configurer explicitement un bean, typiquement pour intégrer des classes tierces de bibliothèques dont on ne possède pas le code source.
   - **`@Primary` vs `@Qualifier` :** `@Primary` désigne sur une classe candidate l'implémentation prioritaire par défaut du bean en cas d'ambiguïté. `@Qualifier("nomDuBean")` est posé au point d'injection pour sélectionner explicitement un bean particulier par son nom.
   - **`IPaiementService` :** Un paiement n'a pas d'existence autonome en dehors du contrat auquel il est associé. Sa création, mise à jour ou suppression doivent obligatoirement être orchestrées par le contrat parent (`Contrat`) pour assurer la cohérence des montants et des règles financières du domaine (principe d'Agrégat DDD).
