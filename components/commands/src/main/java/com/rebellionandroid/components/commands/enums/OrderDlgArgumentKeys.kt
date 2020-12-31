package com.rebellionandroid.components.commands.enums

enum class OrderDlgArgumentKeys(val value: String) {
    // Dialog Input Parameters
    DialogTitleText("DlgTitleText"),
    PositiveBtnText("PositiveBtnText"),
    NegativeBtnText("NegativeBtnText"),
    ComponentsToShow("ComponentsToShow"),
    OrderProcedure("OrderProcedure"),

    // Order Procedure Parameters
    MoveShipId("MoveShipId"),

    // Output Keys
    SelectedCtorYardBuildType("SelectedCtorYardBuildType"),
    SelectedPlanetId("SelectedPlanetId"),
}