package com.rebllelionandroid.core.database.gamestate.enums

enum class FactoryBuildTargetType(val value: String) {
    ConstructionYard_ConstructionYard("Construction Yard"),
    ConstructionYard_ShipYard("Ship Yard"),
    ConstructionYard_TrainingFacility("Training Facility"),
    ConstructionYard_OrbitalBattery("Orbital Battery"),
    ConstructionYard_PlanetaryShield("Planetary Shield"),

    ShipYard_Bireme("Bireme"),
    ShipYard_Trireme("Trireme"),
    ShipYard_Quadrireme("Quadrireme"),
    ShipYard_Quinquereme("Quinquereme"),
    ShipYard_Hexareme("Hexareme"),
    ShipYard_Septireme("Septireme"),
    ShipYard_Octere("Octere"),

    TrainingFac_Garrison("Garrison"),
    TrainingFac_SpecOps("Special Ops")
}