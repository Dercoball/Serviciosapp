package com.example.serviciosapp.data.sample

import com.example.serviciosapp.data.model.Review
import com.example.serviciosapp.data.model.ServiceProvider

object SampleData {
    val categories = listOf("Pintor", "Plomero", "Electricista", "Albañil")

    val providers = listOf(
        ServiceProvider(
            id = "p1",
            name = "Juan Perez",
            trade = "Pintor",
            rating = 4.8,
            city = "CDMX",
            description = "Especialista en acabados finos, interiores y fachadas modernas.",
            category = "Pintor",
            latitude = 19.4326,
            longitude = -99.1332,
            badges = listOf("Verificado", "Top"),
            portfolioSwatches = listOf(0xFF0EA5E9, 0xFF38BDF8, 0xFF1E3A8A),
            reviews = listOf(
                Review("Laura M.", 5.0, "Trabajo impecable y muy puntual.", "hace 2 dias"),
                Review("Carlos T.", 4.8, "Muy buen acabado, limpio y ordenado.", "hace 1 semana")
            )
        ),
        ServiceProvider(
            id = "p2",
            name = "Lucia Gomez",
            trade = "Plomera",
            rating = 4.6,
            city = "CDMX",
            description = "Urgencias 24/7, reparacion de fugas y mantenimiento preventivo.",
            category = "Plomero",
            latitude = 19.4390,
            longitude = -99.1250,
            badges = listOf("Verificado"),
            portfolioSwatches = listOf(0xFF10B981, 0xFF34D399, 0xFF047857),
            reviews = listOf(
                Review("Marco A.", 4.5, "Llego rapido y resolvio la fuga.", "hace 3 dias"),
                Review("Karen V.", 4.7, "Excelente trato y precio justo.", "hace 2 semanas")
            )
        ),
        ServiceProvider(
            id = "p3",
            name = "Carlos Ruiz",
            trade = "Electricista",
            rating = 4.9,
            city = "CDMX",
            description = "Instalaciones residenciales seguras y domotica basica.",
            category = "Electricista",
            latitude = 19.4270,
            longitude = -99.1415,
            badges = listOf("Top"),
            portfolioSwatches = listOf(0xFFF59E0B, 0xFFFBBF24, 0xFFB45309),
            reviews = listOf(
                Review("Adriana P.", 5.0, "Explico todo y dejo todo seguro.", "hace 5 dias"),
                Review("Luis R.", 4.9, "Muy profesional, recomiendo.", "hace 1 mes")
            )
        ),
        ServiceProvider(
            id = "p4",
            name = "Ana Torres",
            trade = "Pintora",
            rating = 4.7,
            city = "CDMX",
            description = "Paletas de color personalizadas y texturizados creativos.",
            category = "Pintor",
            latitude = 19.4215,
            longitude = -99.1300,
            badges = listOf("Verificado"),
            portfolioSwatches = listOf(0xFFEC4899, 0xFFDB2777, 0xFFBE185D),
            reviews = listOf(
                Review("Fernanda S.", 4.6, "Buen gusto y asesoramiento.", "hace 4 dias"),
                Review("Jorge C.", 4.8, "Acabados finos, volveria a contratar.", "hace 3 semanas")
            )
        ),
        ServiceProvider(
            id = "p5",
            name = "Miguel Lopez",
            trade = "Plomero",
            rating = 4.5,
            city = "CDMX",
            description = "Instalacion de calentadores, bombas y revision de presion.",
            category = "Plomero",
            latitude = 19.4455,
            longitude = -99.1400,
            badges = listOf("Top"),
            portfolioSwatches = listOf(0xFF8B5CF6, 0xFF7C3AED, 0xFF4C1D95),
            reviews = listOf(
                Review("Sofia L.", 4.5, "Resolvió rápido sin sorpresas.", "hace 6 dias"),
                Review("Diego H.", 4.4, "Precio razonable y atento.", "hace 2 meses")
            )
        )
    )
}
