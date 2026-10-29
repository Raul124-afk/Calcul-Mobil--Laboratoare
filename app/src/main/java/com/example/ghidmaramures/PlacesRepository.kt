package com.example.ghidmaramures

enum class Category(val label: String) {
    SPORT("Sport"), MUSEUMS("Muzee"), RESTAURANTS("Restaurante")
}

data class Place(
    val id: Int,
    val name: String,
    val category: Category,
    val city: String,
    val description: String
)

// Singura sursă pentru toate cele 9 obiective. Nu necesită internet sau bază de date.
object PlacesRepository {
    val places: List<Place> = listOf(
        Place(1, "Domeniul schiabil Cavnic", Category.SPORT, "Cavnic",
            "Zonă montană din Maramureș pentru sporturi de iarnă. În zona SuperSki există și activități de agrement în sezonul cald."),
        Place(2, "Telegondola și pârtia Borșa", Category.SPORT, "Borșa",
            "Domeniu pentru schi și snowboard, cu acces prin telegondolă și priveliști spre Munții Rodnei."),
        Place(3, "Parcul Național Munții Rodnei", Category.SPORT, "Borșa / Moisei",
            "Destinație pentru drumeții montane. Traseele se aleg în funcție de pregătire, anotimp și condițiile meteo."),
        Place(4, "Muzeul Satului Maramureșean", Category.MUSEUMS, "Sighetu Marmației",
            "Muzeu în aer liber care prezintă gospodării și arhitectură tradițională din Maramureș."),
        Place(5, "Memorialul Victimelor Comunismului și al Rezistenței", Category.MUSEUMS, "Sighetu Marmației",
            "Muzeu amenajat în fosta închisoare din Sighet, dedicat memoriei victimelor comunismului și rezistenței împotriva regimului."),
        Place(6, "Muzeul Județean de Istorie și Arheologie Maramureș", Category.MUSEUMS, "Baia Mare",
            "Muzeu cu colecții de istorie și arheologie care prezintă trecutul orașului și al regiunii."),
        Place(7, "Castel Transilvania", Category.RESTAURANTS, "Baia Mare",
            "Restaurant din complexul Castel Transilvania, cu preparate din bucătăria românească și internațională."),
        Place(8, "Casa Iurca de Călinești", Category.RESTAURANTS, "Sighetu Marmației",
            "Restaurant cu specific maramureșean, situat pe strada Dragoș Vodă."),
        Place(9, "Il Padrino", Category.RESTAURANTS, "Baia Mare",
            "Restaurant cu specific italian, situat pe strada Școlii. Meniul include paste și deserturi.")
    )

    fun findById(id: Int): Place? = places.find { it.id == id }
    fun filtered(category: Category?): List<Place> =
        places.filter { category == null || it.category == category }
}
