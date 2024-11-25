import java.io.Serializable;

public class ClientAddress implements Serializable {
    private final String ipAdress;
    private final int portNumber;

    public ClientAddress(String ipAdress, int portNumber){
        this.ipAdress = ipAdress;
        this.portNumber = portNumber;
    }

    public int getPortNumber(){
        return portNumber;
    }

    public String getipAdress(){
        return ipAdress;
    }
}
