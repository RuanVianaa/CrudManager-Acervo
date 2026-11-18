<%-- Botoes de paginacao. Quem inclui define antes a variavel "paginationUrl". --%>
<c:if test="${totalPages > 1}">
	<ul class="pagination">

		<c:choose>
			<c:when test="${currentPage > 1}">
				<li><a href="${paginationUrl}?page=${currentPage - 1}">&lt; Anterior</a></li>
			</c:when>
			<c:otherwise>
				<li class="disabled"><a>&lt; Anterior</a></li>
			</c:otherwise>
		</c:choose>

		<c:forEach var="i" begin="1" end="${totalPages}">
			<li class="${i == currentPage ? 'active' : ''}">
				<a href="${paginationUrl}?page=${i}">${i}</a>
			</li>
		</c:forEach>

		<c:choose>
			<c:when test="${currentPage < totalPages}">
				<li class="next"><a href="${paginationUrl}?page=${currentPage + 1}" rel="next">Próximo &gt;</a></li>
			</c:when>
			<c:otherwise>
				<li class="disabled"><a>Próximo &gt;</a></li>
			</c:otherwise>
		</c:choose>

	</ul>
</c:if>