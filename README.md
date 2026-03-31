# Airline Management

Projet Java console de gestion d'une compagnie aérienne, conforme au cahier des charges POO : gestion des avions, vols, passagers et réservations, avec recherche, tri, statistiques, historique et génération automatique des sièges.

## Structure du projet

```text
ING3-ProjetS2-CompagnieAerienne
├── docs
│   ├── rapport.md
│   └── uml.puml
├── pom.xml
├── src
│   ├── main
│   │   └── java
│   │       ├── app
│   │       │   └── Main.java
│   │       ├── model
│   │       │   ├── Avion.java
│   │       │   ├── Passager.java
│   │       │   ├── Reservation.java
│   │       │   ├── StatutReservation.java
│   │       │   └── Vol.java
│   │       ├── service
│   │       │   └── CompagnieAerienne.java
│   │       └── utils
│   │           └── MenuUtils.java
│   └── test
│       └── java
│           └── app
│               └── SmokeTest.java
```

## Fonctionnalités implémentées

- gestion complète des avions : ajout, modification, suppression, affichage, recherche
- gestion complète des vols : ajout, modification, suppression, affichage, recherche
- gestion complète des passagers : ajout, modification, suppression, affichage, recherche
- gestion complète des réservations : réservation, annulation, affichage des passagers d'un vol, vols d'un passager, historique
- protections métier : doublons d'identifiants, doublons de passeport, double réservation, vol complet, incohérences de capacité
- fonctionnalités avancées :
  - tri des vols par destination
  - tri des vols par date
  - tri des passagers par nom
  - recherche multicritère des vols par destination et date
  - calcul du taux de remplissage d'un vol
  - statistiques des vols les plus fréquentés
  - historique des réservations d'un passager
  - génération automatique des numéros de siège

## Exécution dans IntelliJ

1. Ouvrir le dossier du projet.
2. Vérifier qu'un JDK 17 est configuré.
3. Lancer la classe `app.Main`.

## Compilation et exécution en ligne de commande

### Compiler

```powershell
Set-Location "C:\Users\User\IdeaProjects\ING3-ProjetS2-CompagnieAerienne"
$sources = Get-ChildItem -Path "src\main\java" -Recurse -Filter *.java | Select-Object -ExpandProperty FullName
if (Test-Path "out") { Remove-Item -Recurse -Force "out" }
New-Item -ItemType Directory -Path "out" | Out-Null
javac -encoding UTF-8 -d "out" $sources
```

### Lancer l'application

```powershell
Set-Location "C:\Users\User\IdeaProjects\ING3-ProjetS2-CompagnieAerienne"
java -cp "out" app.Main
```

### Compiler et lancer le test de fumée

```powershell
Set-Location "C:\Users\User\IdeaProjects\ING3-ProjetS2-CompagnieAerienne"
$mainSources = Get-ChildItem -Path "src\main\java" -Recurse -Filter *.java | Select-Object -ExpandProperty FullName
$testSources = Get-ChildItem -Path "src\test\java" -Recurse -Filter *.java | Select-Object -ExpandProperty FullName
if (Test-Path "out") { Remove-Item -Recurse -Force "out" }
if (Test-Path "out-test") { Remove-Item -Recurse -Force "out-test" }
New-Item -ItemType Directory -Path "out" | Out-Null
New-Item -ItemType Directory -Path "out-test" | Out-Null
javac -encoding UTF-8 -d "out" $mainSources
javac -encoding UTF-8 -cp "out" -d "out-test" $testSources
java -cp "out;out-test" app.SmokeTest
```

## Remarques

- L'application charge automatiquement quelques données de démonstration au démarrage pour faciliter la présentation.
- Le stockage est réalisé en mémoire avec `ArrayList`, conformément au cahier des charges.
- Les règles métier sont centralisées dans `service/CompagnieAerienne.java`.

