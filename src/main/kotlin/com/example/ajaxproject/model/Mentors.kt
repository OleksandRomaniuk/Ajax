package com.example.ajaxproject.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "mentors")
class Mentors(

    @Id
    @JsonProperty("id")
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int = 0,

    @JsonProperty("description")
    @Column(name = "description", length = 100)
    var description: String? = null,

    @JsonProperty("isOnline")
    @Column(name = "isOnline", length = 100)
    var isOnline: Boolean = false,

    @Column(name = "is_offline_in")
    var isOfflineIn: Boolean = false,

    @JsonProperty("rating")
    @Column(name = "rating", length = 10)
    var rating: Double = 0.0,

    @OneToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "user_id")
    var user: User = User())

