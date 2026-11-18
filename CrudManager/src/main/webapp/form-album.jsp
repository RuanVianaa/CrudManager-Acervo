<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
	<%@include file="base-head.jsp"%>
	<title>CRUD Manager - Álbum</title>
</head>
<body>
	<%@include file="nav-menu.jsp"%>

	<div id="container" class="container-fluid">
		<h3 class="page-header">${not empty album ? 'Atualizar' : 'Adicionar'} Álbum</h3>

		<form action="${pageContext.request.contextPath}/album/${action}" method="POST">

			<input type="hidden" value="${album.getId()}" name="albumId">

			<div class="row">
				<div class="form-group col-md-6">
					<label for="titulo">Título</label>
					<input type="text" class="form-control" id="titulo" name="titulo"
						   autofocus="autofocus" placeholder="Título do álbum" maxlength="150"
						   required
						   oninvalid="this.setCustomValidity('Por favor, informe o título do álbum.')"
						   oninput="setCustomValidity('')"
						   value="<c:out value='${album.getTitulo()}' />" />
				</div>

				<div class="form-group col-md-3">
					<label for="dataLancamento">Data de lançamento</label>
					<input type="date" class="form-control" id="dataLancamento" name="dataLancamento"
						   required
						   oninvalid="this.setCustomValidity('Por favor, informe a data de lançamento.')"
						   oninput="setCustomValidity('')"
						   value="${album.getDataLancamento()}" />
				</div>

				<div class="form-group col-md-3">
					<label for="faixas">Número de faixas</label>
					<input type="number" class="form-control" id="faixas" name="faixas"
						   min="1" max="200" placeholder="Ex.: 12"
						   required
						   oninvalid="this.setCustomValidity('Informe um número de faixas entre 1 e 200.')"
						   oninput="setCustomValidity('')"
						   value="${album.getFaixas()}" />
				</div>
			</div>

			<div class="row">
				<div class="form-group col-md-6">
					<label for="artista">Artista</label>
					<select id="artista" class="form-control" name="artista"
							required
							oninvalid="this.setCustomValidity('Por favor, informe o artista.')"
							oninput="setCustomValidity('')">

						<option value="" ${not empty album ? '' : 'selected'}>Selecione um artista</option>

						<c:forEach var="art" items="${artistas}">
							<option value="${art.getId()}"
								${album.getArtista().getId() == art.getId() ? 'selected' : ''}>
								<c:out value="${art.getNome()}" />
							</option>
						</c:forEach>
					</select>

					<c:if test="${empty artistas}">
						<span class="help-block text-danger">
							Nenhum artista cadastrado.
							<a href="${pageContext.request.contextPath}/artista/form">Cadastre um artista</a> primeiro.
						</span>
					</c:if>
				</div>

				<div class="form-group col-md-6">
					<label>Formato</label>
					<div>
						<c:forEach var="f" items="${formatos}">
							<label class="radio-inline">
								<input type="radio" name="formato" value="${f}" required
									${(empty album ? f eq 'CD' : album.getFormato() eq f) ? 'checked' : ''}>
								${f}
							</label>
						</c:forEach>
					</div>

					<div class="checkbox">
						<label>
							<input type="checkbox" name="explicito"
								${not empty album and album.isExplicito() ? 'checked' : ''}>
							Conteúdo explícito
						</label>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="form-group col-md-12">
					<label for="descricao">Descrição</label>
					<textarea class="form-control" id="descricao" name="descricao" rows="4"
							  placeholder="Fale um pouco sobre o álbum"><c:out value="${album.getDescricao()}" /></textarea>
				</div>
			</div>

			<hr />
			<div id="actions" class="row pull-right">
				<div class="col-md-12">
					<a href="${pageContext.request.contextPath}/albuns" class="btn btn-default">Cancelar</a>

					<button type="submit" class="btn btn-primary">${not empty album ? 'Atualizar' : 'Cadastrar'} Álbum</button>
				</div>
			</div>
		</form>
	</div>

</body>
</html>