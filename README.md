
# Kata Delivery

## Description

Kata Delivery est une application destinée à la gestion des créneaux de livraison. 
Elle permet aux clients de choisir un mode de livraison et de réserver des créneaux horaires en fonction de règles métier précises. 
Le système est construit autour d’une architecture hexagonale (Ports & Adapters), garantissant modularité, testabilité et évolutivité.
## Sommaire
- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
- [Documentation](#documentation)
- [Business Rules](#business-rules)
- [Tests](#tests)
- [Choix Techniques](#choix-techniques)
- [Docker Compose](#docker-compose)


## Requirements
- Java 21
- Maven
- Git
- Docker & Docker Compose

## Installation
1. Cloner le repository sur gitlab:
```   
git clone https://github.com/rtamskint/delivery-slot-service.git
```   
2. Vérifier que Docker est bien installé.
3. lLancer les services avec Docker Compose.
## Usage

### Démarrer l'application :

mvn spring-boot:run

### Accéder a l'API

**http://localhost:8080/api/delivery**

### Récupérer les créneaux horaires disponibles :
  
**GET /time-slots?mode=DeliveyMode**

Paramètres :
mode peut être : DRIVE, DELIVERY, DELIVERY_TODAY, DELIVERY_ASAP

Exemple: Pour obtenir les créneaux disponibles pour la méthode de livraison DRIVE, la requête serait :

**GET /time-slots?mode=DRIVE**

Réponse : La réponse contiendra une liste de créneaux horaires disponibles au format JSON. Voici un exemple de réponse :
```   
[
  {
    "id": 228,
    "deliveryMode": "DRIVE",
    "startTime": "2025-06-20T17:15:00",
    "endTime": "2025-06-20T17:30:00"
  },
  {
    "id": 229,
    "deliveryMode": "DRIVE",
    "startTime": "2025-06-20T17:30:00",
    "endTime": "2025-06-20T17:45:00"
  },
  {
    "id": 230,
    "deliveryMode": "DRIVE",
    "startTime": "2025-06-20T17:45:00",
    "endTime": "2025-06-20T18:00:00"
  }
  ]
```   

### Réserver un créneau :

**POST /time-slots?/{slotId}?customerId=customerId**

Paramètres :
- customerId : doit prendre la valeur de customerId :

Exemple: Pour réserver un time slot avec "id= 228" pour un customerId "c70", la requete serait :

**POST /time-slots?/228?customerId=c70**

Réponse :
```   
{
  "id": 228,
  "deliveryMode": "DRIVE",
  "startTime": "2025-06-20T17:15:00",
  "endTime": "2025-06-20T17:30:00"
}
```   

### Supprimer une réservation :

**DELETE /time-slots?/{slotId}?customerId=customerId**

Paramètres
- customerId : doit prendre la valeur de customerId :

Exemple: Pour supprimer une réservation avec "id= 228" pour une customerId "c70", la requete serait :

**DELETE /time-slots?/228?customerId=c70**

Réponse :
```
{
  "id": 228,
  "deliveryMode": "DRIVE",
  "startTime": "2025-06-20T17:15:00",
  "endTime": "2025-06-20T17:30:00"
}
```   

## Documentation

### swagger:

Pour y accéder :

**http://localhost:8080/api-docs**

## Business Rules
Les créneaux sont générés dynamiquement selon les règles suivantes :

- DRIVE

    - 07:00 à 22:00

    - Créneau de 15 min

    - Préparation : +180 min

    - Réservation : de J à J+2

- DELIVERY

    - 09:00 à 18:00

    - Créneau de 30 min

    - Réservation à partir de J+1

- DELIVERY_TODAY

    - 09:00 à 18:00

    - Créneau de 30 min

    - Préparation : +120 min

    - Réservation pour le jour même

- DELIVERY_ASAP

    - 09:00 à 18:00

    - Créneau de 30 min

    - Début dans 2h à partir de l’heure actuelle

    - Réservation pour le jour même   
## Tests

Ce projet contient :

    - des tests unitaires (logique métier)

    - des tests d’intégration (WebTestClient)
### Exécuter les tests :

Pour lancer les tests, utilisez l'une des méthodes suivantes :

mvn test

## Choix Techniques

### Architecture :

Architecture hexagonale

Ce projet applique le modèle hexagonal (aussi appelé Ports and Adapters), structurant le code autour du domaine métier. Cela offre :

une forte isolation entre la logique métier et l'infrastructure

une testabilité renforcée

une évolutivité facilitée (remplacement facile de la base de données, du transport, etc.)

### Technologies utilisées :

- **Backend** : Spring Boot 3.x.x
- **Base de données** : MySQL avec R2DBC pour les opérations non-bloquantes
- **Tests** : JUnit, WebTestClient pour les tests d'intégration WebFlux
- **API REST** : HATEOAS avec Spring WebFlux pour les appels asynchrones
## Docker Compose

Ce projet inclut un fichier docker-compose.yml pour lancer rapidement les services nécessaires.
### Prérequis :

- **Docker** : Assurez-vous que Docker est installé sur votre machine.
- **Docker Compose** : Vérifiez que Docker Compose est également installé.

### Fichier `docker-compose.yml` :

Voici le contenu de notre fichier `docker-compose.yml` :


```
version: "3.7"

services:
  mysqldb:
    image: "mysql:8.0"
    restart: always
    ports:
      - 3306:3306
    networks:
      - kata-net
    environment:
      MYSQL_DATABASE: delivery_db
      MYSQL_USER: kata_user
      MYSQL_PASSWORD: kata_password
      MYSQL_ROOT_PASSWORD: kata_password
    volumes:
      - ./init_db.sql:/docker-entrypoint-initdb.d/init_db.sql
      
networks:
  kata-net:
```

### Démarrer les services :

En pré-requis, il faut générer le package avec : `mvn package`

Pour lancer les services, exécutez la commande suivante dans le répertoire où se trouve le fichier docker-compose.yml :

```
docker compose up --build
```
