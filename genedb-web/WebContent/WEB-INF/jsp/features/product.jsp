<%@ include file="/WEB-INF/jsp/topinclude.jspf" %>
<%@ taglib prefix="db" uri="db" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="misc" uri="misc" %>

<format:header name="Lucene General Search">
	<st:init />
	<link rel="stylesheet" href="<c:url value="/"/>includes/style/alternative.css" type="text/css"/>
</format:header>
<a href="./ProductSearch">Back to Search Page</a>
<img src="<c:url value="/" />/includes/images/purpleDot.gif" width="100%" height="2" alt="----------------------">
<img src="<c:url value="/" />/includes/images/purpleDot.gif" width="100%" height="2" alt="----------------------">
<display:table name="results" uid="count" pagesize="30" requestURI="" class="simple" cellspacing="0" cellpadding="4">
	<display:column property="title" title="Title" class="fonts" />
	<display:column property="chr" title="Systematic ID" href="./Search/FeatureByName" paramId="name"/>
</display:table>
<img src="<c:url value="/" />/includes/images/purpleDot.gif" width="100%" height="2" alt="----------------------">
<format:footer />