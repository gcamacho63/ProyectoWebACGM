package controller.Incidencias;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.ConexionDB;
import model.Tipificacion;

/**
 * Servlet implementation class TipificaServlet
 */
@WebServlet("/TipificaServlet")
public class TipificaServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HttpSession sesion = request.getSession();
		String mensaje = null;
		String tipoMensaje = null;		
		ConexionDB.conectarDB();
		String action = request.getParameter("accion");
		if(action!=null)
		{
			String idTipificacion =  request.getParameter("idTipificacion");
			if(action.equals("editar"))
			{
				Tipificacion[] tipEditar = Tipificacion.ConsultarTipificaciones("id_tipificacion = '"+idTipificacion+"'");
				sesion.setAttribute("tipEditar", tipEditar);
				response.sendRedirect("index.jsp?sec=client_incidencias&mod=admin_tipificaciones.jsp");
			}
			else if(action.equals("activar"))
			{
				if(ConexionDB.Update("inc_tipificacion", "estado=0", "id_tipificacion = '"+idTipificacion+"'"))
				{
					mensaje="Registro Activado";
					tipoMensaje="success";
				}
				else
				{
					mensaje="Error en la operacion";
					tipoMensaje="alert";
				}
			}
			else if(action.equals("desactivar"))
			{
				if(ConexionDB.Update("inc_tipificacion", "estado=1", "id_tipificacion = '"+idTipificacion+"'"))
				{
					mensaje="Registro Desactivado";
					tipoMensaje="success";
				}
				else
				{
					mensaje="Error en la operacion";
					tipoMensaje="alert";
				}				
			}	
			Tipificacion[] tip=Tipificacion.ConsultarTipificaciones("1 ORDER BY estado,id_tipificacion DESC");
			sesion.setAttribute("tip", tip);
			if(!action.equals("editar"))
			{
				response.sendRedirect("index.jsp?sec=client_incidencias&mod=admin_tipificaciones.jsp&mensaje="+mensaje+"&tipoMensaje="+tipoMensaje);
			}			
		}	
		else
		{
			Tipificacion[] tip=Tipificacion.ConsultarTipificaciones("1 ORDER BY estado,id_tipificacion DESC");
			sesion.setAttribute("tip", tip);	             
			response.sendRedirect("index.jsp?sec=client_incidencias&mod=admin_tipificaciones.jsp");
		}	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HttpSession sesion = request.getSession();
		String nombre = request.getParameter("nombre");
		String descripcion = request.getParameter("descripcion");
		String action = request.getParameter("accion");
		ConexionDB.conectarDB();
		String mensaje="";
		String tipoMensaje="";
		if(action.equals("crear"))
		{		
			if(ConexionDB.Insert("inc_tipificacion", "nombre_tipificacion,descripcion","'"+nombre+"','"+descripcion+"'"))
			{
				mensaje="Registro guardado";
				tipoMensaje="success";
			}
			else
			{
				mensaje="Error en la operacion";
				tipoMensaje="alert";
			}			
		}	
		else if(action.equals("actualizar"))
		{
			String idTipificacion =  request.getParameter("id_elemento");
			if(ConexionDB.Update("inc_tipificacion", "nombre_tipificacion= '"+nombre+"',descripcion ='"+descripcion+"'","id_tipificacion = '"+idTipificacion+"'"))
			{
				mensaje="Registro editado";
				tipoMensaje="success";
			}
			else
			{
				mensaje="Error en la operacion";
				tipoMensaje="alert";
			}
		}
		Tipificacion[] tip=Tipificacion.ConsultarTipificaciones("1 ORDER BY estado,id_tipificacion DESC");
		sesion.setAttribute("tip", tip);
		response.sendRedirect("index.jsp?sec=client_incidencias&mod=admin_tipificaciones.jsp&mensaje="+mensaje+"&tipoMensaje="+tipoMensaje);
	}
}
