package build.proj;

// probably should move to a specific file. 
enum Type_Service {
    BACKUP
}

public class ThreadServices extends Thread {

    protected Type_Service type;

    public ThreadServices(Type_Service type, ) {
        this.type = type;
    }

    public void run() {
        switch (type) {
        case Type_Service.BACKUP:
            backup();
            break;
        default:
            System.out.println("Not a valid command!");
            return;
        }
    }

    public void backup() {

    }

    
}
