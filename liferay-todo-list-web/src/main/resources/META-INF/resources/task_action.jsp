<%@ include file="/init.jsp" %>

<%
ResultRow row = (ResultRow)request.getAttribute("SEARCH_CONTAINER_RESULT_ROW");

Task task = (Task)row.getObject();
%>

<liferay-ui:icon-menu
	direction="left-side"
	icon="<%= StringPool.BLANK %>"
	markupView="lexicon"
	message="<%= StringPool.BLANK %>"
	showWhenSingleIcon="<%= true %>"
>
	<portlet:renderURL var="editURL">
		<portlet:param name="mvcPath" value="/edit_task.jsp" />
		<portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
	</portlet:renderURL>

	<liferay-ui:icon
		label="<%= true %>"
		message="edit"
		url="<%= editURL %>"
	/>

	<%--
	 	// TODO: add permissions menu item
	--%>

	<portlet:actionURL name="deleteTag" var="deleteURL">
		<portlet:param name="redirect" value="<%= currentURL %>" />
		<portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
	</portlet:actionURL>

	<liferay-ui:icon-delete
		url="<%= deleteURL %>"
	/>
</liferay-ui:icon-menu>