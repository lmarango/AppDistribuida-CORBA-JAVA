package cliente.cliente;

import s_gestion_usuarios.sop_corba.FapCllbckIntPOA;

public class FapCllbckImpl extends FapCllbckIntPOA{

    public FapCllbckImpl(){
        super();
    }
    @Override
    public void informarIngreso(String nombreCompleto, int id) {
        System.out.println("*** objeto callback desde informarIngreso()...");
        System.out.println("***El usuario [" + nombreCompleto + "] identificado con id [" +
                            id + "] esta disponible para la valoracion***");
        System.out.println("==  == == ==  == == ==  ==");
        
    }
    
}
