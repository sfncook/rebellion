package com.rebllelionandroid.core.database.gamestate.enums

enum class FactoryBuildTargetType(val value: String) {
    ConstructionYard_ConstructionYard("ConstructionYard_ConstructionYard"),
    ConstructionYard_ShipYard("ConstructionYard_ShipYard"),
    ConstructionYard_TrainingFacility("ConstructionYard_TrainingFacility"),
    ConstructionYard_OrbitalBattery("ConstructionYard_OrbitalBattery"),
    ConstructionYard_PlanetaryShield("ConstructionYard_PlanetaryShield"),

    ShipYard_Bireme("ShipYard_Bireme"),
    ShipYard_Trireme("ShipYard_Trireme"),
    ShipYard_Quadrireme("ShipYard_Quadrireme"),
    ShipYard_Quinquereme("ShipYard_Quinquereme"),
    ShipYard_Hexareme("ShipYard_Hexareme"),
    ShipYard_Septireme("ShipYard_Septireme"),
    ShipYard_Octere("ShipYard_Octere"),

    TrainingFac_Garrison("TrainingFac_Garrison"),
    TrainingFac_SpecOps("TrainingFac_SpecOps")
}