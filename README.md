# Lights

The main purpose of this micro project is to learn java threads and concurrency and websockets in java 
and custom logging.

## main features



- Traffic light creation

Users can create new traffic lights using a POST endpoint. 
every new traffic light will create a new thread, which will go through its own custom length of steps of RED, YELLOW and GREEN.

- Traffic lights has to follow the order  
G -> Y -> R
R -> Y -> G

-  Traffic lights have to have customizable durations
-  API has to provide a websocket connection, where user can get Traffic lights current condition




## setup
**API**
First the API module has to be executed via ``./gradlew bootrun`` or something similar

**UI**
navigate to UI folder and execute

``
npm i
npm run dev
``
