<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
	<%@include file="base-head.jsp"%>
	<title>CRUD Manager - Login</title>
	<style>
		body {
			background-color: #121214 !important;
			display: flex;
			align-items: center;
			justify-content: center;
			min-height: 100vh;
			margin: 0;
			padding-top: 0 !important;
		}

		.login-box {
			width: 100%;
			max-width: 400px;
			padding: 35px;
			background-color: #18181b !important;
			border: 1px solid #27272a !important;
			border-radius: 12px !important;
			box-shadow: 0 10px 30px rgba(0, 0, 0, 0.6) !important;
		}

		.login-box .logo {
			text-align: center;
			margin-bottom: 25px;
		}

		.login-box .logo h3 {
			color: #ffffff !important;
			font-weight: 700;
			margin-top: 12px;
			margin-bottom: 4px;
		}

		.login-box .logo p {
			color: #a1a1aa !important;
			font-size: 0.9rem;
			margin-bottom: 0;
		}

		.login-box form {
			background: transparent !important;
			border: none !important;
			padding: 0 !important;
			box-shadow: none !important;
		}

		.login-box .form-group {
			margin-bottom: 18px;
		}

		.login-box label {
			color: #a1a1aa !important;
			font-weight: 800;
			margin-bottom: 6px;
		}

		/* Corrige fundo azul/branco do autofill do navegador */
		input:-webkit-autofill,
		input:-webkit-autofill:hover, 
		input:-webkit-autofill:focus, 
		input:-webkit-autofill:active {
			-webkit-box-shadow: 0 0 0 30px #09090b inset !important;
			-webkit-text-fill-color: #ffffff !important;
		}

		.btn-block {
			width: 100%;
			margin-top: 25px;
			height: 44px;
			font-size: 2rem;
		}
	</style>
</head>
<body>
	<div class="login-box">
		<div class="logo">
			<img height="52px" width="52px" alt="Logo" src="${pageContext.request.contextPath}/images/logo.png">
			<h3>CRUD Manager</h3>
			<p>Entre para acessar o sistema</p>
		</div>

		<c:if test="${not empty erro}">
			<div class="alert alert-danger" style="background-color: #ef4444; color: #fff; border: none; margin-bottom: 20px;">
				<c:out value="${erro}" />
			</div>
		</c:if>

		<form action="${pageContext.request.contextPath}/login" method="POST">
			<div class="form-group">
				<label for="email">E-mail</label>
				<input type="email" class="form-control" id="email" name="email"
					   autofocus="autofocus" required placeholder="seu@email.com"
					   value="<c:out value='${email}' />" />
			</div>

			<div class="form-group">
				<label for="senha">Senha</label>
				<input type="password" class="form-control" id="senha" name="senha"
					   required placeholder="Sua senha" />
			</div>

			<button type="submit" class="btn btn-primary btn-block">Entrar</button>
		</form>
	</div>
</body>
</html>