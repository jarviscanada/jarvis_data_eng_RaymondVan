# Introduction
This app uses the Twitter API to perform CRUD operations. 
The architecture consists of following the MVC design pattern.
Arguments are passed in via the command line which gets processed by the 
main app (TwitterCLIApp). We then rely on Spring to create and manage all
of our dependencies (Controller, Service, DAO) via dependency injection. 
Our arguments gets passed down through the dependencies to make 
a HTTP request to the Twitter REST API. The JSON response then gets handled
by our Tweet Model and gets sent back to the user.