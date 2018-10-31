import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Rule {
    ArrayList<HashMap<Integer,Trie>> map;
    String minIP;
    String maxIP;
    int minPort;
    int maxPort;
    
    public Rule() {
        map = new ArrayList<HashMap<Integer,Trie>>();
        //4 Possible combination of Direction + Protocol.
        for(int i = 0; i<4; i++) {
            map.add(new HashMap<Integer,Trie>());
        }
    }
    
    //Helper function used to compute the has based on the direction and protocol.
    //Inbound + TCP = 0. Inbound + UDP = 1. Outbound + TCP = 2. Outbound + UDP = 3.
    public int hash(String direction, String protocol) {
        String key = direction.toLowerCase() + protocol;
        if(key.equals("inboundtcp"))
            return 0;
        else if(key.equals("inboundudp"))
            return 1;
        else if(key.equals("outboundtcp"))
            return 2;
        else
            return 3;
    }
    
    //Convert IP to Long. 
    //Source: https://www.mkyong.com/java/java-convert-ip-address-to-decimal-number/
    public long ipToLong(String ipAddress) {
        String[] ipAddressInArray = ipAddress.split("\\.");
        long result = 0;
        for (int i = 0; i < ipAddressInArray.length; i++) {
            int power = 3 - i;
            int ip = Integer.parseInt(ipAddressInArray[i]);
            result += ip * Math.pow(256, power);
        }
        return result;
      }
    
    //Convert Long to IP.
    //Source: https://www.mkyong.com/java/java-convert-ip-address-to-decimal-number/
    public String longToIp(long ip) {
        StringBuilder result = new StringBuilder(15);
        for (int i = 0; i < 4; i++) {
            result.insert(0,Long.toString(ip & 0xff));
            if (i < 3) {
                result.insert(0,'.');
            }
            ip = ip >> 8;
        }
        return result.toString();
      }
    
    //Function to parse the range of IP, ports in the given set of rules.
    public void parse(String port, String ip) {
        //Port range parsing
        if(port.contains("-")) {
            String[] portArray = port.split("-");
            this.minPort = Integer.parseInt(portArray[0]);
            this.maxPort = Integer.parseInt(portArray[1]);
        }
        //Single port
        else {
            int tempPort = Integer.parseInt(port);
            this.minPort = tempPort;
            this.maxPort = tempPort;
        }

        //IP range Parsing
        if(ip.contains("-")) {
            String[] ipArray = ip.split("-");
            this.minIP = ipArray[0];
            this.maxIP = ipArray[1];
        }
        //Single IP
        else {
            this.minIP = ip;
            this.maxIP = ip;
        }
    }


    //Function that reads the file, and forms the data structure.
    //The data structure is an ArrayList of HashMap. Essentially, every Direction + Protocol
    //combination will have it's own HashMap.
    //This HashMap would take Port Number as the Key and Store the corresponding Trie Object as value.
    //The trie would keep track of the IP.
    //The time complexity would be O(1) for each IP. For a given range of IP and Port, the complexity
    //would be O(Max(MaxPort - MinPort, Long(MaxIP) - Long(MinIP))
    //The idea for this Data Structure stems from a project I did to mimic MapReduce infrastructure.
    public void readFile(String fileName) {
        try {
            Scanner scanner = new Scanner(new File(fileName),"UTF-8");
            while(scanner.hasNextLine()) {
                String input = scanner.nextLine();
                String[] parameter = input.split(",");
                int hashIndex = this.hash(parameter[0], parameter[1]);
                HashMap<Integer,Trie> portMap = map.get(hashIndex);
                parse(parameter[2],parameter[3]);
                
                //0 Indexed port
                for(int i = minPort - 1; i <= maxPort - 1; i++) {
                    Trie head = portMap.get(i);
                    if(head == null) {
                        head = new Trie();
                        portMap.put(i, head);
                    }
                    
                    for(long j = ipToLong(this.minIP); j <= ipToLong(this.maxIP); j++) {
                        String IP = longToIp(j);
                        head.insert(IP.split("\\."));
                    }
                }
            }
            
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Programming exiting");
            System.exit(0);
        }
    }
    
    public boolean validateInput(String direction, String protocol, int port, String ip_address) {
        int hashIndex = this.hash(direction, protocol);
        HashMap<Integer,Trie> portMap = map.get(hashIndex);
        //Port is 0 indexed.
        Trie head = portMap.get(port-1);
        if(head != null) {
            return head.search(ip_address.split("\\."));
        }
        return false;
    }
}
