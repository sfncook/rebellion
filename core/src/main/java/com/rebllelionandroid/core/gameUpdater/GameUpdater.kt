package com.rebllelionandroid.core.gameUpdater

import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.*
import com.rebllelionandroid.core.database.gamestate.enums.FactoryBuildTargetType
import com.rebllelionandroid.core.database.gamestate.enums.FactoryType
import com.rebllelionandroid.core.database.gamestate.enums.ShipType
import com.rebllelionandroid.core.database.gamestate.enums.UnitType
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty
import com.rebllelionandroid.core.gameUpdater.events.*
import com.rebllelionandroid.core.gameUpdater.uprising.UprisingEval
import com.rebllelionandroid.core.database.gamestate.Unit
import kotlin.random.Random

class GameUpdater {

    companion object {
        fun updateGameState(gameStateWithSectors: GameStateWithSectors): GameUpdateResponse {
            val updateEvents = mutableListOf<UpdateEvent>()
            val newFactories = mutableListOf<Factory>()
            val newShips = mutableListOf<Ship>()
            val newUnits = mutableListOf<Unit>()

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
                        applyDamage(teamsToShips[TeamLoyalty.TeamA]!!, teamsToShips[TeamLoyalty.TeamB]!!)
                        applyDamage(teamsToShips[TeamLoyalty.TeamB]!!, teamsToShips[TeamLoyalty.TeamA]!!)

                        // units on surface
                        planetWithUnits.units.forEach { unit ->

                        }
                    }
                }// planets
            }// sectors


            // *** Update Loyalties ***
            gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
                // planets
                sectorWithPlanets.planets.forEach { planetWithUnits ->
                    val planet = planetWithUnits.planet

                    val uprisingRank = UprisingEval.getUprisingRank(
                        planet.teamALoyalty,
                        planetWithUnits.units.size
                    )
                }// planets
            }// sectors


            updateEntityMovement(gameStateWithSectors, updateEvents)


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
                    planetWithUnits.units.forEach { unit ->

                    }
                }// planets
            }// sectors

            updateFactoryBuildOrders(gameStateWithSectors, updateEvents, newFactories, newShips, newUnits)

            return GameUpdateResponse(gameStateWithSectors, updateEvents, newFactories, newShips)
        }// updateGameState

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
            newUnits: MutableList<Unit>,
        ) {
            val gameTime = gameStateWithSectors.gameState.gameTime
            gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
                // planets
                sectorWithPlanets.planets.forEach { planetWithUnits ->
                    planetWithUnits.factories.forEach { factory ->
                        if(factory.buildTargetType != null && gameTime >= factory.dayBuildComplete!!) {
                            val dstPlanetWithUnits = Utilities.getPlanetWithId(gameStateWithSectors, factory.deliverBuiltStructureToPlanetId!!)
                            if(dstPlanetWithUnits!=null) {
                                when(factory.buildTargetType) {
                                    FactoryBuildTargetType.ConstructionYard_ConstructionYard ->
                                        createFactory(
                                            FactoryType.ConstructionYard,
                                            planetWithUnits,
                                            dstPlanetWithUnits,
                                            gameTime,
                                            newFactories,
                                            updateEvents
                                        )
                                    FactoryBuildTargetType.ConstructionYard_ShipYard ->
                                        createFactory(
                                            FactoryType.ShipYard,
                                            planetWithUnits,
                                            dstPlanetWithUnits,
                                            gameTime,
                                            newFactories,
                                            updateEvents
                                        )
                                    FactoryBuildTargetType.ConstructionYard_TrainingFacility ->
                                        createFactory(
                                            FactoryType.ShipYard,
                                            planetWithUnits,
                                            dstPlanetWithUnits,
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
            gameTime: Int,
            newFactories: MutableList<Factory>,
            updateEvents: MutableList<UpdateEvent>
        ) {
            val newFactory = Factory(
                id = Random.nextLong(),
                factoryType = factoryType,
                locationPlanetId = dstPlanetWithUnits.planet.id,
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
                newFactory.dayArrival = tripArrivalDay.toLong()
            }
            newFactories.add(newFactory)
            updateEvents.add(FactoryBuiltEvent(newFactory, dstPlanetWithUnits.planet))
        }

        private fun createShip(
            shipType: ShipType,
            srcPlanetWithUnits: PlanetWithUnits,
            dstPlanetWithUnits: PlanetWithUnits,
            teamLoyalty: TeamLoyalty,
            gameTime: Int,
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
                newShip.dayArrival = tripArrivalDay.toLong()
            }
            newShips.add(newShip)
            updateEvents.add(ShipBuiltEvent(newShip, dstPlanetWithUnits.planet))
        }

        private fun createUnit(
            unitType: UnitType,
            srcPlanetWithUnits: PlanetWithUnits,
            dstPlanetWithUnits: PlanetWithUnits,
            teamLoyalty: TeamLoyalty,
            gameTime: Int,
            newUnits: MutableList<Unit>,
            updateEvents: MutableList<UpdateEvent>
        ) {
            val newUnit = Unit(
                id = Random.nextLong(),
                unitType = UnitType.values().random(),
                team = teamLoyalty,
                created = true
            )
            val needsDelivery = srcPlanetWithUnits.planet.id != dstPlanetWithUnits.planet.id
            val tripArrivalDay = Utilities.getTravelArrivalDay(
                srcPlanetWithUnits.planet,
                dstPlanetWithUnits.planet,
                gameTime
            )
//            if(needsDelivery && tripArrivalDay > gameTime) {
//                newShip.isTraveling = true
//                newShip.dayArrival = tripArrivalDay.toLong()
//            }
            newUnits.add(newUnit)
            updateEvents.add(NewUnitTrainedEvent(newUnit, dstPlanetWithUnits.planet))
        }
    }// component
}
