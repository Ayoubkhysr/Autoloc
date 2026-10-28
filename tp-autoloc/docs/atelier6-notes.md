# Atelier 6 : Patrons de conception GOF et principes SOLID
## Note d'analyse et de synthèse (`docs/atelier6-notes.md`)

---

### 1. Partie 1 : Analyse de AutoLoc v0

#### Question 1 : Combien de responsabilités possède la classe `AutoLocManager` ?
La classe `AutoLocManager` possède **5 responsabilités distinctes** :
1. **Calcul tarifaire métier :** calcul du montant en fonction du type de réservation (Weekend, LongueDuree, Standard) et de la durée.
2. **Gestion du contrat :** création et mise en forme logique du contrat.
3. **Paiement :** enregistrement et validation financière (`enregistrerPaiement()`).
4. **Notification client multicanal :** formatage et envoi des messages (`envoyerEmail()`, `envoyerSMS()`).
5. **Persistance des données :** sauvegarde du contrat en base de données (`sauvegarderContrat()`).

#### Question 2 : Quel principe SOLID n'est pas respecté ?
Le principe violé est le **Single Responsibility Principle (SRP)** :
- *"Une classe ne doit posséder qu'une seule et unique raison de changer."*
- `AutoLocManager` agit comme un **God Object** (ou classe fourre-tout). Une modification de la grille tarifaire, un changement de passerelle de paiement, une refonte du template d'email ou un changement de base de données obligent tous à modifier cette même classe.

#### Question 3 : Que se passe-t-il si AutoLoc ajoute un nouveau type de réservation ? (Exemple : Pleinesaison)
- **Conséquence :** Il faut obligatoirement modifier le code source de la méthode `louerVehicule()` en y insérant une nouvelle clause `else if(typeReservation.equals("Pleinesaison")) { ... }`.
- **Risques :** Régression sur les cas de calcul existants, allongement excessif de la méthode, complexité cyclomatique accrue et re-tests nécessaires sur tout le flux.
- **Principe SOLID violé :** L'**Open/Closed Principle (OCP)** :
  - *"Une classe doit être ouverte à l'extension, mais fermée à la modification."*
- **Violation supplémentaire (DIP) :** Le code dépend d'implémentations directes plutôt que d'abstractions, violant le **Dependency Inversion Principle (DIP)**.

---

### 2. Partie 2 : Refactoring avec le patron Strategy (GOF)

#### Solution mise en place :
1. **Abstraction :** Définition de l'interface `ITarificationStrategy` :
   - `BigDecimal calculerTarif(Vehicule vehicule, Reservation reservation);`
   - `String getCode();`
2. **Stratégies concrètes :**
   - `TarifStandardStrategy` : tarif journalier × nombre de jours.
   - `TarifWeekendStrategy` : remise de 10% (coefficient 0.90).
   - `TarifLongueDureeStrategy` : remise de 20% (coefficient 0.80).
3. **Bénéfices :**
   - Chaque algorithme de calcul est parfaitement isolé et testable unitairement (respect du SRP).
   - Pour ajouter une tarification *"Pleinesaison"*, il suffit de créer une nouvelle classe `TarifPleineSaisonStrategy` implémentant `ITarificationStrategy` **sans modifier une seule ligne de code existante** (respect total de l'OCP).

---

### 3. Partie 3 : Patron Factory (GOF)

#### 1. `TarificationStrategyFactory`
- **Rôle :** Centralise la logique de sélection de la bonne stratégie de calcul selon les caractéristiques de la réservation (durée ≥ 7 jours → Longue durée, séjour sur le week-end → Weekend, sinon Standard).
- **Injection Spring dynamique :** Toutes les stratégies implémentant `ITarificationStrategy` sont injectées par Spring dans une liste, puis indexées par leur `code` dans une `Map<String, ITarificationStrategy>`.

#### 2. `NotificationFactory`
- **Rôle :** Factory Method fournissant l'observateur de notification approprié (`EmailNotificationObserver` ou `SmsNotificationObserver`) à partir d'un canal passé en paramètre (`"EMAIL"`, `"SMS"`), sans exposer les classes concrètes au code appelant.

---

### 4. Partie 4 : Patron Observer (GOF)

#### Architecture mise en place :
- **Interface Observateur (`IReservationObserver`) :** Définit les méthodes `onReservationConfirmee(Reservation)` et `onReservationAnnulee(Reservation)`.
- **Sujet Observable (`ReservationSubject`) :** Maintient la liste des observateurs enregistrés et diffuse les notifications lors des événements du cycle de vie d'une réservation.
- **Observateurs concrets :**
  - `EmailNotificationObserver` : trace l'envoi d'emails via logs SLF4J (`[EMAIL] Confirmation envoyee...`).
  - `SmsNotificationObserver` : trace l'envoi de SMS via logs SLF4J (`[SMS] Confirmation envoyee...`).

#### Bénéfices pour AutoLoc :
- Le service métier `ReservationServiceImpl` ne se soucie plus du détail technique des canaux de notification.
- L'ajout d'un nouveau canal (ex. `PushNotificationObserver` ou `SlackNotificationObserver`) se fait par simple ajout d'un bean implémentant `IReservationObserver`, automatiquement détecté et injecté par Spring sans toucher au service métier.

---

### 5. Synthèse des patrons appliqués

| Patron GOF | Rôle dans AutoLoc | Principe SOLID servi |
| :--- | :--- | :--- |
| **Strategy** | Calcul interchangeable du tarif (Standard, Weekend, Longue durée). | **OCP** (extensibilité des tarifs), **SRP** (isolation du calcul). |
| **Factory Method** | Instanciation et résolution dynamique des stratégies et des observateurs. | **DIP** (découplage du client vis-à-vis des classes concrètes). |
| **Observer** | Déclenchement automatique des notifications métier lors du cycle de vie des réservations. | **SRP** (découplage notification / métier), **OCP** (nouveaux canaux sans modification). |
