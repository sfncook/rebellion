package com.rebellionandroid.gamePlay.gameUpdater

import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.*
import com.rebllelionandroid.core.database.gamestate.enums.*
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty
import com.rebllelionandroid.core.gameUpdater.GameUpdateResponse
import com.rebllelionandroid.core.gameUpdater.Updater
import com.rebllelionandroid.core.gameUpdater.ai.AiUpdater
import com.rebllelionandroid.core.gameUpdater.events.*
import com.rebllelionandroid.core.gameUpdater.missionUpdaters.MissionUpdaterDiplomacy
import com.rebllelionandroid.core.gameUpdater.missionUpdaters.MissionUpdaterInsurrection
import com.rebllelionandroid.core.gameUpdater.missionUpdaters.MissionUpdaterIntelligence
import com.rebllelionandroid.core.gameUpdater.missionUpdaters.MissionUpdaterSabotage
import kotlin.random.Random

class GameUpdater: Updater {

    private val UPRISING_INC_THRESHOLD_PERC = 5
    private val missionUpdaterSabotage = MissionUpdaterSabotage()
    private val missionUpdaterInsurrection = MissionUpdaterInsurrection()
    private val missionUpdaterIntelligence = MissionUpdaterIntelligence()
    private val missionUpdaterDiplomacy = MissionUpdaterDiplomacy()
    private val aiUpdater = AiUpdater()

    override fun updateGameState(gameStateWithSectors: GameStateWithSectors): GameUpdateResponse {
        val updateEvents = mutableListOf<UpdateEvent>()
        val newFactories = mutableListOf<Factory>()
        val newShips = mutableListOf<Ship>()
        val newUnits = mutableListOf<Personnel>()
        val newStructures = mutableListOf<DefenseStructure>()

        gameStateWithSectors.gameState.gameTime = gameStateWithSectors.gameState.gameTime.plus(1)

        aiUpdater.update(gameStateWithSectors)

        // *** Update Conflict Results ***
        gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
            // planets
            sectorWithPlanets.planets.forEach { planetWithUnits ->
                val planet = planetWithUnits.planet

                if(planet.inConflict) {
                    // ships
                    val teamsToShips =
                        Utilities.getTeamsToShipsForList(planetWithUnits.shipsWithUnits)
                    val teamAShips = teamsToShips[TeamLoyalty.TeamA]
                    val teamBShips = teamsToShips[TeamLoyalty.TeamB]

                    val teamsToPersonnels =
                        Utilities.getTeamsToPersonnelOnPlanetSurface(planetWithUnits)
                    val teamAPersonnels = teamsToPersonnels[TeamLoyalty.TeamA] ?: listOf()
                    val teamBPersonnels = teamsToPersonnels[TeamLoyalty.TeamB] ?: listOf()

                    val planetTeamLoyalty = Utilities.getPlanetLoyalty(planet)
                    val teamADefStructures = if(planetTeamLoyalty== TeamLoyalty.TeamA) planetWithUnits.defenseStructures else listOf()
                    val teamBDefStructures = if(planetTeamLoyalty== TeamLoyalty.TeamB) planetWithUnits.defenseStructures else listOf()

                    val teamAOffensiveStructures =
                        if(planetTeamLoyalty== TeamLoyalty.TeamA)
                            planetWithUnits.defenseStructures.filter { it.defenseStructureType== DefenseStructureType.OrbitalBattery }
                        else listOf()
                    val teamBOffensiveStructures =
                        if(planetTeamLoyalty== TeamLoyalty.TeamB)
                            planetWithUnits.defenseStructures.filter { it.defenseStructureType== DefenseStructureType.OrbitalBattery }
                        else listOf()

                    if(teamAShips!=null && teamBShips!=null) {
                        applyDamage(
                            offensiveShips = teamAShips,
                            offensiveStructures = teamAOffensiveStructures,
                            defensiveShips = teamBShips,
                            defensiveStructures = teamBDefStructures,
                            defensivePersonnels = teamBPersonnels
                        )
                        applyDamage(
                            offensiveShips = teamBShips,
                            offensiveStructures = teamBOffensiveStructures,
                            defensiveShips = teamAShips,
                            defensiveStructures = teamADefStructures,
                            defensivePersonnels = teamAPersonnels
                        )
                    }
                }
            }// planets
        }// sectors


        updateEntityMovement(gameStateWithSectors, updateEvents)


