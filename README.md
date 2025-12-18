# Hotel Reservation System

Un système de réservation d'hôtel simplifié développé en Java pour gérer les chambres, les utilisateurs et les réservations.

## 📋 Description

Ce projet implémente un système de gestion de réservations d'hôtel permettant de :
- Créer et gérer des chambres avec différents types et prix
- Créer et gérer des utilisateurs avec un solde
- Effectuer des réservations de chambres pour des périodes spécifiques
- Vérifier la disponibilité des chambres
- Conserver l'historique des réservations avec les informations au moment de la réservation

## 🏗️ Architecture du Projet

```
src/main/java/dev/lka/
├── Main.java                    # Point d'entrée avec les cas de test
├── Service.java                 # Service principal avec toute la logique métier
├── model/
│   ├── User.java               # Entité Utilisateur
│   ├── Room.java               # Entité Chambre
│   ├── RoomType.java           # Enum des types de chambres
│   └── Booking.java            # Entité Réservation
├── exception/
│   └── CustomException.java    # Exception personnalisée
└── util/
    └── CustomSimpleDateFormat.java  # Utilitaire pour le formatage des dates
```

## 🎯 Entités

### User (Utilisateur)
- **id** : Identifiant unique de l'utilisateur
- **balance** : Solde disponible de l'utilisateur

### Room (Chambre)
- **id** : Numéro de la chambre
- **type** : Type de chambre (standard, junior, master)
- **pricePerNight** : Prix par nuit

### RoomType (Type de Chambre)
- `standard` : Suite standard
- `junior` : Suite junior
- `master` : Suite master

