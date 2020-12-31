package com.rebellionandroid.components.commands.enums

enum class OrderDlgArgumentKeys(val value: String) {
    // Dialog Input Parameters
    ComponentsToShow("ComponentsToShow"),
    OrderProcedure("OrderProcedure"),

    // Order Procedure Parameters
    MoveShipId("MoveShipId"),
    FactoryId("FactoryId"),

    // Output Keys
    BuildTargetType("BuildTargetType"),
    SelectedPlanetId("SelectedPlanetId"),
}