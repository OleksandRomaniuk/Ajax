package com.example.ajaxproject.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

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
    val description: String? = null,

    @JsonProperty("isOnline")
    @Column(name = "isOnline", length = 100)
    val isOnline: Boolean = false,

    @Column(name = "is_offline_in")
    val isOfflineIn: Boolean = false,

    @JsonProperty("rating")
    @Column(name = "rating", length = 10)
    val rating: Double = 0.0,

    @OneToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "user_id")
    var user: User = User())