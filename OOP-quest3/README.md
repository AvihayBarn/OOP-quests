# OOP_Ex3
Our fourth quest in the OOP course


#### Authors: Avihay Barnholtz and Dolev Brender
  This documentation contains only the new classes that are extend of the previous quest OOP_Ex2
  
## MyGameGui
  The Class MyGameGUI. This class gives you the option to play manually or automatically in "Robots and Fruits" game.
  The game is an instance of the interface "game_service" and this class is a graphical representation of this interface.
  When you decelerate on MyGameGui object and run the program,white screen shows and menu bar in the left side up.
  In the menu bar you can choose to play automatically by clicking "Automatic Game" and manually by clicking "Manual Game"
  after you choose your game style,you choose a "scenario",which is the difficulty of the game level. there are scenarios from 0 to 23 
  and you have to just just from this range of numbers.
  after you choose the scenario the game is starts.
  If you play manually,you make the robots move by click on a node they can reach and the first robot from robot list that has no         destination
  yet goes to this node.
  during the game the time left,your current score,your current move and the scenario of the game are printed in the left up corner of     the screen.
  In the end of the manually game nothing happen and the game wait for new commands,but in the automatic game a panel with an offer to     save the game info
  in KML format is shown.
 

## AutoGameGenerator
 The Abstract Class AutoGameGenerator.this is the tool that manages the automatic game in MyGameGui.
 it moves the the robots systematically by processing the information about the robot's speed, the fruit's value 
 and the distance of the robot from the fruit-and deciding which robot should reach each fruit.
 in addition , this class adds the robots to the best node in the graph to start Which will give a good yield of points for the       q   beginning 
 of the game.


## Fruit and Robot
Custom classes that contain all the information of the robots and fruits in the game

## KML Logger
The Class KML_Logger. it gives you the option the save Your game review in a kml format

### Algorithms
The Abstract Class Algorithms.
It contains a collection of useful algorithms on graphs 




