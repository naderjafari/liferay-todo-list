package com.chberndt.liferay.todo.list.internal.servlet.taglib.clay;

import com.chberndt.liferay.todo.list.internal.servlet.taglib.util.TaskActionDropdownItemsProvider;
import com.chberndt.liferay.todo.list.model.Task;

import com.liferay.frontend.taglib.clay.servlet.taglib.soy.BaseVerticalCard;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.portal.kernel.dao.search.RowChecker;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author Christian Berndt
 */
public class TaskVerticalCard extends BaseVerticalCard {

	public TaskVerticalCard(
		Task task, RenderRequest renderRequest, RenderResponse renderResponse,
		RowChecker rowChecker, String taskURL,
		PermissionChecker permissionChecker, ResourceBundle resourceBundle) {

		super(task, renderRequest, rowChecker);

		_task = task;

		// TODO
		// _trashHelper = trashHelper;

		_renderResponse = renderResponse;

		_taskURL = taskURL;
		_permissionChecker = permissionChecker;
		_resourceBundle = resourceBundle;
	}

	public List<DropdownItem> getActionDropdownItems() {
		TaskActionDropdownItemsProvider taskActionDropdownItemsProvider =
			new TaskActionDropdownItemsProvider(
				_task, renderRequest, _renderResponse, _permissionChecker,
				_resourceBundle);

		return taskActionDropdownItemsProvider.getActionDropdownItems();
	}

	// TODO

	//	public String getDefaultEventHandler() {
	//	}

	@Override
	public String getHref() {

		// TODO: add permission check

		return _taskURL;
	}

	@Override
	public String getIcon() {
		return "check-circle";
	}

	@Override
	public String getSubtitle() {
		Date dueDate = _task.getDueDate();

		String dueDateDescription = LanguageUtil.getTimeDescription(
			PortalUtil.getHttpServletRequest(renderRequest),
			dueDate.getTime() - System.currentTimeMillis(), true);

		return LanguageUtil.format(
			_resourceBundle, "due-in-x", new Object[] {dueDateDescription});
	}

	@Override
	public String getTitle() {
		return HtmlUtil.unescape(_task.getTitle());
	}

	private final PermissionChecker _permissionChecker;
	private final RenderResponse _renderResponse;
	private final ResourceBundle _resourceBundle;
	private final Task _task;
	private final String _taskURL;

	// TODO: Add TrashHelper support
	// private final TrashHelper _trashHelper;

}