<%@ include file="/WEB-INF/jsp/topinclude.jspf" %>
<%@ taglib prefix="db" uri="db" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<format:header title="Search Results" />
<format:page>
<br>

<div id="col-2-1">

    <display:table name="items" id="row" pagesize="30" requestURI="${baseUrl}/History/View" class="search-data-table" sort="external" cellspacing="0" cellpadding="4" partialList="true" size="${fn:length(items)}">
        <display:column title="No." >
            ${row_rowNum}
        </display:column>
        <display:column title="Type" >
            ${row.historyType}
        </display:column>
        <display:column title="Description" >
            ${descriptions[row.name]}<br>
        </display:column>
        
        <display:column title="Hits" >
            ${row.numberItems}
        </display:column>
        <display:column title="Download" >
            <a href="Download/${row_rowNum}">Download</a>
        </display:column>
        
        <display:column title="View/Edit" >
            <a href="History/${row_rowNum}">View / Edit</a>
        </display:column>
        
        <display:column title="Remove" >
            <form action="History?historyItem=${row_rowNum}" method="POST"><input type="submit" value="Remove"/></form>
        </display:column>
        
    </display:table>

</div>




</format:page>
