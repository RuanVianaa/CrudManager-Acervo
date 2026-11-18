<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
	<%@include file="base-head.jsp"%>
	<title>CRUD Manager - Álbuns</title>
</head>
<body>
	<%@include file="modal.html"%>
	<%@include file="nav-menu.jsp"%>

	<div id="container" class="container-fluid">
		<div id="alert" style="${not empty message ? 'display: block;' : 'display: none;'}" class="alert alert-dismissable ${alertType eq 1 ? 'alert-success' : 'alert-danger'}">
		  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
		  ${message}
		</div>

		<div id="top" class="row">
			<div class="col-md-9">
				<h3>Álbuns</h3>
			</div>

			<div class="col-md-3">
				<a href="${pageContext.request.contextPath}/album/form" class="btn btn-danger pull-right h2"><span class="glyphicon glyphicon-plus"></span>&nbsp;Adicionar Álbum</a>
			</div>
		</div>

		<hr />

		<div id="list" class="row">
			<div class="table-responsive col-md-12">
				<table class="table table-striped table-hover">
					<thead>
						<tr>
							<th>Título</th>
							<th>Artista</th>
							<th>Lançamento</th>
							<th>Faixas</th>
							<th>Formato</th>
							<th>Explícito</th>
							<th>Editar</th>
							<th>Excluir</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="alb" items="${albuns}">
							<tr>
								<td><c:out value="${alb.getTitulo()}" /></td>
								<td><c:out value="${alb.getArtista().getNome()}" /></td>
								<td><fmt:formatDate value="${alb.getDataLancamento()}" pattern="dd/MM/yyyy" /></td>
								<td>${alb.getFaixas()}</td>
								<td>${alb.getFormato()}</td>
								<td>
									<c:choose>
										<c:when test="${alb.isExplicito()}"><span class="label label-danger">Sim</span></c:when>
										<c:otherwise><span class="label label-default">Não</span></c:otherwise>
									</c:choose>
								</td>

								<td class="actions">
									<a class="btn btn-info btn-xs"
									   href="${pageContext.request.contextPath}/album/update?albumId=${alb.getId()}">
									   <span class="glyphicon glyphicon-edit"></span>
									</a>
								</td>

								<td class="actions">
									<a class="btn btn-danger btn-xs modal-remove"
									   album-id="${alb.getId()}"
									   album-titulo="<c:out value='${alb.getTitulo()}' />" data-toggle="modal"
									   data-target="#delete-modal" href="#"><span
									   class="glyphicon glyphicon-trash"></span></a>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>

		<div id="bottom" class="row">
			<div class="col-md-12">
				<c:set var="paginationUrl" value="${pageContext.request.contextPath}/albuns" />
				<%@include file="pagination.jsp"%>
			</div>
		</div>
	</div>

	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			setTimeout(function() {
				$("#alert").slideUp(500);
			}, 3000);

			$(".modal-remove").click(function() {
				var titulo = $(this).attr('album-titulo');
				var id = $(this).attr('album-id');
				$(".modal-body #hiddenValue").text("o álbum '" + titulo + "'");
				$("#id").attr("value", id);
				$("#form").attr("action", "album/delete");
			})
		});
	</script>
</body>
</html>