        // *** Update Loyalties ***
        gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
            // planets
            sectorWithPlanets.planets.forEach { planetWithUnits ->
                val planet = planetWithUnits.planet
                val planetTeamLoyalty = Utilities.getPlanetLoyalty(planet)
                val manyUnitsAffectedByUprisings = (
                    planetWithUnits.defenseStructures.size +
                        planetWithUnits.factories.size +
                        planetWithUnits.personnels.size
                    )
                if(manyUnitsAffectedByUprisings>0) {
                    val dominantTeamLoyalty: Int =
                        if(planetTeamLoyalty== TeamLoyalty.TeamA) {
                            planet.teamALoyalty
                        } else {
                            planet.teamBLoyalty
                        }
                    val increaseUprisingRank = dominantTeamLoyalty<=25 && UPRISING_INC_THRESHOLD_PERC >= Random.nextInt(
                        0,
                        99
                    )
                    val decreaseUprisingRank = dominantTeamLoyalty>=75 && UPRISING_INC_THRESHOLD_PERC >= Random.nextInt(
                        0,
                        99
                    )
                    if(increaseUprisingRank) {
                        planet.uprisingRank =
                            Utilities.increaseUprisingRank(planet.uprisingRank)
                        planet.updated = true
                    }
                    if(decreaseUprisingRank) {
                        planet.uprisingRank =
                            Utilities.decreaseUprisingRank(planet.uprisingRank)
                        planet.updated = true
                    }
                }
            }// planets
        }// sectors

        updatePersonnelMissions(gameStateWithSectors, updateEvents)

        // *** Check for conflict flags ***
        gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
            // planets
            sectorWithPlanets.planets.forEach { planetWithUnits ->
                val planet = planetWithUnits.planet

                // ships
                val teamsToShips =
                    Utilities.getTeamsToShipsForList(planetWithUnits.shipsWithUnits)
                if(
                    teamsToShips[TeamLoyalty.TeamA]?.any { !it.ship.isTraveling && !it.ship.destroyed } == true &&
                    teamsToShips[TeamLoyalty.TeamB]?.any { !it.ship.isTraveling && !it.ship.destroyed } == true
                ) {
                    planet.inConflict = true
                    planet.updated = true
                    if(!planet.inConflict) {
                        updateEvents.add(PlanetConflictStartsEvent(planet))
                    } else {
                        updateEvents.add(PlanetConflictContinuesEvent(planet))
                    }
                } else {
                    planet.inConflict = false
                    planet.updated = true
                }

                // units on surface
                planetWithUnits.personnels.forEach { unit ->

                }
            }// planets
        }// sectors


        // Update Health Points when planet not in conflict
        gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
            sectorWithPlanets.planets.forEach { planetWithUnits ->
                val planet = planetWithUnits.planet
                if(!planet.inConflict) {
                    planetWithUnits.shipsWithUnits.forEach { shipWithUnits ->
                        val ship = shipWithUnits.ship
                        if(ship.healthPoints != ship.maxHealthPoints) {
                            ship.healthPoints += 1
                            ship.healthPoints.coerceAtMost(ship.maxHealthPoints)
                            ship.updated = true
                        }
                    }

                    planetWithUnits.defenseStructures.forEach { structure ->
                        if(structure.healthPoints != structure.maxHealthPoints) {
                            structure.healthPoints += 1
                            structure.healthPoints.coerceAtMost(structure.maxHealthPoints)
                            structure.updated = true
                        }
                    }
                }
            }
        }

        updateFactoryBuildOrders(gameStateWithSectors, updateEvents, newFactories, newShips, newUnits, newStructures)

        return GameUpdateResponse(
            gameStateWithSectors,
            updateEvents,
            newFactories,
            newShips,
            newUnits,
            newStructures
        )
    }// updateGameState

    private fun updatePersonnelMissions(
        gameStateWithSectors: GameStateWithSectors,
        updateEvents: MutableList<UpdateEvent>
    ) {
        gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
            // planets
            sectorWithPlanets.planets.forEach { planetWithUnits ->
                planetWithUnits.personnels.forEach { personnel ->
                    when(personnel.missionType) {
                        MissionType.Sabotage -> missionUpdaterSabotage.update(
                            gameStateWithSectors, updateEvents, planetWithUnits, personnel
                        )
                        MissionType.Insurrection -> missionUpdaterInsurrection.update(
                            gameStateWithSectors, updateEvents, planetWithUnits, personnel
                        )
                        MissionType.Diplomacy -> missionUpdaterDiplomacy.update(
                            gameStateWithSectors, updateEvents, planetWithUnits, personnel
                        )
                        MissionType.Intelligence -> missionUpdaterIntelligence.update(
                            gameStateWithSectors, updateEvents, planetWithUnits, personnel
                        )
                        else -> {}// null mission, do nothing
                    }
                }
            }// planets
        }// sectors
    }

    private fun applyDamage(
        offensiveShips: List<ShipWithUnits>,
        offensiveStructures: List<DefenseStructure>,
        defensiveShips: List<ShipWithUnits>,
        defensiveStructures: List<DefenseStructure>,
        defensivePersonnels: List<Personnel>
    ) {
        val planetHasShield = defensiveStructures.any { it.defenseStructureType== DefenseStructureType.PlanetaryShield }
        val potentialTargets = mutableListOf<Any?>()
        potentialTargets.addAll(defensiveShips)
        if(!planetHasShield) {
            potentialTargets.addAll(defensiveStructures)
            potentialTargets.addAll(defensivePersonnels)
        }


        offensiveShips.forEach { shipWithUnits ->
            val ofShip = shipWithUnits.ship
            if(Random.nextBoolean()) {
                val target = potentialTargets.random()
                when(target!!::class) {
                    ShipWithUnits::class -> applyDamageToEntity(target as ShipWithUnits, ofShip.attackStrength)
                    DefenseStructure::class -> applyDamageToEntity(target as DefenseStructure, ofShip.attackStrength)
                }
            }
        }

        offensiveStructures.forEach { structure ->
            if(Random.nextBoolean()) {
                applyDamageToEntity(defensiveShips.random(), structure.attackStrength)
            }
        }
    }

    private fun applyDamageToEntity(shipWithUnits: ShipWithUnits, attackStrength: Int) {
        val defShip = shipWithUnits.ship
        val attackPoints = Random.nextInt(1, attackStrength)
        defShip.healthPoints = defShip.healthPoints - attackPoints
        println("Ship takes damage: ${defShip.team} damage:${attackPoints} healthPoints:${defShip.healthPoints}")
        if (defShip.healthPoints <= 0) defShip.destroyed = true else defShip.updated = true
    }

    private fun applyDamageToEntity(structure: DefenseStructure, attackStrength: Int) {
        val attackPoints = Random.nextInt(1, attackStrength)
        structure.healthPoints = structure.healthPoints - attackPoints
        println("Structure takes damage:${attackPoints} healthPoints:${structure.healthPoints}")
        if (structure.healthPoints <= 0) structure.destroyed = true else structure.updated = true
    }

    private fun updateEntityMovement(gameStateWithSectors: GameStateWithSectors, updateEvents: MutableList<UpdateEvent>) {
        val gameTime = gameStateWithSectors.gameState.gameTime
        gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
            // planets
            sectorWithPlanets.planets.forEach { planetWithUnits ->
                val planet = planetWithUnits.planet

                planetWithUnits.shipsWithUnits.forEach { shipWithUnits ->
                    val ship = shipWithUnits.ship
                    if(ship.isTraveling && gameTime >= ship.dayArrival!!) {
                        ship.isTraveling = false
                        ship.dayArrival = 0
                        ship.updated = true
                        updateEvents.add(ShipArrivalEvent(ship, planet))
                    }
                }

                planetWithUnits.factories.forEach { factory ->
                    if(factory.isTraveling && gameTime >= factory.dayArrival ) {
                        factory.isTraveling = false
                        factory.dayArrival = 0
                        factory.updated = true
                        updateEvents.add(FactoryArrivalEvent(factory, planetWithUnits.planet))
                    }
                }
            }// planets
        }// sectors
    }

    private fun updateFactoryBuildOrders(
        gameStateWithSectors: GameStateWithSectors,
        updateEvents: MutableList<UpdateEvent>,
        newFactories: MutableList<Factory>,
        newShips: MutableList<Ship>,
        newPersonnels: MutableList<Personnel>,
        newStructures: MutableList<DefenseStructure>,
    ) {
        val gameTime = gameStateWithSectors.gameState.gameTime
        gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
            // planets
            sectorWithPlanets.planets.forEach { planetWithUnits ->
                planetWithUnits.factories.forEach { factory ->
                    if(factory.buildTargetType != null && gameTime >= factory.dayBuildComplete!!) {
                        val dstPlanetWithUnits = Utilities.findPlanetWithId(
                            gameStateWithSectors,
                            factory.deliverBuiltStructureToPlanetId!!
                        )
                        if(dstPlanetWithUnits!=null) {
                            when(factory.buildTargetType) {
                                FactoryBuildTargetType.ConstructionYard_ConstructionYard ->
                                    createFactory(
                                        FactoryType.ConstructionYard,
                                        planetWithUnits,
                                        dstPlanetWithUnits,
                                        factory.team,
                                        gameTime,
                                        newFactories,
                                        updateEvents
                                    )
                                FactoryBuildTargetType.ConstructionYard_ShipYard ->
                                    createFactory(
                                        FactoryType.ShipYard,
                                        planetWithUnits,
                                        dstPlanetWithUnits,
                                        factory.team,
                                        gameTime,
                                        newFactories,
                                        updateEvents
                                    )
                                FactoryBuildTargetType.ConstructionYard_TrainingFacility ->
                                    createFactory(
                                        FactoryType.TrainingFaciliy,
                                        planetWithUnits,
                                        dstPlanetWithUnits,
                                        factory.team,
                                        gameTime,
                                        newFactories,
                                        updateEvents
                                    )
                                FactoryBuildTargetType.ConstructionYard_OrbitalBattery ->
                                    createStructure(
                                        DefenseStructureType.OrbitalBattery,
                                        planetWithUnits,
                                        dstPlanetWithUnits,
                                        gameTime,
                                        newStructures,
                                        updateEvents
                                    )
                                FactoryBuildTargetType.ConstructionYard_PlanetaryShield ->
                                    createStructure(
                                        DefenseStructureType.PlanetaryShield,
                                        planetWithUnits,
                                        dstPlanetWithUnits,
                                        gameTime,
                                        newStructures,
                                        updateEvents
                                    )

                                FactoryBuildTargetType.ShipYard_Bireme ->
                                    createShip(
                                        ShipType.Bireme,
                                        planetWithUnits,
                                        dstPlanetWithUnits,
                                        factory.team,
                                        gameTime,
                                        newShips,
                                        updateEvents
                                    )
                                FactoryBuildTargetType.ShipYard_Trireme ->
                                    createShip(
                                        ShipType.Trireme,
                                        planetWithUnits,
                                        dstPlanetWithUnits,
                                        factory.team,
                                        gameTime,
                                        newShips,
                                        updateEvents
                                    )
                                FactoryBuildTargetType.ShipYard_Quadrireme ->
                                    createShip(
                                        ShipType.Quadrireme,
                                        planetWithUnits,
                                        dstPlanetWithUnits,
                                        factory.team,
                                        gameTime,
                                        newShips,
                                        updateEvents
                                    )
                                FactoryBuildTargetType.ShipYard_Quinquereme ->
                                    createShip(
                                        ShipType.Quinquereme,
                                        planetWithUnits,
                                        dstPlanetWithUnits,
                                        factory.team,
                                        gameTime,
                                        newShips,
                                        updateEvents
                                    )
                                FactoryBuildTargetType.ShipYard_Hexareme ->
                                    createShip(
                                        ShipType.Hexareme,
                                        planetWithUnits,
                                        dstPlanetWithUnits,
                                        factory.team,
                                        gameTime,
                                        newShips,
                                        updateEvents
                                    )
                                FactoryBuildTargetType.ShipYard_Septireme ->
                                    createShip(
                                        ShipType.Septireme,
                                        planetWithUnits,
                                        dstPlanetWithUnits,
                                        factory.team,
                                        gameTime,
                                        newShips,
                                        updateEvents
                                    )
                                FactoryBuildTargetType.ShipYard_Octere ->
                                    createShip(
                                        ShipType.Octere,
                                        planetWithUnits,
                                        dstPlanetWithUnits,
                                        factory.team,
                                        gameTime,
                                        newShips,
                                        updateEvents
                                    )

                                FactoryBuildTargetType.TrainingFac_Garrison ->
                                    createUnit(
                                        UnitType.Garrison,
                                        planetWithUnits,
                                        dstPlanetWithUnits,
                                        factory.team,
                                        gameTime,
                                        newPersonnels,
                                        updateEvents
                                    )
                                FactoryBuildTargetType.TrainingFac_SpecOps ->
                                    createUnit(
                                        UnitType.SpecialForces,
                                        planetWithUnits,
                                        dstPlanetWithUnits,
                                        factory.team,
                                        gameTime,
                                        newPersonnels,
                                        updateEvents
                                    )

                                else -> println("updateFactoryBuildOrders Warning: Unhandled buildTargetType:${factory.buildTargetType}")
                            }
                        } else {
                            println("updateFactoryBuildOrders ERROR! Could not find a planet with id: ${factory.deliverBuiltStructureToPlanetId}")
                        }
                        factory.buildTargetType = null
                        factory.dayBuildComplete = null
                        factory.deliverBuiltStructureToPlanetId = null
                        factory.updated = true
                    }
                }
            }// planets
        }// sectors
    }

    private fun createFactory(
        factoryType: FactoryType,
        srcPlanetWithUnits: PlanetWithUnits,
        dstPlanetWithUnits: PlanetWithUnits,
        teamLoyalty: TeamLoyalty,
        gameTime: Long,
        newFactories: MutableList<Factory>,
        updateEvents: MutableList<UpdateEvent>
    ) {
        val newFactory = Factory(
            id = Random.nextLong(),
            factoryType = factoryType,
            locationPlanetId = dstPlanetWithUnits.planet.id,
            team = teamLoyalty,
            created = true
        )
        val needsDelivery = srcPlanetWithUnits.planet.id != dstPlanetWithUnits.planet.id
        val tripArrivalDay = Utilities.getTravelArrivalDay(
            srcPlanetWithUnits.planet,
            dstPlanetWithUnits.planet,
            gameTime
        )
        if(needsDelivery && tripArrivalDay > gameTime) {
            newFactory.isTraveling = true
            newFactory.dayArrival = tripArrivalDay
        }
        newFactories.add(newFactory)
        updateEvents.add(FactoryBuiltEvent(newFactory, dstPlanetWithUnits.planet))
    }

    private fun createStructure(
        structureType: DefenseStructureType,
        srcPlanetWithUnits: PlanetWithUnits,
        dstPlanetWithUnits: PlanetWithUnits,
        gameTime: Long,
        newStructures: MutableList<DefenseStructure>,
        updateEvents: MutableList<UpdateEvent>
    ) {
        val newStructure = DefenseStructure(
            id = Random.nextLong(),
            defenseStructureType = structureType,
            locationPlanetId = dstPlanetWithUnits.planet.id,
            created = true
        )
        Utilities.setStructureStrengthValues(newStructure)

        val needsDelivery = srcPlanetWithUnits.planet.id != dstPlanetWithUnits.planet.id
        val tripArrivalDay = Utilities.getTravelArrivalDay(
            srcPlanetWithUnits.planet,
            dstPlanetWithUnits.planet,
            gameTime
        )
        if(needsDelivery && tripArrivalDay > gameTime) {
            newStructure.isTraveling = true
            newStructure.dayArrival = tripArrivalDay
        }
        newStructures.add(newStructure)
        updateEvents.add(StructureBuiltEvent(newStructure, dstPlanetWithUnits.planet))
    }

    private fun createShip(
        shipType: ShipType,
        srcPlanetWithUnits: PlanetWithUnits,
        dstPlanetWithUnits: PlanetWithUnits,
        teamLoyalty: TeamLoyalty,
        gameTime: Long,
        newShips: MutableList<Ship>,
        updateEvents: MutableList<UpdateEvent>
    ) {
        val newShip = Ship(
            id = Random.nextLong(),
            locationPlanetId = dstPlanetWithUnits.planet.id,
            shipType = shipType,
            team = teamLoyalty,
            created = true
        )
        Utilities.setShipStrengthValues(newShip)
        val needsDelivery = srcPlanetWithUnits.planet.id != dstPlanetWithUnits.planet.id
        val tripArrivalDay = Utilities.getTravelArrivalDay(
            srcPlanetWithUnits.planet,
            dstPlanetWithUnits.planet,
            gameTime
        )
        if(needsDelivery && tripArrivalDay > gameTime) {
            newShip.isTraveling = true
            newShip.dayArrival = tripArrivalDay
        }
        newShips.add(newShip)
        updateEvents.add(ShipBuiltEvent(newShip, dstPlanetWithUnits.planet))
    }

    private fun createUnit(
        unitType: UnitType,
        srcPlanetWithUnits: PlanetWithUnits,
        dstPlanetWithUnits: PlanetWithUnits,
        teamLoyalty: TeamLoyalty,
        gameTime: Long,
        newPersonnels: MutableList<Personnel>,
        updateEvents: MutableList<UpdateEvent>
    ) {
        val newUnit = Personnel(
            id = Random.nextLong(),
            locationPlanetId = dstPlanetWithUnits.planet.id,
            unitType = unitType,
            team = teamLoyalty,
            created = true
        )
        Utilities.setUnitStrengthValues(newUnit)
        val needsDelivery = srcPlanetWithUnits.planet.id != dstPlanetWithUnits.planet.id
        val tripArrivalDay = Utilities.getTravelArrivalDay(
            srcPlanetWithUnits.planet,
            dstPlanetWithUnits.planet,
            gameTime
        )
        if(needsDelivery && tripArrivalDay > gameTime) {
            newUnit.isTraveling = true
            newUnit.dayArrival = tripArrivalDay
        } else {

        }
        newPersonnels.add(newUnit)
        updateEvents.add(NewUnitTrainedEvent(newUnit, dstPlanetWithUnits.planet))
    }
}