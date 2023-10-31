package com.example.ajaxproject

import com.example.ajaxproject.MessageDestinations.REQUEST_PREFIX

object NatsSubject {
    object UserRequest {
        private const val USER_PREFIX = "$REQUEST_PREFIX.user"

        const val ADD_USER = "$USER_PREFIX.add_user"
        const val GET_USER_BY_ID = "$USER_PREFIX.get_user_by_id"
        const val GET_ALL_USERS = "$USER_PREFIX.get_all_users"
        const val DELETE_USER_BY_ID = "$USER_PREFIX.delete_user_by_id"
    }
}
