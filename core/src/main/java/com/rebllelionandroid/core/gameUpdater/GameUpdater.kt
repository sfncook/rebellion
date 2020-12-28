package com.rebllelionandroid.core.gameUpdater

import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.*
import com.rebllelionandroid.core.database.gamestate.enums.FactoryBuildTargetType
import com.rebllelionandroid.core.database.gamestate.enums.FactoryType
import com.rebllelionandroid.core.database.gamestate.enums.ShipType
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty
import com.rebllelionandroid.core.gameUpdater.events.*
import com.rebllelionandroid.core.gameUpdater.uprising.UprisingEval
import kotlin.random.Random

class GameUpdater {

    companion object {
        fun updateGameState(gameStateWithSectors: GameStateWithSectors): GameUpdateResponse {
            val updateEvents = mutableListOf<UpdateEvent>()
            val newFactories = mutableListOf<Factory>()
            val newShips = mutableListOf<Ship>()

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

            updateFactoryBuildOrders(gameStateWithSectors, updateEvents, newFactories, newShips)

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
                        if(ship.isTraveling && gameTime >= ship.dayArrival ) {
                            ship.isTraveling = false
                            ship.dayArrival = 0
                            ship.updated = true
                            updateEvents.add(ShipArrivalEvent(ship, planet))
                        }
                    }

                    planetWithUnits.factories.forEach { factory ->
                        if(factory.isTravelling && gameTime >= factory.dayArrival ) {
                            factory.isTravelling = false
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
            newShips: MutableList<Ship>
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
            val newFactory: Factory
            val needsDelivery = srcPlanetWithUnits.planet.id != dstPlanetWithUnits.planet.id
            val tripArrivalDay = Utilities.getTravelArrivalDay(
                srcPlanetWithUnits.planet,
                dstPlanetWithUnits.planet,
                gameTime
            )
            if(needsDelivery && tripArrivalDay > gameTime) {
                newFactory = Factory(
                    id = Random.nextLong(),
                    factoryType = factoryType,
                    locationPlanetId = dstPlanetWithUnits.planet.id,
                    isTravelling = true,
                    dayArrival = tripArrivalDay.toLong(),
                    created = true
                )
            } else {
                newFactory = Factory(
                    id = Random.nextLong(),
                    factoryType = factoryType,
                    locationPlanetId = dstPlanetWithUnits.planet.id,
                    isTravelling = false,
                    created = true
                )
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
            val newShip: Ship
            val needsDelivery = srcPlanetWithUnits.planet.id != dstPlanetWithUnits.planet.id
            val tripArrivalDay = Utilities.getTravelArrivalDay(
                srcPlanetWithUnits.planet,
                dstPlanetWithUnits.planet,
                gameTime
            )
            if(needsDelivery && tripArrivalDay > gameTime) {
                val tripArrivalDay = Utilities.getTravelArrivalDay(
                    srcPlanetWithUnits.planet,
                    dstPlanetWithUnits.planet,
                    gameTime
                )
//                Ship(
//                    @PrimaryKey var id: Long = 0,
//                    var shipType: ShipType = ShipType.Bireme,
//                    var team: TeamLoyalty = TeamLoyalty.Neutral,
//                    var attackStrength: Int = 0,
//                    var defenseStrength: Int = 0,
//
//                    @ColumnInfo(name = "planet_id", index = true) var locationPlanetId: Long = 0,
//                    var isTraveling: Boolean = false,
//                    var dayArrival: Long = 0,
//                    var destroyed: Boolean = false,
//                    var healthPoints: Int = 0,
//
//                    @Ignore var updated: Boolean = false,
//                    @Ignore var created: Boolean = false
//                )
                newShip = Ship(
                    id = Random.nextLong(),
                    locationPlanetId = dstPlanetWithUnits.planet.id,
                    shipType = shipType,
                    isTraveling = false,
                    dayArrival = 0,
                    team = teamLoyalty,
                    attackStrength = shipType.attackStrength.toInt(),
                    defenseStrength = shipType.defenseStrength.toInt(),
                    destroyed = false,
                    healthPoints = shipType.defenseStrength.toInt()
                )
            } else {
                newShip = Ship(
                    id = Random.nextLong(),
                    shipType = shipType,
                    locationPlanetId = dstPlanetWithUnits.planet.id,
                    team = teamLoyalty,
                    isTraveling = false,
                    created = true
                )
            }
            newShips.add(newShip)
            updateEvents.add(ShipBuiltEvent(newShip, dstPlanetWithUnits.planet))
        }
    }// component
}
