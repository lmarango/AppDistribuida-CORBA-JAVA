package cliente.cliente;

import s_gestion_usuarios.sop_corba.AdmCllbckIntPOA;

public class AdmCllbckImpl extends AdmCllbckIntPOA{
    public AdmCllbckImpl(){
        super();
    }
    @Override
    public void notificar(String nombreCompleto, int id) {
        System.out.println("*** objeto callback desde notficar()...");
        System.out.println("***El usuario [" + nombreCompleto + "] identificado con id [" +
                            id + "] ingreso a la aplicacion***");
        System.out.println("==  == == ==  == == ==  ==");
    }    
}
