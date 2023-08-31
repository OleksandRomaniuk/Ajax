package com.example.ajaxproject.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*


@Entity
@Table(name = "mentorToCategories")
data class MentorToCategories(
    @Id
    @JsonProperty("id")
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Int = 0,

    @JsonProperty("price")
    @Column(name = "price", length = 5)
    val price: Double = 0.0,


    @JsonProperty("rating")
    @Column(name = "rating", length = 3)
    val rating: Double = 0.0,

    @ManyToOne
    @JoinColumn(name = "categories_id")
    var course: Categories = Categories(),

    @ManyToOne
@JoinColumn(name = "mentor_id")
var mentors: Mentors = Mentors()
)