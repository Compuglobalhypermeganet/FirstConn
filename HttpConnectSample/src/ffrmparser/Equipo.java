package ffrmparser;

import java.util.Vector;

public class Equipo {

	  private Vector<String> dni, nombre, situacion, amonestaciones, goles;
	  private String nombre_e, tipo;
	  private int golesPropiaPuerta;

	public void consolaEquipo(){
	  System.out.println("Nombre: "+getNombre());
	  System.out.println("Tamaño: "+this.getSize());
	  System.out.println("Tipo: "+getTipo());

	    for (int i=0;i<this.getSize();i++)
	    System.out.println(getNombreJugador(i)+" "+getSituacionJugador(i)+" "+getGol(i)+" "+getAmonestacion(i));
	}
	  public String getNombre() {
	    return this.nombre_e;
	  }

	  public int getSize() {
	    return this.dni.size();
	  }

	  public String getTipo() {
	    return this.tipo;
	  }

	  public int getPP(){
	    return this.golesPropiaPuerta;
	  }

	  public void setPP(int golesPP){
	    this.golesPropiaPuerta = golesPP;
	  }

	  public void setJugador(String dniN, String nombreN, String situacionN, String gol, String amon) {
	    if (!estaYa(dniN)){
	      dni.addElement(dniN);
	      nombre.addElement(nombreN);
	      situacion.addElement(situacionN);
	      amonestaciones.addElement(amon);
	      goles.addElement(gol);
	    }
	  }

	  //evitamos que se metan duplicados en la tabla de jugadores
	  public boolean estaYa(String dni){
		  int i = 0;
		  for (i = 0; i < getSize(); i++)
			  if (getDniJugador(i).startsWith(dni))
				  return true;

		  return false;

	  }

	  public String getDniJugador(int indice) {
	    return (String) dni.elementAt(indice);
	  }

	  public String getNombreJugador(int indice) {
	    return (String) nombre.elementAt(indice);
	  }

	  public boolean getSituacionJugador(int indice) {
	    String aux = (String) situacion.elementAt(indice);
	    if (aux.startsWith("S"))
	    	return true;
	    return false;
	  }
	  public String getConvocadoNo(int indice){
	        return ((String) situacion.elementAt(indice));
	  }

	  public void setSituacion(int jugador, String estado) {
	    situacion.setElementAt(estado, jugador);
	  }

	  public void setAmonestacion(int jugador, String amonesta) {
	    amonestaciones.setElementAt(amonesta, jugador);
	  }

	  public void setGol(int jugador, String cantidad) {
	    goles.setElementAt(cantidad, jugador);
	  }

	  public String getGol(int indice) {
		    return (String) goles.elementAt(indice);
		  }

	  public void setNombre(String nombreE) {
		    nombre_e = nombreE;
		  }

	  public void setTipo(String tipoE) {
		    tipo = tipoE;
		  }

	  public String getAmonestacion(int indice) {
	    return (String) amonestaciones.elementAt(indice);
	  }

	  public boolean convocatoriaRellenada(){
		  int i = 0;
		  for (i = 0; i < getSize(); i++)
			  if (((String)situacion.elementAt(i)).startsWith("S"))
				  return true;

		  return false;
	  }
	}
	
