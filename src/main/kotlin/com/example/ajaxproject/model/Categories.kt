package com.example.ajaxproject.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.util.*


@Entity
@Table(name = "categories")
class Categories(
    @Id
    @JsonProperty("id")
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Long = 0,


    @JsonProperty("name")
    @Column(name = "name", length = 10)
    var name: String? = null,


)