<%@page import="model.Modulo" %>
<%@page import="model.ModulosGrupos" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%! 
ModulosGrupos[] grupos;
Modulo[] modulos;
%>
<%
    grupos= (ModulosGrupos[]) request.getSession().getAttribute("grupos");
	modulos= (Modulo[]) request.getSession().getAttribute("modulos");
%>
<div class="sidebar-nav navbar-collapse">
    <!--Menu-->
    <ul class="nav" id="side-menu">                                   
        <li>
            <a href="index.jsp"><i class="fa fa-home"></i> Inicio</a>
        </li>
        <%
        if(grupos!=null&&modulos!=null)
        {
            int num=grupos.length;
            
        	for(int i=0;i<num;i++)
            {
        %>
           	<li>
               <a href="#"><i class="fa-fw"></i><%=grupos[i].getNombre() %><span class="fa arrow"></span></a>
               <ul class="nav nav-second-level">
               <%
		        if(grupos!=null)
		        {		            
		        	int conMod=modulos.length;
		        	for(int u=0;u<conMod;u++)
		            {
		        		if(grupos[i].getId_grupo()==modulos[u].getGrupo())
		        		{
	        			%>
   	                	<li>
		                    <!-- <a href="<%=modulos[u].getControlador() %>"><%=modulos[u].getNombre() %></a> -->
		                    <a href="index.jsp?sec=<%=modulos[u].getCarpeta() %>&mod=<%=modulos[u].getArchivo() %>"><%=modulos[u].getNombre() %></a>
		                </li>
	                   	<%        					        			
		        		}		        
		            }
		        }
		        %>  
               </ul>
            </li> 
        <%
            }
        }
        %>                  
    </ul>
     <!--Menu-->
</div>
