# Lights

The main purpose of this micro project is to learn java threads and concurrency and websockets in java 
and custom logging.

## main features

### Traffic light creation

Users can create new traffic lights using a POST endpoint. 
every new traffic light will create a new thread, which will go through its own custom length of steps of RED, YELLOW and GREEN.


### Traffic lights can depend on one another

if there is a main traffic light, then others have to mirror it or be the reverse of it.

So 

         r   |   G                                  g   |   R
          ------                                      ------
        g    |      r       will become            r    |      g        


With the upper right one being the master, lower right mirror and the rest reverses.


### traffic light has to have a intersection name, where it belongs.


### Traffic lights has to follow the order  
G -> Y -> R
R -> Y -> G



### Traffic lights have to have customizable durations




### API has to provide a websocket connection, where user can get Traffic lights current condition
