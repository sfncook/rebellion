package com.rebllelionandroid.core.gameUpdater

import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.*
import com.rebllelionandroid.core.database.gamestate.Personnel
import com.rebllelionandroid.core.database.gamestate.enums.*
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty
import com.rebllelionandroid.core.gameUpdater.events.*
import com.rebllelionandroid.core.gameUpdater.missionUpdaters.MissionUpdaterSabotage
import com.rebllelionandroid.core.gameUpdater.uprising.UprisingEval
import kotlin.random.Random

class GameUpdater {

    companion object {
        val missionUpdaterSabotage = MissionUpdaterSabotage()

        fun updateGameState(
            gameStateWithSectors: GameStateWithSectors,
            gameStateViewModel: GameStateViewModel
        ): GameUpdateResponse {
            val updateEvents = mutableListOf<UpdateEvent>()
            val newFactories = mutableListOf<Factory>()
            val newShips = mutableListOf<Ship>()
            val newUnits = mutableListOf<Personnel>()

            gameStateWithSectors.gameState.gameTime = gameStateWithSectors.gameState.gameTime.plus(1)
            val gameTime = gameStateWithSectors.gameState.gameTime

            // *** Update Conflict Results ***
            gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
                // planets
                sectorWithPlanets.planets.forEach { planetWithUnits ->
                    val planet = planetWithUnits.planet

                    if(planet.inConflict) {
                        // ships
                        val teamsToShips = Utilities.getTeamsToShipsForList(planetWithUnits.shipsWithUnits)
                        val teamAShips = teamsToShips[TeamLoyalty.TeamA]
                        val teamBShips = teamsToShips[TeamLoyalty.TeamB]
                        if(teamAShips!=null && teamBShips!=null) {
                            applyDamage(teamAShips, teamBShips)
                            applyDamage(teamBShips, teamAShips)
                        }

                        // units on surface
                        planetWithUnits.personnels.forEach { unit ->

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

                    val uprisingRank = UprisingEval.getUprisingRank(
                        planet.teamALoyalty,
                        planetWithUnits.personnels.size
                    )
                }// planets
            }// sectors

            updatePersonnelMissions(gameStateWithSectors, gameStateViewModel, updateEvents)

            // *** Check for conflict flags ***
            gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
                // planets
                sectorWithPlanets.planets.forEach { planetWithUnits ->
                    val planet = planetWithUnits.planet

                    // ships
                    val teamsToShips = Utilities.getTeamsToShipsForList(planetWithUnits.shipsWithUnits)
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

            updateFactoryBuildOrders(gameStateWithSectors, updateEvents, newFactories, newShips, newUnits)

            return GameUpdateResponse(gameStateWithSectors, updateEvents, newFactories, newShips, newUnits)
        }// updateGameState

        private fun updatePersonnelMissions(
            gameStateWithSectors: GameStateWithSectors,
            gameStateViewModel: GameStateViewModel,
            updateEvents: MutableList<UpdateEvent>
        ) {
            val gameTime = gameStateWithSectors.gameState.gameTime
            gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
                // planets
                sectorWithPlanets.planets.forEach { planetWithUnits ->
                    planetWithUnits.personnels.forEach { personnel ->
                        if(personnel.missionType!=null && personnel.dayMissionComplete!! <= gameTime) {
                            when(personnel.missionType) {
                                MissionType.Sabotage -> missionUpdaterSabotage.update(
                                    gameStateWithSectors,
                                    updateEvents,
                                    planetWithUnits,
                                    personnel
                                )
                                else -> {}
                            }
                        }
                    }
                }// planets
            }// sectors
        }

        private fun applyDamage(offensiveShips: List<ShipWithUnits>, defensiveShips: List<ShipWithUnits>) {
            offensiveShips.forEach { shipWithUnits ->
                val ofShip = shipWithUnits.ship
                if(Random.nextBoolean()) {
                    val defShipWithUnits = defensiveShips.random()
                    val defShip = defShipWithUnits.ship
                    val attackPoints = Random.nextInt(1, ofShip.attackStrength)
                    defShip.healthPoints = defShip.healthPoints - attackPoints
                    println("Ship takes damage: ${defShip.team} damage:${attackPoints} healthPoints:${defShip.healthPoints}")
                    if(defShip.healthPoints<=0) defShip.destroyed = true
                    defShip.updated = true
                }
            }
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
        ) {
            val gameTime = gameStateWithSectors.gameState.gameTime
            gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
                // planets
                sectorWithPlanets.planets.forEach { planetWithUnits ->
                    planetWithUnits.factories.forEach { factory ->
                        if(factory.buildTargetType != null && gameTime >= factory.dayBuildComplete!!) {
                            val dstPlanetWithUnits = Utilities.findPlanetWithId(gameStateWithSectors, factory.deliverBuiltStructureToPlanetId!!)
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
    }// component
}
