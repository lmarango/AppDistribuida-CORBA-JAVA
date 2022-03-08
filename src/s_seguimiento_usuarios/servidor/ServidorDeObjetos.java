package s_seguimiento_usuarios.servidor;

import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import org.omg.PortableServer.*;

import s_seguimiento_usuarios.sop_corba.GestionPacientesInt;
import s_seguimiento_usuarios.sop_corba.GestionPacientesIntHelper;
public class ServidorDeObjetos {
	public static void main(String args[]){
    	try {
			ORB orb = ORB.init(args, null);

			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();

			GestionPacientesImpl  pacientesImpl = new GestionPacientesImpl();
			//pacientesImpl.setORB(orb);

			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(pacientesImpl);
			GestionPacientesInt cref = GestionPacientesIntHelper.narrow(ref);

			org.omg.CORBA.Object objRef =
					orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			
			String name = "objRemotoPacientes";
			NameComponent path[] = ncRef.to_name(name);
			ncRef.rebind(path, cref);

			System.out.println("El servidor gestion de usuarios Inicio y esta esperando Acciones");
			orb.run();
		}

		catch(Exception e) {
			System.err.println("ERROR : " + e);
			e.printStackTrace(System.out);
		}
	}
}

