package com.ahmed.content_provider

class contact {

    private var id : String
    private var name : String
    private var email : String
    private var number : String
    private var photo : String
    private var other_details : String

    constructor(id:String,name: String,email:String,number:String,photo:String,other_details:String){

        this.id=id
        this.name=name
        this.email=email
        this.number=number
        this.photo=photo
        this.other_details=other_details
    }

    fun setid(id:String){
        this.id=id
    }

    fun setname(name:String){
        this.name=name
    }

    fun setemail(email: String){
        this.email=email
    }

    fun setnumber(number: String){
        this.number=number
    }

    fun setphoto(photo: String){
        this.photo=photo
    }

    fun setother_details(other_details:String){
        this.other_details=other_details
    }


    fun getid() : String {
    return id
    }

    fun getname() : String {
        return name
    }

    fun getemail() : String {
        return email
    }

    fun getnumber() : String {
        return number
    }

    fun getphoto():String{
        return photo
    }

    fun getother_details() : String {
        return other_details
    }
}
