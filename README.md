# crawlerSearch.java

The main functionality of crawlerSearch is to first perform a web crawl that parses and saves the body and link tag data (term frequencies, outgoing links, etc.) from the HTML of a collection of basic webpages. 
The program implements a graphical user interface using Java FX that takes in user input (string queries and Boolean boost toggle). 
It then retrieves and interprets the relevant data within the crawled data, returning a list of the top 10 most relevant links, sorted in order from highest to lowest.

/* Model */
//Crawler.java
The role of the Crawler class is quite clear and predictable—it is in charge of executing the crawl of the data. 
As the crawler itself, this object is in charge of centralizing the extraction, collection, processing, and saving of the necessary data required to perform a search. 
The crawl itself begins once the “crawl” method is called, which then calls various methods defined by the Crawler class itself and other classes. 
Given the fact that these methods are in charge of the actual page parsing and processing, the crawl method itself is intentionally concise and readable, allowing for easy understanding of what exactly the crawl is doing.
//Link.java
Given the shear amount of data contained within a given link/webpage, I decided to make a class, Link, which would represent each link and its corresponding data as an object. 
To this extent, the Link object contains many attributes (ex TFIDF values, outgoing links, page rank, etc.) which are ultimately defined outside of the object itself, using getter and setter methods.
Ultimately, this allows for efficient processing and storing of link data, since each Link object can be serialized and saved to a file.
//PageRankCalculator.java
In order to find the PageRank value of a given link, many complex matrix operations are computations are required.
Thus, I decided it would be best to create a PageRankCalculator which would be in charge of calculating this value. 
In this sense, the class is very minimal, mainly relying on the pageRank method as behavior of finding and returning a given Link’s page rank value. 
This process is further decomposed into two other classes which ultimately allow for the successful calculation of page rank values:
  //AdjacencyMatrix.java
  Given the geometrical structuring of the adjacency matrix, it was easier create an AdjacencyMatrix class to handle the matrix operations needed to find the page ranks. 
  The object has a size, 𝑛, and rows and columns, and behavior that allows the construction and processing of a matrix. 
  Thus, any operations involving matrices, including matrix multiplication and scalar multiplication, are defined within the class itself.
  //URLMapping.java
  Given the geometric structuring of the AdjacencyMatrix object, in which each link is represented in a particular row and column index, there needs to be a way to “remember” which link corresponds with which positioning.
  Thus, I decided to create the URLMapping class, which allows the adjacency matrix to define its own universal mapping from URL to index and vice versa, which thus allows us to be able to process the matrix data in a meaningful manner.
//Search.java
Once the crawl is successfully completed and all Link data is saved to files, there needs to be a way to retrieve and process the data saved in the files. 
This is the responsibility of the Search class, which acts as a sort of file handler, a middleman between saved file data and retrievable Java instance. 
To this extent, it is not the responsibility of the Search class to perform any computations, it is primarily in charge of finding and returning the relevant data needed by a specific user inquiry. 
Thus, the Search class works in tandem with Tester class to retrieve and return the desired data.
//Tester.java
The Tester class implements the ProjectTester interface, and in this sense, acts as the “’glue’ between my classes and an automated tester. 
I intentionally abstracted this class to the highest extent as possible in order to presentably outline which classes are performing what tasks and why.
This was achieved by creating instances of the Crawler and Search classes and outsourcing the data storage and retrieval. 
In this way, the Tester is easy to approach and understand, and is thus accessible to anybody hoping to implement or understand the search process.
//UserInput.java
Given the fact that our search engine, by its very definition, must handle user input inquiries, I found it necessary to create a class called UserInput, which not only receives and processes UserInput strings, but also compares the user query with the saved crawl data. 
First and foremost, the class parses the raw string data to its relevant components, interpreting the input by term frequency and TFIDF. 
Then, its main behavior is to find and return a sorted list of the top 10 search results, defined in the scoreList method. 
Thus, the main role of this class is, by its namesake, to handle and process user input queries, returning SearchScore results.
  //SearchScore.java
  The SearchScore class is a very simple class that implements the SearchResult interface. 
  Overall, its attributes embody the values of a SearchResult (i.e., title and link), and its implementation of the helper class comparatorHelper allow the SearchResult data to be efficiently sorted, easily accessed and returned in a concise manner.
//Maths.java
Simple strictly behavioral class with no attributes, acting as a calculator class for the classes within the project. 
By creating an instance of Maths, we essentially create a calculator which is able to perform and return these computations: logarithms of base 2, Euclidean distance, cosine similarity. 
Ultimately this class makes for better readability in highly computational methods such as pageRank, and also prevents error in repetitious implementation.

/* View */
//TesterView.java
Operating in the MVC (Model View Controller) paradigm, the simple nature of the user search input inquiries (a string query and boost Boolean) requires a minimal, yet organized, user interface. 
Thus, I was able to implement the view in a singular class, TesterView, which is in mainly in charge of the aesthetic representation of the UI window. Other than the fact that the view updates its own attributes, its implementation is independent of both the model and controller.

/* Controller */
//TesterController.java
Given its roll as the “glue” between my classes and the testing, it made the most sense to design the controller around the Tester class. 
In this way, the controller is able to receive the user’s query. Yet, instead of performing any processing at all, the TesterController simply expedites the processing to the model through the middleman of the Tester. 
In this way, the TesterController does exactly as intended, first accepting input from the user, and then updating the model and view accordingly.

