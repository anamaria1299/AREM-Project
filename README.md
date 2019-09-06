# HTTP Server Project

In this project was implemented an HTTP Server that can resolve requests of HTML pages and resources like image PNG, also this handle POJO (Plain Java Object) through an implemented framework that works with @WEB annotation that handle the methods that the user want to show into a web interface. Besides, It can receive multiple no concurrent requests. 

# Architecture

If you want to know how is the architecture of this application you must go to the following [architecture file](https://github.com/anamaria1299/AREM-Project/blob/master/Arquitectura.pdf)

# Test

## Bringing an image
![](https://github.com/anamaria1299/AREM-Project/blob/master/resources/wolf.PNG)

## Bringing hello page
![](https://github.com/anamaria1299/AREM-Project/blob/master/resources/hello.PNG)

## Bringing hello with parameter name page
![](https://github.com/anamaria1299/AREM-Project/blob/master/resources/helloName.PNG)

## Bringing not found page
![](https://github.com/anamaria1299/AREM-Project/blob/master/resources/NotFound.PNG)

# Deployment

[Deploy link](https://http-server-ana.herokuapp.com/)

The deploy of this application was in [Heroku](https://www.heroku.com/), you can find this app in [http-server-app](https://http-server-ana.herokuapp.com), there are pages that contains simple html and another one that contains images:

#### Images [wolf](https://http-server-ana.herokuapp.com/lobo.png) - [dog](https://http-server-ana.herokuapp.com/dog.png) - [cat](https://http-server-ana.herokuapp.com/cat.png) 

#### HTML [Hello](https://http-server-ana.herokuapp.com/apps/hello) - [Hello with name](https://http-server-ana.herokuapp.com/apps/helloName?name=Ana)

you could change the name to yours making url?name={your_name}. Also is important to know that if you are looking something that does not exist the server will sed an [error page](https://http-server-ana.herokuapp.com/hi).

# Build with

* [Maven](https://maven.apache.org/) - Dependency Management

# Author

* Ana María Rincón Casallas - Software engineering Student 

# License

This project is licensed under the GNU License - see the  [License](https://github.com/anamaria1299/AREM-Project/blob/master/LICENSE) file for details
