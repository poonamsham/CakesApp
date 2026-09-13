package com.example.cakes.util

/**
 * Global constants used throughout the application.
 */
class CakeConstants {

     companion object {
         /**
          * Base URL for the remote API.
          */
         const val BASE_URL = "https://raw.githubusercontent.com/"

         /**
          * Endpoint path for fetching the cakes JSON data.
          */
         const val ENDPOINT = "Waracle/mobile-coding-test-api/refs/heads/main/cakes"

         /**
          * The application name as displayed in the UI.
          */
         const val APP_NAME = "Dream Cakes"
     }
}
