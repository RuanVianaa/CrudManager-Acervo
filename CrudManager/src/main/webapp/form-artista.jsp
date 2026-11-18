<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
	<%@include file="base-head.jsp"%>
	<title>CRUD Manager - Artista</title>
</head>
<body>
	<%@include file="nav-menu.jsp"%>

	<div id="container" class="container-fluid">
		<h3 class="page-header">${not empty artista ? 'Atualizar' : 'Adicionar'} Artista</h3>

		<form action="${pageContext.request.contextPath}/artista/${action}" method="POST">

			<input type="hidden" value="${artista.getId()}" name="artistaId">

			<div class="row">
				<div class="form-group col-md-6">
					<label for="nome">Nome</label>
					<input type="text" class="form-control" id="nome" name="nome"
						   autofocus="autofocus" placeholder="Nome do artista" maxlength="150"
						   required
						   oninvalid="this.setCustomValidity('Por favor, informe o nome do artista.')"
						   oninput="setCustomValidity('')"
						   value="<c:out value='${artista.getNome()}' />" />
				</div>

				<div class="form-group col-md-3">
					<label for="dataNascimento">Data de nascimento</label>
					<input type="date" class="form-control" id="dataNascimento" name="dataNascimento"
						   required
						   oninvalid="this.setCustomValidity('Por favor, informe a data de nascimento.')"
						   oninput="setCustomValidity('')"
						   value="${artista.getDataNascimento()}" />
				</div>

				<div class="form-group col-md-3">
					<label for="pais">País de origem</label>
					<select id="pais" class="form-control" name="pais"
							required
							oninvalid="this.setCustomValidity('Por favor, escolha o país.')"
							oninput="setCustomValidity('')">

						<option value="" ${not empty artista ? '' : 'selected'}>Selecione um país</option>

						<c:forEach var="p" items="${paises}">
							<option value="${p}" ${artista.getPais() == p ? 'selected' : ''}>${p}</option>
						</c:forEach>
					</select>
				</div>
			</div>

			<div class="row">
				<div class="form-group col-md-6">
					<label for="user">Curador (usuário responsável)</label>
					<select id="user" class="form-control" name="user"
							required
							oninvalid="this.setCustomValidity('Por favor, informe o curador.')"
							oninput="setCustomValidity('')">

						<option value="" ${not empty artista ? '' : 'selected'}>Selecione um usuário</option>

						<c:forEach var="user" items="${users}">
							<option value="${user.getId()}"
								${artista.getUser().getId() == user.getId() ? 'selected' : ''}>
								<c:out value="${user.getName()}" />
							</option>
						</c:forEach>
					</select>
				</div>

				<div class="form-group col-md-6">
					<label>Carreira</label>
					<div class="checkbox">
						<label>
							<input type="checkbox" name="ativo"
								${empty artista or artista.isAtivo() ? 'checked' : ''}>
							Artista em atividade
						</label>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="form-group col-md-12">
					<label for="biografia">Biografia</label>
					<textarea class="form-control" id="biografia" name="biografia" rows="4"
							  placeholder="Conte um pouco sobre o artista"><c:out value="${artista.getBiografia()}" /></textarea>
				</div>
			</div>

			<hr />
			<div id="actions" class="row pull-right">
				<div class="col-md-12">
					<a href="${pageContext.request.contextPath}/artistas" class="btn btn-default">Cancelar</a>

					<button type="submit" class="btn btn-primary">${not empty artista ? 'Atualizar' : 'Cadastrar'} Artista</button>
				</div>
			</div>
		</form>
	</div>

</body>
</html>