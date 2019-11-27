package com.stepasha.ageofempirez.models

import java.io.Serializable

abstract class API(
    open var id: Int = 0,
    open val name: String? = null,
    open var category: String = ""
): Serializable {

    open fun info(): String {
        return "Name: $name"
    }

    open fun description(): String {
        return "Age of Empires"
    }
}
data class Civilization(
    var isFavorite: Boolean,
    var army_type: String,
    var unique_unit: List<String>,
    var unique_tech: List<String>,
    var team_bonus: String,
    var civilization_bonus: List<String>
): API() {

    override fun description(): String {


        return "Army Type: $army_type\nTeam Bonus: $team_bonus\n\nCivilization Bonus: " +
                "\nIs it a Favorite?:$isFavorite"
    }
}
data class Structure(
    var isFavorite: Boolean,
    var age: String,
    var build_time: Int,
    var hit_points: Int,
    var line_of_sight: Int,
    var armor: String,
    var special: List<String>
): API() {

    override fun description(): String {

        var fav = false


        return "Age: $age\n\nBuild Time: $build_time\n\nHit Points: " +
                "$hit_points\n\nLine of Sight: $line_of_sight\n\nArmor: " +
                "$armor\n\nSpecial:Is it a Favorite?: $isFavorite"
    }
}
data class Technology(
    var isFavorite: Boolean,
    var description: String,
    var age: String,
    var develops_in: String,
    var build_time: Int,
    var applies_to: List<String>
): API() {

    override fun description(): String {

        return "Description: $description\n\nAge: $age\n\nDevelops In: " +
                "$develops_in\n\nBuild Time: $build_time\n\nApplies To: " +
                "Is it a Favorite?: $isFavorite"
    }
}