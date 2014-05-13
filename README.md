Bookkeeping_App_Android
=======================

Android application that interact with the API Rest and allows the user to interact with it

For make this app works:

* Download the API Rest (https://github.com/jesus-/Shop_bookkeeping_API_REST).

* Create user from the rails console:
 user = User.new
 user.name = "user"
 user.salt= BCrypt::Engine.generate_salt
 user.password = BCrypt::Engine.hash_secret("password", user.salt)
 user.email = "Email_of_user@gmail.com"
 user.save
#this creates an user with name user, and password password.

* Once you have the user, you have to tell your app where the API server is:

  In the class ConnectionAPI, define the final variable URLServer, for example:
  
  	final static String URLServer ="http://prueba.herokuapp.com/"; // if you upload your API to Heroku or another server
    
    final static String URLServer ="http://localhost:3000"; // If you have it in you computer, AND YOU ARE EXECUTING IT 
    FROM THE EMULATOR.
    
    final static String URLServer ="http://localhost:8080"; // If you have it in you computer, and you connect your phone 
    with an usb and use Chrome devices and map the port 8080 to the 3000.
    
    final static  String URLServer ="http://192.168.1.93:3000";// if your phone and your server are in the same network, you 
    can use the private IP to connecto to it.
    
* Final step, when you open your app, go to settings and insert the user and password you created, this will save that 
information in the app preferences and you wont need to insert it again. If you dont introduce user and password, the app
will tell you that there is a problem with your network (the server will send you 403 all the time, unnathorized acces).

  
