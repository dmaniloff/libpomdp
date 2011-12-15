package libpomdp.simulator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileInputStream;

import java.lang.ClassNotFoundException;

class PrintSimulatorStatistics {

    public static void main(String args[]) {

        SimulatorStatistics stats = null;

        try {
            FileInputStream fis = new FileInputStream(args[0]);
            ObjectInputStream oi = new ObjectInputStream(fis);

            stats = (SimulatorStatistics)
                oi.readObject();
            oi.close();
        } catch ( IOException e ) {
            e.printStackTrace();
            System.exit(1);
        } catch ( ClassNotFoundException ex ) {
            ex.printStackTrace();
            System.exit(1);
        }

        System.out.println( stats.toString() );
    }

}