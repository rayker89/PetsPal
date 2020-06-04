package com.example.petspal.Onboarding


class Pet(var name:String?, var dateOfBirth:String?, var species:String?, var gender:String?, var breed:String?, var weight:String?, var image:String?){

    constructor() : this(null, null, null, null, null, null, null) {
    }
}