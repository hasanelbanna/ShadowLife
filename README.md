# ShadowLife

The purpose of this project is to: 
• gain experience working with an object-oriented programming language (Java) using OOP principles. 
• introduce fundamental game programming concepts (2D graphics, timing, geometric calculations) 
• gain experience writing software using an external library (Bagel)

## Description of the simulation

"Shadow Life is a graphical simulation of a world inhabited by creatures called gatherers. Their purpose in life is to gather fruit from the trees, and deposit them at stockpiles. Once they have gathered all the fruit from their trees, they rest in front of fences.
Making their life dificult is the thief who aims to steal fruit from the stockpiles and place it in their hoards. The thief and gatherers follow rigid rules, and once they all reach their final goals (the fence), the simulation halts. They are quite industrious workers—with enough time, they could calculate anything that any computer can!

The behaviour of the simulation is entirely determined by the world file loaded when the Shadow Life program starts: each gatherer, thief, and other element begins at a specified location and follows a set of rules to determine their behaviour. Once all gatherers and thieves have reached a fence, the simulation halts, and the amount of fruit at each stockpile and hoard is tallied up. The simulation proceeds in ticks, with the tick rate (time between ticks) determined by a command- line parameter. If more than a maximum number of ticks (also determined by a command-line parameter) pass before halting, the simulation times out. Otherwise, the number of elapsed ticks, together with the amounts of fruit at each location, is printed to form the result of the world file."

To watch the demo simulation on YouTube click [this link](https://youtu.be/x2hx7rPrTVg)
---
## Run the sumilation 
1. Clone repository 
2. Open in IntelliJ or any other IDE you use for Java as a project
3. Add bagel.jar file under "lib" folder as project library 
4. Run the ShadowLife.java file to view the simulation

(Optional: there are other simulation worlds provided in res/worlds folder. To view the simulation in a different world:
Open args.txt file -> replace 'product' with desired world -> save -> Run ShadowLife.java)
