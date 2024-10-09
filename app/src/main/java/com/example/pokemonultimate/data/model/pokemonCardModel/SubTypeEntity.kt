package com.example.pokemonultimate.data.model.pokemonCardModel

import androidx.room.Entity
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Entity
@Serializable
enum class SubTypeEntity {
    @SerialName("ACE SPEC")
    ACE_SPEC,

    @SerialName("Ancient")
    ANCIENT,

    BREAK,

    @SerialName("Baby")
    BABY,

    @SerialName("Basic")
    BASIC,

    @SerialName("Eternamax")
    ETERNAMAX,

    @SerialName("Fusion Strike")
    FUSION_STRIKE,

    @SerialName("Future")
    FUTURE,

    @SerialName("GX")
    GX,

    @SerialName("Goldenrod Game Corner")
    GOLDENROD_GAME_CORNER,

    @SerialName("Item")
    ITEM,

    @SerialName("LEGEND")
    LEGEND,

    @SerialName("Level-Up")
    LEVEL_UP,

    @SerialName("MEGA")
    MEGA,

    @SerialName("Pokémon Tool")
    POKEMON_TOOL,

    @SerialName("Pokémon Tool F")
    POKEMON_TOOL_F,

    @SerialName("Prime")
    PRIME,

    @SerialName("Prism Star")
    PRISM_STAR,

    @SerialName("Radiant")
    RADIANT,

    @SerialName("Rapid Strike")
    RAPID_STRIKE,

    @SerialName("Restored")
    RESTORED,

    @SerialName("Rocket's Secret Machine")
    ROCKETS_SECRET_MACHINE,

    @SerialName("SP")
    SP,

    @SerialName("Single Strike")
    SINGLE_STRIKE,

    @SerialName("Special")
    SPECIAL,

    @SerialName("Stadium")
    STADIUM,

    @SerialName("Stage 1")
    STAGE_1,

    @SerialName("Stage 2")
    STAGE_2,

    @SerialName("Star")
    STAR,

    @SerialName("Supporter")
    SUPPORTER,

    @SerialName("TAG TEAM")
    TAG_TEAM,

    @SerialName("Team Plasma")
    TEAM_PLASMA,

    @SerialName("Technical Machine")
    TECHNICAL_MACHINE,

    @SerialName("Tera")
    TERA,

    @SerialName("Ultra Beast")
    ULTRA_BEAST,

    @SerialName("V")
    V,

    @SerialName("V-UNION")
    V_UNION,

    @SerialName("VMAX")
    VMAX,

    @SerialName("VSTAR")
    VSTAR,

    @OptIn(ExperimentalSerializationApi::class)
    @JsonNames("ex", "EX")
    EX
}
