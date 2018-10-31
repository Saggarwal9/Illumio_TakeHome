# Illumio Take-Home Assignment
Thank you, Illumio for giving me the opportunity to interview and tackle an interesting problem.  

# My Time-Line: 
6:00 - 6:05 PM : Read and understood the problem at hand.  
6:05 - 6:25 PM : Sketched my thoughts, decided data structures, class functionality and structure.  
6:25 - 7:20 PM : Implementing the code in Java using Eclipse IDE.  
7:20 - 7:30 PM : Testing.  

(and extra 10 minutes to write this Readme.)

# Some important design decisions that I would like to mention: 

### Firewall Class:
I've tried to abstract the implementation details away from the Firewall Class by introducing a Rule class.  
The firewall class only consist of two methods and a constructor. The constructor and the accept_packet  
doesn't have any functionility except for calling some methods in Rule class to start the operation.  
The third function is a test function, that I had to quickly write to test my program (More on this below).  

###  Data Structure:
The data-structure has been inspired from my MapReduce infrastructure implementation. You can look at the code here:  
https://github.com/Saggarwal9/MapReduce/blob/master/mapreduce.c  
Essentially, every Direction + Protocol combination will have it's own ArrayList index (0-3).  
Every IP would be stored as a Trie to get O(1) lookup (Inspired from Telephone Directory problem).  
The data structure is an ArrayList of HashMap with the Port number as the key and a Trie Node object as its value. 
The HashMap would take Port Number as the Key and Store the corresponding Trie of the IPs as value.  
**The time complexity to insert** would be O(1) for a single IP and a single Port.  
For a given range of IP and Port, **the time complexity to insert would be O(Max(MaxPort - MinPort, Long(MaxIP) - Long(MinIP)).**  
**The time complexity for look-up** will always be **constant**.  
The worst case **Space Complexity** while inserting would be O(4 * 65535 * (255^4)) {4 Hash Combinations * Port Table Sizes * Maximum IP possible}.  
In the wrost case, the program will run out of the heap space.  

### Converting IP to Long and Vice-Versa.
For my implementatiion, I was required to convert the IP given in string to it's unique Hexa representation.  
In order to do that, I referred the code from: https://www.mkyong.com/java/java-convert-ip-address-to-decimal-number/ .  

### Assumptions
My program will always assume that the input is valid. I ran out of time before I could add input validations.  

# Testing. 
I had initially alloted 20 minutes to write my own-test suite, however my program took longer to implement.  
After implementation, I encountered a very random bug where Scanner was unable to read the UTF-8 format of my given  
input file, which took a significant amount of time to debug.  

I, however, wrote some test cases on my own to check whether the code is working for a 
  * Long port range, single IP. (Passing)    
  * Long port range, medium IP range. (Passing)  
  * Single port, long IP range. (Passing)  
  * Medium port range, long IP range. (Passing)  
  * Long port range, long IP range. (Program ran out of heap space -- could be fixed by changing the default VM space).  
  This problem can also be solved using a Database.  

# Future Implementations/Optimization:
### Test Suite
Some integration testing to test each Trie and Rule class would help ensure consistent program functionality.  

### Database
Connection to a MySQL/SQLite database through JDBC would ensure persistent storage, and prevent running out of Heap space.  

### Trie for ports
After finishing my implementation, I realized I could have also used Trie to represent ports.  
It would've required a seperate Trie class, as for a port, the trie's height would vary (1-5)  
but for an IP it is always 4.  
So my datastructure would have been ArrayList of PortTries where each lastNode would have contained the object to IPTrie.  
This would have reduced space complexity for Ports from (4\*65536) to (4\*9^5).

### Trie for Protocol + Direction.
To further optimize the data structure, I could have just created seperate Trie nodes for Protocol and Hash.  
After you've reached the end of port trie, the program would then check the ProtocolTrie object within the PortTrie.    
The protocol trie would be of size 2 where the 0th index would indicate TCP and 1st index would indicate UDP.  
The protocol trie would also contain an object to the direction Trie.  
The direction trie would be of size 2 where the 0th index would indicate outbound and inbound index would indicate UDP.  
This would have eliminated the need to maintain four seperate portTries in the above mentioned optimization.  


### Concurrency
(To the current datastructure, not the above mentioned Trie implementation).  
* To make insertions faster, I could have used one thread per Hashtable (4 Hashtables).  
In worst case, it doesn't change the run-time.  
In best case, it would cut the insertion run-time by 4.  

* For each hashtable, I could have multiple worker threads.  
I could concurrently add IP's belonging to distinct port by making a thread acquire lock for the corresponding port's Trie,
and then inserting the new IP (Similar to what I did in MapReduce infrastructure).  
In worst case, the run-time increases due to scheduler interrupts.  
In best case, it would **SIGNIFICANTLY** cut down the run-time.

# Team
I find both the Platform and Policy team equally interesting and would like to be considered for both. However, if I have to rank:  
1) Platform team.  
2) Policy team.  

Thank you for taking the time to read through this,

Shubham Aggarwal

