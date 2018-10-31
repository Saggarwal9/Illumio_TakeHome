public class Firewall {
    Rule rule;
    
    public Firewall(String fileName) {
        rule = new Rule();
        rule.readFile(fileName);
    }
    
     
    public boolean accept_packet(String direction, String protocol, int port, String ip_address) {
        return rule.validateInput(direction, protocol, port, ip_address);
    }
    
    public static void manualTesting() {
        Firewall fire = new Firewall("rules.txt");
        
        //Given test cases.
        System.out.println(fire.accept_packet("inbound", "tcp", 80 , "192.168.1.2")); //First rule
        System.out.println(fire.accept_packet("inbound", "udp", 53, "192.168.2.1")); //Third rule
        System.out.println(fire.accept_packet("outbound", "tcp", 10234, "192.168.10.11")); //Second rule
        System.out.println(fire.accept_packet("inbound", "tcp", 81, "192.168.1.2")); //False
        System.out.println(fire.accept_packet("inbound", "udp", 24, "52.12.48.92")); //False
        
        //I would have liked to write a test suite with JUnit, however due to the time limitation,
        //I'll just be writing some extra test cases to see if the program works as expected.
        //Advanced test cases.
        System.out.println(fire.accept_packet("inbound", "tcp", 90 , "1.1.1.1")); //Fifth rule
        
        System.out.println(fire.accept_packet("inbound", "tcp", 40000 , "192.168.10.20")); //Last rule border
        System.out.println(fire.accept_packet("inbound", "udp", 40001, "192.168.10.21")); //Fail
        System.out.println(fire.accept_packet("inbound", "tcp", 40001, "255.255.255.255")); //Fail
        System.out.println(fire.accept_packet("inbound", "udp", 40001, "255.255.255.255")); //Pass
        
    }
    
    public static void main(String args[]) {
        manualTesting();
    }
}
