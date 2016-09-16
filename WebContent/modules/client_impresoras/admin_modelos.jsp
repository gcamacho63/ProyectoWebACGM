<!-----------  Importaciones -------------------->
	<%@page import="model.ModeloImpresora" %>
	<%@page import="controller.Impresoras.ModelosServlet" %>
<!------------ Definicion de variables---------->
<%! 

    //Variables generales
	private String mensaje=null;
	private String tipoMensaje=null;
	private ModeloImpresora[] listadoDeObjetos=null; 
	private ModeloImpresora[] editarArray=null;
	private ModeloImpresora objetoEditar=null;
	private String controlador="ModelosServlet";
	private String vacio="";
	
	//Variables de preparacion del formulario
	private ModeloImpresora[] grupos=null;
	
	//Campos del Formulario
	private String nombre_modelo="";
	private String descripcion="";
	private int idElemento=0;
	private String archivo;
	private String servlet;
	private int grupoActual;
	//
	private String titulo="";
%>

<!-----------  Asignacion  --------------------->
<%
//Inicializa los valores del formulario
ModelosServlet.preparaPagina(request, response);
//Prepacion del formulario
HttpSession sesion = request.getSession();
//grupos= (ModeloImpresora[])sesion.getAttribute("gruposTodos");

//Inicializa el objeto a editar
editarArray=(ModeloImpresora[])request.getSession().getAttribute("editarArray");
request.getSession().removeAttribute("editarArray");
if(editarArray!=null)
{
	objetoEditar= editarArray[0];
	
	nombre_modelo=objetoEditar.getNombre();
	descripcion=objetoEditar.getDescripcion();
	idElemento=objetoEditar.getId_modelo();
	//grupoActual=objetoEditar.getGrupo();
	titulo="Editar Modelo";
}
else
{
	nombre_modelo="";
	descripcion="";
	archivo= "";
	servlet= "";
	idElemento=0;
	grupoActual=0;
	titulo="Crear Modelo";
}

//Listado de elementos existentes
listadoDeObjetos=(ModeloImpresora[])request.getSession().getAttribute("listadoDeObjetos");
request.getSession().removeAttribute("listadoDeObjetos");

//Muestra alerta de retorno del Servlet
mensaje		=request.getParameter("mensaje");
tipoMensaje =request.getParameter("tipoMensaje");
if(mensaje!=null)
{
	%>
	<div class="alert alert-<%=tipoMensaje%>" id="divAlert">
	   <%=mensaje %>
	   <span class="tools pull-right">
		<a href="#" onclick="document.getElementById('divAlert').style.display='none';"><i class="fa fa-times"></i></a>
	   </span>
	</div>
<%
}
%>

<!-- Scripts -->
<script>
	function asignarAccion(action)
	{
		$("#accion").val(action);
	}
</script>

<!--------------------  Formulario de inicio ---------------------->
<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">
             <center><strong>Crear Modelos</strong></center>
            </div>
            <div class="panel-body">
			    <form name="forma" method="post" action="<%=controlador %>" class="form-horizontal">
			     <div class="form-group">
			      <label class="col-lg-2 col-sm-2 control-label" for="asunto">Nombre Modelo</label>
			      <div class="col-lg-8">
			       <input type="text"   required="required" id="nombre_modelo"   name="nombre_modelo"   value="<%=nombre_modelo%>" class="form-control">
			      </div>
			     </div>
			    		     
			     <div class="form-group">
			      <label class="col-lg-2 col-sm-2 control-label" for="asunto">Descripcion</label>
			      <div class="col-lg-8">
			       <textarea class="form-control" required="required" name="descripcion" id="descripcion" ><%=descripcion %></textarea>
			      </div>
			     </div>
			     	    
			     
			     <div class="text-center">
			     	  <% 
			     	  if(editarArray==null)
			     	  {
			     	  %>
			     	  	<input type="submit" class="btn btn-success" value="Crear" name="crear" onclick="asignarAccion('crear');" />
			     	  <%
			     	  }
			     	  else
			     	  {
			     	  %>
			     	  	<input type="submit" class="btn btn-primary" value="Editar" name="actualizar" onclick="asignarAccion('actualizar');" />
				      <%  
			     	  }
			     	  %>     			       		            
			     </div>
			     
			     
			      <input type="hidden" id="accion" name="accion" />
			     <input type="hidden" name="id_elemento" id="id_elemento" value="<%=idElemento %>"><!--Aqui se almacena el ID del elemento que se este modificando-->
			    </form>
			  </div><!-- Panel body -->			 
         </div>
    </div>
</div>    			    
			<!------------------- LISTADO ----------------->
<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">
               <center><strong>Administrar Modelos</strong></center>
            </div>           
            <div class="panel-body">			   		    
			    <div>
			     <table class="table table-hover table-bordered">
			       <tr>
				        <td class="text-center"><b>Nombre</b></td>
				        <td class="text-center"><b>Descripcion</b></td> 
				        <td class="text-center"><b>Estado</b></td>
				        <td class="text-center"><b>Acciones</b></td>
			       </tr>			
		           <%
		           if(listadoDeObjetos!=null)
		           {
		        	 for(int i=0;i < listadoDeObjetos.length ;i++ )
		        	 {
		        		 int idElemento1=listadoDeObjetos[i].getId_modelo();	        		 
		        	%>
		        	<tr>
		               <td class="text-center"><%=listadoDeObjetos[i].getNombre() %></td>
		               <td class="text-center"><%=listadoDeObjetos[i].getDescripcion() %></td>
		               
		            <%
		               String estado="";
		               String color="";
		               if(listadoDeObjetos[i].getEstado()==0)
		               {
		            	   estado="Activo";
		            	   color="green";
		               }
		               else
		               {
		            	   estado="Inactivo";
		            	   color="red";
		               }
		               %>
		               <td class="text-center"><font color="<%=color %>" ><%=estado %></font></td>            
		               <td class="text-center">
		               	<a href="<%=controlador %>?accion=editar&idElemento=<%=idElemento1 %>"><input type="button" value="Editar" class="btn btn-info btn-xs" /></a>
		               <%
		               if(listadoDeObjetos[i].getEstado()==0)
		               {
					   %>
					   	<a href="<%=controlador %>?accion=desactivar&idElemento=<%=idElemento1 %>"><input type="button"  name="desactivar" id="desactivar" value="Desactivar" class="btn btn-danger btn-xs" /></a>
					   <%
		               }
		               else
		               {
		            	%>
		            	<a href="<%=controlador %>?accion=activar&idElemento=<%=idElemento1 %>"><input type="button" name="activar" id="activar" value="Activar" class="btn btn-success btn-xs" /></a>
					    <%
		               }
		               %>          		                                     					                 				               	
		               </td>
		           </tr>
		        	<% 
		        	 }
		           }
		           %>		          					    
			     </table>	
			    </div>
			 </div><!-- Panel body -->			 
         </div>
    </div>
</div>       