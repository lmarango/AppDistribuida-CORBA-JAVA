package cliente;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import org.omg.PortableServer.*;

import cliente.cliente.AdmCllbckImpl;
import cliente.cliente.FapCllbckImpl;
import cliente.utilidades.UtilidadesConsola;
import s_gestion_usuarios.sop_corba.*;
import s_seguimiento_usuarios.sop_corba.*;
public class ClienteDeObjetos {
    public static GestionPersonalInt href1;
    public static GestionPacientesInt href2;
    public static void main(String args[]) {
        try {
            // Crea e inicializa el orb
            ORB orb = ORB.init(args,null);
            // obtiene el contexto de nombrado raiz de Name Service
             org.omg.CORBA.Object ref=orb.resolve_initial_references("NameService");
            // Usa NamingContextExt
            NamingContextExt ncref = NamingContextExtHelper.narrow(ref);

            String name = "objRemotoPersonal";
            href1 = GestionPersonalIntHelper.narrow(ncref.resolve_str(name));

            //Gestion pacientes
            String name2 = "objRemotoPacientes";
            href2 = GestionPacientesIntHelper.narrow(ncref.resolve_str(name2));
    
            //Estructura de registro para el Callback del Administrador
            POA rootPOA1 = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootPOA1.the_POAManager().activate();
            // instancia el servant
            AdmCllbckImpl cliente = new AdmCllbckImpl();
            // obtiene la referencia del rootpoa & activate el POAManager
            org.omg.CORBA.Object ref2 = rootPOA1.servant_to_reference(cliente);
            AdmCllbckInt href3 = AdmCllbckIntHelper.narrow(ref2);

            //Estructura de registro para e callback del Fap
            POA rootPOA2 = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootPOA2.the_POAManager().activate();
            // instancia el servant
            FapCllbckImpl fap = new FapCllbckImpl();
            // obtiene la referencia del rootpoa & activate el POAManager
            org.omg.CORBA.Object ref3 = rootPOA1.servant_to_reference(fap);
            FapCllbckInt href4 = FapCllbckIntHelper.narrow(ref3);

            MenuPrincipal(href1, href2, href3, href4);
            
        } catch (Exception e) {
            System.out.println("ERROR : " + e) ;
            e.printStackTrace(System.out);
        }
    }
    private static void MenuPrincipal(GestionPersonalInt ref, GestionPacientesInt ref2, 
                                      AdmCllbckInt refCllbckAdmin, FapCllbckInt refCllbckFap){
        int opcion = 0;
        do {
            System.out.println("==Menu Principal==");
            System.out.println("1. Abrir Sesion");
            System.out.println("2. Salir");
            System.out.println("========================");
            System.out.print("Ingrese su opcion: ");
            opcion = UtilidadesConsola.leerEntero();
            switch (opcion) {
                case 1:
                    Login(ref, ref2, refCllbckAdmin, refCllbckFap);
                    break;
                case 2:
                    System.out.println("Saliendo...");;
                    break;
                default:
                    System.out.println("Opcion incorrecta");
            }
        } while (opcion != 2);
    }
    private static void Login(GestionPersonalInt ref, GestionPacientesInt ref2, 
                            AdmCllbckInt refCllbckAdmin, FapCllbckInt refCllbckFap){
        try {
            String user="";
            String password="";
            int id=0;
            boolean login = false;
            System.out.println("==Iniciar Sesion==");
            System.out.println("Usuario: ");
            user = UtilidadesConsola.leerCadena();
            System.out.println("Clave: ");
            password = UtilidadesConsola.leerCadena();
            System.out.println("ID: ");
            id = UtilidadesConsola.leerEntero();
            credencialDTO varCredencial = new credencialDTO(user, password, id);
            login = ref.abrirSesion(varCredencial);
            if (login) {
                boolean resultado;
                personalDTOHolder objPersonal = new personalDTOHolder();
                //usuarioDTOHolder objUsuario = new usuarioDTOHolder();
                //res = ref.consultarUsuario(id, objUsuario);
                resultado = ref.consultarPersonal(id, objPersonal);
                if (resultado == true && objPersonal != null) {
                    switch (objPersonal.value.ocupacion) {
                        case "Admin":
                            ref.registrarCallback(refCllbckAdmin);
                            MenuAdmin(ref);
                            break;
                        case "SEC":
                            MenuSec(ref);
                            break;
                        case "PAF":
                            ref.registrarCallbackFap(refCllbckFap);
                            MenuPaf(ref, ref2);
                            break;
                    }
                }else{
                    MenuUser(varCredencial.id, ref2);
                }
            } else {
                System.out.println("El usuario " + user + " NO esta autorizado para ingresar al sistema.");
                System.out.println("***Verificar que el Usuario y Clave sean correctas***");
            }
        } catch (Exception e) {
            System.out.println("ERROR NO CONTROLADO");
        }
    }
    //Menu admin
    private static void MenuAdmin(GestionPersonalInt ref){
        int opcion = 0;
        do {
            System.out.println("==Menu Admin==");
            System.out.println("1. Registrar Personal   ");
            System.out.println("2. Consultar Personal   ");
            System.out.println("3. Salir");
            System.out.println("========================");
            System.out.print("Ingrese su opcion: ");
            opcion = UtilidadesConsola.leerEntero();
            switch (opcion) {
                case 1:
                    RegistrarPersonal(ref);
                    break;
                case 2:
                    ConsultarPersonal(ref);
                    break;
                case 3:
                    System.out.println("Salir...");
                    break;
                default:
                    System.out.println("Opcion incorrecta");
            }
        } while (opcion != 3);
    }
    //Metodos consumidos por el Admin
    private static void RegistrarPersonal(GestionPersonalInt ref){
        try {
            System.out.println("== Registrar Personal ==");
            String tipoID = "";
            int opc = 0;
            do {
                System.out.println("=Tipo de identificacion=");
                System.out.println("1. Cedula de Ciudadania(CC)   ");
                System.out.println("2. Tarjeta de Identidad(TI)   ");
                System.out.println("3. Pasaporte(PP)      ");
                System.out.println("========================");
                System.out.print("Ingrese su opcion: ");
                
                opc = UtilidadesConsola.leerEntero();
                
                switch (opc) {
                    case 1:
                        tipoID = "CC";
                        break;
                    case 2:
                        tipoID = "TI";
                        break;
                    case 3:
                        tipoID = "PP";
                        break;
                    default:
                        System.out.println("ADV: Ingrese una opcion valida");
                }
            } while (opc < 1 || opc > 3);
            int id = 0;
            do{
                System.out.println("Ingrese la Identificacion: ");
                id = UtilidadesConsola.leerEntero();
                if (id < 1) {
                    System.out.println("La identificacion debe ser mayot a cero (0)");
                }
            }while(id < 1);
            String nombre = "";
            do{
                System.out.println("Ingrese El nombre completo: ");
                nombre = UtilidadesConsola.leerCadena();
                if (nombre.length() < 2) {
                    System.out.println("El nombre debe contener al menos 2 caracteres");
                }
            }while(nombre.length() < 2);
            String ocupacion = "";
            do {
                System.out.println("== Ocupacion ==");
                System.out.println("1. Secretaria(SEC)   ");
                System.out.println("2. Profesional de Acondicionamiento Fisico (PAF)   ");   
                System.out.println("========================");
                System.out.print("Ingrese su opcion: ");             
                opc = UtilidadesConsola.leerEntero();
                
                switch (opc) {
                    case 1:
                        ocupacion = "SEC";
                        break;
                    case 2:
                        ocupacion = "PAF";
                        break;
                        
                    default:
                        System.out.println("Seleccione una opcion valida");
                }
            } while (opc < 1 || opc > 2);
            String usuario = "";
            do{
                System.out.println("Digite el usuario: ");
                usuario = UtilidadesConsola.leerCadena();
                if (usuario.length() < 8) {
                    System.out.println("El usuario debe contener al menos 8 caracteres!");
                }
            }while(usuario.length() < 2);
            String clave = "";
            do{
                System.out.println("Digite la clave: ");
                clave = UtilidadesConsola.leerCadena();
                if (clave.length() < 8) {
                    System.out.println("la clave debe contener al menos 8 caracteres");
                }
            }while(clave.length() < 8);

            personalDTO objPersonal = new personalDTO(tipoID, id, nombre, ocupacion, usuario, clave);
            BooleanHolder res = new BooleanHolder();
            ref.registrarPersonal(objPersonal, res);
            if (res.value == true) {
                System.out.println("***Personal Registrado Exitosamente***");
            } else {
                System.out.println("***ERROR: NO se pudo registrar el Personal***");
            }
        } catch (Exception e) {
            System.err.println("ERROR NO CONTROLADO");
        }
    }
    private static void ConsultarPersonal(GestionPersonalInt ref){
        System.out.println("==Consulta de Personal==");
        System.out.println("Ingrese la identificacion: ");
        int id = UtilidadesConsola.leerEntero();
        try {
            boolean resultado;
            personalDTOHolder objPersonal = new personalDTOHolder();
            resultado = ref.consultarPersonal(id, objPersonal);
            if (resultado) {
                System.out.println();
                System.out.println("==INI Resultado de la consulta==");
                System.out.println("Tipo ID: " + objPersonal.value.tipo_id);
                System.out.println("ID: " + objPersonal.value.id);
                System.out.println("Nombre Completo: " + objPersonal.value.nombreCompleto);
                System.out.println("Ocupacion: " + objPersonal.value.ocupacion);
                System.out.println("Usuario: " + objPersonal.value.usuario);
                System.out.println("==FIN Resultado de la consulta==");
                System.out.println();
            }else{
                System.out.println("El personal con id " + id + " no esta registrado en el sistema!");
            }
        } catch (Exception e) {
            System.err.println("El usuario con ID " + id + " no se encontro!");
        }
    }
    //Menu sercretaria
    private static void MenuSec(GestionPersonalInt ref){
        int opcion = 0;
        do {
            System.out.println("==Menu Secretaria==");
            System.out.println("1. Registrar Usuario");
            System.out.println("2. Consultar Usuario");
            System.out.println("3. Salir");
            System.out.println("========================");
            System.out.print("Ingrese su opcion: ");
            opcion = UtilidadesConsola.leerEntero();
            switch (opcion) {
                case 1:
                    RegistrarUsuario(ref);
                    break;
                case 2:
                    ConsultarUsuario(ref);
                    break;
                case 3:
                    System.out.println("Salir...");
                    break;
                default:
                    System.out.println("Opcion incorrecta");
            }
        } while (opcion != 3);
    }
    //Metodos consumidos por la secretaria
	private static void RegistrarUsuario(GestionPersonalInt ref){
		try{
			System.out.println("==== Registrar Usuario ====");
			
            System.out.println("Ingrese el nombre del paciente");
            String nombreUsuario = UtilidadesConsola.leerCadena();
            int id = 0;
            do{
                System.out.println("Ingrese la identificacion");
                id = UtilidadesConsola.leerEntero();
            }while(id < 1);
            String facultad="";
            int opcion = 0;
            do{
                System.out.println("===Facultad a la que pertenece===");
                System.out.println("|1. FACARTES");			
                System.out.println("|2. FACAGRO");
                System.out.println("|3. FSALUD");
                System.out.println("|4. FHUMANAS");
                System.out.println("|5. FCCEA");
                System.out.println("|6. FACNED");
                System.out.println("|7. FDERECHO");
                System.out.println("|8. FCIVIL");
                System.out.println("|9. FIET");
                System.out.println("============================");
                System.out.print("Ingrese su opcion: ");

                opcion = UtilidadesConsola.leerEntero();
            }while(opcion < 1 || opcion > 9);
            switch (opcion) {
                case 1:
                    facultad = "FACARTES";
                    break;
                case 2:
                    facultad = "FACAGRO";
                    break;
                case 3:
                    facultad = "FSALUD";
                    break;
                case 4:
                    facultad = "FHUMANAS";
                    break;
                case 5:
                    facultad = "FCCEA";
                    break;
                case 6:
                    facultad = "FACNED";
                    break;
                case 7:
                    facultad = "FDERECHO";
                    break;
                case 8:
                    facultad = "FCIVIL";
                    break;
                case 9:
                    facultad = "FIET";
                    break;
                default:
                    System.out.println("ERROR. La fulctad no esta registrada");
                    break;
            }
            do{
                System.out.println("=== Tipo de usuario ===");
                System.out.println("|1. Docente		      |");			
                System.out.println("|2. Administrativo    |");
                System.out.println("=======================");
                System.out.print("Ingrese su opcion: ");
                opcion = UtilidadesConsola.leerEntero();
            }while(opcion < 1 || opcion > 2);
            String tipoUsuario="";
            switch (opcion) {
                case 1:
                    tipoUsuario = "Docente";
                    break;
                case 2:
                    tipoUsuario = "Administrativo";
                    break;
            }
            
            Calendar fecha = new GregorianCalendar();
            int dia = fecha.get(Calendar.DAY_OF_MONTH);
            int mes = fecha.get(Calendar.MONTH) + 1;
            int anio = fecha.get(Calendar.YEAR);
            String fechaIngreso = dia+"/"+mes+"/"+anio;
            do{
                System.out.println("=== Patologia usuario ===");
                System.out.println("|1. Ingresar Patologia  |");			
                System.out.println("|2. Ninguna 	        |");
                System.out.println("=========================");
                System.out.print("Ingrese su opcion: ");
                opcion = UtilidadesConsola.leerEntero();
            }while(opcion < 1 || opcion > 2);
            String patologia="";
            switch (opcion) {
                case 1:
                    do{
                        patologia = UtilidadesConsola.leerCadena();
                    }while(patologia.length()<1);
                    break;
                case 2:
                    patologia = "Ninguna";
                    break;
            }
            String usuario="";
            do{
                System.out.println("Ingrese el usuario: ");
                usuario = UtilidadesConsola.leerCadena();
            }while(usuario.length() < 8);
            String clave="";
            do{
                System.out.println("Ingrese la contraseña: ");
                clave = UtilidadesConsola.leerCadena();
            }while(clave.length() < 8);

            usuarioDTO nuevoUsuario = new usuarioDTO(id, nombreUsuario, facultad, tipoUsuario, fechaIngreso, patologia, usuario, clave);
            BooleanHolder valor = new BooleanHolder();
            ref.registrarUsuario(nuevoUsuario, valor);//invocacion al metodo remoto
            if(valor.value)
                System.out.println("Registro realizado satisfactoriamente...");
            else
                System.out.println("no se pudo realizar el registro...");
			
		}
		catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	private static void ConsultarUsuario(GestionPersonalInt ref){
        System.out.println("==== Consultar Usuario ====");
		System.out.println("Ingrese la identificacion: ");
		int id = UtilidadesConsola.leerEntero();

		try {
            boolean resultado;
			usuarioDTOHolder objUsuario = new usuarioDTOHolder();
            resultado = ref.consultarUsuario(id,objUsuario);
			if (resultado) {
				System.out.println("Usario recuperado exitosamente!");
				System.out.println("=== INFO DEL USUARIO ===!");
				System.out.println("Nombre: " + objUsuario.value.nombreCompleto);
				System.out.println("ID: " + objUsuario.value.id);
				System.out.println("Tipo: " + objUsuario.value.tipo);
				System.out.println("Facultad: " + objUsuario.value.facultad);
				System.out.println("Usuario: " + objUsuario.value.usuario);
				System.out.println("Patologia: " + objUsuario.value.patologia);
				System.out.println("Fecha de Ingreso: " + objUsuario.value.fechaIngreso);
				System.out.println("==== FIN CONSULTA ====");
			} else {
				System.out.println("El Usuario con id " + id + " no esta registrado en el sistema");
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
    }
    //Menu PAF
    private static void MenuPaf(GestionPersonalInt ref, GestionPacientesInt ref2){
        int opcion = 0;
        do {
            System.out.println("==Menu PAF==");
            System.out.println("1. Valorar PAF");
            System.out.println("2. Programa Fisico");
            System.out.println("3. Registrar Asistencia");
            System.out.println("4. Salir");
            System.out.println("========================");
            System.out.print("Ingrese su opcion: ");
            opcion = UtilidadesConsola.leerEntero();
            switch (opcion) {
                case 1:
                    registrarValoracion(ref, ref2);
                    break;
                case 2:
                    registrarProgramaFisico(ref, ref2);
                    break;
                case 3:
                    registrarAsistencia(ref, ref2);
                    break;
                case 4:
                    System.out.println("Salir...");
                    break;
                default:
                    System.out.println("Opcion incorrecta");
            }
        } while (opcion != 4);
    }
    //Metodos consumidos por el PAF
    private static void registrarValoracion(GestionPersonalInt ref, GestionPacientesInt ref2){
        try {
			System.out.println("==== Valorar Paciente ====");
			int id=0;
			do{
				System.out.println("Ingrese la identificacion");
				id = UtilidadesConsola.leerEntero();
			}while(id < 1);
            usuarioDTOHolder varObjUsuario =  new usuarioDTOHolder();
			if(ref.consultarUsuario(id, varObjUsuario)){
                ValoracionDTOHolder varObjValoracion = new ValoracionDTOHolder();
                boolean res = ref2.consultarValoracion(id, varObjValoracion);
				if (!res) {
					Calendar fecha = new GregorianCalendar();
					int dia = fecha.get(Calendar.DAY_OF_MONTH);
					int mes = fecha.get(Calendar.MONTH) + 1;
					int anio = fecha.get(Calendar.YEAR);
					String fechaValoracion = dia+"/"+mes+"/"+anio;
					int fecCarReposo = 0;
					do{
						System.out.println("Frecuencia Cardiaca en reposo: ");
						fecCarReposo = UtilidadesConsola.leerEntero();
					}while(fecCarReposo < 1);
					int fecCarActiva = 0;
					do{
						System.out.println("Frecuencia Cardiaca Activa: ");
						fecCarActiva = UtilidadesConsola.leerEntero();
					}while(fecCarActiva < 1);
					int estatura = 0;
					do{
						System.out.println("Estatura(cm): ");
						estatura = UtilidadesConsola.leerEntero();
					}while(estatura < 1);
					int brazo = 0;
					do{
						System.out.println("Brazo(cm): ");
						brazo = UtilidadesConsola.leerEntero();
					}while(brazo < 1);
					int pierna = 0;
					do{
						System.out.println("Pierna(cm): ");
						pierna = UtilidadesConsola.leerEntero();
					}while(pierna < 1);
					int pecho = 0;
					do{
						System.out.println("Pecho(cm): ");
						pecho = UtilidadesConsola.leerEntero();
					}while(pecho < 1);
					int cintura = 0;
					do{
						System.out.println("Cintura(cm): ");
						cintura = UtilidadesConsola.leerEntero();
					}while(cintura < 1);
					String estado ="";
					ValoracionDTO objValoracion= new ValoracionDTO(id, fechaValoracion, fecCarReposo, fecCarActiva, estatura, brazo, pierna, pecho, cintura, estado);
					BooleanHolder valor = new BooleanHolder();
                    ref2.registrarValoracion(objValoracion, valor);//invocacion al metodo remoto
					if(valor.value)
						System.out.println("Registro realizado satisfactoriamente...");
					else
						System.out.println("no se pudo realizar el registro...");	
				}else{
					System.out.println("El paciente con id " + id + " ya tiene una valoracion.");	
				}
			}else{
			System.out.println("El paciente con id " + id + " no se encuentra registrado.");
			}
		} catch (Exception e) {
			System.out.println("ERROR: No controlado");
		}
	}
    private static void registrarProgramaFisico(GestionPersonalInt ref, GestionPacientesInt ref2){
        try {
			System.out.println("==== Programa Fisico ====");
			int id=0;
			do{
				System.out.println("Ingrese la identificacion: ");
				id = UtilidadesConsola.leerEntero();
			}while(id < 1);
            usuarioDTOHolder varObjUsuario = new usuarioDTOHolder();
			if(ref.consultarUsuario(id, varObjUsuario)){
                ProgramaFisicoDTOHolder varObjProgramaFisico = new ProgramaFisicoDTOHolder();
				if (!ref2.consultarProgramaFisico(id, varObjProgramaFisico)) {
                    ValoracionDTOHolder varObjValoracion = new ValoracionDTOHolder();
					if(ref2.consultarValoracion(id, varObjValoracion)){
						String fechaIni = "";
						do {
							System.out.println("==== Fecha de Inicio del programa ====");
							System.out.println("Formato (dd/mm/aa): ");
							fechaIni = UtilidadesConsola.leerCadena();
						} while (fechaIni.length() != 8);
						ProgramaFisicoDTO objProgSemana;
						ArrayList<ProgramaDTO> objProgramaSemanal = new ArrayList<ProgramaDTO>();
						for (int i = 0; i < 3; i++) {
							String nomEjercicio = "";
							int repeticiones = 0;
							int peso = 0;
							ArrayList<EjercicioDTO> tmplstEjercicio = new ArrayList<EjercicioDTO>();
							ProgramaDTO tmpPrograma;
							EjercicioDTO tmpEjecercicio;
							System.out.println("dia " + (i+1) + "de la semana.");
							for (int j = 0; j < 3; j++) {
								do {
									System.out.println("Nombre del ejercicio " + (j+1) + ":");
									nomEjercicio = UtilidadesConsola.leerCadena();
								} while (nomEjercicio.length() < 3);
								do {
									System.out.println("Repeticiones: ");
									repeticiones = UtilidadesConsola.leerEntero();
								} while (repeticiones < 1);
								do {
									System.out.println("Peso: ");
									peso = UtilidadesConsola.leerEntero();
								} while (peso < 3);
								tmpEjecercicio = new EjercicioDTO(nomEjercicio, repeticiones, peso);
								tmplstEjercicio.add(tmpEjecercicio);
							}
                            EjercicioDTO[] varObjEjercicios = new EjercicioDTO[3];
                            for (int j = 0; j < tmplstEjercicio.size(); j++) {
                                varObjEjercicios[j] = tmplstEjercicio.get(j);
                            }
							tmpPrograma = new ProgramaDTO((i+1), varObjEjercicios);
							objProgramaSemanal.add(tmpPrograma);
						}
                        ProgramaDTO[] varObjLstPrograma = new ProgramaDTO[3];
                        for (int k = 0; k < objProgramaSemanal.size(); k++) {
                            varObjLstPrograma[k] = objProgramaSemanal.get(k);
                        }
						objProgSemana = new ProgramaFisicoDTO(id, fechaIni, varObjLstPrograma);
            
						BooleanHolder resultado = new BooleanHolder();
                        ref2.registrarProgramaFisico(objProgSemana, resultado);
						if (resultado.value) {
							System.out.println("Se registro el programa con exito");
						} else {
							System.out.println("ERROR: No se pudo realizar el registro");
						}
					}else{
						System.out.println("El paciente con id " + id + " no ha sido valorado.");	
					}	
				}else{
					System.out.println("El paciente con id " + id + " Ya tiene un programa registrado");	
				}
			}else{
				System.out.println("El paciente con id " + id + " no se encuentra registrado.");
			}
		} catch (Exception e) {
			System.out.println("ERROR: La oprecion no se pudo completar. " + e.getMessage());
		}
    }
    private static void registrarAsistencia(GestionPersonalInt ref, GestionPacientesInt ref2){
        try {
			System.out.println("==== Registrar Asistencia ====");
			int id=0;
			do{
				System.out.println("Ingrese la identificacion: ");
				id = UtilidadesConsola.leerEntero();
			}while(id < 1);
            usuarioDTOHolder varObjUsuario = new usuarioDTOHolder();
			if(ref.consultarUsuario(id, varObjUsuario)){
                ValoracionDTOHolder varObjValoracion = new ValoracionDTOHolder();
				if(ref2.consultarValoracion(id, varObjValoracion)){
                    ProgramaFisicoDTOHolder varObjProgramaFisico =  new ProgramaFisicoDTOHolder();
					if (ref2.consultarProgramaFisico(id, varObjProgramaFisico)) {
						String fechaAsistencia = "";
						do {
							System.out.println("==== Fecha de asistencia ====");
							System.out.println("Formato (dd/mm/aa): ");
							fechaAsistencia = UtilidadesConsola.leerCadena();
						} while (fechaAsistencia.length() != 8);
						String observacion ="";
						int opcion=0;
						do
						{
							System.out.println("=== Observacion ===");
							System.out.println("|1. Ingresar excusa");			
							System.out.println("|2. No asiste");
							System.out.println("|3. Ninguna");
							System.out.println("============================");
							System.out.print("Ingrese su opcion: ");
							opcion = UtilidadesConsola.leerEntero();
						}while(opcion < 1 || opcion > 3);
						switch (opcion) {
							case 1:
								System.out.print("Ingrese la excusa: ");
								observacion = UtilidadesConsola.leerCadena();
								break;
							case 2:
								observacion = "No asiste";
								break;
							case 3:
								observacion = "Ninguna";
								break;
						}
						AsistenciaDTO objAsistencia = new AsistenciaDTO(id, fechaAsistencia, observacion);
						BooleanHolder resultado = new BooleanHolder();
                        ref2.registrarAsistencia(objAsistencia,resultado);
						IntHolder contador = new IntHolder();
                        ref2.contarFaltas(id, contador);
						if (resultado.value) {
							System.out.println("Se registro la asistencia con exito!");
							if (contador.value >= 3) {
								System.out.println("Se han exedido el numero de faltas.");
								System.out.println("El usuario con id " + id + " tendra que volver a rezalizar valoracion,\ny elaborar el programa fisico.");
							}
						} else {
							System.out.println("ERROR :No se pudo registrar la asistencia.");
						}
					}else{
						System.out.println("El paciente con id " + id + " aun no tiene registrado el programa fisico!");
					}
				}else{
					System.out.println("El paciente con id " + id + " aun no ha sido valorado!");
				}
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
    }
    //Menu Ususario
    private static void MenuUser(int id, GestionPacientesInt ref){
		int opcion = 0;
		do{
			System.out.println("===== Menu Usuario =====");
			System.out.println("|1. Consultar Valoracion");
            System.out.println("|2. Consultar Programa");            			
			System.out.println("|3. Consultar asistencia");
			System.out.println("|4. Salir");
			System.out.println("========================");
			System.out.println("Ingrese su opcion: ");
			opcion = UtilidadesConsola.leerEntero();
			switch(opcion)
			{
				case 1:
                    ConsultarValoracion(id, ref);
                    break;
				case 2:
                    ConsultarPrograma(id, ref);
                    break;	
				case 3:
                    ConsultarAsistencia(id, ref);
                    break;
                case 4:
                    System.out.println("Saliendo...");
                    break;
				default:
						System.out.println("Opcion incorrecta");
			}
		}while(opcion != 4);
	}
    //Metodos que consume el usaurio-paciente
    private static void ConsultarValoracion(int id, GestionPacientesInt ref){
        try {
			System.out.println("==== Consultar Valoracion ====");
			System.out.println("==== Valoracion del paciente " +  id + " ====");
			System.out.println("==== Resultados de consulta ====");
			ValoracionDTOHolder varObjValoracion = new ValoracionDTOHolder(); 
            boolean resultado = ref.consultarValoracion(id, varObjValoracion);
			if (resultado) {
				System.out.println("Fecha de valoracion: " + varObjValoracion.value.fechaValoracion);
				System.out.println("Frecuencia cardiaca en reposo: " + varObjValoracion.value.fecCardiacaReposo);
				System.out.println("Frecuencia cardiaca activa: " + varObjValoracion.value.fecCardiacaActiva);
				System.out.println("Estatura: " + varObjValoracion.value.estatura + " cm.");
				System.out.println("Medida del brazo: " + varObjValoracion.value.brazo + " cm.");
				System.out.println("Medida de la pierna: " + varObjValoracion.value.pierna + " cm.");
				System.out.println("Medida del pecho: " + varObjValoracion.value.pecho + " cm.");
				System.out.println("Medida de la cintura: " + varObjValoracion.value.cintura + " cm.");
				System.out.println("Estado: " + varObjValoracion.value.estado);
			}else{
				System.out.println("No hay datos para su consulta!");
			}
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
    }
    private static void ConsultarPrograma(int id, GestionPacientesInt ref){
        try {
			System.out.println("==== Consultar Programa Fisico ====");
			System.out.println("==== Programa del paciente " +  id + " ====");
			System.out.println("==== Resultados de consulta ====");
			ProgramaFisicoDTOHolder varObjProgramaFisico = new ProgramaFisicoDTOHolder(); 
            boolean resultado = ref.consultarProgramaFisico(id, varObjProgramaFisico);
			if (resultado) {
				System.out.println("Fecha de inicio: " + varObjProgramaFisico.value.fechaInicio);
                System.out.println("====Programa de la semana====");
                for (int i = 0; i < varObjProgramaFisico.value.listaProgramaSemana.length; i++) {
                    System.out.println("----------------------");
                    System.out.println("dia de la semana: " + varObjProgramaFisico.value.listaProgramaSemana[i].dia);
                    for (int j = 0; j < varObjProgramaFisico.value.listaProgramaSemana[i].listaEjercicios.length; j++) {
                        System.out.println("Nombre del ejercicio: " + varObjProgramaFisico.value.listaProgramaSemana[i].listaEjercicios[j].nombreEjercicio);
                        System.out.println("Repeticiones a realizar: " + varObjProgramaFisico.value.listaProgramaSemana[i].listaEjercicios[j].repeticiones);
                        System.out.println("Peso: " + varObjProgramaFisico.value.listaProgramaSemana[i].listaEjercicios[j].peso + " Kg");
                        System.out.println("----------------------");
                    }
                }
			}else{
				System.out.println("No hay datos para su consulta!");
			}
            System.out.println("========================");
		} catch (Exception e) {
			System.out.println("ERROR:" + e.getMessage());
        }
    }  
    private static void ConsultarAsistencia(int id, GestionPacientesInt ref){
        try {	
			System.out.println("==== Consultar Asistencia ====");
			System.out.println("=== Asistencia del paciente con id " + id + " ===");
			System.out.println("==== Resultados de consulta ====");
            ValoracionDTOHolder varObjValoracion = new ValoracionDTOHolder();
			if (ref.consultarValoracion(id, varObjValoracion)) {
                ProgramaFisicoDTOHolder varObjProgramaFisico = new ProgramaFisicoDTOHolder();
				if (ref.consultarProgramaFisico(id, varObjProgramaFisico)) {
                    ArrayAsistenciaHolder lstAsistencia = new ArrayAsistenciaHolder();
					boolean resultado = ref.consultarAsistencia(id, lstAsistencia);
					if (resultado) {
						for (int i = 0; i < lstAsistencia.value.length; i++) {
							System.out.println("Registro numero: " + (i+1));
							System.out.println("Fecha de Asistencia: " + lstAsistencia.value[i].fechaAsistencia);
							System.out.println("Observacion: " + lstAsistencia.value[i].observacion);
							System.out.println("===================================");
							System.out.println("");
						}
					}else{
						System.out.println("No posee registro de asistencia!");
					}		
				}else{
					System.out.println("Para acceder al registro el PAF debe primero elaborar su programa!");
				}
			}else{
				System.out.println("Para acceder al registro debe ser valorado primero!");
			}
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
    }
}
