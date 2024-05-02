## Programming partners: Buddy Luong, Danielle Justo, Isabelle Wang

## Websites/Resources Consulted: kaggle, Java Oracle Documentation, GeeksforGeeks, and javapoint AWT tutorials.

### Project Description:
 This project is a statistical analysis of the effectiveness of Pokemon types against each other. We have two data sets, one of generation one pokemon and their information, and one of pokemon types and their effectiveness. We chose to make two graphs using this data. One of pokemon types and their weaknesses, and one of their strengths. These graphs will be directed. We also made a user interaction portion in which a user can select a pokemon and recieve the types that pokemon is strongest and weakest against.
 
## Journal Entry (minimum one per phase): 

Phase 0: 

Currently, we have two data sets, one of generation one pokemon and their information, and one of pokemon types and their effectiveness. 

We chose to make two graphs using this data. One of pokemon types and their weaknesses, and one of their strengths. These graphs will be directed. We also plan to make a user interaction portion in which a user can select a pokemon and recieve the types that pokemon is strongest and weakest against. 


Phase 1: 

We used a CSV data file containing the pokemon types and how they are effective against each other.

In order to read in this data, I created a method called arrayify() which takes in single lines of the file and returns an array containing the data of that line. Each element of the array was a string that was split from the full line at the index of the comma.

I then created two separate methods to graph the effectiveness of pokemon types toward each other (one for strengths and one for weaknesses). A "1" value in the CSV file denotes a normal strength multiplier (1x), a "2" denotes a super effective multiplier (2x), and a "0.5" denotes a not very effective multiplier (0.5x). Because of this, I on added edges for "2" values in the strengths chart and I only added an edge for "0.5" values in the weaknesses chart.

The edges are directed, starting from the type that is effective against the other type. For example, if an edge is directed from Water to Fire in the strength graph, then Fire has a weakness toward Water.

This data was then used to return the strength multipliers of specific pokemon in the computation phase.


Phase 2: 

We made methods to access the data in our graph. 

First, we made a hashtable to align each pokemon with their types. The pokemons are the keys while the types are the values. Some pokemon had multiple types, so we set it as a list of types. Then, we constructed methods to access the types. We added generic methods that showed the number of degrees per node, the node with the maximum degree, the node with the minimum degree, the average degree number of the nodes. We also made a breadth first traversal for the types in the list. To make this more interactive, we used Scanner for users to choose what data they want to view. 

Phase 3: 
We made a GUI with multiple sections to access different parts of our statistical analysis. Two of the pages hold the graphs. One allows you to analyze individual pokemon. And the last allows us to access specific statistics, holding options in a drop down menu for easy access. 

## Reflection on assigment: 

Dani: 
This assignment was fun! I feel like because this is a topic we are all interested in, and there are three of us, the main portion of the assignment went by pretty quickly. With extra time, we were able to construct the GUI and add fun extra things to it! 

Isabelle: Our choice to work on Pokemon was very fun and interesting! I enjoyed working with Buddy and Dani on this project and adding a GUI to make our program more interactive. I also learned a lot with java awt and swing and applying what I learned in this course to this project. With us three, the main goal of the assignment was achieved and we had additional time for front-end. 

Buddy: I had a lot of fun working on this project! I think that the topic was interesting and I learned a lot about Pokemon that I didn't know before. I also learned a lot about Java AWT and Swing, which I had never used before. I think that the project was a success and I'm happy with the final product. I also worked on this project with partners for the first time ever so it was super fun to collaborate and bounce ideas off each other.


## More Journal
**Date:** 4/24/2024
   - Initial commit of the project. Began thinking through what data values correspond to nodes and which ones correspond to edges, found a dataset that we wanted to proceed with, and began reading text file.
 - **Date:** 4/26/2024
   - PHASE 1 {done} -> Finished reading in the data from the CSV file and created two graphs for strengths and weaknesses. Created methods to graph the effectiveness of pokemon types toward each other (one for strengths and one for weaknesses). Added edges for "2" values in the strengths chart and added an edge for "0.5" values in the weaknesses chart.
   - Started working on the computation phase. 
 - **Date:** 4/27/2024
   - Working on the GUI!
   - PHASE 2 {done} -> Made methods to access the data in our graph. Made a hashtable to align each pokemon with their types. Constructed methods to access the types. Added generic methods that showed the number of degrees per node, the node with the maximum degree, the node with the minimum degree, the average degree number of the nodes. Made a breadth first traversal for the types in the list. Used Scanner for users to choose what data they want to view.
   - PHASE 3 {done} -> Made a GUI with multiple sections to access different parts of our statistical analysis. 
 - **Date:** 4/30/2024 
 -   - Did the Demo in class! we realized that not everyone knows many things about Pokemon, so we added a section to the GUI that has a list of all the pokemon.
 - **Date:** 5/1/2024
   - Finalized the project. Added comments and cleaned up the code. Made sure everything was working properly.


