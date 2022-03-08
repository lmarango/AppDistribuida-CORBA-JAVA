package s_gestion_usuarios.servidor;
import s_gestion_usuarios.sop_corba.*;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import org.omg.PortableServer.*;

public class ServidorDeObjetos {
    public static void main(String args[]){
		try {
		    ORB orb = ORB.init(args, null);
		    POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
		    rootpoa.the_POAManager().activate();

		    GestionPersonalImpl  personalImpl = new GestionPersonalImpl();
		    //pingImpl.setORB(orb);

		    org.omg.CORBA.Object ref = rootpoa.servant_to_reference(personalImpl);
		    GestionPersonalInt cref = GestionPersonalIntHelper.narrow(ref);

		    org.omg.CORBA.Object objRef =
			           orb.resolve_initial_references("NameService");
		    NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

		    String name = "objRemotoPersonal";
		    NameComponent path[] = ncRef.to_name(name);
		    ncRef.rebind(path, cref);

			System.out.println("consultando Referencia Remota...");
	    	personalImpl.consultarReferenciaRemota(ncRef,"objRemotoPacientes");
			System.out.println("El servidor gestion de usuarios Inicio y esta esperando Acciones");
		    orb.run();
		}

		catch(Exception e) {
		    System.err.println("ERROR : " + e);
		    e.printStackTrace(System.out);
		}
	}
}
