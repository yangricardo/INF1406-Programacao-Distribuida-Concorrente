package StockMarket;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import java.util.Properties;

/**
 * Created by eaverdeja on 19/06/17.
 */
public class StockServerMain {
    public static void main(String[] args) {
        Properties orbProps = new Properties();
        orbProps.setProperty("org.omg.CORBA.ORBClass", "org.jacorb.orb.ORB");
        orbProps.setProperty("org.omg.CORBA.ORBSingletonClass", "org.jacorb.orb.ORBSingleton");

        ORB orb = ORB.init(args, orbProps);

        StockServerImpl stockServer = new StockServerImpl();

        POA poa = null;
        try {
            poa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            poa.the_POAManager().activate();
            org.omg.CORBA.Object o = poa.servant_to_reference(stockServer);
        } catch (InvalidName invalidName) {
            System.err.println(invalidName.getMessage());
            invalidName.printStackTrace();
        } catch (ServantNotActive servantNotActive) {
            System.err.println(servantNotActive.getMessage());
            servantNotActive.printStackTrace();
        } catch (WrongPolicy wrongPolicy) {
            System.err.println(wrongPolicy.getMessage());
            wrongPolicy.printStackTrace();
        } catch (AdapterInactive adapterInactive) {
            System.err.println(adapterInactive.getMessage());
            adapterInactive.printStackTrace();
        }

        orb.run();
    }
}
