package controller.Usuarios;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Area;
import model.ConexionDB;
import model.Modulo;
import model.ModulosGrupos;

/**
 * Servlet implementation class AreasServlet
 */
@WebServlet("/AreasServlet")
public class AreasServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	private String tabla="user_areas";
	private String campoId="id_area";
	private String pagina="index.jsp?sec=client_usuarios&mod=admin_areas.jsp";
	private String mensaje = "";
	private String tipoMensaje = "";
	private String alerta="";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		HttpSession sesion = request.getSession();								
		ConexionDB.conectarDB();
		String action = request.getParameter("accion");
		
		if(action!=null)
		{
			String idElemento =  request.getParameter("idElemento");
			if(action.equals("editar"))
			{
				Area[] editarArray = Area.Consultar(campoId+" = '"+idElemento+"'");
				sesion.setAttribute("editarArray", editarArray);
				alerta="";
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
		
		String nombre_area = request.getParameter("nombre");
		String descripcion = request.getParameter("descripcion");		
	
		//------------------------		
		String action = request.getParameter("accion");
				
		ConexionDB.conectarDB();
		if(action.equals("crear"))
		{		
			if(ConexionDB.Insert(tabla, "nombre_area,descripcion","'"+nombre_area+"','"+descripcion+"'"))
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
			if(ConexionDB.Update(tabla, "nombre_area= '"+nombre_area+"',descripcion ='"+descripcion+"'" ,campoId+" = '"+idElemento+"'"))
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
	public static void inicializaListado(HttpServletRequest request)
	{
		HttpSession sesion = request.getSession();
		Area[] listadoDeObjetos= Area.Consultar("1 ORDER BY estado ASC");
		if(listadoDeObjetos!=null)
		{
			sesion.setAttribute("listadoDeObjetos", listadoDeObjetos);
		}
	}
	public static void preparaPagina(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		inicializaListado(request);
	}
}