### Booking (Réservation)
- **id** : Identifiant unique de la réservation
- **user** : Utilisateur qui a effectué la réservation
- **room** : Chambre réservée (clonée pour préserver l'historique)
- **dateCheckIn** : Date d'arrivée
- **dateCheckOut** : Date de départ

## 🔧 Fonctionnalités

### Service

La classe `Service` contient toutes les fonctionnalités principales :

#### `setRoom(int roomNumber, RoomType roomType, int roomPricePerNight)`
- Crée une nouvelle chambre si elle n'existe pas
- Met à jour une chambre existante (type et prix)
- **Important** : Les modifications n'affectent pas les réservations précédentes (grâce au clonage)

#### `setUser(int userId, int balance)`
- Crée un nouvel utilisateur si il n'existe pas
- Met à jour le solde d'un utilisateur existant

#### `bookRoom(int userId, int roomNumber, Date checkIn, Date checkOut)`
- Réserve une chambre pour un utilisateur sur une période donnée
- Vérifie que :
  - L'utilisateur existe
  - La chambre existe
  - L'utilisateur a suffisamment de solde pour la période complète
  - La chambre est disponible pour la période demandée
  - Les dates sont valides (checkIn < checkOut)
- Déduit automatiquement le coût total du solde de l'utilisateur
- Le coût total = prix par nuit × nombre de nuits

#### `printAll()`
- Affiche toutes les chambres (du plus récent au plus ancien)
- Affiche toutes les réservations (du plus récent au plus ancien)
- Pour chaque réservation, affiche toutes les informations de la chambre et de l'utilisateur au moment de la réservation

#### `printAllUsers()`
- Affiche tous les utilisateurs (du plus récent au plus ancien)

## 🚀 Compilation et Exécution

### Prérequis
- Java 17 ou supérieur
- Maven 3.6 ou supérieur

### Compilation
```bash
mvn compile
```

### Exécution
```bash
mvn exec:java -Dexec.mainClass="dev.lka.Main"
```

Ou après compilation :
```bash
java -cp target/classes dev.lka.Main
```

## 📝 Cas de Test

Le fichier `Main.java` contient les cas de test suivants :

1. **Création de 3 chambres** :
   - Chambre 1 : standard, 1000/nuit
   - Chambre 2 : junior, 2000/nuit
   - Chambre 3 : master, 3000/nuit

2. **Création de 2 utilisateurs** :
   - Utilisateur 1 : ID=1, Solde=5000
   - Utilisateur 2 : ID=2, Solde=10000

3. **Tentatives de réservation** :
   - User 1 → Room 2 : 30/06/2026 au 07/07/2026 (7 nuits) ✅
   - User 1 → Room 2 : 07/07/2026 au 30/06/2026 ❌ (dates invalides)
   - User 1 → Room 1 : 07/07/2026 au 08/07/2026 (1 nuit) ✅
   - User 2 → Room 1 : 07/07/2026 au 09/07/2026 (2 nuits) ❌ (chevauchement)
   - User 2 → Room 3 : 07/07/2026 au 08/07/2026 (1 nuit) ✅

4. **Modification d'une chambre** :
   - `setRoom(1, master, 10000)` : Change Room 1 en master suite avec nouveau prix

5. **Affichage final** :
   - `printAll()` : Affiche toutes les chambres et réservations
   - `printAllUsers()` : Affiche tous les utilisateurs avec leurs soldes mis à jour

## ⚙️ Exigences Techniques Implémentées

✅ **Gestion des entités** : User, Room, Booking créées
✅ **Types de chambres** : standard, junior, master
✅ **Réservation conditionnelle** : Vérification du solde et de la disponibilité
✅ **Préservation de l'historique** : `setRoom()` n'affecte pas les réservations précédentes (clonage)
✅ **Création automatique** : `setRoom()` et `setUser()` créent si n'existe pas
✅ **Affichage ordonné** : Du plus récent au plus ancien
✅ **Pas de repositories** : Utilisation directe d'ArrayLists
✅ **Normalisation des dates** : Seulement année/mois/jour (heure à 00:00:00)
✅ **Gestion des exceptions** : Exceptions personnalisées pour les erreurs
✅ **Calcul du coût total** : Prix par nuit × nombre de nuits
✅ **Détection des chevauchements** : Vérification correcte des périodes qui se chevauchent

## 🛡️ Gestion des Exceptions

Le système lève des `CustomException` dans les cas suivants :
- Chambre n'existe pas
- Utilisateur n'existe pas
- Dates invalides (checkIn >= checkOut)
- Solde insuffisant
- Chambre non disponible (chevauchement de dates)

## 📊 Exemple de Sortie

```
*********** Users list **********
- User: 2; Balance: 7000
- User: 1; Balance: 2000

********** Rooms list **********
- Id: 1; Type: master; Price per night: 10000
- Id: 3; Type: master; Price per night: 3000
- Id: 2; Type: junior; Price per night: 2000

=======================================
********** Bookings list **********
- Booking id: 3; User: 2; User Balance: 10000; Reserved room: 3; Reserved room type: master; Reserved room price per night: 3000; Check in: 07/07/2026; Check out: 08/07/2026
- Booking id: 2; User: 1; User Balance: 5000; Reserved room: 1; Reserved room type: standard; Reserved room price per night: 1000; Check in: 07/07/2026; Check out: 08/07/2026
- Booking id: 1; User: 1; User Balance: 5000; Reserved room: 2; Reserved room type: junior; Reserved room price per night: 2000; Check in: 30/06/2026; Check out: 07/07/2026
```

## 🎨 Design Decisions

### Clonage des Chambres
Lors de la création d'une réservation, la chambre est clonée pour préserver les informations (type et prix) au moment de la réservation. Cela garantit que les modifications ultérieures via `setRoom()` n'affectent pas l'historique des réservations.

### Normalisation des Dates
Les dates sont normalisées à 00:00:00 pour ne considérer que l'année, le mois et le jour, conformément aux exigences.

### Détection des Chevauchements
La vérification des chevauchements utilise la formule :
```
checkIn < existingCheckOut && checkOut > existingCheckIn
```
Cette formule garantit une détection correcte de tous les cas de chevauchement.

## 📄 Licence

Ce projet a été développé dans le cadre d'un test technique.

## 👤 Auteur

Développé pour Skypay Technical Test 2