/* Design */
Now that we have a general idea of how the project was implemented, I will now describe my decision-making process in doing so—why I decided to implement the project in this way.
One of the most fundamentally intrinsic attributes of OOP is certainly the principle of abstraction, which was thus a priority in my conception of the project. 
Starting at the most general theoretical level, abstraction was an integral motivating factor in my approach to class conception. 
Before I even began writing any code, I spent a lot of time trying to abstractly break apart the individual aspects of a search engine, which ultimately inspired the classes I decided to create. 
At a conceptual level, I found that the perception of certain elements of the project as objects (for example, each page as a Link) encouraged me to better understand the significance of that particular component and its role in the project as a whole, mainly by having to conceive of both its attributes and behavior. 
Ultimately, I found that this approach to class conceptualization led to my project being a lot more modular, a more generalized collection of standardized components as opposed to a context-specific singular block of code.
At the more specific level of individual classes, I took advantage of decomposition to break the actual implementation process and code writing into smaller, more approachable pieces. 
For example, the crawl method in the Crawler class is itself only 41 lines (including line breaks and comments). This was achieved by breaking down the crawl process even more, into more task-specific methods such as findIdf or parseBody. 
This presents a massive advantage towards reducing repetitious code, as these methods can be called upon later to accomplish the same task.
The Tester class also perfectly exemplifies this embodiment of abstraction; it performs no processing or computation in itself, it merely calls on other methods to accomplish these tasks and is consequently very easy to understand. 
In doing so, my code is not only more readable to myself, in an organizational sense, but also to others, who can easily understand that findIdf means that the crawl method will find the IDF values, without necessarily understanding how—this is an example of abstraction at its finest.
Along with the notion of abstraction, each class within my project adheres to the conditions of encapsulation.
Mainly, I made use of the principles of encapsulation in order to further secure the robust character of my code. At the most fundamental class level, encapsulation was achieved by the declaration of private attribute/instance variables. 
By doing so, I was able to ensure that these variables cannot be altered from outside the class without the specified permission of a get/set method. 
In doing so, I was able to avoid the potential threat of accidental variable modification, which could undermine the entire execution of the program with unintended null pointer exceptions or spurious data results. 
Moreover, the nature of this project suggests that the user should have no reason to access or modify the source code in any way, seeing as any user inquiry is intended to be dealt with by the GUI (view and controller).
Therefore, encapsulation helps us to assure that the program is being used properly and not being tampered with in either a nefarious or accidental way.
Another way in which I was able to enforce the intended interaction between the user and the GUI was through the manner in which I decided to store the crawl data into files. 
Specifically, I first created a head folder, crawlData, in order to contain all the crawled data in a single, easy to locate, dedicated folder. 
Then, each Link object is serialized and stored as a Link object within a corresponding individual file. Considering the unreadable nature of this serialized metadata content, the visual string contents of the files themselves are rendered irrelevant to the human user. 
This was intentional, as it grants me the ability to prevent any desire on the user’s behalf to read or potentially alter the file contents, and consequently streamlines the user into using the GUI to obtain search results or crawl data. 
I think this is a good approach given the specific constraints of this project, but I am also wary that this could present issues in other contexts, such as projects that work with larger data sets that could waste a lot of memory by saving potentially unnecessary object attributes to file, or projects that require readable file data. 
However, organizationally speaking, saving each Link object to a file also allows for very simple code and efficacious storage, as individual attributes do not need to be further parsed or organized within file directories. 
This also prevents the risk of accidentally misplacing a given piece of information, as all the Link data is stored together.
Finally, I would like to conclude this overview by briefly addressing the OOP principles of inheritance and polymorphism and explaining their absence in my project (excluding, of course, the implementation of the interfaces SearchResult, ProjectTester, and Comparator). 
During the early conceptualization stages of my project, I greatly meditated how inheritance could play a role in the implementation of a search engine or web crawler.
Yet, after analyzing the nature of the project and the relationship between the various classes and objects involved (Link, UserInput, SearchScore, etc.), I realized that none of these classes represent a naturally hierarchal relationship with one another (an “is a” relationship), and in this sense inheritance would likely pose a harmful effect to the project if forced upon the project in this contrived way.
Given its inherent relationship with inheritance, I found that the same realization applied to polymorphism; ultimately, it is not worth potentially jeopardizing the authenticity and genuineness of my project in order to forcefully impose an unnecessary instance of double dispatching, for example.

/* Runtime Complexity */
Although my primary focus was geared towards the implementation of OOP principles in my project, I also made an effort to minimize the runtime complexity of my program. 
Specifically, I made a conscious decision to perform much of the data computation during the crawl, so as to avoid lengthy computations during the user search process.
Moreover, if it was necessary to iterate through a long list (for example, iterating through all the links), I tried to accomplish as many tasks within that loop as possible, so as to prevent the need for another long loop elsewhere in the project.
Another manner in which runtime efficiency was achieved was through the use of hash sets and maps for larger data sets, making the iteration much more efficient. 
Finally, the very nature of OOP class definition, namely object attributes, enabled me to store the most significant, reoccurring information, as global variables and thus avoid the need to perform unnecessary redundant computations.
