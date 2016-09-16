package controller.Incidencias;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.ConexionDB;
import model.Estado;

/**
 * Servlet implementation class EstadosServlet
 */
@WebServlet("/EstadosServlet")
public class EstadosServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L; 
	
	private  String pagina="index.jsp?sec=client_incidencias&mod=admin_estados.jsp";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EstadosServlet() 
    {
        super();
        // TODO Auto-generated constructor stub
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{		
		HttpSession sesion = request.getSession();
		
		String mensaje = null;
		String tipoMensaje = null;
		String alerta="";
		
		String tabla="inc_estados";
		String campoId="id_estado";
		
		ConexionDB.conectarDB();
		String action = request.getParameter("accion");
				
		if(action!=null)
		{
			String idElemento =  request.getParameter("idTipificacion");
			if(action.equals("editar"))
			{
				Estado[] editarArray = Estado.ConsultarEstados(campoId+" = '"+idElemento+"'");
				sesion.setAttribute("editarArray", editarArray);
			}
			else if(action.equals("activar"))
			{
				if(ConexionDB.Update(tabla, "estado=0", campoId+" = '"+idElemento+"'"))
				{
					mensaje="Registro Activado";
					tipoMensaje="success";
				}
				else
				{
					mensaje="Error en la operacion";
					tipoMensaje="danger";
				}
				alerta="&mensaje="+mensaje+"&tipoMensaje="+tipoMensaje;
			}
			else if(action.equals("desactivar"))
			{
				if(ConexionDB.Update(tabla, "estado=1", campoId+" = '"+idElemento+"'"))
				{
					mensaje="Registro Desactivado";
					tipoMensaje="success";
				}
				else
				{
					mensaje="Error en la operacion";
					tipoMensaje="danger";
				}
				alerta="&mensaje="+mensaje+"&tipoMensaje="+tipoMensaje;
			}	
			inicializaListado(request);			
			response.sendRedirect(pagina+alerta);			
		}				
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//Campos recibidos del formulario
		String nombre = request.getParameter("nombre");
		String descripcion = request.getParameter("descripcion");
		//------------------------
		
		String action = request.getParameter("accion");
		String pagina="index.jsp?sec=client_incidencias&mod=admin_estados.jsp";
		String tabla="inc_estados";
		String campoId="id_estado";
		
		ConexionDB.conectarDB();
		String mensaje="";
		String tipoMensaje="";
		if(action.equals("crear"))
		{		
			if(ConexionDB.Insert(tabla, "nombre_estado,descripcion","'"+nombre+"','"+descripcion+"'"))
			{
				mensaje="Registro guardado";
				tipoMensaje="success";
			}
			else
			{
				mensaje="Error en la operacion";
				tipoMensaje="danger";
			}			
		}	
		else if(action.equals("actualizar"))
		{
			String idElemento =  request.getParameter("id_elemento");
			if(ConexionDB.Update(tabla, "nombre_estado= '"+nombre+"',descripcion ='"+descripcion+"'",campoId+" = '"+idElemento+"'"))
			{
				mensaje="Registro editado";
				tipoMensaje="success";
			}
			else
			{
				mensaje="Error en la operacion";
				tipoMensaje="danger";
			}
		}
		inicializaListado(request);
		response.sendRedirect(pagina+"&mensaje="+mensaje+"&tipoMensaje="+tipoMensaje);
	}
	
	//Metodo que inicializa ellistado de objetos para el JSP
	public static void inicializaListado(HttpServletRequest request)
	{
		HttpSession sesion = request.getSession();
		Estado[] listadoDeObjetos= Estado.ConsultarEstados("1 ORDER BY estado,id_estado DESC");
		if(listadoDeObjetos==null)
		{
			String vacio="vacio";
			sesion.setAttribute("vacio", vacio);
		}
		else
		{
			sesion.setAttribute("listadoDeObjetos", listadoDeObjetos);
		}
	}
	public static void preparaPagina(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		inicializaListado(request);
	}
}